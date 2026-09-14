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
