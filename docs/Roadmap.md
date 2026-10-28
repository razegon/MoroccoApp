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

## Aktualizacja 4 października 2026 -- rozpoczęcie migracji KMP

### Zrobione -- fundament KMP domeny

- `shared` z modelami domenowymi
- `Place` przeniesiony do `shared`
- `PlaceType` w `shared`
- `WikimediaImageRef` w `shared`
- `PlaceContent` w `shared`
- `PlaceFallback` w `shared`
- `PlaceData` w `shared`
- rozdzielenie danych strukturalnych (`Place`) od treści (`PlaceContent`)
- `PlacesRepository` pracujący na `PlaceData`
- `PlacesRepositoryImpl` pracujący na `PlaceData`
- `PlacesViewModel` pracujący na `PlaceData`
- `CitiesViewModel` pracujący na `PlaceData`
- `PlacesUiState` i `CitiesUiState` dostosowane do `PlaceData`
- `CityDetailsUiState` dostosowany do `PlaceData`
- `CitiesScreen` i `CityListItem` dostosowane do `PlaceData`
- `CityDetailsScreen` dostosowany do `PlaceData`
- wspólne `createPages()` dostosowane do `String` descriptions
- `DetailsPage.description` dostosowane do `String?`
- Build OK po migracji
- działanie aplikacji sprawdzone po migracji

### Aktualny przepływ

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

### Stan przejściowy

`MoroccoDataSource` nadal znajduje się w module `app` i korzysta z `R.string.*`. `PlaceContent` używa obecnie `String` jako tymczasowego rozwiązania pozwalającego zweryfikować nowy przepływ danych.

Nie projektujemy jeszcze finalnego formatu lokalizacji ani akapitów. Zostanie to ustalone podczas migracji treści do JSON.

## Najbliższy etap -- DataSource i JSON

1. Przeanalizować kompletny `MoroccoDataSource`.
2. Zaprojektować strukturę JSON na podstawie rzeczywistych danych.
3. Oddzielić dane strukturalne od treści i danych platformowych.
4. Przenieść treści do JSON.
5. Zbudować data source w `shared`, który będzie zwracał `List<PlaceData>`.
6. Zachować istniejący przepływ Repository → ViewModel → UiState → UI.
7. Dopiero po ustabilizowaniu JSON ustalić finalną strategię lokalizacji i formatowania akapitów.

### Zasada kolejnego etapu

Nie łączyć jednocześnie migracji JSON, lokalizacji, UI i kolejnych funkcji. Najpierw doprowadzić DataSource do poprawnego, platform-independent modelu i po każdym większym kroku wykonać Build oraz test działania aplikacji.

## Aktualizacja 4 października 2026 -- JSON DataSource

### Zrobione -- pierwszy etap JSON

- serializowalne DTO w `commonMain`
- `PlaceDto`
- `PlaceContentDto`
- `PlaceFallbackDto`
- `WikimediaImageRefDto`
- `PlaceMapper`
- `JsonPlaceDataSource`
- `JsonResourceReader`
- Androidowa implementacja `AndroidJsonResourceReader`
- `morocco.json` w `shared/src/androidMain/assets`
- `androidResources { enable = true }`
- test `JsonPlaceDataSourceTest`
- poprawne odczytanie JSON na fizycznym urządzeniu
- pierwszy rekord -- Marrakesz
- `TEST` jako tymczasowy opis potwierdzający odczyt z JSON
- poprawne działanie Wikimedia po migracji
- poprawne działanie Wikidata po migracji
- Build OK

### Aktualny przepływ

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
UiState
    ↓
Compose UI
```

### Najbliższy etap -- uporządkowanie DI

1. Scentralizować tworzenie `PlacesRepository`.
2. Przestać tworzyć `PlacesRepositoryImpl` niezależnie w kilku ViewModel factories.
3. Przekazywać tę samą konfigurację Repository do ViewModeli.
4. Wykonać Build i sprawdzić wszystkie ekrany korzystające z `PlacesRepository`.

### Dopiero później -- rozszerzenie JSON

5. Zmienić JSON z jednego obiektu na listę `PlaceDto`.
6. Dodać kilka miejsc testowych.
7. Sprawdzić `CITY`, `parentPlaceId`, `null`, Wikimedia i fallbacki.
8. Dopiero po stabilizacji rozszerzyć JSON do wszystkich miejsc.
9. Usunąć stary hardcoded `MoroccoDataSource`.

### Jeszcze później -- pełna niezależność platformowa

10. Przygotować odpowiednik `JsonResourceReader` dla iOS.
11. Sprawdzić ten sam `JsonPlaceDataSource` na iOS.
12. Dopiero wtedy rozszerzać migrację KMP o kolejne elementy.

### Zasada kolejnego etapu

Nie łączyć jednocześnie:

```text
DI
+
pełnej migracji JSON
+
lokalizacji
+
kolejnych funkcji
```

Najpierw jedna odpowiedzialność, Build, test działania, checkpoint.

Najbliższy konkretny cel:

```text
MyApp / composition root
        ↓
PlacesRepository
        ↓
ViewModel factories
        ↓
ViewModels
```

Hilt pozostaje na późniejszym etapie. Najpierw ręczne DI ma zostać uporządkowane i zrozumiane.
---

## Aktualny stan przed finalnym pushem — baza `95fcc35`

Ta sekcja jest aktualnym podsumowaniem stanu projektu na potrzeby `main`. Historyczne plany poniżej wcześniejszych wpisów dokumentują poprzednie etapy i nie powinny być odczytywane jako aktualny status zadań.

### Zrealizowane

- Moduł `shared` z kodem domenowym i logiką danych Kotlin Multiplatform.
- Rozdzielenie danych miejsca na `Place`, `PlaceContent` i `PlaceData`.
- DTO oraz mapper z JSON do modelu domenowego.
- Źródło danych `JsonPlaceDataSource`, korzystające z `ma_cities.json`, `ma_monuments.json` i `ma_nature.json`.
- `PlacesRepository` i jego implementacja oparte na `PlaceData`.
- Współdzielone implementacje repozytoriów Wikimedia i Wikidata.
- Przekazywanie platformowych adapterów API i cache do repozytoriów z `AppContainer`.
- Ręczne, scentralizowane DI bez Hilt.
- Działanie aplikacji Android i wyświetlanie wszystkich miejsc Maroka — potwierdzone ręcznie przez użytkownika.

### Otwarte zadania

1. **Walidacja przed pushem:** uruchomić build i testy, a asercje testów JSON porównać z aktualnym zestawem rekordów.
2. Zaimplementować `PlacesRepository.search()` i dodać testy wyszukiwania.
3. Dodać lub uzupełnić testy mapowania DTO → `PlaceData`, typów miejsc i relacji rodzic–dziecko.
4. Zaprojektować oraz zaimplementować sposób odczytu zasobów dla iOS; zweryfikować wspólny przepływ danych na każdym zadeklarowanym targetcie.
5. Ustalić docelową strategię lokalizacji i formatowania treści, jeśli aplikacja ma obsługiwać wiele języków.
6. Dopiero po ustabilizowaniu danych i granic platformowych rozważyć dalsze etapy migracji UI.

### Poza zakresem tej bazy

- Migracja ekranów do Compose Multiplatform nie jest częścią stanu `95fcc35`; interfejs Androida pozostaje w `app`.
- Nie należy scalać commitów `2ba5e36` ani `c60215d` tylko po to, by zaktualizować dokumentację.
- Zadeklarowane targety iOS nie oznaczają ukończonej aplikacji iOS.
- Hilt nie jest wdrożony.

### Kolejność dalszych prac

```text
Build + testy na bazie 95fcc35
        ↓
weryfikacja spójności JSON i testów
        ↓
implementacja i testy wyszukiwania
        ↓
platformowy reader zasobów dla iOS
        ↓
weryfikacja współdzielonego przepływu na iOS
        ↓
dopiero potem decyzje o dalszej migracji UI
```
