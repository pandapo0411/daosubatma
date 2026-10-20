package com.vibecode.daosubatma.feature.talisman

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vibecode.daosubatma.R

@Composable
fun TalismanTimer(secondsLeft: Int) {
    val color = if (secondsLeft <= 2) Color(0xFFC6404D) else Color(0xFFF5C451)
    Text(stringResource(R.string.time_left_seconds, secondsLeft), color = color, fontSize = 22.sp, fontWeight = FontWeight.Black)
}
