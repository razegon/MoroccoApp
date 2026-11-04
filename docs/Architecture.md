# MoroccoApp -- Architecture

## Cel projektu

MoroccoApp to nowoczesna aplikacja podróżnicza w Kotlin + Jetpack Compose. Projekt jest budowany etapami z naciskiem na rozdzielenie odpowiedzialności między UI, ViewModel, Repository i DataSource.

## Warstwy

```text
UI
↓
ViewModel
↓
Repository
↓
DataSource
↓
Model
```

UI nie pobiera danych bezpośrednio z DataSource. ViewModel korzysta z Repository, a stan ekranu jest wystawiany przez StateFlow.

## Przepływ danych

```text
DataSource → Repository → ViewModel → StateFlow → collectAsState() → Compose UI
```

## Model i Repository

`Place` reprezentuje zarówno miasta, jak i miejsca znajdujące się w miastach. O typie obiektu informuje `PlaceType`.

`PlacesRepository` udostępnia m.in.:

```kotlin
fun getPlaces(): List<Place>
fun getPlace(placeId: Int): Place?
fun getPlacesByType(placeType: PlaceType): List<Place>
fun getChildren(parentPlaceId: Int): List<Place>
fun search(query: String): List<Place>
```

`getPlace()` służy do pobrania konkretnego obiektu po identyfikatorze, natomiast `getChildren()` pozwala powiązać miejsca z miastem nadrzędnym.

### Dane zdjęć i opisów

`Place` przechowuje listy zdjęć i opisów. Zdjęcia na ekranach szczegółów mają typ nullable:

```kotlin
placeImages: List<String?>
placeDescriptions: List<Int>
```

`null` w `placeImages` ma znaczenie: oznacza, że na danej stronie nie ma zdjęcia. Nie powinien być traktowany jako błąd ładowania zdjęcia i dlatego nie powoduje wyświetlenia `default_image`.

Liczba zdjęć i opisów nie musi być taka sama. Pozycje są łączone przez system stron `CityPage`.

## Lista miast

```text
CitiesScreen
↓
LazyColumn
↓
CityListItem
↓
Card
├── AsyncImage
└── Column
    ├── placeName
    ├── placeTranslate
    └── placeSnippet
```

`CityListItem` nie zna ViewModelu i nie wykonuje Navigation. Zgłasza kliknięcie przez:

```kotlin
onClick: (Place) -> Unit
```

## Navigation do miasta

Po kliknięciu miasta przekazywany jest jego identyfikator, a nie cały obiekt `Place`.

```text
CityListItem
↓
CitiesScreen
↓
navController.navigate("city_details/{cityId}")
↓
CityDetailsScreen(placeId)
```

Argument Navigation jest pobierany jako `String`, a następnie konwertowany do `Int`.

## CityDetailsScreen

`CityDetailsScreen` otrzymuje `placeId`. Sam tworzy `CityDetailsViewModel` przy użyciu `CityDetailsViewModelFactory` i obserwuje jego `uiState`.

```text
Navigation
↓
placeId
↓
CityDetailsScreen
↓
CityDetailsViewModel
↓
PlacesRepository
```

### CityDetailsUiState

```kotlin
data class CityDetailsUiState(
    val place: Place? = null,
    val children: List<Place> = emptyList()
)
```

`place` jest głównym miastem, a `children` to miejsca przypisane do tego miasta.

### CityDetailsViewModel

ViewModel pobiera oba elementy przez Repository:

```text
getPlace(cityId)
getChildren(cityId)
```

i zapisuje je w jednym `CityDetailsUiState`.

### CityDetailsViewModelFactory

Ponieważ `CityDetailsViewModel` wymaga `placeId` w konstruktorze, używany jest `ViewModelProvider.Factory`.

Schemat:

```text
cityId
↓
Int
↓
CityDetailsViewModelFactory(placeId)
↓
CityDetailsViewModel(placeId)
```

## Miejsca przypisane do miasta

`CityDetailsScreen` prezentuje `uiState.children` w poziomym `LazyRow`.

```text
CityDetailsScreen
↓
LazyRow
↓
PlaceListItem
```

`PlaceListItem` jest osobnym komponentem, ponieważ ma inne zastosowanie niż `CityListItem`.

- `CityListItem` — lista miast w `CitiesScreen`.
- `PlaceListItem` — małe karty miejsc w `LazyRow` miasta.

`PlaceListItem` również nie wykonuje Navigation samodzielnie. Otrzymuje callback:

```kotlin
onClick: (Place) -> Unit
```

## System stron szczegółów

Zarówno `CityDetailsScreen`, jak i `PlaceDetailsScreen` korzystają z `HorizontalPager`.

Dane są przekształcane do listy `CityPage` przez funkcje `createCityPages()` / `createPlacePages()`.

Liczba stron jest wyznaczana na podstawie większej z liczby zdjęć i opisów, z dodatkową stroną `index == 0` na informacje podstawowe:

```kotlin
val pageCount = maxOf(
    images.size,
    descriptions.size
) + 1
```

Pierwsza strona:

```text
index == 0
↓
zdjęcie, jeśli istnieje
↓
nazwa
↓
tłumaczenie
↓
snippet
↓
LazyRow miejsc (CityDetails)
```

Kolejne strony:

```text
index > 0
↓
opcjonalne zdjęcie
↓
description
```

Opis dla strony jest pobierany z pozycji `index - 1` listy `placeDescriptions`, natomiast zdjęcie z pozycji `index` listy `placeImages`. Dzięki temu można niezależnie sterować liczbą opisów i zdjęć.

`null` w `placeImages` oznacza pustą przestrzeń zdjęciową na konkretnej stronie:

```text
Page 1 → image + description
Page 2 → null + description
Page 3 → image + description
```

Brak zdjęcia wynikający z `null` nie wyświetla placeholdera. Placeholder jest używany tylko tam, gdzie `AsyncImage` faktycznie próbuje załadować obraz i występuje błąd lub brak modelu w miejscach, w których przewidziano obsługę fallback.

## Pionowy scroll i collapsing

Każda strona `HorizontalPager` ma własny `rememberScrollState()` i `verticalScroll()`.

```text
HorizontalPager
↓
page
↓
rememberScrollState()
↓
Column.verticalScroll(scrollState)
```

Dzięki temu przesuwanie poziome między kartami jest niezależne od pionowego przewijania treści danej karty.

Zdjęcie hero ma maksymalną wysokość `240.dp`. Podczas pionowego scrollowania wysokość zdjęcia jest zmniejszana o aktualną wartość scrolla, aż do zera.

Do przeliczania `dp` i `px` używany jest `LocalDensity.current`:

```kotlin
val imageHeight = with(density) {
    (imageMaxHeight.toPx() - scrollState.value)
        .coerceAtLeast(0f)
        .toDp()
}
```

Schemat działania:

```text
scrollState.value
↓
zmniejszenie wysokości zdjęcia
↓
0 dp
↓
zdjęcie znika
↓
tekst pozostaje i zajmuje dostępne miejsce
```

Przy powrocie na górę wysokość zdjęcia ponownie rośnie.

## PageIndicator

`PageIndicator` jest wspólnym komponentem UI używanym przez ekrany szczegółów.

Wyświetla serię małych okręgów odpowiadających liczbie stron. Aktywny okrąg wskazuje `pagerState.currentPage`.

```text
● ○ ○ ○
```

Komponent przyjmuje:

```kotlin
pageCount: Int
currentPage: Int
modifier: Modifier
```

`modifier` pozwala rodzicowi określić pozycję wskaźnika, np. przez `align(Alignment.BottomCenter)` w `Box` zawierającym zdjęcie.

## PlaceDetailsScreen

`PlaceDetailsScreen` korzysta z tego samego mechanizmu stron co `CityDetailsScreen`.

```text
PlaceListItem
↓
onClick(place)
↓
Navigation
↓
place_details/{placeId}
↓
PlaceDetailsScreen
↓
PlaceDetailsViewModel
↓
PlacesRepository.getPlace(placeId)
```

Na pierwszej stronie pokazuje podstawowe informacje o miejscu. Kolejne strony prezentują `placeDescriptions` oraz opcjonalne zdjęcia.

`PlaceDetailsScreen` ma również:
- `HorizontalPager`,
- pionowy scroll każdej strony,
- collapsing zdjęcia,
- `PageIndicator`,
- HTML-owe opisy.

## HTML w opisach

Opisy w `placeDescriptions` są przechowywane jako identyfikatory zasobów `R.string`, ale ich zawartość może zawierać HTML, np.:

```html
<h4>Etymologia</h4>
<p>Treść akapitu...</p>
<b>Ważna informacja</b>
```

Do konwersji używany jest komponent `HtmlText`.

`HtmlText` wykorzystuje:

```kotlin
Html.fromHtml(
    text,
    Html.FROM_HTML_MODE_LEGACY
)
```

Następnie analizowane są spany Androida i przekształcane do `AnnotatedString` Compose.

Obsługiwane są m.in.:
- `StyleSpan` → `FontWeight.Bold` / `FontStyle.Italic`,
- `RelativeSizeSpan` → `SpanStyle(fontSize = ...)`,
- struktura akapitów generowana przez HTML,
- wyrównanie tekstu przez `TextAlign`.

`HtmlText` przyjmuje parametr:

```kotlin
textAlign: TextAlign = TextAlign.Justify
```

Dzięki temu ekran może użyć np. `TextAlign.Justify`, zachowując formatowanie HTML.

## Docelowy przepływ szczegółów

```text
CitiesScreen
↓
CityDetailsScreen
↓
HorizontalPager
├── podstawowe informacje + LazyRow
├── description + opcjonalne zdjęcie
├── description + opcjonalne zdjęcie
└── ...
        ↓
   PlaceListItem
        ↓
PlaceDetailsScreen
↓
HorizontalPager
├── podstawowe informacje
├── description + opcjonalne zdjęcie
├── description + opcjonalne zdjęcie
└── ...
```

Miasto i konkretne miejsce mają osobne ekrany szczegółów, ale korzystają z tej samej koncepcji prezentacji treści.

## Obrazy

W listach reprezentacyjne zdjęcie może być wybierane przez:

```kotlin
place.placeImages.firstOrNull()
```

Na ekranach szczegółów lista może zawierać `null`, aby zachować kontrolę nad tym, na których stronach ma pojawić się zdjęcie.

Obraz ładuje Coil 3 przez `AsyncImage`.

```text
placeImages
↓
AsyncImage
↓
HTTPS / OkHttp
```

Aplikacja wymaga:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## UI i layout

`CityListItem` korzysta z `Card`, `Row`, `Column`, `fillMaxWidth()`, `weight(1f)`, `padding()`, zdjęcia 112 dp i `ContentScale.Crop`.

Karta używa:

```kotlin
shape = MaterialTheme.shapes.medium
elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
```

Hierarchia tekstu:

```text
placeName
↓ 8 dp
placeTranslate
↓ 4 dp
placeSnippet
```

`placeTranslate` jest wyświetlane kursywą.

Na ekranach szczegółów zdjęcie jest traktowane jako szeroki element typu hero. Tekst poniżej ma własne marginesy, dzięki czemu zdjęcie może wykorzystać pełną szerokość ekranu.

## Zasada odpowiedzialności komponentów

Komponenty UI nie powinny same decydować o Navigation ani pobierać danych z Repository.

```text
PlaceListItem
↓
onClick(place)
↓
rodzic
↓
Navigation
```

Navigation przekazuje identyfikatory, a ViewModel pobiera właściwe obiekty domenowe.

## Aktualny etap

Podstawowa architektura list, Navigation, ekranów szczegółów, pagera, scrollowania, collapsing oraz formatowania opisów HTML jest zaimplementowana.

Kolejne planowane obszary:

```text
dalsze dopracowanie UX
↓
Weather
↓
Maps
↓
Search
↓
Favorites
↓
Room
↓
Hilt
```

## Zdjęcia i licencje

W przyszłości planowane jest korzystanie m.in. ze zdjęć Wikimedia Commons. Docelowo można rozważyć model zdjęcia zawierający URL, źródło, autora i licencję.


## Aktualizacja 30–31 sierpnia 2026

### Wikimedia ImageLoader

Konfiguracja ładowania zdjęć Wikimedia została scentralizowana w `data/image/WikimediaImageLoader.kt`. `createWikimediaImageLoader(context)` tworzy Coil `ImageLoader` wykorzystujący wspólny klient OkHttp z odpowiednim `User-Agent`. Dzięki temu konfiguracja nie jest kopiowana w `CityDetailsScreen`, `PlaceDetailsScreen` i `CityListItem`.

Przepływ: 

```text
WikimediaRetrofit
↓
WikimediaRepository
↓
WikimediaViewModel
↓
Map<Int, WikimediaImage>
↓
createWikimediaImageLoader()
↓
AsyncImage
```

### Wspólny DetailsPager

Logika `CityDetailsScreen` i `PlaceDetailsScreen` została zrefaktoryzowana do `DetailsPager`. Wspólny komponent obsługuje pager, zdjęcia, attribution, collapsing, scroll, PageIndicator, nazwę, tłumaczenie, snippet i descriptions.

```text
CityDetailsScreen ──┐
                    ├── DetailsPager
PlaceDetailsScreen ─┘
```

Różnice są przekazywane przez opcjonalny slot Compose:

```kotlin
content: @Composable () -> Unit = {}
```

City przekazuje `LazyRow` miejsc, Place nie przekazuje dodatkowej zawartości.

### Wspólne tworzenie stron

Wcześniejsze `createCityPages()` i `createPlacePages()` zostały zastąpione wspólnym `createPages()`. Dzięki temu sposób mapowania `WikimediaImageRef`, lokalnych URL-i i opisów jest jeden dla obu ekranów.

### Odpowiedzialność layoutu

W `DetailsPager` `Box` zawiera wyłącznie hero image i elementy nakładane na zdjęcie (`WikimediaAttributionIcon`, `PageIndicator`). Nazwa, translate, snippet, descriptions i opcjonalny `content()` znajdują się w nadrzędnym `Column`, dzięki czemu nie nachodzą na zdjęcie.

### Przyszła Application

Projekt obecnie nie ma własnej klasy `Application`. Jest to świadoma sytuacja na obecnym etapie. Przy planowanym dodaniu map OSM konfiguracja aplikacji może zostać przeniesiona do własnej klasy `Application`, podobnie jak wcześniej w GlobeTrotter.


## Aktualizacja 31 sierpnia – 1 września 2026

### Źródła danych online

Do architektury dołączono Wikidata jako źródło dynamicznych danych dla obiektów posiadających `wikidataId`. Wikimedia pozostaje źródłem informacji o zdjęciach i ich attribution.

```text
Place
  └── wikidataId
        ↓
WikidataRepository
        ↓
WikidataInfo
        ↓
CityDetails / PlaceDetails
```

Dla City `WikidataInfo` zawiera obecnie `population`, `area`, `elevation` oraz `inception: WikidataTime?`.

### Repository i cache

Repository jest nadal interfejsem w `domain/repository`, a implementacja znajduje się w `data/repository`. Do danych online dodano interfejsy cache w domenie oraz implementacje pamięciowe w warstwie data.

```text
domain/repository
├── WikidataRepository
├── WikidataCache
└── WikimediaCache

 data/repository
├── WikidataRepositoryImpl
└── WikimediaRepositoryImpl

 data/cache
├── WikidataMemoryCache
└── WikimediaMemoryCache
```

### `MyApp` i współdzielone zależności

Projekt ma już własną klasę `MyApp : Application`. Na obecnym etapie przechowuje wspólne instancje `WikidataMemoryCache` i `WikimediaMemoryCache`. Jest to punkt centralizacji zależności, który w przyszłości może zostać rozszerzony o konfigurację OSM i inne elementy infrastruktury.

### Wspólny klient HTTP

Konfiguracja `User-Agent` została wydzielona do wspólnego klienta HTTP, wykorzystywanego przez usługi Wikimedia/Wikidata oraz Coil. Eliminuje to kolejne duplikaty konfiguracji i centralizuje zachowanie sieciowe.

### Wikimedia -- ograniczenie requestów

`WikimediaViewModel` ogranicza liczbę jednoczesnych requestów do trzech (`Semaphore(3)`) oraz blokuje równoległe ponowne pobieranie tego samego `pageId`. Repository korzysta z `WikimediaCache`, dzięki czemu dane pobrane wcześniej w bieżącej sesji mogą być używane bez kolejnego requestu.

### Loading obrazu

Stan ładowania właściwego pliku obrazu jest obecnie kontrolowany przez Coil w `DetailsPager`, a nie przez stan pobierania metadanych Wikimedia. `LinearProgressIndicator` jest wyświetlany podczas `onLoading` i znika przy `onSuccess`/`onError`.

### Offline

Aktualnie dostępny jest cache w pamięci procesu. Dane są dostępne po utracie sieci w obrębie tej samej sesji. Cache znika po zakończeniu procesu aplikacji. Trwały storage nie został jeszcze wdrożony. Room pozostaje przyszłym krokiem, kiedy potrzebny będzie offline po restarcie.

### Planowane rozszerzenia

W przyszłości baza lokalna może przechowywać dane Wikidata, Wikimedia, a następnie dane Quizu, ulubione obiekty, postęp użytkownika i inne dane aplikacji. Room jest obecnie odłożony, aby najpierw ustabilizować model domenowy i przepływy online/offline.

## Aktualizacja 2–3 września 2026 -- Room, Git i Quiz

### Trwały cache z Room

Do projektu dodano Room 3 oraz KSP. Room przechowuje lokalnie metadane Wikidata i Wikimedia, dzięki czemu dane mogą być odzyskane po zakończeniu procesu aplikacji.

Utworzono:
- `AppDatabase`
- `WikidataEntity` / `WikidataDao`
- `WikimediaEntity` / `WikimediaDao`
- `WikidataMapper`
- `WikimediaMapper`

Mechanizm dostępu do danych:

```text
Wikidata / Wikimedia metadata:
MemoryCache → Room → API
API → Room → MemoryCache → UI
```

Dla obrazów właściwy plik jest niezależnie obsługiwany przez Coil i jego persistent `DiskCache`.

```text
Wikimedia image URL
        ↓
      Coil
        ↓
   DiskCache
```

`MyApp : Application` przechowuje obecnie wspólne MemoryCache, bazę Room oraz współdzielony `WikimediaImageLoader`.

### Test offline po restarcie

Przetestowano działanie persistent cache po zakończeniu procesu aplikacji i ponownym uruchomieniu bez internetu. Logcat wykazał odczyty `ROOM CACHE HIT`, a użytkownik potwierdził wyświetlenie zapisanych zdjęć offline.

### Git i organizacja pracy

Projekt został podłączony do Git i GitHub. Praca nad funkcjami odbywa się na osobnych branchach.

Aktualna historia:

```text
main
├── chore: initialize MoroccoApp repository
├── chore: configure Room and KSP
├── feat: add persistent Wikidata cache
└── feat: add persistent Wikimedia cache
```

Branch `feature/room` został scalony do `main` metodą `Fast-forward`.

Następnie utworzono branch `feature/quiz`.

### Quiz -- aktualna architektura

Pierwsza wersja quizu korzysta z osobnego modelu, Repository i ViewModelu.

```text
HomeScreen
    ↓
Navigation
    ↓
QuizScreen
    ↓
QuizViewModel
    ↓
QuizRepository
    ↓
QuizQuestion
```

Ukończono:
- `QuizQuestion`
- `QuizRepository`
- `QuizRepositoryImpl`
- `QuizViewModel`
- `QuizViewModelFactory`
- `QuizScreen`
- trasę `quiz` w Navigation
- przycisk wejścia do quizu na `HomeScreen`

Quiz zawiera obecnie 10 pytań. Każde pytanie ma 4 odpowiedzi i dokładnie jedną poprawną odpowiedź.

Po wybraniu odpowiedzi użytkownik nie może jej zmienić. Poprawna odpowiedź jest oznaczana na zielono, a błędna wybrana odpowiedź na czerwono. Na ostatnim pytaniu przycisk zmienia się z `NASTĘPNE` na `PODSUMOWANIE`.

### Najbliższe elementy quizu

```text
QuizScreen
    ↓
QuizSummaryScreen
    ↓
nickname + score + totalQuestions + date
    ↓
Room
    ↓
TOP 10 / TOP 20
```

## Aktualizacja 14 września 2026 -- aktualny stan po domknięciu Quiz MVP

### Quiz -- źródło pytań

Pytania quizowe nie są już przechowywane jako hardkodowana lista w `QuizRepositoryImpl`. Są przechowywane w pliku:

```text
app/src/main/assets/quiz/questions.json
```

Każdy rekord zawiera:

```text
id
difficulty
question
answers
correctAnswerIndex
```

Aktualnie baza zawiera 50 pytań:

```text
VERY_EASY  → 10
EASY       → 10
MEDIUM     → 10
HARD       → 10
VERY_HARD  → 10
```

### Przepływ danych quizu

```text
questions.json
      ↓
QuizQuestionDataSource
      ↓
Gson
      ↓
QuizQuestionDto
      ↓
QuizQuestionMapper
      ↓
QuizQuestion
      ↓
QuizRepository
      ↓
QuizViewModel
      ↓
Compose UI
```

`QuizQuestionDto` odwzorowuje format JSON, natomiast `QuizQuestion` jest modelem domenowym. Konwersja `difficulty` z tekstu JSON do `QuizDifficulty` odbywa się w mapperze.

### Trudność quizu

`QuizDifficulty` znajduje się w `domain/model` i posiada pięć poziomów:

```kotlin
VERY_EASY
EASY
MEDIUM
HARD
VERY_HARD
```

Poziom jest wybierany na osobnym `QuizDifficultyScreen`. `QuizViewModel.startQuiz(difficulty)` zapamiętuje wybrany poziom i pobiera odpowiednie pytania przez Repository.

### Klasyczny quiz

Klasyczny quiz składa się z 10 pytań. Każde pytanie posiada 4 odpowiedzi i dokładnie jedną poprawną. Po udzieleniu odpowiedzi wybór zostaje zablokowany. Poprawna odpowiedź jest oznaczana na zielono, a wybrana błędna odpowiedź na czerwono.

### Przerwanie quizu

`QuizScreen` posiada mały przycisk `X` w prawym górnym rogu. Przed opuszczeniem rozgrywki wyświetlany jest `AlertDialog` z potwierdzeniem.

```text
X
↓
potwierdzenie
├── ZOSTAŃ → pozostaje w quizie
└── WYJDŹ → reset stanu + powrót do ekranu startowego quizu
```

Przerwanie quizu nie zapisuje wyniku. Resetowany jest bieżący stan rozgrywki.

### Wyniki quizu i Room

`QuizResult` zawiera obecnie:

```kotlin
nickname
difficulty
score
totalQuestions
date
```

Wynik jest zapisywany przez `QuizResultRepository` do Room. Ranking jest sortowany przede wszystkim po liczbie poprawnych odpowiedzi (`score DESC`). Poziom trudności jest informacją prezentowaną obok wyniku i nie daje automatycznego bonusu w rankingu.

Przykład:

```text
10/10 MEDIUM
9/10 HARD
```

### Quiz UI i Theme

Dla aplikacji wprowadzono własny Material 3 Theme dla Light i Dark Mode. Dynamic colors są wyłączone.

Wspólne elementy Theme obejmują:

```text
ColorScheme
Shapes
Typography
```

`Shape.kt` definiuje wspólną skalę zaokrągleń. Ekrany startowy i wyboru trudności korzystają z tej samej hierarchii typografii i spacingu.

### Git

Etap `feature/quiz` został ukończony, przetestowany i scalony do `main` metodą `Fast-forward`. Zaktualizowany `main` został wypchnięty do GitHub.

Następnym etapem będzie osobny branch dla `Arcade Mode`.

## Aktualizacja 3 października 2026 -- Arcade Mode

### Architektura Arcade

Arcade korzysta z istniejącego `QuizRepository`, dlatego wykorzystuje tę samą pulę 50 pytań co klasyczny Quiz.

```text
ArcadeScreen
    ↓
ArcadeViewModel
    ↓
QuizRepository
    ↓
QuizQuestionDataSource
    ↓
questions.json
```

`ArcadeViewModel` zarządza stanem pojedynczej rozgrywki. Stan jest przechowywany w `ArcadeGameState` i obejmuje m.in. aktualne pytanie, wybraną odpowiedź, wynik, pozostały czas, identyfikatory wykorzystanych pytań oraz informację o zakończeniu gry.

### Zasady rozgrywki

Arcade wykorzystuje wszystkie pytania dostępne w `QuizRepository`. W jednej rozgrywce pytanie nie może pojawić się ponownie.

```text
czas początkowy: 45 s
poprawna odpowiedź: +5 s, +1 punkt
błędna odpowiedź: -5 s
koniec gry: 0 s
wynik: liczba poprawnych odpowiedzi
```

Po odpowiedzi przyciski odpowiedzi zostają zablokowane. Następne pytanie jest losowane dopiero po przejściu dalej. `usedQuestionIds` pozwala wykluczyć pytania wykorzystane wcześniej w tej samej rozgrywce.

### Stan Arcade

`ArcadeGameState` jest osobnym modelem stanu, ponieważ Arcade ma inne reguły niż klasyczny quiz.

```text
ArcadeGameState
├── currentQuestion
├── selectedAnswerIndex
├── isAnswerChecked
├── score
├── remainingTimeSeconds
├── usedQuestionIds
└── isGameOver
```

Oddzielenie stanu Arcade od `QuizViewModel` pozwala zachować osobną logikę gry bez dokładania timerów i dodatkowych stanów do klasycznego quizu.

### Timer

Timer jest zarządzany przez `ArcadeViewModel`. Odliczanie jest uruchamiane razem z rozpoczęciem gry i zatrzymywane po zakończeniu rozgrywki lub opuszczeniu Arcade.

Zmiana czasu po odpowiedzi jest wykonywana w ViewModelu, a UI jedynie obserwuje `remainingTimeSeconds` przez `StateFlow`.

### Zakończenie i wyjście z Arcade

`ArcadeScreen` posiada przycisk `X` w prawym górnym rogu. Przed opuszczeniem aktywnej rozgrywki wyświetlany jest `AlertDialog`.

```text
X
↓
potwierdzenie
├── ZOSTAŃ → powrót do gry
└── WYJDŹ → zakończenie/reset bieżącej rozgrywki
```

Niezakończona rozgrywka nie jest zapisywana do rankingu.

### Wynik Arcade

Dla Arcade utworzono osobny model domenowy:

```kotlin
data class ArcadeResult(
    val nickname: String,
    val score: Int,
    val date: Long
)
```

Wynik jest niezależny od `QuizResult`, ponieważ klasyczny quiz zapisuje dodatkowo poziom trudności i liczbę pytań.

### Room -- ranking Arcade

Arcade ma osobną tabelę Room:

```kotlin
@Entity
data class ArcadeResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nickname: String,
    val score: Int,
    val date: Long
)
```

`ArcadeResultDao` udostępnia zapis wyniku oraz pobranie TOP 10. Ranking sortuje wyniki po `score DESC`, a przy takim samym wyniku po `date ASC`.

Przepływ zapisu:

```text
ArcadeScreen
    ↓
ArcadeResultViewModel
    ↓
ArcadeResultRepository
    ↓
ArcadeResultDao
    ↓
Room
```

Mapper rozdziela model domenowy `ArcadeResult` od encji Room `ArcadeResultEntity`.

### Zapis wyniku

Wynik można zapisać dopiero po zakończeniu gry. Nick jest ograniczony do 15 znaków i nie może być pusty po usunięciu białych znaków.

Po zapisaniu wynik jest blokowany przed ponownym zapisem w tej samej rozgrywce. Dopiero wtedy dostępne są przyciski:

```text
ZAPISZ WYNIK
      ↓
WYNIK ZAPISANY
      ↓
TOP 10 ARCADE
MENU QUIZU
```

### Ranking Arcade

`ArcadeRankingScreen` pobiera wyniki przez `ArcadeResultViewModel` i wyświetla 10 najlepszych zapisanych wyników.

Ranking Arcade jest oddzielony od rankingu klasycznego quizu. Nie korzysta z `QuizResultEntity` ani `QuizResultDao`.

### Room Migration 1 → 2

Dodanie tabeli `ArcadeResultEntity` wymagało zwiększenia wersji `AppDatabase` i dodania migracji `MIGRATION_1_2`. Migracja tworzy tabelę wyników Arcade bez usuwania istniejących danych cache i wyników klasycznego quizu.

```text
Room v1
  ↓ MIGRATION_1_2
Room v2
├── WikidataEntity
├── WikimediaEntity
├── QuizResultEntity
└── ArcadeResultEntity
```

### Integracja w `MoroccoApp`

W `MyApp` znajduje się wspólna instancja `AppDatabase`. W `MoroccoApp` tworzony jest osobny `ArcadeResultRepository` i `ArcadeResultViewModel` na podstawie `ArcadeResultDao`.

Dzięki temu UI nie zna Room bezpośrednio:

```text
ArcadeScreen
↓
ArcadeResultViewModel
↓
ArcadeResultRepository
↓
ArcadeResultDao
```

### Navigation Arcade

Arcade posiada osobne trasy dla gry i rankingu:

```text
QuizStartScreen
├── Quiz
├── Arcade
│    ↓
│    ArcadeScreen
│    ↓
│    ArcadeRankingScreen
└── QuizRanking
```

Uruchomienie Arcade wywołuje `arcadeViewModel.startGame()` przed przejściem do ekranu gry. Dzięki temu powrót z `ArcadeRankingScreen` nie uruchamia automatycznie nowej rozgrywki i zachowuje bieżący stan gry.

### Build i testy

Po kolejnych logicznych zmianach Arcade wykonywano Build i testy ręczne. Sprawdzono m.in. przebieg rozgrywki, brak powtórzeń pytań, zmianę czasu, zapis wyniku, ochronę przed wielokrotnym zapisem, ranking oraz powrót do menu quizu.

## Aktualizacja 4 października 2026 -- KMP: migracja modelu Place do PlaceData

### Cel etapu

Rozpoczęto właściwą migrację domeny do Kotlin Multiplatform. Pierwszym krokiem było oddzielenie danych strukturalnych miejsca od treści prezentacyjnych i usunięcie zależności Android `R` z modelu współdzielonego.

### Współdzielone modele domenowe

Modele platform-independent znajdują się w:

```text
shared/src/commonMain/kotlin/com/rezegon/shared/domain/model/
```

Aktualnie znajdują się tam:

- `Place`
- `PlaceData`
- `PlaceContent`
- `PlaceFallback`
- `PlaceType`
- `WikimediaImageRef`

`Place` zawiera dane strukturalne miejsca, m.in. `placeId`, `placeType`, `parentPlaceId`, `wikidataId`, referencje Wikimedia, lokalne obrazy oraz dane mapy.

`PlaceContent` zawiera treść prezentacyjną:

```kotlin
data class PlaceContent(
    val id: String,
    val name: String,
    val translation: String? = null,
    val snippet: String? = null,
    val weather: String? = null,
    val openingHours: String? = null,
    val descriptions: List<String> = emptyList(),
    val fallback: PlaceFallback? = null,
)
```

`PlaceData` łączy oba modele:

```kotlin
data class PlaceData(
    val place: Place,
    val content: PlaceContent,
)
```

### Aktualny przepływ danych

W warstwach, które zostały już zmigrowane, obowiązuje:

```text
DataSource
    ↓
List<PlaceData>
    ↓
Repository
    ↓
ViewModel
    ↓
UiState
    ↓
Compose UI
```

Nie stosujemy mapowania `PlaceData → Place` tylko po to, aby zachować stary przepływ danych.

### Zmigrowane elementy

Do `PlaceData` zostały dostosowane:

- `PlacesRepository`
- `PlacesRepositoryImpl`
- `PlacesViewModel`
- `PlacesUiState`
- `CitiesViewModel`
- `CitiesUiState`
- `CityDetailsUiState`
- `CitiesScreen`
- `CityListItem`
- `CityDetailsScreen`
- wspólne `createPages()`
- `DetailsPage`

`CityDetailsUiState` przechowuje obecnie `PlaceData?` dla głównego miasta oraz `List<PlaceData>` dla jego dzieci.

### Treść opisów

`PlaceContent.descriptions` oraz `DetailsPage.description` używają obecnie `String` / `String?` zamiast wcześniejszych identyfikatorów `R.string`.

Jest to rozwiązanie przejściowe. Docelowo treści zostaną przeniesione do JSON. Wtedy zostanie ustalona finalna strategia lokalizacji oraz formatowania akapitów.

### Aktualny DataSource

`MoroccoDataSource` nadal znajduje się w module Android `app` i zawiera odwołania do `R.string.*`. Jest to świadomie pozostawiony kolejny etap migracji.

Docelowy kierunek:

```text
JSON
    ↓
shared data layer
    ↓
List<PlaceData>
    ↓
Repository
    ↓
ViewModel
    ↓
Compose UI
```

Schemat JSON nie jest jeszcze traktowany jako finalny. Zostanie zaprojektowany po przeanalizowaniu całego obecnego `MoroccoDataSource`.

### Stan po etapie

- Gradle Sync: OK
- Build: OK
- aplikacja uruchamia się poprawnie
- lista miast działa
- szczegóły miasta działają
- zdjęcia Wikimedia działają
- opisy działają
- lista miejsc przypisanych do miasta działa

Jest to checkpoint przed kolejnym etapem migracji DataSource i treści do JSON.

## Aktualizacja 4 października 2026 -- JSON DataSource i KMP

### DTO i mapper

W `commonMain` utworzono DTO przeznaczone do deserializacji JSON:

```text
PlaceDto
├── PlaceDtoData
├── PlaceContentDto
├── PlaceFallbackDto
└── WikimediaImageRefDto
```

DTO są mapowane do modeli domenowych przez `PlaceMapper`.

```text
JSON
↓
PlaceDto
↓
PlaceMapper
↓
PlaceData
```

### JsonPlaceDataSource

Utworzono wspólny `JsonPlaceDataSource`, który korzysta z `kotlinx.serialization`.

Jego odpowiedzialność ogranicza się do odczytania tekstu JSON, deserializacji DTO oraz mapowania DTO do `PlaceData`.

### Rozdzielenie odpowiedzialności między commonMain i Android

`commonMain` nie korzysta bezpośrednio z Androidowego `Context` ani `AssetManager`.

Zdefiniowano:

```kotlin
interface JsonResourceReader {
    fun readText(path: String): String
}
```

Android dostarcza implementację:

```text
commonMain
    ↓
JsonResourceReader
    ↑
AndroidJsonResourceReader
    ↓
Context.assets
```

Dzięki temu sposób odczytu pliku jest zależnością platformową, a parsowanie JSON i mapowanie danych pozostaje wspólne.

### Aktualna lokalizacja JSON

Obecny plik znajduje się w:

```text
shared/src/androidMain/assets/morocco.json
```

W konfiguracji modułu `shared` włączone jest:

```kotlin
androidResources {
    enable = true
}
```

### Pierwszy zmigrowany rekord

Na tym etapie JSON zawiera jeden rekord -- Marrakesz.

Weryfikacja na fizycznym urządzeniu potwierdziła:

- `JsonPlaceDataSourceTest` poprawnie odczytuje JSON,
- `placeId = 4100`,
- nazwa `Marrakesz`,
- `wikidataId = "Q101625"`,
- dane Wikimedia,
- dane Wikidata,
- opis z JSON wyświetlany jako `TEST`.

Oznacza to, że przepływ działa end-to-end:

```text
morocco.json
↓
AndroidJsonResourceReader
↓
JsonPlaceDataSource
↓
PlaceDto
↓
PlaceMapper
↓
PlaceData
↓
PlacesRepository
↓
ViewModel
↓
Compose UI
```

### Ręczne dependency injection

ViewModel otrzymuje `PlacesRepository` przez konstruktor zamiast samodzielnie tworzyć zależność.

Przykładowy kierunek:

```kotlin
class PlacesViewModel(
    private val repository: PlacesRepository
) : ViewModel()
```

Na tym etapie używane jest ręczne DI oraz `ViewModelProvider.Factory`. Hilt nie został jeszcze wprowadzony.

### Najbliższy krok architektoniczny

Obecnie kilka factory tworzy `PlacesRepository` niezależnie. Następnym krokiem będzie scentralizowanie tworzenia tej zależności, tak aby wszystkie ViewModel korzystały z tej samej konfiguracji Repository.

Dopiero po uporządkowaniu DI JSON zostanie rozszerzony z jednego rekordu do kilku, a następnie do kompletnego zestawu miejsc.
---

## Stan architektury na commit `95fcc35` — przed zmianami `2ba5e36` i `c60215d`

Ta sekcja opisuje stan przyjęty jako baza do finalnego pushu na `main`. Zachowuje wcześniejsze wpisy jako historię etapów migracji; poniższy opis zastępuje nieaktualne plany z pierwszego etapu KMP.

### Granica migracji

W tym stanie projekt ma dwa moduły Gradle:

- `:app` — aplikacja Android, interfejs Compose, nawigacja, integracje platformowe i konfiguracja zależności.
- `:shared` — moduł Kotlin Multiplatform z logiką domenową, źródłem danych JSON, mapperami, kontraktami i implementacjami repozytoriów oraz współdzielonymi ViewModelami i stanami UI.

**Interfejs Compose pozostaje po stronie `:app`.** Ten opis nie zakłada migracji ekranów do Compose Multiplatform ani wymiany istniejącego UI. Commity `2ba5e36` i `c60215d` nie należą do dokumentowanego punktu bazowego.

### Model domenowy i treści

W `shared/src/commonMain/kotlin/com/rezegon/shared/domain/model/` model jest rozdzielony na:

- `Place` — dane strukturalne miejsca: identyfikator, typ, relacja rodzic–dziecko, identyfikator Wikidata, referencje do Wikimedia, obrazy lokalne i współrzędne.
- `PlaceContent` — nazwę i teksty prezentacyjne, m.in. tłumaczenie, zajawkę, opisy, godziny otwarcia oraz dane zapasowe.
- `PlaceData` — połączenie `Place` i `PlaceContent`.
- `PlaceType`, `PlaceFallback` i `WikimediaImageRef` — typy pomocnicze.

Współdzielony model nie powinien zależeć od Androidowego `R.string`. Teksty są przekazywane jako dane, a DTO JSON nie jest bezpośrednio modelem używanym przez warstwę domenową.

### Aktualne źródło danych miejsc

Źródłem danych jest `JsonPlaceDataSource` w `commonMain`. Ładuje listy rekordów z plików:

```text
shared/src/androidMain/assets/
├── ma_cities.json
├── ma_monuments.json
└── ma_nature.json
```

Przepływ danych:

```text
AndroidJsonResourceReader
        ↓
JsonPlaceDataSource
        ↓
List<PlaceDto>
        ↓
PlaceDto.toDomain()
        ↓
List<PlaceData>
        ↓
PlacesRepository
        ↓
ViewModel / UiState
        ↓
Compose UI w :app
```

`PlaceDto`, DTO zagnieżdżone oraz `PlaceMapper` odpowiadają za deserializację i mapowanie do modelu domenowego. `JsonResourceReader` jest abstrakcją w `commonMain`, natomiast `AndroidJsonResourceReader` korzysta z Androidowego `Context.assets`. Dzięki temu logika odczytu i mapowania jest współdzielona, ale dostęp do zasobów nie jest jeszcze w pełni niezależny od platformy.

### Repozytoria Wikimedia i Wikidata

Implementacje `WikimediaRepositoryImpl` i `WikidataRepositoryImpl` znajdują się w `shared/commonMain`. Współdzielona warstwa definiuje kontrakty źródeł danych i cache. Konkretną komunikację Retrofit oraz trwałe cache oparte na Room dostarcza moduł Android i przekazuje je do repozytoriów przez `AppContainer`.

Typowa kolejność odczytu danych z tych repozytoriów:

1. cache pamięciowy,
2. cache trwały,
3. zewnętrzne API.

Błąd odczytu lub zapisu cache nie powinien sam w sobie uniemożliwiać próby pobrania danych z API. Błędy są obsługiwane w repozytoriach, a nie powinny powodować niekontrolowanego przerwania całego ekranu.

### Dependency injection

Zależności są centralizowane w `AppContainer`. Kontener tworzy czytnik zasobów, `JsonPlaceDataSource` i `PlacesRepository`, a także konfiguruje repozytoria Wikimedia/Wikidata z adapterami API i cache.

Jest to **ręczne DI**. Hilt nie jest częścią opisanego stanu.

### Ograniczenia znane na tym etapie

- `PlacesRepository.search()` pozostaje do zaimplementowania.
- Czytnik JSON jest dostępny dla Androida; odpowiednik dla iOS nie jest jeszcze opisany jako gotowy.
- Zadeklarowanie targetów iOS w Gradle nie oznacza ukończonej aplikacji iOS ani zweryfikowanego odczytu danych na iOS.
- UI Compose pozostaje w `:app`; ten punkt bazowy nie dokumentuje migracji ekranów do Compose Multiplatform.

---

## Aktualizacja 2 listopada 2026 — miniaturki Wikimedia i współbieżność

### Model metadanych obrazu

`WikimediaImage` przechowuje zarówno oryginalny adres pliku, jak i dane miniatury:

```kotlin
data class WikimediaImage(
    val title: String,
    val url: String,
    val width: Int,
    val height: Int,
    val thumbnailUrl: String?,
    val thumbnailWidth: Int?,
    val thumbnailHeight: Int?,
    val author: String?,
    val license: String?,
    val licenseUrl: String?,
    val filePageUrl: String?,
    val credit: String?
)
```

Pola miniatury są nullable, ponieważ API nie musi zwrócić miniatury dla każdego pliku. Informacje o miniaturze są przenoszone przez cały przepływ danych: DTO Wikimedia → mapper → `WikimediaImage` → `WikimediaEntity` w Room. `WikimediaMapper` mapuje je w obie strony.

`KtorWikimediaDataSource` wysyła parametr `iiurlwidth=1200`. Wikimedia może zwrócić najbliższy dostępny wariant, dlatego rzeczywista szerokość pliku miniatury może różnić się od żądanej.

### Wybór adresu obrazu w UI

Listy miejsc i miast oraz `DetailsPager` preferują miniaturę, a w razie jej braku używają oryginalnego URL:

```kotlin
image.thumbnailUrl ?: image.url
```

W ekranach list lokalne `placeImages` pozostają dodatkowym fallbackiem, gdy metadane Wikimedia nie są jeszcze dostępne. W `DetailsPage` przechowywany jest `pageId`, aby `DetailsPager` mógł pobrać właściwe metadane i nadal wyświetlać atrybucję Wikimedia. Wybór miniatury nie usuwa informacji o autorze, licencji ani stronie pliku.

### Cache metadanych Wikimedia

`WikimediaRepositoryImpl` korzysta z trzech poziomów źródła danych, w tej kolejności:

1. `WikimediaCache` — cache w pamięci,
2. `WikimediaPersistentCache` — trwały cache oparty na Room,
3. `WikimediaDataSource` — API Wikimedia, jeśli danych nie ma w obu cache.

Wynik API jest zapisywany w Room i cache pamięciowym. Odczyt z Room uzupełnia również cache pamięciowy. Błąd odczytu lub zapisu Room jest obsługiwany i nie powinien blokować próby pobrania z API.

### Bezpieczna aktualizacja stanu obrazów

`WikimediaViewModel` przechowuje wyniki w:

```kotlin
private val _images =
    MutableStateFlow<Map<Int, WikimediaImage>>(emptyMap())

val images: StateFlow<Map<Int, WikimediaImage>> = _images
```

Wiele korutyn może kończyć pobieranie w zbliżonym czasie. Aktualizacja mapy musi więc być atomowa:

```kotlin
if (image != null) {
    _images.update { current ->
        current + (pageId to image)
    }
}
```

Nie należy zastępować tego operacją odczyt–modyfikacja–zapis:

```kotlin
_images.value += (pageId to image)
```

Przy równoległych zapisach taka operacja może utracić aktualizację innej korutyny. `update` używa mechanizmu CAS i ponawia obliczenie, jeżeli stan zmienił się między odczytem a zatwierdzeniem. Lambda przekazana do `update` powinna być wolna od efektów ubocznych, ponieważ może zostać wykonana więcej niż raz.

`loadingImagesInternal` jest osobnym stanem zbioru identyfikatorów aktualnie pobieranych obrazów. Dodawanie identyfikatora wykorzystuje `compareAndSet`, a jego usuwanie `update`. `Semaphore(5)` ogranicza liczbę równoległych operacji repozytorium do pięciu; pozostałe korutyny oczekują na permit.

### Weryfikacja

- Test `:shared:testAndroidHostTest` zakończył się `BUILD SUCCESSFUL`.
- Build `:app:assembleDevDebug` zakończył się `BUILD SUCCESSFUL`.
- Testy na urządzeniu potwierdziły wyświetlanie miniaturek na listach i ekranach szczegółów.
- Diagnostyczne logi potwierdziły odczyty `ROOM HIT` oraz `MEMORY HIT`; przy ponownym wejściu do Places nie było potrzeby ponownego pobierania metadanych z API.
- Po zastąpieniu nieatomowej aktualizacji mapy przez `_images.update` obrazy zaczęły wyświetlać się poprawnie w powtarzanych próbach, także po ponownej instalacji aplikacji.

Obserwacje wskazują na wyścig podczas równoległej aktualizacji wspólnej mapy jako przyczynę wcześniejszego, niestabilnego gubienia obrazów. Atomowa aktualizacja rozwiązała obserwowany problem w testach; nie należy jednak traktować pojedynczej serii testów jako formalnego dowodu braku wszystkich możliwych problemów współbieżności.


---

## Aktualizacja 3 listopada 2026 — etap `feature/kmp-ui-migration`

### Wspólne definicje tras

Definicje tras nawigacyjnych zostały przeniesione z modułu Android `app` do:

```text
shared/src/commonMain/kotlin/com/razegon/shared/navigation/Screen.kt
```

Dzięki temu definicje `Screen` są dostępne w kodzie wspólnym. Samo przeniesienie tras **nie oznacza jeszcze**, że cała implementacja nawigacji ani wszystkie ekrany zostały przeniesione do `shared`. Należy rozróżniać współdzielone definicje tras od platformowej integracji nawigacji i UI.

W ramach tego etapu usunięto również pusty placeholder `shared/.../MoroccoApp.kt`, aby nie pozostawiać w module wspólnym pliku, który nie reprezentował rzeczywistego punktu wejścia aplikacji.

### Android UI

W module `app` wprowadzono poprawki obsługi edge-to-edge w `MainActivity` oraz powiązanych ekranach/komponentach UI (`CitiesScreen`, `PlacesScreen`, `DetailsPager`). Są to zmiany warstwy Android UI; nie należy na ich podstawie wnioskować, że te ekrany są już wspólne dla Androida i iOS.

### Odporność odpowiedzi Wikimedia

W `WikimediaResponse.kt` pole `imageInfo` otrzymało domyślną wartość pustej listy. Odpowiedź API, w której brakuje tego pola, może dzięki temu zostać zdeserializowana bez traktowania braku `imageInfo` jako błędu wymagającego wartości nie-null.

### Android Network Security Config — kompromis kompatybilności

Manifest Androida wskazuje `@xml/network_security_config`. Aktualna konfiguracja bazowa ufa zarówno systemowym certyfikatom CA, jak i certyfikatom CA zainstalowanym przez użytkownika:

```xml
<base-config>
    <trust-anchors>
        <certificates src="system" />
        <certificates src="user" />
    </trust-anchors>
</base-config>
```

Takie ustawienie zostało zachowane po teście na tablecie Huawei z Androidem 7: przy tej konfiguracji obrazy wyświetlały się poprawnie, natomiast zawężona konfiguracja dopuszczająca certyfikaty użytkownika tylko dla `commons.wikimedia.org` i `upload.wikimedia.org` powodowała, że część zdjęć nie była wyświetlana.

**Ostrzeżenie bezpieczeństwa:** `src="user"` w `base-config` oznacza zaufanie do certyfikatów CA zainstalowanych przez użytkownika dla połączeń objętych konfiguracją aplikacji, a nie wyłącznie do jednego wskazanego certyfikatu lub jednej domeny. Jest to świadomy kompromis przyjęty dla kompatybilności ze starym urządzeniem. Nie należy opisywać tej konfiguracji jako ograniczonej wyłącznie do Wikimedia.

Jest to konfiguracja Android Network Security Config. Nie konfiguruje ona zaufania TLS na iOS; platforma iOS ma odrębny mechanizm polityki połączeń sieciowych.

### Stan gałęzi i zakres potwierdzonych zmian

Zmiany etapu zostały zapisane na branchu `feature/kmp-ui-migration` i wypchnięte do `origin/feature/kmp-ui-migration`.

Potwierdzone commity:

```text
b90c59e Add Android network security config for user certificates
4801d34 Handle missing Wikimedia image info
3a48cf1 Improve Android edge-to-edge UI handling
cbdbb36 Move navigation routes to shared module
```

Na tym etapie potwierdzono przeniesienie definicji tras do `shared`, poprawki Android UI, domyślną pustą listę `imageInfo` oraz konfigurację sieciową Androida. Nie jest to równoznaczne z ukończeniem migracji Compose UI do `commonMain` ani z gotowością aplikacji iOS.
