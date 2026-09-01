package com.rezegon.moroccoapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.domain.model.WikidataInfo
import com.rezegon.moroccoapp.domain.model.WikidataTime

@Composable
fun WikidataInfoSection(
    info: WikidataInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            start = 20.dp,
            top = 16.dp,
            end = 20.dp
        )
    ) {

        info.population?.let {
            Text(
                text = "Ludność: %,d".format(it),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        info.area?.let {
            Text(
                text = "Powierzchnia: $it km²",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        info.elevation?.let {
            Text(
                text = "Wysokość: ${it.toInt()} m n.p.m.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        info.inception?.let {
            Text(
                text = "Założenie: ${formatWikidataTime(it)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun formatWikidataTime(
    time: WikidataTime
): String {

    val year = time.year ?: return time.raw.orEmpty()

    return when (time.precision) {
        9 -> year.toString()

        8 -> "około $year"

        7 -> "około $year"

        else -> year.toString()
    }
}

