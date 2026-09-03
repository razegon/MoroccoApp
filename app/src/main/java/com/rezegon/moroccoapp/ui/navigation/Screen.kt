package com.rezegon.moroccoapp.ui.navigation

// Defines all navigation routes used by the application.
sealed class Screen(
    val route: String
) {
    data object Home : Screen("home")

    data object Cities : Screen("cities")

    data object CityDetails : Screen("city_details/{cityId}")

    data object Places : Screen("places")

    data object PlaceDetails : Screen("place_details/{placeId}")

    data object Quiz : Screen("quiz")

    data object QuizDifficulty : Screen("quiz_difficulty")

    data object QuizGame : Screen("quiz_game")

    data object QuizSummary : Screen("quiz_summary")

    data object QuizRanking : Screen("quiz_ranking")
}
