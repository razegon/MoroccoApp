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
