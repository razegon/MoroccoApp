package com.rezegon.moroccoapp.data.datasource

import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.domain.model.PlaceType
import com.rezegon.moroccoapp.domain.model.WikimediaImageRef

object MoroccoDataSource {

    fun getPlaces(): List<Place> = places

    private val places: List<Place> = listOf(
        // ============ CITIES ====================================================================
        // Marrakech
        Place(
            placeName = R.string.marrakesh_name,
            placeId = 4100,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(113679691),
                WikimediaImageRef(24518813)
            ),
            placeDescriptions = listOf(
                R.string.desc1_marrakesh
            ),
            wikidataId = "Q101625",
            placeTranslate = R.string.trans_marrakesh,
            // placeIcon = R.drawable.ic_marrakesh,
            placeWeather = R.string.marrakesh_weather,
            latitude = 31.62823,
            longitude = -7.98882,
            zoom = 13f,
        ),

        // Rabat
        Place(
            placeName = R.string.rabat_name,
            placeId = 4101,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(62178572),
                WikimediaImageRef(57424072),
                WikimediaImageRef(1598471),
                null,
                null
            ),
            placeDescriptions = listOf(
                R.string.desc1_rabat,
                R.string.desc2_rabat,
                R.string.desc3_rabat,
                R.string.desc4_rabat
            ),
            wikidataId = "Q3551",
            placeTranslate = R.string.trans_rabat,
            // placeIcon = R.drawable.ic_rabat,
            placeWeather = R.string.rabat_weather,
            latitude = 34.018078,
            longitude = -6.854858,
            zoom = 13f,
        ),

        // Fes
        Place(
            placeName = R.string.fes_name,
            placeId = 4102,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(188805401),
                WikimediaImageRef(110536782),
                WikimediaImageRef(85811057),
                WikimediaImageRef(57424678),
                WikimediaImageRef(150417943),
                null,
                WikimediaImageRef(85811049),
            ),
            placeDescriptions = listOf(
                R.string.desc1_fes,
                R.string.desc2_fes,
                R.string.desc3_fes,
                R.string.desc4_fes,
                R.string.desc5_fes,
                R.string.desc6_fes,
            ),
            wikidataId = "Q80985",
            placeTranslate = R.string.trans_fes,
            // placeIcon = R.drawable.ic_fez,
            placeWeather = R.string.fes_weather,
            latitude = 34.02092,
            longitude = -5.00790,
            zoom = 13f,
        ),

        // Meknes
        Place(
            placeName = R.string.meknes_name,
            placeId = 4103,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(164002172),
                WikimediaImageRef(38945606),
                WikimediaImageRef(23556021),
                WikimediaImageRef(48578895),
                WikimediaImageRef(27741214),
                WikimediaImageRef(92840831),
            ),
            placeDescriptions = listOf(
                R.string.desc1_meknes,
                R.string.desc2_meknes,
                R.string.desc3_meknes,
                R.string.desc4_meknes,
                R.string.desc5_meknes
            ),
            wikidataId = "Q178663",
            placeTranslate = R.string.trans_meknes,
            // placeIcon = R.drawable.ic_meknes,
            placeWeather = R.string.meknes_weather,
            latitude = 33.87199,
            longitude = -5.53926,
            zoom = 13f,
            ),

        // Casablanca
        Place(
            placeName = R.string.casablanca_name,
            placeId = 4104,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(140442526),
                WikimediaImageRef(57687173),
                WikimediaImageRef(85687773),
            ),
            placeDescriptions = listOf(
                R.string.desc1_casablanca
            ),
            wikidataId = "Q7903",
            placeTranslate = R.string.trans_casablanca,
            // placeIcon = R.drawable.ic_casablanca,
            placeWeather = R.string.casablanca_weather,
            latitude = 33.59606,
            longitude = -7.57482,
            zoom = 13f,
        ),

        // El Jadida
        Place(
            placeName = R.string.el_jadida_name,
            placeId = 4105,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(138986891),
                WikimediaImageRef(139368313),
                WikimediaImageRef(158634495),
                WikimediaImageRef(151435949)
            ),
            placeDescriptions = listOf(
                R.string.desc1_el_jadida,
                R.string.desc2_el_jadida
            ),
            wikidataId = "Q219583",
            placeTranslate = R.string.trans_el_jadida,
            // placeIcon = R.drawable.ic_el_jadida,
            placeWeather = R.string.el_jadida_weather,
            latitude = 33.24650,
            longitude = -8.50771,
            zoom = 13f,
            ),

        // Essaouira
        Place(
            placeName = R.string.essaouira_name,
            placeId = 4106,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(5650469),
                WikimediaImageRef(57428333),
                WikimediaImageRef(193609551)
            ),
            placeDescriptions = listOf(
                R.string.desc1_essaouira
            ),
            wikidataId = "Q216939",
            placeTranslate = R.string.trans_essaouira,
            // placeIcon = R.drawable.ic_essaouira,
            placeWeather = R.string.essaouira_weather,
            latitude = 31.52060,
            longitude = -9.76382,
            zoom = 13f,
            ),

        // Agadir
        Place(
            placeName = R.string.agadir_name,
            placeId = 4107,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(162395398),
                WikimediaImageRef(104323865),
                WikimediaImageRef(48236357),
                WikimediaImageRef(149015877),
            ),
            placeDescriptions = listOf(
                R.string.desc1_agadir,
                R.string.desc2_agadir,
                R.string.desc3_agadir
            ),
            wikidataId = "Q170525",
            placeTranslate = R.string.trans_agadir,
            //placeIcon = R.drawable.ic_agadir,
            placeWeather = R.string.agadir_weather,
            latitude = 30.43541,
            longitude = -9.62756,
            zoom = 13f,
        ),

        // Larache
        Place(
            placeName = R.string.larache_name,
            placeId = 4108,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(155554216),
                WikimediaImageRef(155867488),
                WikimediaImageRef(157387177),
                WikimediaImageRef(157385268),
                WikimediaImageRef(97925153)
            ),
            placeDescriptions = listOf(
                R.string.desc1_larache,
                R.string.desc2_larache,
                R.string.desc3_larache,
                R.string.desc4_larache,
            ),
            wikidataId = "@606775",
            placeTranslate = R.string.trans_larache,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.larache_weather,
            latitude = 35.1972,
            longitude = -6.15216,
            zoom = 13f,
            ),

        // Asilah
        Place(
            placeName = R.string.asilah_name,
            placeId = 4109,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(7385225),
                WikimediaImageRef(155885960),
                WikimediaImageRef(74057618),
                WikimediaImageRef(7385236),
            ),
            placeDescriptions = listOf(
                R.string.desc1_asilah,
                R.string.desc2_asilah,
                R.string.desc3_asilah,
            ),
            wikidataId = "Q730328",
            placeTranslate = R.string.trans_asilah,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.asilah_weather,
            latitude = 35.46676,
            longitude = -6.036128,
            zoom = 13f,
            ),

        // Tanger
        Place(
            placeName = R.string.tanger_name,
            placeId = 4110,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(90617827),
                WikimediaImageRef(156490926),
                WikimediaImageRef(137562092),
                WikimediaImageRef(129942646),
                WikimediaImageRef(86467120),
            ),
            placeDescriptions = listOf(
                R.string.desc1_tanger,
                R.string.desc2_tanger,
                R.string.desc3_tanger,
                R.string.desc4_tanger,
            ),
            wikidataId = "Q126148",
            placeTranslate = R.string.trans_tanger,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.tanger_weather,
            latitude = 35.78837,
            longitude = -5.81244,
            zoom = 13f,
            ),

        // Tetouan
        Place(
            placeName = R.string.tetouan_name,
            placeId = 4111,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(6154057),
                WikimediaImageRef(155146685),
                WikimediaImageRef(157380958),
                WikimediaImageRef(162986175),
            ),
            placeDescriptions = listOf(
                R.string.desc1_tetouan,
                R.string.desc2_tetouan,
                R.string.desc3_tetouan,
            ),
            wikidataId = "Q185157",
            placeTranslate = R.string.trans_tetouan,
            //placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.tetouan_weather,
            latitude = 35.57006,
            longitude = -5.37427,
            zoom = 13f,
        ),

        // Chefchaouen
        Place(
            placeName = R.string.chefchaouen_name,
            placeId = 4112,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(49156478),
                WikimediaImageRef(139604219),
                WikimediaImageRef(66006858),
                WikimediaImageRef(197429545),
            ),
            placeDescriptions = listOf(
                R.string.desc1_chefchaouen,
                R.string.desc2_chefchaouen,
                R.string.desc3_chefchaouen,
            ),
            wikidataId = "Q676778",
            placeTranslate = R.string.trans_chefchaouen,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.chefchaouen_weather,
            latitude = 35.168538,
            longitude = -5.26192,
            zoom = 13f,
        ),

        // Taroudant
        Place(
            placeName = R.string.taroudant_name,
            placeId = 4113,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(63619129),
                WikimediaImageRef(145552226),
            ),
            placeDescriptions = listOf(
                R.string.desc1_taroudant
            ),
            wikidataId = "Q762727",
            placeTranslate = R.string.trans_taroudant,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.taroudant_weather,
            latitude = 30.46886,
            longitude = -8.87795,
            zoom = 13f,
        ),

        // Ouarzazate
        Place(
            placeName = R.string.ouarzazate_name,
            placeId = 4114,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(116752933),
                WikimediaImageRef(126651110),
                WikimediaImageRef(49951014),
                WikimediaImageRef(141565160),
                WikimediaImageRef(103843880),
            ),
            placeDescriptions = listOf(
                R.string.desc1_ouarzazate,
                R.string.desc2_ouarzazate,
                R.string.desc3_ouarzazate,
                R.string.desc4_ouarzazate
            ),
            wikidataId = "Q505208",
            placeTranslate = R.string.trans_ouarzazate,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.ouarzazate_weather,
            latitude = 30.920307,
            longitude = -6.89974,
            zoom = 13f,
        ),

        // Zaghura
        Place(
            placeName = R.string.zagora_name,
            placeId = 4115,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(40011734),
                WikimediaImageRef(77170334),
                WikimediaImageRef(130226162),
            ),
            placeDescriptions = listOf(
                R.string.desc1_zagora,
                R.string.desc2_zagora
            ),
            wikidataId = "Q140354",
            placeTranslate = R.string.trans_zagora,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.zagora_weather,
            latitude = 30.32079,
            longitude = -5.84009,
            zoom = 13f,
        ),

        // Arfoud
        Place(
            placeName = R.string.arfoud_name,
            placeId = 4116,
            placeType = PlaceType.CITY,
            wikimediaImages = listOf(
                WikimediaImageRef(3062453),
                WikimediaImageRef(158463206),
                WikimediaImageRef(157412617),
                WikimediaImageRef(69716318),
            ),
            placeDescriptions = listOf(
                R.string.desc1_arfoud,
                R.string.desc2_arfoud,
                R.string.desc3_arfoud
            ),
            wikidataId = "Q648987",
            placeTranslate = R.string.trans_arfoud,
            // placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.arfoud_weather,
            latitude = 31.43522,
            longitude = -4.23325,
            zoom = 13f,
        ),

        // ========================= NATURE ====================================================

        // Gibraltar Straight
        Place(
            placeName = R.string.gibraltar_strait_name,
            placeId = 4200,
            placeType = PlaceType.NATURE,
            wikimediaImages = listOf(
                WikimediaImageRef(141747),
                WikimediaImageRef(45589174),
                WikimediaImageRef(20784814),
                WikimediaImageRef(15350259),
            ),
            placeDescriptions = listOf(
                R.string.desc1_gibraltar_strait,
                R.string.desc2_gibraltar_strait,
                R.string.desc3_gibraltar_strait
            ),
            placeTranslate = R.string.trans_gibraltar_strait,
            placeSnippet = R.string.gibraltar_strait_founded,
            placeWeather = R.string.tanger_weather,
            //placeIcon = R.drawable.ic_morocco_green,
            ),

        // Saffron
        Place(
            placeName = R.string.safran_name,
            placeId = 4201,
            placeType = PlaceType.NATURE,
            wikimediaImages = listOf(
                WikimediaImageRef(31281822),
                WikimediaImageRef(31281823),
                WikimediaImageRef(12174938),
                WikimediaImageRef(31281828),
            ),
            placeDescriptions = listOf(
                R.string.desc1_safran,
                R.string.desc2_safran,
                R.string.desc3_safran
            ),
            placeTranslate = R.string.trans_safran,
            placeSnippet = R.string.trans_safran,
            //placeIcon = R.drawable.ic_morocco_green,
            ),

        // Atlas Mountains
        Place(
            placeName = R.string.atlas_name,
            placeId = 4202,
            placeType = PlaceType.NATURE,
            wikimediaImages = listOf(
                WikimediaImageRef(94296619),
                WikimediaImageRef(196203046),
                WikimediaImageRef(125335557),
                WikimediaImageRef(130470085),
                WikimediaImageRef(30288242),
            ),
            placeDescriptions = listOf(
                R.string.desc1_atlas,
                R.string.desc2_atlas,
                R.string.desc3_atlas,
                R.string.desc4_atlas
            ),
            placeTranslate = R.string.trans_atlas,
            placeSnippet = R.string.atlas_founded,
            //placeIcon = R.drawable.ic_morocco_green,
           ),

        // Khettars
        Place(
            placeName = R.string.khettar_name,
            placeId = 4203,
            placeType = PlaceType.NATURE,
            wikimediaImages = listOf(
                WikimediaImageRef(5398736),
                null,
                WikimediaImageRef(125508516),
                null,
                null
            ),
            placeImages = listOf(
                null,
                "https://www.marrakech-desert-trips.com/wp-content/uploads/2024/05/Kettara-system-diagram--1024x631.jpg",
                null,
                null,
                null
            ),
            placeDescriptions = listOf(
                R.string.desc1_khetter,
                R.string.desc2_khettar,
                R.string.desc3_khettar,
                R.string.desc4_khettar
            ),
            placeTranslate = R.string.trans_khettar,
            placeSnippet = R.string.khettar_founded,
            //placeIcon = R.drawable.ic_morocco_green,
           ),

        // Argan Tree
        Place(
            placeName = R.string.argan_name,
            placeId = 4204,
            placeType = PlaceType.NATURE,
            wikimediaImages = listOf(
                WikimediaImageRef(75553539),
                WikimediaImageRef(69174302),
                WikimediaImageRef(44456070),
                WikimediaImageRef(4331730),
            ),
            placeDescriptions = listOf(
                R.string.desc1_argan,
                R.string.desc2_argan,
                R.string.desc3_argan,
            ),
            placeTranslate = R.string.trans_argan,
            placeSnippet = R.string.argan_founded,
            //placeIcon = R.drawable.ic_morocco_green,
            ),

        // Maamora Forest
        Place(
            placeName = R.string.maamora_forest_name,
            placeId = 4205,
            placeType = PlaceType.NATURE,
            wikimediaImages = listOf(
                WikimediaImageRef(40405613),
                WikimediaImageRef(156961840),
            ),
            placeDescriptions = listOf(
                R.string.desc1_maamora_forest,
            ),
            placeTranslate = R.string.trans_maamora_forest,
            placeSnippet = R.string.maamora_forest_founded,
            //placeIcon = R.drawable.ic_morocco_green,
            ),

        // Cap Spartel
        Place(
            placeName = R.string.cap_spartel_name,
            placeId = 4206,
            placeType = PlaceType.NATURE,
            wikimediaImages = listOf(
                WikimediaImageRef(82390402),
                WikimediaImageRef(75489068)
            ),
            placeDescriptions = listOf(
                R.string.desc1_cap_spartel,
            ),
            placeTranslate = R.string.trans_cap_spartel,
            placeSnippet = R.string.cap_spartel_founded,
            //placeIcon = R.drawable.ic_morocco_green,
            placeWeather = R.string.tanger_weather,
            ),

        // ================== MONUMENTS ===============================================
        // Volubilis
        Place(
            placeName = R.string.volubilis_name,
            placeId = 4000,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(5494930),
                WikimediaImageRef(22418358),
                WikimediaImageRef(62956342),
                WikimediaImageRef(5494930),
                WikimediaImageRef(62956341),
                WikimediaImageRef(22457678),
                null,
                WikimediaImageRef(11675206),
            ),
            placeDescriptions =
                listOf(
                    R.string.desc1_volubilis,
                    R.string.desc2_volubilis,
                    R.string.desc3_volubilis,
                    R.string.desc4_volubilis,
                    R.string.desc5_volubilis,
                    R.string.desc6_volubilis,
                    R.string.desc7_volubilis
                ),
            placeTranslate = R.string.trans_volubilis,
            placeSnippet = R.string.founded_volubilis,
            //placeIcon = R.drawable.ic_bahia_palace,
            latitude = 34.07196,
            longitude = -5.85721,
            zoom = 18f,
            openingHours = R.string.volubilis_opening_hours,
            altitude = 400,
        ),

        // Bahia Palace
        Place(
            placeName = R.string.bahia_palace_name,
            placeId = 4001,
            parentPlaceId = 4100,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(82450652),
                WikimediaImageRef(82416144),
                WikimediaImageRef(82428248),
                WikimediaImageRef(90951163),
            ),
            placeDescriptions =
                listOf(
                    R.string.desc1_bahia_palace,
                    R.string.desc2_bahia_palace,
                    R.string.desc3_bahia_palace
                ),
            placeTranslate = R.string.trans_bahia_palace,
            placeSnippet = R.string.founded_bahia_palace,
            //placeIcon = R.drawable.ic_bahia_palace,
            latitude = 31.621858,
            longitude = -7.981985,
            zoom = 18f,
            openingHours = R.string.bahia_palace_opening_hours,
            altitude = 460
        ),

        // Jardin Secret
        Place(
            placeName = R.string.jardin_secret_name,
            placeId = 4002,
            parentPlaceId = 4100,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(90800881),
                WikimediaImageRef(90800896),
                WikimediaImageRef(87201983),
                WikimediaImageRef(114099907),
            ),
            placeDescriptions =
                listOf(
                    R.string.desc1_jardin_secret,
                    R.string.desc2_jardin_secret,
                    R.string.desc3_jardin_secret
                ),
            placeTranslate = R.string.trans_jardin_secret,
            placeSnippet = R.string.founded_jardin_secret,
            //placeIcon = R.drawable.ic_jardin_secret,
            latitude = 31.63086,
            longitude = -7.98968,
            zoom = 18f,
            openingHours = R.string.jardin_secret_opening_hours,
            altitude = 460
        ),

        // Qubba Almoravide
        Place(
            placeName = R.string.qubba_name,
            placeId = 4003,
            parentPlaceId = 4100,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(93876721),
                WikimediaImageRef(89119379),
                WikimediaImageRef(148365817),
                WikimediaImageRef(24418129),
            ),
            placeDescriptions = listOf(
                R.string.desc1_qubba,
                R.string.desc2_qubba,
                R.string.desc3_qubba
            ),
            placeTranslate = R.string.trans_qubba,
            placeSnippet = R.string.founded_qubba,
            //placeIcon = R.drawable.ic_qubba,
            latitude = 31.63158,
            longitude = -7.98718,
            zoom = 18f,
            openingHours = R.string.qubba_opening_hours,
            altitude = 460
        ),

        // Hassan Tower
        Place(
            placeName = R.string.hassan_tower_name,
            placeId = 4004,
            parentPlaceId = 4101,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(6611584),
                WikimediaImageRef(8453276)
            ),
            placeDescriptions = listOf(
                R.string.desc1_hassan_tower
            ),
            placeTranslate = R.string.trans_hassan_tower,
            placeSnippet = R.string.founded_hassan_tower,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.rabat_weather,
            latitude = 34.02393,
            longitude = -6.82278,
            zoom = 18f,
            openingHours = R.string.hassan_tower_opening_hours,
            altitude = 40
        ),

        // Mausoleum Mohammed V
        Place(
            placeName = R.string.mausoleum_muhammed_v_name,
            placeId = 4005,
            parentPlaceId = 4101,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(153537216),
                WikimediaImageRef(144128655)
            ),
            placeDescriptions = listOf(
                R.string.desc1_mausoleum_muhammed_v
            ),
            placeTranslate = R.string.trans_mausoleum_muhammed_v,
            placeSnippet = R.string.founded_mausoleum_muhammed_v,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.casablanca_weather,
            latitude = 34.02393,
            longitude = -6.82278,
            zoom = 18f,
            openingHours = R.string.hassan_tower_opening_hours,
            altitude =50
        ),

        // Hassan II Mosque
        Place(
            placeName = R.string.hassan_II_mosque_name,
            placeId = 4006,
            parentPlaceId = 4104,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(184944971),
                WikimediaImageRef(91983549),
                WikimediaImageRef(77219915),
                null,
                WikimediaImageRef(184944974),
                WikimediaImageRef(175970114),
            ),
            placeDescriptions = listOf(
                R.string.desc1_hassan_II_mosque,
                R.string.desc2_hassan_II_mosque,
                R.string.desc3_hassan_II_mosque,
                R.string.desc4_hassan_II_mosque,
                R.string.desc5_hassan_II_mosque,
            ),
            placeTranslate = R.string.trans_hassan_II_mosque,
            placeSnippet = R.string.founded_hassan_II_mosque,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.casablanca_weather,
            latitude = 33.6060597,
            longitude = -7.6307139,
            zoom = 18f,
            openingHours = R.string.hassan_II_mosque_opening_hours,
            altitude = 5
        ),

        // Medersa Ben Joussef
        Place(
            placeName = R.string.medersa_ben_youssef_name,
            placeId = 4007,
            parentPlaceId = 4100,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(123876537),
                WikimediaImageRef(123876575),
                WikimediaImageRef(63637562)
            ),
            placeDescriptions = listOf(
                R.string.desc1_medersa_ben_youssef,
                R.string.desc2_medersa_ben_youssef,
            ),
            placeTranslate = R.string.trans_medersa_ben_youssef,
            placeSnippet = R.string.founded_medersa_ben_youssef,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.marrakesh_weather,
            latitude = 31.63205,
            longitude = -7.9888,
            zoom = 18f,
            openingHours = R.string.medersa_ben_youssef_opening_hours,
            altitude = 460
        ),

        // Dar el Bacha
        Place(
            placeName = R.string.dar_el_bacha_name,
            placeId = 4008,
            parentPlaceId = 4100,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(82742695),
                WikimediaImageRef(80102619),
                null
            ),
            placeDescriptions = listOf(
                R.string.desc1_dar_el_bacha,
                R.string.desc2_dar_el_bacha,
            ),
            placeTranslate = R.string.trans_dar_el_bacha,
            placeSnippet = R.string.founded_dar_el_bacha,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.marrakesh_weather,
            latitude = 31.6314376,
            longitude = -7.9970531,
            zoom = 18f,
            openingHours = R.string.dar_el_bacha_opening_hours,
            altitude = 460
        ),

        // Jemaa el Fnaa
        Place(
            placeName = R.string.jemaa_el_fnaa_name,
            placeId = 4009,
            parentPlaceId = 4100,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(972789),
                WikimediaImageRef(24417557),
                WikimediaImageRef(95111424),
                WikimediaImageRef(157764323),
            ),
            placeDescriptions = listOf(
                R.string.desc1_jemaa_el_fnaa,
                R.string.desc2_jemaa_el_fnaa,
                R.string.desc3_jemaa_el_fnaa
            ),
            placeTranslate = R.string.trans_jemaa_el_fnaa,
            placeSnippet = R.string.founded_jemaa_el_fnaa,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.marrakesh_weather,
            latitude = 31.62578,
            longitude = -7.98920,
            zoom = 18f,
            openingHours = R.string.jemaa_el_fnaa_opening_hours,
            altitude = 460
        ),
        // Mausoleum Moulay Ismail
        Place(
            placeName = R.string.mausoleum_moulay_ismail_name,
            placeId = 4010,
            parentPlaceId = 4103,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(144995108),
                WikimediaImageRef(57732962),
            ),
            placeDescriptions = listOf(
                R.string.desc1_mausoleum_moulay_ismail,
            ),
            placeTranslate = R.string.trans_mausoleum_moulay_ismail,
            placeSnippet = R.string.founded_mausoleum_moulay_ismail,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.meknes_weather,
            latitude = 33.8907148,
            longitude = -5.5663948,
            zoom = 18f,
            openingHours = R.string.mausoleum_moulay_ismail_opening_hours,
            altitude = 550
        ),

        // Mohammed VI Tower
        Place(
            placeName = R.string.mohammed_vi_tower_name,
            placeId = 4011,
            parentPlaceId = 4101,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(152671609),
                WikimediaImageRef(147473948),
            ),
            placeDescriptions = listOf(
                R.string.desc1_mohammed_vi_tower,
            ),
            placeTranslate = R.string.trans_mohammed_vi_tower,
            placeSnippet = R.string.founded_mohammed_vi_tower,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.rabat_weather,
            latitude = 34.0189375,
            longitude = -6.8077678,
            zoom = 18f,
            openingHours = R.string.mohammed_vi_tower_opening_hours,
            altitude = 10
        ),

        // Kasbah Oudayas
        Place(
            placeName = R.string.kasbah_oudayas_name,
            placeId = 4012,
            parentPlaceId = 4101,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(1598478),
                WikimediaImageRef(5377035),
                WikimediaImageRef(133576546),
            ),
            placeDescriptions = listOf(
                R.string.desc1_kasbah_oudayas,
                R.string.desc2_kasbah_oudayas
            ),
            placeTranslate = R.string.trans_kasbah_oudayas,
            placeSnippet = R.string.founded_kasbah_oudayas,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.rabat_weather,
            latitude = 34.0311698,
            longitude = -6.8368932,
            zoom = 18f,
            openingHours = R.string.kasbah_oudayas_opening_hours,
            altitude = 60
        ),

        // Mellah Fes
        Place(
            placeName = R.string.mellah_fes_name,
            placeId = 4013,
            parentPlaceId = 4102,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(88728608),
                WikimediaImageRef(95272314),
                WikimediaImageRef(138692419),
                WikimediaImageRef(138695245),
            ),
            placeDescriptions = listOf(
                R.string.desc1_mellah_fes,
                R.string.desc2_mellah_fes,
                R.string.desc3_mellah_fes
            ),
            placeTranslate = R.string.trans_mellah_fes,
            placeSnippet = R.string.founded_mellah_fes,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.fes_weather,
            latitude = 34.0520091,
            longitude = -4.9918905,
            zoom = 18f,
            openingHours = R.string.mellah_fes_opening_hours,
            altitude = 450
        ),

        // Chouara
        Place(
            placeName = R.string.chouara_tennery_name,
            placeId = 4014,
            parentPlaceId = 4102,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(144499930),
                WikimediaImageRef(94133804)
            ),
            placeDescriptions = listOf(
                R.string.desc1_chouara_tennery
            ),
            placeTranslate = R.string.trans_chouara_tennery,
            placeSnippet = R.string.founded_chouara_tennery,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.fes_weather,
            latitude = 34.0659713,
            longitude = -4.9712981,
            zoom = 18f,
            openingHours = R.string.chouara_tennery_opening_hours,
            altitude = 415
        ),

        // Qarawiyyin University
        Place(
            placeName = R.string.qarawiyyin_name,
            placeId = 4015,
            parentPlaceId = 4102,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(76425809),
                WikimediaImageRef(76425798),
                WikimediaImageRef(91122178),
                null
            ),
            placeDescriptions = listOf(
                R.string.desc1_qarawiyyin,
                R.string.desc2_qarawiyyin,
                R.string.desc3_qarawiyyin
            ),
            placeTranslate = R.string.trans_qarawiyyin,
            placeSnippet = R.string.founded_qarawiyyin,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.fes_weather,
            latitude = 34.0640168,
            longitude = -4.9748402,
            zoom = 18f,
            openingHours = R.string.qarawiyyin_opening_hours,
            altitude = 430
        ),

        // Mazagan Fortress
        Place(
            placeName = R.string.mazagan_name,
            placeId = 4016,
            parentPlaceId = 4105,
            placeType = PlaceType.MONUMENT,
            wikimediaImages = listOf(
                WikimediaImageRef(1667700),
                WikimediaImageRef(83104377),
                WikimediaImageRef(98803010)
            ),
            placeDescriptions = listOf(
                R.string.desc1_mazagan,
                R.string.desc2_mazagan,
            ),
            placeTranslate = R.string.trans_mazagan,
            placeSnippet = R.string.founded_mazagan,
            //placeIcon = R.drawable.ic_qubba,
            placeWeather = R.string.el_jadida_weather,
            latitude = 33.256732,
            longitude = -8.502583,
            zoom = 18f,
            openingHours = R.string.qarawiyyin_opening_hours,
            altitude = 15
        ),
    )
}