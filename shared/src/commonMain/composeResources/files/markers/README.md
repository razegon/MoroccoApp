# MoroccoApp – Map Marker Icons

Version: 1.0

This package contains 12 consistent SVG map-pin markers. Each marker uses a category-specific
background color and a white pictogram. SVGs are vector assets and can be rasterized by each
platform's map renderer.

## PlaceType mapping

| PlaceType | SVG file | Color |
|---|---|---|
| `CITY` | `markers/city.svg` | `#596579` |
| `MONUMENT` | `markers/monument.svg` | `#B85C38` |
| `RELIGIOUS_SITE` | `markers/religious_site.svg` | `#7956A8` |
| `BEACH` | `markers/beach.svg` | `#3976B9` |
| `MUSEUM` | `markers/museum.svg` | `#9670B5` |
| `RESTAURANT` | `markers/restaurant.svg` | `#D46B35` |
| `HOTEL` | `markers/hotel.svg` | `#347C79` |
| `PARK` | `markers/park.svg` | `#438B52` |
| `NATURE` | `markers/nature.svg` | `#568B48` |
| `VIEWPOINT` | `markers/viewpoint.svg` | `#287F9C` |
| `SHOP` | `markers/shop.svg` | `#A66C36` |
| `MARKET` | `markers/market.svg` | `#C28B24` |

## Suggested KMP integration

- Keep the `PlaceType -> iconKey/color` mapping in `shared/commonMain`.
- Keep these SVG files as shared source assets if your KMP resource setup supports SVG.
- Android and iOS map adapters should rasterize the same SVG source into the native marker
  image format required by the map SDK.
- Do not duplicate category/color decisions in platform-specific renderers.
- For clustering, use a separate cluster marker rather than reusing a single PlaceType marker.

## Notes

- These are original, lightweight SVG pictograms designed for this project.
- The icon names are semantic keys; they are not dependencies on a platform icon font.
- `MARKET` is included as a proposed PlaceType. Keep it only if you decide to add it to the enum.
- `RELIGIOUS_SITE` is a neutral category covering churches, mosques, madrasas and other
  religious/cultural sites. The pictogram is intentionally generic.
