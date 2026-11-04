
package com.razegon.shared.ui.screens.arcade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.razegon.shared.domain.model.ArcadeResult

@Composable
fun ArcadeRankingContent(
    results: List<ArcadeResult>,
    onBackClick: () -> Unit,
    formatResultDate: (Long) -> String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "←",
                    fontSize = 28.sp
                )
            }
        }

        Text(
            text = "TOP 20 ARCADE",
            style = MaterialTheme.typography.headlineMedium
        )

        if (results.isEmpty()) {
            Text(
                text = "Brak zapisanych wyników",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(results) { index, result ->
                    ArcadeRankingItem(
                        position = index + 1,
                        result = result,
                        formattedDate = formatResultDate(result.date)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArcadeRankingItem(
    position: Int,
    result: ArcadeResult,
    formattedDate: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "$position. ${result.nickname}    ${result.score}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
