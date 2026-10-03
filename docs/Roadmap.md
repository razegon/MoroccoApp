# MoroccoApp -- Roadmap

## Zrobione

### Fundament
- Place
- PlaceType
- MoroccoDataSource
- Repository
- RepositoryImpl
- ViewModel
- StateFlow

### Lista miast
- CitiesScreen
- LazyColumn
- CityListItem
- obsługa kliknięć
- Material 3
- Card / Row / Column
- typografia i hierarchia tekstu
- spacing i layout
- Card shape i elevation

### Obrazy
- Coil 3
- AsyncImage
- obrazy HTTPS
- `INTERNET` permission
- `ContentScale.Crop` dla kart listy
- `ContentScale.Inside` dla zdjęć szczegółów
- `error` i `fallback`
- default image
- test poprawnego URL, braku zdjęcia i błędnego URL

### Navigation i CityDetails
- `Screen` jako sealed class
- `city_details/{cityId}`
- przekazywanie `placeId`
- CityDetailsScreen
- CityDetailsUiState
- CityDetailsViewModel
- CityDetailsViewModelFactory
- `getPlace(placeId)`
- `getChildren(parentPlaceId)`

### Miejsca miasta
- LazyRow
- PlaceListItem
- osobny komponent kart miejsc
- kontrolowana szerokość kart
- odstępy między kartami
- zdjęcia i nazwy miejsc
- kliknięcie i przejście do `PlaceDetailsScreen`

### PlaceDetails
- `PlaceDetailsUiState`
- `PlaceDetailsViewModel`
- `PlaceDetailsViewModelFactory`
- Navigation `place_details/{placeId}`
- PlaceDetailsScreen
- prezentacja nazwy, tłumaczenia i snippet

### System stron szczegółów
- `CityPage`
- `createCityPages()`
- `createPlacePages()`
- `HorizontalPager`
- pierwsza strona z podstawowymi informacjami
- kolejne strony z opisami
- niezależna liczba zdjęć i opisów
- `placeImages: List<String?>`
- `null` jako świadomy brak zdjęcia na konkretnej stronie

### Scroll i collapsing
- `rememberScrollState()` dla każdej strony
- `verticalScroll(scrollState)`
- zdjęcie hero o maksymalnej wysokości 240 dp
- zmniejszanie zdjęcia podczas scrollowania
- zdjęcie znika po osiągnięciu 0 dp
- powrót zdjęcia przy scrollowaniu do góry
- `LocalDensity.current` do przeliczania px/dp

### Wskaźnik stron
- wspólny `PageIndicator`
- aktywny punkt dla `pagerState.currentPage`
- umieszczenie wskaźnika na dole zdjęcia przez `Box` i `Alignment.BottomCenter`

### HTML w opisach
- `HtmlText`
- `Html.fromHtml(..., FROM_HTML_MODE_LEGACY)`
- konwersja `StyleSpan` do Compose `SpanStyle`
- obsługa pogrubienia i kursywy
- `RelativeSizeSpan` dla nagłówków
- obsługa akapitów HTML
- `TextAlign.Justify`

## Aktualny stan

```text
CitiesScreen
↓
CityListItem
↓
Navigation
↓
CityDetailsScreen
↓
HorizontalPager
├── informacje podstawowe + LazyRow
├── description + opcjonalne zdjęcie
├── description + opcjonalne zdjęcie
└── ...
        ↓
   PlaceListItem
        ↓
PlaceDetailsScreen
↓
HorizontalPager
├── informacje podstawowe
├── description + opcjonalne zdjęcie
├── description + opcjonalne zdjęcie
└── ...
```

Oba ekrany szczegółów mają pionowy scroll oraz collapsing zdjęcia. Oba wykorzystują `PageIndicator` i HTML-owe opisy.

## Następnie

1. Dalsze dopracowanie UX ekranów szczegółów.
2. Weather.
3. Maps.
4. Search.
5. Favorites.
6. Room.
7. Hilt.

## Później

- źródła, autorzy i licencje zdjęć, szczególnie Wikimedia Commons
- ewentualne rozszerzenie modelu zdjęcia o URL, źródło, autora i licencję
- dalsze dopracowanie UX i Material 3
- stany loading/error


## Aktualizacja 30–31 sierpnia 2026

### Nowo ukończone
- centralny `WikimediaImageLoader`
- wspólny klient OkHttp z `User-Agent` dla Wikimedia
- zdjęcia Wikimedia dla City i children
- fallback zdjęcia Wikimedia → `placeImages`
- `WikimediaAttributionIcon` i `WikimediaAttributionDialog` w obu ekranach szczegółów
- wspólny `DetailsPager` dla City i Place
- wspólne `createPages()` zamiast `createCityPages()` / `createPlacePages()`
- opcjonalny slot `content()` w `DetailsPager`
- `LazyRow` miejsc przekazywany do City przez `content()`
- descriptions obsługiwane wspólnie przez `DetailsPager`
- poprawiona hierarchia `Box` / `Column`, bez nakładania tekstu i LazyRow na zdjęcie
- build po zakończeniu refactoru

## Aktualny stan

```text
CitiesScreen
↓
CityListItem
↓
Navigation
↓
CityDetailsScreen
↓
DetailsPager
├── informacje podstawowe
├── LazyRow miejsc
└── descriptions + zdjęcia
        ↓
   PlaceListItem
        ↓
PlaceDetailsScreen
↓
DetailsPager
├── informacje podstawowe
└── descriptions + zdjęcia
```

## Najbliższy etap

1. Dalsze dopracowanie UX ekranów szczegółów.
2. Uporządkowanie loading/error.
3. Weather.
4. Maps — w tym przygotowanie konfiguracji `Application` i map OSM.
5. Search.
6. Favorites.
7. Room.
8. Hilt.


## Aktualizacja 31 sierpnia – 1 września 2026

### Zrobione od poprzedniej wersji
- `wikidataId` dodane do `Place` dla 17 miast.
- `WikidataApi` i `WikidataRetrofit`.
- `WikidataRepository` / `WikidataRepositoryImpl`.
- `WikidataInfo` i `WikidataTime`.
- live data City: population, area, elevation, inception.
- `CityDetailsUiState.wikidataInfo`.
- `WikidataInfoSection`.
- wspólny `MoroccoHttpClient` z `User-Agent`.
- `MyApp : Application`.
- `WikidataMemoryCache` i `WikimediaMemoryCache`.
- obsługa błędów Wikimedia bez crashu.
- ograniczenie Wikimedia do 3 równoległych requestów.
- ochrona przed równoległym pobieraniem tego samego `pageId`.
- loading indicator Coil w `DetailsPager`.
- `HorizontalPager.verticalAlignment = Alignment.Top`.
- naturalne proporcje zdjęć na kolejnych stronach; zdjęcia panoramiczne mogą wykorzystywać szerokość ekranu, a pionowe są przewijalne.
- testy offline w obrębie jednej sesji.

### Aktualny stan City

```text
CitiesScreen
↓
CityDetailsScreen
↓
CityDetailsViewModel
├── Place
├── children
└── WikidataInfo
        ↓
DetailsPager
├── zdjęcia Wikimedia / local URL
├── attribution
├── loading indicator
├── name / translate / snippet
├── WikidataInfoSection
└── LazyRow Places
```

### Najbliższy etap
1. Domknięcie infrastruktury cache Wikimedia/Wikidata i dalsze testy offline.
2. Uporządkowanie wspólnego sposobu dostępu do `MyApp`/zależności przed większym wzrostem projektu.
3. Rozszerzenie live data na `PlaceDetails`.
4. Quiz.
5. Mapy OSM.

### Później
- trwały cache/offline po restarcie, najprawdopodobniej z Room,
- Search,
- Favorites,
- Weather,
- Hilt / DI,
- dalsze UX i Material 3,
- źródła, autorzy i licencje zdjęć.

## Aktualizacja 2–3 września 2026

### Zrobione -- infrastruktura

- Room 3
- KSP dla Room
- `AppDatabase`
- `WikidataDao` / `WikidataEntity`
- `WikimediaDao` / `WikimediaEntity`
- `WikidataMapper`
- `WikimediaMapper`
- persistent cache Wikidata
- persistent cache metadanych Wikimedia
- Coil persistent `DiskCache` dla obrazów Wikimedia
- wspólny `WikimediaImageLoader` w `MyApp`
- test offline po restarcie procesu

### Zrobione -- Git

- Git repository dla projektu
- GitHub
- branch `feature/room`
- logiczne commity dla kolejnych etapów Room/cache
- merge `feature/room` → `main` przez `Fast-forward`
- push zaktualizowanego `main` do GitHub

### Zrobione -- Quiz MVP

- `QuizQuestion`
- `QuizRepository`
- `QuizRepositoryImpl`
- `QuizViewModel`
- `QuizViewModelFactory`
- `QuizScreen`
- route `quiz`
- przycisk Quiz na `HomeScreen`
- 10 pytań
- 4 odpowiedzi na pytanie
- jedna poprawna odpowiedź
- blokowanie ponownego wyboru odpowiedzi
- zielone oznaczenie poprawnej odpowiedzi
- czerwone oznaczenie wybranej błędnej odpowiedzi
- przechodzenie przez kolejne pytania
- `PODSUMOWANIE` na ostatnim pytaniu

### Aktualny etap

```text
feature/quiz
    ↓
Quiz MVP ✅
    ↓
QuizSummaryScreen
    ↓
zapis wyniku w Room
    ↓
nickname
    ↓
TOP 10 / TOP 20
```

### Następnie

1. Quiz — ekran podsumowania.
2. Quiz — zapis wyniku w Room.
3. Quiz — nickname gracza.
4. Quiz — ranking TOP 10/20.
5. Rozbudowa danych quizu i UX.
6. Mapy OSM.
7. Search.
8. Favorites.
9. Weather.
10. Hilt / DI.

## Aktualizacja 14 września 2026

### Zrobione -- Quiz

- `QuizQuestion`
- `QuizDifficulty`
- `QuizRepository` / `QuizRepositoryImpl`
- `QuizQuestionDataSource`
- `QuizQuestionDto`
- `QuizQuestionMapper`
- 50 pytań w `questions.json`
- 5 poziomów trudności
- ekran startowy quizu
- ekran wyboru trudności
- klasyczny quiz 10 pytań
- 4 odpowiedzi na pytanie
- jedna poprawna odpowiedź
- blokowanie odpowiedzi po wyborze
- zielone oznaczenie poprawnej odpowiedzi
- czerwone oznaczenie wybranej błędnej odpowiedzi
- ekran podsumowania
- nickname
- zapis wyników do Room
- TOP 10
- poziom trudności wyświetlany obok wyniku
- możliwość przerwania quizu z potwierdzeniem
- reset stanu po przerwaniu quizu

### Zrobione -- UI / Theme

- własny Light Theme
- własny Dark Theme
- wyłączone dynamic colors
- wspólne `Shapes`
- rozszerzona typografia Material 3
- odświeżony `QuizStartScreen`
- odświeżony `QuizDifficultyScreen`
- mały przycisk `X` do opuszczenia quizu

### Zrobione -- Git

- `feature/quiz` rozwijany jako osobny branch
- logiczne commity
- testy Build po kolejnych etapach
- merge `feature/quiz` → `main` przez `Fast-forward`
- push zaktualizowanego `main` do GitHub

## Aktualny stan aplikacji

```text
main
  ↓
Quiz MVP ✅
  ↓
50 pytań JSON ✅
  ↓
5 poziomów trudności ✅
  ↓
TOP 10 + Room ✅
  ↓
Theme Light/Dark ✅
```

## Historyczna wersja planu Arcade -- stan sprzed implementacji

W starszej wersji roadmapy Arcade było jeszcze etapem planowanym. Założenia zapisane wtedy wyglądały następująco:

```text
pula pytań: wszystkie dostępne pytania
czas początkowy: 30 s
poprawna odpowiedź: +5 s
błędna odpowiedź: -3 s
koniec gry: 0 s
```

Zakładano również brak powtórek pytań oraz osobny ranking Arcade. Te wartości były planem roboczym i zostały później zmienione przed ukończeniem implementacji. Aktualne, obowiązujące zasady znajdują się w sekcji poniżej.

## Aktualizacja 3 października 2026 -- Arcade Mode ukończony

### Zrobione -- Arcade

- osobny `ArcadeScreen`
- osobny `ArcadeViewModel`
- osobny `ArcadeGameState`
- wykorzystanie wspólnego `QuizRepository`
- pula wszystkich 50 pytań
- brak powtórek pytań w jednej rozgrywce
- timer startujący od 45 sekund
- `+5 s` za poprawną odpowiedź
- `-5 s` za błędną odpowiedź
- `+1 punkt` za poprawną odpowiedź
- koniec gry przy 0 sekundach
- blokowanie odpowiedzi po wyborze
- przycisk `X` i potwierdzenie wyjścia
- brak zapisu niedokończonej rozgrywki

### Zrobione -- ranking Arcade

- `ArcadeResult`
- `ArcadeResultEntity`
- `ArcadeResultDao`
- `ArcadeResultMapper`
- `ArcadeResultRepository` / `ArcadeResultRepositoryImpl`
- `ArcadeResultViewModel` / `ArcadeResultViewModelFactory`
- osobna tabela Room dla wyników Arcade
- TOP 10 Arcade
- sortowanie `score DESC`, następnie `date ASC`
- nickname ograniczony do 15 znaków
- blokada wielokrotnego zapisu tego samego wyniku
- przyciski `TOP 10 ARCADE` i `MENU QUIZU` po zapisaniu wyniku

### Zrobione -- Room i Navigation

- migracja `MIGRATION_1_2`
- zwiększenie wersji `AppDatabase`
- zachowanie istniejących danych przy migracji
- osobna trasa `QuizArcade`
- osobna trasa `QuizArcadeRanking`
- start gry wykonywany przed nawigacją do Arcade
- zachowanie stanu gry po powrocie z rankingu

### Aktualny stan aplikacji

```text
main
  ↓
Quiz klasyczny + TOP 10 + Room ✅
  ↓
Theme Light/Dark ✅
  ↓
Arcade Mode + TOP 10 Arcade + Room ✅
```

## Następne etapy

1. Mapy OSM.
2. Search.
3. Favorites.
4. Weather.
5. Dalsze dopracowanie UX całej aplikacji.
6. Hilt / DI.
7. Przygotowanie architektury pod KMP.
8. Dalsze źródła, autorzy i licencje zdjęć.
