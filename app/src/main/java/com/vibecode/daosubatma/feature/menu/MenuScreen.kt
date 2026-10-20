package com.vibecode.daosubatma.feature.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.vibecode.daosubatma.ui.GoldButton
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Mist
import com.vibecode.daosubatma.ui.Paper

@Composable
fun MenuScreen(onStart: () -> Unit, onOpenCollection: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Ink).padding(28.dp)) {
        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("符", color = Gold, fontSize = 72.sp)
            Text(stringResource(R.string.app_name), color = Paper, fontWeight = FontWeight.Black, fontSize = 30.sp, textAlign = TextAlign.Center)
            Text(stringResource(R.string.menu_subtitle), color = Mist, fontSize = 16.sp)
            Spacer(Modifier.height(52.dp))
            GoldButton(stringResource(R.string.open_spiritual_sight), onStart)
            Spacer(Modifier.height(14.dp))
            Text(stringResource(R.string.ghost_compendium), color = Gold, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onOpenCollection))
            Spacer(Modifier.height(14.dp))
            Text(stringResource(R.string.mvp_caption), color = Mist, fontSize = 12.sp)
        }
        Column(Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.rank_apprentice), color = Gold, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.menu_stats), color = Paper, fontSize = 13.sp)
        }
    }
}
