# MoroccoApp -- Developer Journal

Autor: Kamil Barszczewski

## Cel projektu

Stworzenie nowoczesnej aplikacji podróżniczej w Kotlin + Jetpack Compose jako projektu portfolio.

## Oś czasu

- Koniec lipca 2026 -- model `Place`, listy zdjęć i opisów.
- Początek sierpnia 2026 -- `PlaceType`, `MoroccoDataSource`, Repository.
- 7 sierpnia 2026 -- ViewModel, StateFlow, collectAsState(), LazyColumn, CityListItem, Card/Row/Column, obsługa kliknięcia.
- 10 sierpnia 2026 -- Material 3 UI, Coil, AsyncImage, HTTPS, obsługa braku/błędu zdjęcia.
- 23 sierpnia 2026 -- rozwój Navigation, CityDetailsScreen, ViewModel z `placeId`, Factory oraz hierarchii miasto → miejsca.
- 24–27 sierpnia 2026 -- rozwój `PlaceDetailsScreen`, pagera zdjęć i opisów, HTML w opisach oraz przewijania.
- 28 sierpnia 2026 -- ujednolicenie CityDetails i PlaceDetails: `HorizontalPager`, niezależny scroll stron, collapsing zdjęcia, `PageIndicator`, `List<String?>` dla zdjęć i obsługa HTML.

## Najważniejsze decyzje

- `Place` przechowuje listę zdjęć i opisów.
- Repository jest interfejsem.
- `CityListItem` nie zna ViewModel.
- Najpierw działanie, później wygląd.
- `CityListItem` zgłasza kliknięcie przez `onClick(place)`.
- Pierwsze zdjęcie jest reprezentacyjne na listach.
- `firstOrNull()` zabezpiecza przed pustą listą.
- Nie losujemy zdjęcia podczas recomposition.
- Navigation przekazuje `placeId`, nie cały `Place`.
- Miasto i konkretne miejsce mają osobne ekrany szczegółów.
- Miejsca miasta są pobierane przez `getChildren(parentPlaceId)`.
- `PlaceListItem` jest osobnym komponentem od `CityListItem`.
- `placeImages` na ekranach szczegółów może zawierać `null`, aby określić strony bez zdjęcia.
- Liczba zdjęć i opisów nie musi być taka sama.
- Pierwsza strona pagera ma `index == 0` i służy do podstawowych informacji.
- Kolejne strony łączą zdjęcie z odpowiednim opisem według indeksu strony.
- CityDetails i PlaceDetails korzystają z tego samego wzorca prezentacji treści.

## CityListItem i obrazy

Karta ma `4.dp` elevation, `MaterialTheme.shapes.medium`, Row z `fillMaxWidth()` i `16.dp` padding, zdjęcie 112 dp, Column z `weight(1f)` i hierarchię Material 3.

```text
placeName
↓ 8 dp
placeTranslate
↓ 4 dp
placeSnippet
```

`placeTranslate` jest kursywą.

### Coil

Dodano Coil 3 i obsługę obrazów sieciowych przez OkHttp.

### Internet

Potrzebne było:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Po dodaniu uprawnienia obrazy HTTPS zaczęły działać.

### Obsługa błędów

```kotlin
error = painterResource(R.drawable.default_image)
fallback = painterResource(R.drawable.default_image)
```

Przetestowano poprawny URL, brak zdjęcia i celowo uszkodzony URL.

## Navigation i CityDetails

Po ukończeniu listy miast:

```text
CityListItem
↓
CitiesScreen
↓
Navigation
↓
CityDetailsScreen
```

Route:

```text
city_details/{cityId}
```

Do Navigation przekazywany jest `placeId`.

`CityDetailsScreen` otrzymuje `placeId`, tworzy ViewModel przez `CityDetailsViewModelFactory` i obserwuje `uiState`.

### Factory

Ponieważ ViewModel ma:

```kotlin
CityDetailsViewModel(
    private val placeId: Int
)
```

potrzebna była `ViewModelProvider.Factory`.

Factory sprawdza `modelClass` i tworzy `CityDetailsViewModel(placeId)`.

## CityDetailsUiState i children

Stan:

```kotlin
data class CityDetailsUiState(
    val place: Place? = null,
    val children: List<Place> = emptyList()
)
```

ViewModel pobiera:

```text
repository.getPlace(placeId)
repository.getChildren(placeId)
```

Dzięki temu ekran ma dane miasta oraz jego miejsc.

## CityDetailsScreen

Początkowy układ:

```text
hero image
↓
placeName
↓
placeTranslate
↓
placeSnippet
↓
LazyRow miejsc
```

Następnie ekran został rozbudowany do systemu stron:

```text
HorizontalPager
├── Page 0: informacje podstawowe + LazyRow
├── Page 1: description + opcjonalne zdjęcie
├── Page 2: description + opcjonalne zdjęcie
└── ...
```

### Dlaczego `List<String?>`

Wcześniej każda pozycja listy zdjęć była traktowana jako istniejący URL. Przy różnej liczbie zdjęć i opisów pojawił się problem kontroli tego, na której stronie zdjęcie ma być pokazane.

Rozwiązaniem jest:

```kotlin
List<String?>
```

Dzięki temu DataSource może zawierać np.:

```kotlin
listOf(
    "zdjecie1",
    null,
    "zdjecie3"
)
```

`null` oznacza świadomy brak zdjęcia na tej pozycji. Nie należy wtedy pokazywać placeholdera.

### `createCityPages()` i `createPlacePages()`

Liczba stron:

```kotlin
val pageCount = maxOf(
    images.size,
    descriptions.size
) + 1
```

Dodatkowa strona wynika z tego, że `index == 0` jest zarezerwowany na informacje podstawowe. Dla `index > 0` opis pochodzi z `descriptions[index - 1]`.

## PlaceDetailsScreen

`PlaceDetailsScreen` został doprowadzony do tego samego wzorca co `CityDetailsScreen`.

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
HorizontalPager
```

Dzięki temu również miejsce może mieć różną liczbę zdjęć i opisów oraz puste pozycje zdjęciowe przez `null`.

## Pionowy scroll i collapsing

Każda karta pagera ma własny:

```kotlin
val scrollState = rememberScrollState()
```

i używa go w:

```kotlin
.verticalScroll(scrollState)
```

To ważne, ponieważ poziomy `HorizontalPager` steruje przechodzeniem między stronami, natomiast `scrollState` odpowiada wyłącznie za pionowe przewijanie aktualnej strony.

Zdjęcie hero ma maksymalnie `240.dp`. Jego wysokość jest zmniejszana wraz ze scrollowaniem:

```text
240 dp
↓ scroll
120 dp
↓ scroll
0 dp
```

Po osiągnięciu `0.dp` zdjęcie znika, a treść poniżej pozostaje na ekranie.

### Dlaczego `LocalDensity.current`

`scrollState.value` jest wartością w pikselach, natomiast wysokość layoutu jest podawana w `dp`. Dlatego do przeliczenia używany jest:

```kotlin
val density = LocalDensity.current
```

oraz:

```kotlin
with(density) {
    (imageMaxHeight.toPx() - scrollState.value)
        .coerceAtLeast(0f)
        .toDp()
}
```

To pozwala jawnie wykonywać konwersję między jednostkami używanymi przez Compose.

## PageIndicator

Dodano wspólny komponent `PageIndicator`, który pokazuje liczbę stron i aktualną stronę:

```text
● ○ ○ ○
```

Komponent otrzymuje:

```kotlin
pageCount: Int
currentPage: Int
modifier: Modifier
```

Na ekranie szczegółów jest umieszczany wewnątrz `Box` zdjęcia i wyrównywany do `Alignment.BottomCenter`.

## HTML w opisach

Opisy w `strings.xml` były już zapisane z użyciem HTML, m.in. `<p>`, `<h4>`, `<h5>` i `<b>`. Zamiast traktować je jak zwykły tekst, utworzono komponent `HtmlText`.

Podstawą jest:

```kotlin
Html.fromHtml(
    text,
    Html.FROM_HTML_MODE_LEGACY
)
```

Następnie odczytywane są spany Androida i tłumaczone na style Compose.

### StyleSpan

`StyleSpan` jest zamieniany na `SpanStyle`:

```text
Typeface.BOLD
↓
FontWeight.Bold

Typeface.ITALIC
↓
FontStyle.Italic

Typeface.BOLD_ITALIC
↓
FontWeight.Bold + FontStyle.Italic
```

### RelativeSizeSpan

HTML-owe nagłówki generują `RelativeSizeSpan`. Jest on zamieniany na `fontSize` w `SpanStyle`.

Dzięki temu nagłówki typu `<h4>` / `<h5>` mogą być większe od tekstu podstawowego.

### Justowanie

`HtmlText` otrzymał parametr:

```kotlin
textAlign: TextAlign = TextAlign.Justify
```

Dzięki temu ekran szczegółów może przekazać:

```kotlin
textAlign = TextAlign.Justify
```

bez mieszania odpowiedzialności za wyrównanie tekstu z logiką konwersji HTML.

## Najważniejszy postęp

Projekt przeszedł od prostego wyświetlania listy do świadomego rozdzielania odpowiedzialności:

- UI odpowiada za prezentację,
- ViewModel za stan,
- Repository za dane,
- Navigation przekazuje identyfikatory,
- komponenty listowe przekazują zdarzenia przez callbacki,
- `HorizontalPager` odpowiada za poziomą nawigację między stronami,
- `scrollState` odpowiada za pionowy scroll konkretnej strony,
- `HtmlText` odpowiada za prezentację formatowanego HTML.

Ważne stało się także projektowanie danych pod zachowanie UI, np. `List<String?>` pozwalające jawnie określić brak zdjęcia na konkretnej stronie.

## Kolejne decyzje

- dalsze dopracowanie UX ekranów szczegółów,
- loading/error,
- Weather,
- Maps,
- Search,
- Favorites,
- Room,
- Hilt,
- źródła, autorzy i licencje zdjęć.


## 30–31 sierpnia 2026 -- Wikimedia ImageLoader i refactor DetailsPager

### Centralizacja ImageLoader

Zauważono, że konfiguracja `ImageLoader` była tworzona w kilku miejscach (`PlaceDetailsScreen`, `CityDetailsScreen`, `CityListItem`). Konfigurację przeniesiono do `data/image/WikimediaImageLoader.kt`. Wspólny klient OkHttp ustawia wymagany `User-Agent`, a `createWikimediaImageLoader(context)` tworzy właściwy Coil `ImageLoader`.

Rozwiązanie usunęło duplikację konfiguracji i przygotowuje projekt do dalszej centralizacji zależności. W przyszłości, wraz z dodaniem map OSM, planowane jest wprowadzenie własnej klasy `Application`, analogicznie do rozwiązania zastosowanego wcześniej w GlobeTrotter.

### Zdjęcia Wikimedia

W trakcie testów bezpośrednie żądania do Wikimedia kończyły się `HTTP 403`. Po zastosowaniu wspólnego klienta OkHttp z `User-Agent` obrazy zaczęły działać.

W `CityDetailsScreen` pobierane są również pierwsze zdjęcia Wikimedia dzieci miasta, dzięki czemu `PlaceListItem` może używać zdjęcia Wikimedia jako reprezentacyjnego obrazu.

### Refactor wspólnej logiki szczegółów

`CityDetailsScreen` i `PlaceDetailsScreen` zawierały dużą ilość identycznej logiki. Utworzono wspólny komponent `ui/components/DetailsPager.kt`. Przeniesiono do niego `HorizontalPager`, scroll, collapsing hero image, `AsyncImage`, `PageIndicator`, Wikimedia attribution, podstawowe informacje oraz descriptions.

Wspólna funkcja `createPages()` zastąpiła wcześniejsze osobne `createCityPages()` i `createPlacePages()`.

Różnicę między ekranami rozwiązano przez opcjonalny slot: `content: @Composable () -> Unit = {}`. `CityDetailsScreen` przekazuje tam `LazyRow` z miejscami, natomiast `PlaceDetailsScreen` korzysta z pustej wartości domyślnej.

Podczas refactoru pojawił się błąd wynikający z umieszczenia tekstów i `content()` wewnątrz `Box` zdjęcia. Poprawna hierarchia to `Column` → `Box` (zdjęcie i elementy nakładane) → teksty → `content()`. `Box` służy do nakładania elementów, a `Column` do układania treści pionowo.

Refactor zakończono pomyślnie i wykonano build projektu.


## 31 sierpnia – 1 września 2026 -- Wikidata, cache i stabilizacja pobierania

### Live data z Wikidata dla miast

Rozpoczęto integrację z Wikidata jako źródłem dynamicznych danych o miastach. Do modelu `Place` dodano `wikidataId`, dzięki czemu każdy obiekt może wskazywać odpowiadającą mu encję Wikidata.

Utworzono:
- `WikidataApi.kt`,
- `WikidataRetrofit.kt`,
- `WikidataRepository` oraz `WikidataRepositoryImpl`,
- `WikidataInfo`,
- `WikidataTime`.

Dla City pobierane są obecnie cztery dane:
- populacja (`P1082`),
- powierzchnia (`P2046`) normalizowana do km²,
- wysokość nad poziomem morza (`P2044`) normalizowana do metrów,
- początek istnienia (`P571`) jako `WikidataTime`, a nie tylko pojedynczy rok.

`WikidataTime` przechowuje `year`, `precision` oraz `raw`, ponieważ wartości historyczne nie zawsze powinny być prezentowane jako prosty „rok założenia”.

### Walidacja danych City

Uzupełniono `wikidataId` dla wszystkich 17 miast. Test kompletności wykazał:
- population: 16/17,
- area: 10/17,
- elevation: 15/17,
- inception: 7/17.

Brak danej w Wikidata jest traktowany jako normalny stan danych. Pola w `WikidataInfo` są nullable, a UI pokazuje tylko dostępne wartości.

### Obsługa HTTP i wspólny klient

Początkowe żądania do Wikidata zwracały `HTTP 403`. Przyczyną była identyfikacja klienta HTTP. Utworzono wspólny klient `MoroccoHttpClient` z odpowiednim `User-Agent`, wykorzystywany przez Wikimedia, Wikidata i konfigurację Coil.

Podczas testów wystąpiło również `HTTP 429 Too Many Requests`. Zamiast wysyłać żądania bez ograniczeń, pobieranie Wikimedia ograniczono do maksymalnie trzech równoległych requestów przez `Semaphore(3)`. Po zakończeniu jednego requestu następny oczekujący może od razu rozpocząć wykonanie.

Dodatkowo `WikimediaRepositoryImpl` obsługuje błędy sieciowe przez `try/catch`, dzięki czemu błędne odpowiedzi API nie powinny powodować crashu aplikacji.

### Memory cache

Dodano wspólne cache w pamięci dla danych online:
- `WikidataMemoryCache`,
- `WikimediaMemoryCache`.

Utworzono własną klasę `MyApp : Application`, która przechowuje wspólne instancje cache. Dzięki temu CityDetails i PlaceDetails mogą korzystać z tej samej pamięci cache zamiast tworzyć nowe obiekty przy każdym ekranie.

Dla Wikidata potwierdzono w Logcat działanie `CACHE MISS → API → CACHE HIT`. Dla Wikimedia również potwierdzono ponowne wykorzystanie danych w ramach tej samej sesji aplikacji.

Memory cache nie przetrzymuje danych po zakończeniu procesu aplikacji. Test po ubiciu procesu i ponownym uruchomieniu bez internetu wykazał brak danych. Po wyłączeniu internetu bez ubijania procesu dane z cache były nadal dostępne.

### Ochrona przed duplikacją i ograniczenie współbieżności Wikimedia

`WikimediaViewModel` ma zbiór `loadingImages`, który blokuje ponowne uruchomienie requestu dla tego samego `pageId`, gdy poprzedni request nadal trwa. Dodatkowo `Semaphore(3)` ogranicza liczbę jednoczesnych requestów do trzech.

Test Logcat potwierdził wzorzec:
```text
START 1
START 2
START 3
END 2
START 4
END 1
START 5
...
```
co oznacza prawidłowe wykorzystanie wolnych miejsc w limicie trzech requestów.

### Loading indicator zdjęcia

Początkowo pasek ładowania był powiązany z pobieraniem metadanych Wikimedia i znikał zanim duży plik obrazu został rzeczywiście pobrany. Zmieniono więc źródło stanu: `DetailsPager` wykorzystuje callbacki Coil `onLoading`, `onSuccess` i `onError`.

Dodano cienki `LinearProgressIndicator` na górnej krawędzi obszaru zdjęcia. Pasek pokazuje rzeczywisty stan ładowania pliku obrazu, a nie pobierania metadanych. Nie wprowadzono procentowego postępu, ponieważ obecny mechanizm nie śledzi jeszcze liczby pobranych bajtów.

### Zachowanie zdjęć w pagerze

`HorizontalPager` otrzymał `verticalAlignment = Alignment.Top`, co usunęło zauważalne przesunięcie/wyśrodkowanie strony podczas zmiany karty, szczególnie przy rotacji ekranu.

Dla `Page 0` zdjęcie wykorzystuje `ContentScale.FillWidth`, aby wypełniać szerokość ekranu. Dla kolejnych stron wysokość zdjęcia jest wyliczana z jego rzeczywistego aspect ratio, dzięki czemu zdjęcia panoramiczne mogą być szerokie, a pionowe wysokie i przewijalne.

### Offline -- aktualny stan

W bieżącej sesji aplikacja może korzystać z danych Wikidata i metadanych Wikimedia z MemoryCache po utracie internetu. Zdjęcia mogą być również nadal dostępne dzięki cache Coil w ramach testowanej sesji. Po całkowitym zakończeniu procesu pamięć cache jest tracona.

Trwały offline cache pozostaje na później. Room nie został jeszcze wdrożony; decyzja została świadomie odłożona do momentu, gdy będzie potrzebny trwały storage po restarcie aplikacji.

### Kolejny kierunek

Po domknięciu obecnego etapu City planowane jest rozszerzenie mechanizmu Wikidata na `PlaceDetails`. Rozważane są także kolejne funkcje aplikacji, m.in. Quiz i mapa. Przy rozwoju infrastruktury dane online powinny korzystać z jednolitego modelu cache/offline.

## 2–3 września 2026 -- Git, Room i rozpoczęcie Quizu

### Git i GitHub

Projekt został podłączony do Git i GitHub. Utworzono branch `feature/room`, na którym wykonano trzy logiczne commity:

```text
chore: configure Room and KSP
feat: add persistent Wikidata cache
feat: add persistent Wikimedia cache
```

Po testach offline branch `feature/room` został scalony do `main` metodą `Fast-forward`.

Do kontroli historii i zmian wykorzystano:

```bash
git status
git log --oneline --graph --decorate --all
git diff --stat main..feature/room
git diff --name-status main..feature/room
git merge feature/room
git push origin main
```

### Room -- persistent cache

Do projektu wdrożono Room 3 + KSP. Dane Wikidata i metadane Wikimedia otrzymały warstwę trwałego storage'u.

Powstały encje, DAO, mappery oraz wspólna `AppDatabase`. Repository korzysta teraz z kolejności:

```text
MemoryCache → Room → API
```

Po sukcesie API dane są zapisywane do Room i MemoryCache.

Wikimedia obrazy mają dodatkowo cache plików Coil.

Przeprowadzono test po zakończeniu procesu aplikacji i ponownym uruchomieniu bez internetu. Room zwracał zapisane dane, a zdjęcia były dostępne offline.

### Rozpoczęcie Quizu

Utworzono branch `feature/quiz`.

Pierwsza wersja quizu została zbudowana etapami:

1. `QuizQuestion`
2. `QuizRepository`
3. `QuizRepositoryImpl`
4. `QuizViewModel`
5. `QuizViewModelFactory`
6. `QuizScreen`
7. Navigation
8. przycisk Quiz na `HomeScreen`

Aktualnie quiz ma 10 pytań, 4 odpowiedzi na pytanie i jedną poprawną odpowiedź.

Mechanizm odpowiedzi:

```text
kliknięcie odpowiedzi
        ↓
sprawdzenie poprawności
        ↓
poprawna → zielona
błędna → czerwona
poprawna odpowiedź → zielona
        ↓
blokada dalszego wyboru
        ↓
NASTĘPNE / PODSUMOWANIE
```

Przetestowano przejście przez wszystkie 10 pytań.

### Następny etap

Następnym krokiem jest ekran podsumowania, a następnie zapis wyniku gracza w Room i ranking TOP 10/20.

## 14 września 2026 -- Domknięcie Quiz MVP i przygotowanie Arcade

### Quiz
- rozszerzono quiz do 50 pytań,
- utworzono 5 poziomów trudności,
- pytania przeniesiono z hardkodowanej listy do `questions.json`,
- dodano `QuizQuestionDto`, `QuizQuestionMapper` i `QuizQuestionDataSource`,
- dodano ekran wyboru trudności,
- dodano ekran startowy quizu,
- dodano TOP 10 w Room,
- wynik zawiera informację o poziomie trudności,
- dodano możliwość przerwania quizu z potwierdzeniem,
- przerwanie quizu resetuje stan bieżącej rozgrywki.

### UI / Theme
- dodano własną paletę Light/Dark,
- wyłączono dynamic colors,
- dodano wspólne `Shapes`,
- rozszerzono typografię Material 3,
- poprawiono wygląd ekranów startowych quizu,
- przycisk wyjścia zmieniono na mały `X` w prawym górnym rogu.

### Git
`feature/quiz` został przetestowany i scalony do `main` przez `Fast-forward`.
Zaktualizowany `main` został wypchnięty do GitHub.

### Następny etap
Nowy branch będzie przeznaczony na `Arcade Mode`.

## 14 września 2026 -- zakończenie etapu Quiz i przygotowanie Arcade

Dzisiejszy etap domknął pierwszą pełną wersję quizu i przygotował projekt pod kolejny tryb gry.

### Dane quizu

Pytania zostały przeniesione z kodu Kotlin do:

```text
app/src/main/assets/quiz/questions.json
```

Baza zawiera 50 pytań rozłożonych po 10 na każdy poziom trudności: `VERY_EASY`, `EASY`, `MEDIUM`, `HARD` i `VERY_HARD`.

Dodano warstwę danych:

```text
QuizQuestionDataSource
QuizQuestionDto
QuizQuestionMapper
```

Dzięki temu `QuizRepositoryImpl` nie zawiera już listy pytań, tylko korzysta ze źródła danych JSON.

### Wybór trudności

Dodano `QuizDifficulty` do warstwy domenowej oraz osobny `QuizDifficultyScreen`. Wybrany poziom trafia do `QuizViewModel.startQuiz(difficulty)` i jest zachowywany razem z wynikiem.

### Ranking

`QuizResult` został rozszerzony o `difficulty`. TOP 10 wyświetla teraz poziom obok wyniku. Ranking nadal priorytetowo traktuje liczbę poprawnych odpowiedzi; poziom trudności jest informacją, a nie mnożnikiem punktów.

### Przerwanie rozgrywki

Dodano możliwość wyjścia z quizu podczas pytań. Zamiast dużego przycisku zastosowano mały `X` w prawym górnym rogu. Wyjście wymaga potwierdzenia. Potwierdzone wyjście resetuje stan bieżącej rozgrywki i nie zapisuje wyniku.

### UI / Theme

Rozpoczęto spójny redesign Material 3 dla całej aplikacji:
- własna paleta Light/Dark,
- wyłączenie dynamic colors,
- wspólne `Shapes`,
- rozszerzona typografia,
- odświeżony `QuizStartScreen`,
- odświeżony `QuizDifficultyScreen`,
- mały przycisk `X` zamiast pełnoszerokiego przycisku wyjścia.

Każda logiczna zmiana była kończona Buildem i testem działania.

### Git

Etap `feature/quiz` został zamknięty po testach i scalony do `main` przez `Fast-forward`. `main` został wypchnięty do GitHub.

### Następny etap -- Arcade Mode

Ustalono wstępne zasady:

```text
pula: wszystkie pytania
czas startowy: 30 s
poprawna odpowiedź: +5 s
błędna odpowiedź: -3 s
koniec: 0 s
```

W jednej rozgrywce pytanie nie powinno pojawić się drugi raz. Szczegóły wyniku i porównywania Arcade z klasycznym rankingiem pozostają do zaprojektowania przed implementacją.


## 3 października 2026 -- implementacja Arcade Mode

Po zakończeniu klasycznego Quizu rozpoczęto osobny etap `Arcade Mode`. Arcade został zbudowany jako oddzielny tryb gry, ale korzysta z tej samej puli pytań i istniejącego `QuizRepository`.

### Zasady Arcade

Ustalono i zaimplementowano następujące zasady:

```text
pula: wszystkie 50 pytań
czas początkowy: 45 s
poprawna odpowiedź: +5 s i +1 punkt
błędna odpowiedź: -5 s
koniec gry: 0 s
wynik: liczba poprawnych odpowiedzi
```

Pytania są losowane bez powtórzeń w obrębie jednej rozgrywki. `ArcadeGameState.usedQuestionIds` przechowuje identyfikatory już wykorzystanych pytań.

### Oddzielny ViewModel i stan gry

Dla Arcade utworzono osobny `ArcadeViewModel` oraz `ArcadeGameState`. Dzięki temu timer, licznik czasu i reguły Arcade nie są mieszane z logiką klasycznego quizu.

Stan obejmuje m.in. aktualne pytanie, wybraną odpowiedź, wynik, pozostały czas, użyte pytania i `isGameOver`.

### Timer i odpowiedzi

Timer jest zarządzany przez `ArcadeViewModel`. Poprawna odpowiedź zwiększa czas o 5 sekund i wynik o 1, a błędna zmniejsza czas o 5 sekund. Osiągnięcie zera kończy grę.

Po udzieleniu odpowiedzi wybór jest blokowany, a kolejne pytanie jest dostępne dopiero po przejściu dalej.

### Wyniki Arcade i Room

Ponieważ wynik Arcade ma inny model niż wynik klasycznego quizu, utworzono osobny zestaw klas:

```text
ArcadeResult
ArcadeResultEntity
ArcadeResultDao
ArcadeResultMapper
ArcadeResultRepository
ArcadeResultRepositoryImpl
ArcadeResultViewModel
ArcadeResultViewModelFactory
```

Ranking Arcade jest przechowywany w osobnej tabeli `ArcadeResultEntity`. Wyniki są sortowane po `score DESC`, a następnie `date ASC`.

### Migracja Room

Dodanie tabeli Arcade wymagało migracji bazy z wersji 1 do 2 (`MIGRATION_1_2`). Migracja tworzy tabelę `ArcadeResultEntity` i zachowuje istniejące dane.

### Zapis wyniku

Wynik jest zapisywany dopiero po zakończeniu gry. Nick ma maksymalnie 15 znaków i nie może być pusty po `trim()`. Po zapisaniu wynik jest blokowany przed kolejnym zapisem w tej samej rozgrywce.

Po poprawnym zapisie pojawiają się przyciski `TOP 10 ARCADE` i `MENU QUIZU`.

### Wyjście z gry

`ArcadeScreen` otrzymał mały przycisk `X` w prawym górnym rogu oraz `AlertDialog`, analogicznie do klasycznego quizu. Potwierdzone wyjście resetuje bieżącą rozgrywkę i nie zapisuje jej do rankingu.

### Navigation

Uruchomienie Arcade wykonuje `arcadeViewModel.startGame()` przed nawigacją do `ArcadeScreen`. Nie uruchamiamy `startGame()` automatycznie w samym route ekranu. Dzięki temu przejście z `ArcadeScreen` do `ArcadeRankingScreen` i powrót nie resetuje trwającej rozgrywki.

### Testy i poprawki

Podczas implementacji wykonano Build po kolejnych zmianach oraz testy manualne. Sprawdzono zapis wyniku, ochronę przed podwójnym zapisem, ranking Arcade oraz nawigację między grą, rankingiem i menu quizu.

Jednym z ważnych błędów było wcześniejsze uruchamianie `startGame()` przy wejściu na route Arcade. Powodowało to rozpoczęcie nowej gry po powrocie z rankingu. Przeniesienie startu gry do akcji `onArcadeClick` usunęło ten problem.
