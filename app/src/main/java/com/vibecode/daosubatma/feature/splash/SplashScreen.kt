package com.vibecode.daosubatma.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecode.daosubatma.R
import com.vibecode.daosubatma.ui.Gold
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Paper
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) { delay(1_400); onFinished() }
    Box(Modifier.fillMaxSize().background(Ink), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("符", color = Gold, fontSize = 92.sp, fontWeight = FontWeight.Black)
            Text(stringResource(R.string.app_name), color = Paper, fontSize = 29.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(10.dp))
            Text(stringResource(R.string.splash_tagline), color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
        }
    }
}
