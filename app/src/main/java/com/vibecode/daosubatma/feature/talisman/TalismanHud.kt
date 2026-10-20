package com.vibecode.daosubatma.feature.talisman

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecode.daosubatma.R
import com.vibecode.daosubatma.ui.Gold
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Mist
import com.vibecode.daosubatma.ui.Paper

@Composable
fun TalismanTopHud(secondsLeft: Int, onBack: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Ink.copy(alpha = .78f), RoundedCornerShape(16.dp)).border(1.dp, Mist.copy(alpha = .6f), RoundedCornerShape(16.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(stringResource(R.string.back), color = Paper, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onBack).padding(4.dp))
        Spacer(Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.talisman_hud_title), color = Gold, fontWeight = FontWeight.Black, fontSize = 14.sp)
            Text(stringResource(R.string.talisman_hud_subtitle), color = Paper, fontSize = 11.sp)
        }
        Spacer(Modifier.weight(1f))
        TalismanTimer(secondsLeft)
    }
}

@Composable
fun TalismanBottomHud() {
    Column(
        Modifier.fillMaxWidth().background(Ink.copy(alpha = .82f), RoundedCornerShape(16.dp)).border(1.dp, Gold.copy(alpha = .58f), RoundedCornerShape(16.dp)).padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.talisman_draw_hint), color = Paper, textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(stringResource(R.string.talisman_timeout_hint), color = Mist, textAlign = TextAlign.Center, fontSize = 12.sp)
    }
}
