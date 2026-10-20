package com.vibecode.daosubatma.feature.talisman

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecode.daosubatma.R
import com.vibecode.daosubatma.core.audio.GameAudio
import com.vibecode.daosubatma.core.audio.SoundEffect
import com.vibecode.daosubatma.feature.hunt.CameraPreview
import com.vibecode.daosubatma.core.model.TalismanType
import com.vibecode.daosubatma.core.model.GhostDefinition
import com.vibecode.daosubatma.core.model.localizedName
import com.vibecode.daosubatma.core.model.captureChance
import com.vibecode.daosubatma.ui.Danger
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Paper
import com.vibecode.daosubatma.ui.GhostArtwork
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import kotlinx.coroutines.delay
import kotlin.random.Random

private const val TALISMAN_TIME_LIMIT_SECONDS = 10

@Composable
fun TalismanScreen(
    talisman: TalismanType,
    ghost: GhostDefinition,
    audio: GameAudio,
    onBack: () -> Unit,
    onCompleted: (score: Float, captured: Boolean) -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var path by remember { mutableStateOf(emptyList<Offset>()) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var isDrawing by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableStateOf(TALISMAN_TIME_LIMIT_SECONDS) }
    var completed by remember { mutableStateOf(false) }
    var pendingScore by remember { mutableStateOf<Float?>(null) }
    var pendingCaptured by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        repeat(TALISMAN_TIME_LIMIT_SECONDS) {
            delay(1_000)
            if (completed) return@LaunchedEffect
            secondsLeft -= 1
        }
        if (!completed) {
            completed = true
            audio.stopSealLoop()
            audio.play(SoundEffect.Timeout)
            onCompleted(0f, false)
        }
    }

    fun finishAttempt() {
        if (completed) return
        completed = true
        audio.stopSealLoop()
        val score = scoreCircle(path, canvasSize)
        val chance = captureChance(ghost, talisman, score)
        pendingScore = score
        pendingCaptured = Random.nextFloat() < chance
    }

    LaunchedEffect(isDrawing) {
        if (isDrawing) {
            audio.play(SoundEffect.DrawStart, volume = .65f)
            audio.startSealLoop()
        } else {
            audio.stopSealLoop()
        }
    }

    Box(Modifier.fillMaxSize().background(Ink)) {
        CameraPreview(Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Ink.copy(alpha = .36f)))
        Column(
            Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TalismanTopHud(secondsLeft) {
                if (pendingScore == null) {
                    audio.stopSealLoop()
                    onBack()
                }
            }
            Spacer(Modifier.height(18.dp))
            GhostArtwork(ghost, Modifier.height(96.dp))
            Text(ghost.localizedName(context), color = Paper, fontSize = 16.sp)
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                TalismanCanvas(path, { path = it }, { canvasSize = it }, { isDrawing = it }, ::finishAttempt, Modifier.fillMaxSize())
                if (pendingScore == null) {
                    Image(
                        painter = painterResource(talisman.artworkRes),
                        contentDescription = null,
                        modifier = Modifier.alpha(if (isDrawing) .92f else .72f).height(178.dp),
                    )
                }
            }
            TalismanBottomHud()
        }
        pendingScore?.let { score ->
            TalismanFlightAfterSeal(talisman) {
                audio.play(if (pendingCaptured) SoundEffect.SealSuccess else SoundEffect.SealFail)
                onCompleted(score, pendingCaptured)
            }
        }
    }
}

@Composable
private fun TalismanFlightAfterSeal(talisman: TalismanType, onArrived: () -> Unit) {
    val progress = remember(talisman) { Animatable(0f) }
    LaunchedEffect(talisman) {
        progress.animateTo(1f, tween(durationMillis = 820, easing = FastOutSlowInEasing))
        delay(100)
        onArrived()
    }
    val fraction = progress.value
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(talisman.artworkRes),
            contentDescription = null,
            modifier = Modifier
                .height(178.dp)
                .offset(y = (-250f * fraction).dp)
                .graphicsLayer {
                    val scale = 1f - (.46f * fraction)
                    scaleX = scale
                    scaleY = scale
                    rotationZ = (1f - fraction) * -7f
                    alpha = 1f - (.12f * fraction)
                },
        )
    }
}
