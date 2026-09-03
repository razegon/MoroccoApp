# Lessons Learned

## Compose i podstawy UI

- StateFlow vs collectAsState()
- Callback `(Place) -> Unit`
- LazyColumn i `items()`
- LazyRow i `items()`
- Card / Row / Column
- stringResource()
- `?.let`
- `R.string` to `Int`, nie `String`
- `Modifier.padding()`, `fillMaxWidth()`, `size()`, `weight()`, `width()`, `height()`
- `Arrangement.spacedBy()`
- Material 3 Typography
- `FontStyle.Italic`
- Card `shape` i `elevation`
- `Box` do nakładania elementów, np. PageIndicator na zdjęciu
- `Alignment.BottomCenter` do pozycjonowania elementu wewnątrz `Box`
- `TextAlign.Justify` do justowania tekstu

## ViewModel i stan

- ViewModel zarządza stanem potrzebnym do wyświetlenia ekranu.
- `MutableStateFlow` pozostaje wewnętrznym stanem ViewModelu, a na zewnątrz wystawiamy `StateFlow`.
- `viewModel()` zachowuje instancję zgodnie z cyklem życia właściciela Compose.
- `init` wykonuje się przy inicjalizacji ViewModelu.
- Jeden UiState może zawierać główny obiekt oraz powiązaną listę.
- `CityDetailsUiState` zawiera `place` i `children`.

## ViewModel z argumentem

- ViewModel z konstruktorem wymagającym `placeId` wymaga dodatkowej konfiguracji.
- `ViewModelProvider.Factory` pozwala przekazać argument do ViewModelu.
- Factory implementuje `create(modelClass)`.
- `modelClass.isAssignableFrom(...)` sprawdza oczekiwany typ.
- Factory tworzy ViewModel z przekazanym argumentem.

Schemat:

```text
Navigation argument
↓
String
↓
Int
↓
ViewModelProvider.Factory
↓
ViewModel(placeId)
```

## Odpowiedzialność komponentów

- `CitiesScreen` pobiera `uiState` i wyświetla listę.
- `CityListItem` odpowiada za miasto.
- `PlaceListItem` odpowiada za miejsce w `LazyRow`.
- Komponenty listowe nie decydują o Navigation.
- Zdarzenie przekazywane jest przez `onClick: (Place) -> Unit`.
- `Card(onClick = ...)` pozwala zrobić całą kartę klikalną.

## Navigation

- Navigation nie powinna przekazywać całego `Place`.
- Przekazywany jest `placeId`.
- Route zawiera argument, np. `city_details/{cityId}` lub `place_details/{placeId}`.
- Ekran szczegółów pobiera pełne dane przez ViewModel i Repository.

## Hierarchia danych

- `Place` może reprezentować miasto lub konkretne miejsce.
- `placeType` określa typ obiektu.
- `parentPlaceId` wiąże miejsce z miastem.
- `getChildren(parentPlaceId)` pobiera miejsca miasta.
- `CityDetailsScreen` prezentuje je w `LazyRow`.

## Layout i UI

- `fillMaxWidth()` wykorzystuje dostępną szerokość.
- `padding()` może określać odstęp od otoczenia lub od krawędzi zawartości.
- `weight(1f)` pozwala Column wykorzystać pozostałą szerokość.
- `Arrangement.spacedBy()` daje równe odstępy.
- Karta miasta używa `MaterialTheme.shapes.medium` i `4.dp` elevation.
- CityDetailsScreen może mieć hero image na pełną szerokość, a tekst z własnym paddingiem.
- LazyRow powinien mieć kontrolowaną szerokość kart i odstępy.
- `Box` jest przydatny, gdy element ma być nałożony na inny element.

## HorizontalPager

- `HorizontalPager` pozwala potraktować kolejne elementy jako osobne karty przewijane poziomo.
- `rememberPagerState()` przechowuje aktualną stronę pagera.
- `pagerState.currentPage` może sterować UI, np. `PageIndicator`.
- Pager i pionowy scroll rozwiązują dwa różne kierunki nawigacji: poziomy pager wybiera stronę, a `verticalScroll()` przewija treść strony.

Schemat:

```text
HorizontalPager
├── Page 0
├── Page 1
├── Page 2
└── ...

każda Page
↓
rememberScrollState()
↓
verticalScroll()
```

## Projektowanie stron na podstawie danych

Gdy liczba zdjęć i opisów jest różna, nie należy zakładać prostego `images[index]` dla każdej strony.

Zastosowano:

```kotlin
val pageCount = maxOf(
    images.size,
    descriptions.size
) + 1
```

Dodatkowa strona wynika z rezerwacji `index == 0` na podstawowe informacje.

Dla pozostałych stron:

```kotlin
description = if (index > 0) {
    descriptions.getOrNull(index - 1)
} else {
    null
}
```

`getOrNull()` jest ważne, ponieważ liczba elementów listy może być różna.

## `List<String?>` i świadomy brak zdjęcia

Wcześniej brak zdjęcia był łatwo mylony z błędem ładowania. Dla ekranów szczegółów potrzebna była większa kontrola.

```kotlin
List<String?>
```

umożliwia:

```kotlin
listOf(
    "zdjecie1",
    null,
    "zdjecie3"
)
```

Znaczenie:

```text
index 0 → pokaż zdjęcie
index 1 → nie pokazuj zdjęcia
index 2 → pokaż zdjęcie
```

To ważne rozróżnienie:

```text
null jako świadomy brak zdjęcia
≠
error podczas ładowania istniejącego URL
```

Dlatego `null` nie powinien automatycznie uruchamiać placeholdera.

## Scroll i collapsing

- `rememberScrollState()` przechowuje pozycję pionowego scrolla.
- `verticalScroll(scrollState)` musi używać tego samego obiektu `scrollState`, który jest używany do obliczenia wysokości zdjęcia.
- `scrollState.value` jest wartością w pikselach.
- Wysokość layoutu jest wyrażana w `dp`.
- `coerceAtLeast(0f)` zabezpiecza przed ujemną wysokością.

Mechanizm:

```text
imageMaxHeight
↓
imageMaxHeight - scrollState.value
↓
coerceAtLeast(0f)
↓
imageHeight
```

### LocalDensity

`LocalDensity.current` daje dostęp do gęstości ekranu potrzebnej do konwersji jednostek.

```kotlin
val density = LocalDensity.current
```

Przeliczenie:

```kotlin
with(density) {
    (imageMaxHeight.toPx() - scrollState.value)
        .coerceAtLeast(0f)
        .toDp()
}
```

Wniosek: gdy operujemy na `scrollState.value` i wymiarach Compose, warto jawnie kontrolować konwersję px/dp zamiast polegać na niejawnych przeliczeniach.

## PageIndicator

- Wskaźnik stron może być osobnym, współdzielonym komponentem.
- Rodzic przekazuje `pageCount`, `currentPage` i `modifier`.
- `modifier` nie powinien być ignorowany w komponencie, ponieważ to rodzic decyduje o położeniu elementu.
- Umieszczenie wskaźnika w `Box` pozwala położyć go na zdjęciu.

## Obrazy i Coil

- `placeImages` w danych szczegółów jest listą `String?`.
- Reprezentacyjne zdjęcie może być wybierane przez `firstOrNull()`.
- Nie losujemy zdjęcia podczas recomposition.
- Coil ładuje obrazy przez `AsyncImage`.
- Obrazy sieciowe korzystają z `coil-network-okhttp`.
- Aplikacja potrzebuje `INTERNET`.
- `ContentScale.Crop` wypełnia ustalony obszar i może przyciąć zdjęcie.
- `ContentScale.Inside` pozwala pokazać całe zdjęcie.
- `fallback` i `error` dotyczą stanów ładowania obrazu; świadome `null` w pagerze powinno być obsłużone przez brak `AsyncImage`, a nie przez placeholder.
- Sam komunikat o chwilowym contention w `DiskLruCache` nie musi oznaczać błędu, jeśli obraz finalnie się ładuje.

## HTML i AnnotatedString

Opisy są przechowywane jako `R.string`, ale ich zawartość może zawierać HTML.

Podstawą jest:

```kotlin
Html.fromHtml(
    text,
    Html.FROM_HTML_MODE_LEGACY
)
```

Android zwraca `Spanned`, czyli tekst z informacją o zakresach stylów.

### StyleSpan

`StyleSpan` opisuje m.in. pogrubienie i kursywę. Można go przełożyć na Compose:

```text
Typeface.BOLD
→ FontWeight.Bold

Typeface.ITALIC
→ FontStyle.Italic

Typeface.BOLD_ITALIC
→ FontWeight.Bold + FontStyle.Italic
```

### RelativeSizeSpan

Nagłówki HTML mogą generować `RelativeSizeSpan`. Odczytanie `span.sizeChange` pozwala przełożyć względną wielkość na `SpanStyle(fontSize = ...)`.

Dzięki temu `<h4>` i `<h5>` mogą zostać wizualnie odróżnione od tekstu podstawowego bez ręcznego tworzenia osobnych stringów Compose.

### `buildAnnotatedString`

`buildAnnotatedString` pozwala zbudować jeden tekst, a następnie nakładać `SpanStyle` na konkretne zakresy znaków.

Ważna rzecz: najpierw trzeba dodać cały tekst przez `append()`, a następnie używać indeksów spanów Androida do nałożenia stylów.

## Justowanie tekstu

`TextAlign.Justify` odpowiada za wyrównanie tekstu do obu stron.

W `HtmlText` można wystawić parametr:

```kotlin
textAlign: TextAlign = TextAlign.Justify
```

i przekazać go do:

```kotlin
Text(
    text = annotatedString,
    textAlign = textAlign
)
```

Wniosek: parametr UI, taki jak wyrównanie tekstu, powinien być przekazywany do komponentu zamiast wpisywania go na stałe w jego wnętrzu.

## Najważniejszy postęp

Projektowanie obejmuje teraz:
- odpowiedzialność klas,
- przepływ danych,
- stany UI,
- identyfikatory Navigation,
- hierarchię danych,
- obsługę brakujących/błędnych obrazów,
- rozdzielenie ekranów miasta i miejsca,
- poziomą nawigację stron,
- niezależny pionowy scroll stron,
- collapsing hero image,
- wskaźnik stron,
- konwersję HTML do stylowanego tekstu Compose.

Zasada pozostaje ta sama: najpierw sprawdzić przepływ danych i działanie, potem dopracowywać wygląd.

## Kolejny etap nauki

Po ukończeniu obecnego mechanizmu szczegółów kolejne większe obszary to:

```text
UX ekranów szczegółów
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


## 30–31 sierpnia 2026 -- refactor i centralizacja

### Centralizacja ImageLoader

Jeżeli kilka komponentów potrzebuje identycznej konfiguracji Coil/OkHttp, nie warto tworzyć `ImageLoader` osobno w każdym miejscu. Konfigurację Wikimedia przeniesiono do `WikimediaImageLoader.kt`, a ekrany korzystają z `createWikimediaImageLoader(context)`.

Wymagany `User-Agent` był istotny, ponieważ Wikimedia zwracało `HTTP 403` przy nieprawidłowo zidentyfikowanych żądaniach.

### Slot `content()`

`content: @Composable () -> Unit = {}` jest prostym sposobem na rozszerzenie wspólnego komponentu bez dodawania do niego wielu warunków `if`.

```kotlin
DetailsPager(...) {
    LazyRow {
        ...
    }
}
```

Domyślne `= {}` sprawia, że PlaceDetails nie musi przekazywać dodatkowej zawartości.

### Box kontra Column

`Box` układa elementy w tej samej przestrzeni, więc świetnie nadaje się do umieszczania ikon i wskaźnika na zdjęciu. Nie powinien jednak zawierać tekstów i `LazyRow`, które mają znajdować się pod zdjęciem. Do tego służy `Column`.

Poprawny wzorzec:

```text
Column
├── Box
│   ├── zdjęcie
│   ├── attribution
│   └── PageIndicator
├── name
├── translate
├── snippet
└── content()
```

### Refactor `createPages()`

Dwie funkcje o niemal identycznym działaniu (`createCityPages()` i `createPlacePages()`) nie były potrzebne. Jedna wspólna `createPages()` upraszcza utrzymanie kodu. Różnice powinny być wyprowadzane na zewnątrz tylko wtedy, gdy rzeczywiście dotyczą zachowania konkretnego ekranu.

### Zasada refactoru

Najpierw warto wydzielić rzeczy faktycznie wspólne i dopiero później abstrahować różnice. Nadmierna abstrakcja może pogorszyć czytelność. W tym przypadku `DetailsPager` jest uzasadniony, ponieważ oba ekrany miały tę samą logikę prezentacji.

### Przyszła Application

Obecnie aplikacja nie ma własnej klasy `Application`. Nie jest ona potrzebna tylko dlatego, że korzystamy z Coil. Przy dodaniu map OSM będzie można wprowadzić `Application` do centralnej konfiguracji map i innych zależności aplikacyjnych.


## Aktualizacja 31 sierpnia – 1 września 2026

### Wikidata -- najważniejsza lekcja dotycząca modelowania danych

Nie każdą informację z Wikidata należy sprowadzać do prostego typu. `P571` może reprezentować moment o różnej precyzji, dlatego utworzono `WikidataTime` z `year`, `precision` i `raw`. Dzięki temu model zachowuje informację potrzebną do późniejszego poprawnego formatowania, np. roku albo przybliżonego okresu historycznego.

### Brak danych nie musi oznaczać błędu

Test 17 miast wykazał, że dostępność właściwości Wikidata jest różna. Model `WikidataInfo` używa nullable pól, a UI pokazuje tylko wartości, które rzeczywiście istnieją. To lepsze niż sztuczne podstawianie danych lub traktowanie brakującej właściwości jako awarii.

### `403` i `User-Agent`

HTTP 403 z usług Wikimedia/Wikidata nie zawsze oznacza błędny endpoint. W tym projekcie problem został rozwiązany przez poprawne zidentyfikowanie klienta HTTP nagłówkiem `User-Agent`. Wspólny klient zapobiega powielaniu tej konfiguracji.

### `429` i kontrola współbieżności

Sam cache nie rozwiązuje problemu, gdy wiele requestów wystartuje jednocześnie przed zapisaniem wyniku. `Semaphore(3)` pozwala ograniczyć liczbę równoległych requestów bez sztucznego opóźniania każdego wywołania. Gdy jeden request się kończy, kolejne oczekujące mogą zostać uruchomione.

Dodatkowo zbiór `loadingImages` chroni przed wielokrotnym uruchomieniem tego samego `pageId` w obrębie jednego `WikimediaViewModel`.

### Metadane a właściwy plik zdjęcia to dwa różne procesy

Ważne rozróżnienie: pobranie `WikimediaImage` przez API nie oznacza jeszcze, że właściwy JPG/PNG został pobrany. Dlatego loading indicator w UI powinien być związany z Coil (`onLoading`, `onSuccess`, `onError`), a nie z pobieraniem metadanych.

### Dwa rodzaje cache

`WikidataMemoryCache` i `WikimediaMemoryCache` przechowują dane w pamięci procesu. Coil może niezależnie korzystać z własnego cache obrazu. Testy pokazały, że offline w obrębie sesji działa, ale po zakończeniu procesu dane z własnego MemoryCache są tracone.

Wniosek: cache RAM jest dobrym pierwszym etapem, ale prawdziwy offline po restarcie wymaga trwałego storage'u.

### `Application` jako centralizacja zależności

Wprowadzenie `MyApp : Application` okazało się przydatne, gdy pojawiły się współdzielone cache. Należy jednak uważać, aby ViewModel nie zaczął znać całej aplikacji. Docelowo zależności powinny być przekazywane przez Factory/DI, a `MyApp` może pozostać miejscem konfiguracji infrastruktury procesu.

### UI -- źródło prawdy o ładowaniu obrazu

`AsyncImage`/Coil daje stan rzeczywistego ładowania pliku. Dzięki wykorzystaniu `onLoading`, `onSuccess` i `onError` można pokazać użytkownikowi dyskretny progress bez sztucznego procentowego postępu.

### `HorizontalPager` i wyrównanie

Przy pagerze z dynamiczną zawartością warto jawnie określić `verticalAlignment = Alignment.Top`. Szczególnie przy rotacji ekranu brak jawnego wyrównania może powodować chwilowe niepożądane pozycjonowanie strony podczas pomiaru i kompozycji.

### Naturalne proporcje zdjęć

Dla kolejnych stron `DetailsPager` korzysta z rzeczywistego aspect ratio obrazu. Sztywna wysokość nie sprawdza się dla zestawu zdjęć panoramicznych i pionowych. Wysokość wynikająca z proporcji pozwala wykorzystać pełną szerokość ekranu, a `verticalScroll()` obsługuje wysokie zdjęcia pionowe.

### Zasada na dalszy rozwój

Przed dodaniem kolejnych funkcji warto najpierw ustabilizować mechanizmy wspólne: sieć, cache, offline i dependency management. Dopiero na takiej bazie warto rozszerzać Wikidata na Places, a następnie dodawać Quiz i mapę.

## Git i praca na branchach

### Branch jako osobny etap funkcji

Funkcję warto rozwijać na osobnym branchu, a następnie scalać ją do `main` dopiero po zakończeniu i przetestowaniu.

W projekcie zastosowano:

```text
main
  ↓
feature/room
  ↓
commity + testy
  ↓
merge
  ↓
main
```

Następnie rozpoczęto analogiczny etap `feature/quiz`.

### `git diff` a `git log`

`git log --oneline --graph --decorate --all` pokazuje historię commitów i relacje między branchami.

`git diff` pokazuje zawartość zmian. Przy porównaniu całego brancha można użyć:

```bash
git diff --stat main..feature/room
git diff --name-status main..feature/room
```

`--stat` daje podsumowanie rozmiaru zmian, a `--name-status` listę plików wraz ze statusem `A` / `M` / `D`.

### Fast-forward merge

Jeżeli `main` nie ma własnych nowych commitów od momentu utworzenia brancha, Git może wykonać `Fast-forward`. Wtedy nie powstaje dodatkowy commit merge — wskaźnik `main` zostaje przesunięty na ostatni commit brancha.

### Commit message

W projekcie stosowane są komunikaty zgodne z Conventional Commits, m.in.:

```text
chore: technical / maintenance changes
feat: new functionality
```

---

## Room i persistent cache

### MemoryCache a trwały storage

MemoryCache działa tylko w czasie życia procesu aplikacji. Room pozwala zachować dane po zakończeniu procesu.

W projekcie rozdzielono:

```text
MemoryCache → szybki dostęp w RAM
Room        → trwałe metadane
Coil        → cache plików obrazów
```

### Dwie warstwy cache nie oznaczają tego samego

Metadane Wikimedia (`WikimediaImage`) i właściwy plik JPG/PNG są osobnymi procesami. Zapisanie metadanych w Room nie oznacza automatycznie zapisania pliku obrazu. Dlatego Coil korzysta z własnego `DiskCache`.

---

## Quiz -- modelowanie stanu

Quiz pokazuje praktyczne zastosowanie kilku wartości stanu w ViewModelu:

```text
currentQuestionIndex
selectedAnswerIndex
score
isAnswerChecked
```

`selectedAnswerIndex` ma wartość `null`, dopóki użytkownik nie wybierze odpowiedzi.

`isAnswerChecked` blokuje ponowny wybór po udzieleniu odpowiedzi.

`nextQuestion()` zwraca `Boolean`, dzięki czemu ekran może później rozróżnić:

```text
true  → następne pytanie
false → koniec quizu / podsumowanie
```

### Odpowiedzialność QuizViewModel

`QuizScreen` nie powinien samodzielnie obliczać wyniku. ViewModel przechowuje stan quizu i wykonuje logikę wyboru odpowiedzi.

### Factory dla ViewModelu

Ponieważ `QuizViewModel` otrzymuje `QuizRepository` w konstruktorze, do jego utworzenia używany jest `QuizViewModelFactory`.
