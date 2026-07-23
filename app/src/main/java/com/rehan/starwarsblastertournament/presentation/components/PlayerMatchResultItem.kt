package com.rehan.starwarsblastertournament.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rehan.starwarsblastertournament.domain.model.MatchResult
import com.rehan.starwarsblastertournament.domain.model.PlayerMatchResult

// One row on the Player Matches screen: left player name, score, right player name.
// Background color shows win (green) / loss (red) / draw (white) from the
// selected player's point of view. No icons here — names only.
@Composable
fun PlayerMatchResultItem(
    matchResult: PlayerMatchResult,
    modifier: Modifier = Modifier
) {

    val backgroundColor = matchResult.result.toBackgroundColor()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = matchResult.leftPlayer.name,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "${matchResult.leftScore} - ${matchResult.rightScore}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = matchResult.rightPlayer.name,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

// Maps a match result to the background color the spec asks for:
// Win = Green, Loss = Red, Draw = White
private fun MatchResult.toBackgroundColor(): Color = when (this) {
    MatchResult.WIN -> Color(0xFF4CAF50)
    MatchResult.LOSS -> Color(0xFFE53935)
    MatchResult.DRAW -> Color.White
}