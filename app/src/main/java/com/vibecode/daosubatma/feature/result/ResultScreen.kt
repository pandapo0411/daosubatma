package com.vibecode.daosubatma.feature.result

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecode.daosubatma.R
import com.vibecode.daosubatma.core.audio.GameAudio
import com.vibecode.daosubatma.core.audio.SoundEffect
import com.vibecode.daosubatma.core.model.GhostDefinition
import com.vibecode.daosubatma.ui.Danger
import com.vibecode.daosubatma.ui.Gold
import com.vibecode.daosubatma.ui.GoldButton
import com.vibecode.daosubatma.ui.GhostArtwork
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Mist
import com.vibecode.daosubatma.ui.Paper

@Composable
fun ResultScreen(
    ghost: GhostDefinition,
    score: Float,
    captured: Boolean,
    audio: GameAudio,
    experienceGained: Int,
    giftDropped: Boolean,
    openedGiftDescription: String?,
    onOpenGift: () -> Unit,
    onContinue: () -> Unit,
    onMenu: () -> Unit,
) {
    val percentage = (score * 100).toInt()
    var isOpeningGift by remember(ghost.speciesId, ghost.rarity) { mutableStateOf(false) }
    LaunchedEffect(ghost.speciesId, captured) {
        if (captured) audio.play(SoundEffect.CardReveal)
    }
    LaunchedEffect(isOpeningGift) {
        if (isOpeningGift) {
            audio.play(SoundEffect.GiftOpen)
            kotlinx.coroutines.delay(850)
            onOpenGift()
        }
    }
    Column(Modifier.fillMaxSize().background(Ink).padding(28.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text(stringResource(if (captured) R.string.capture_impact else R.string.talisman_faded), color = if (captured) Gold else Danger, fontSize = 35.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(12.dp))
        GhostArtwork(ghost, Modifier.height(132.dp).padding(vertical = 6.dp))
        Text(stringResource(if (captured) R.string.capture_success else R.string.ghost_gone), color = Paper, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(stringResource(R.string.stroke_score, percentage), color = Mist, fontSize = 16.sp)
        if (captured) {
            Text(stringResource(R.string.power_reward), color = Gold, fontSize = 14.sp, modifier = Modifier.padding(top = 10.dp))
            Text(stringResource(R.string.experience_gain, experienceGained), color = Gold, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            if (giftDropped) {
                Text(stringResource(R.string.gift_box_drop), color = Gold, fontSize = 14.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 14.dp))
                GiftBoxDrop(isOpeningGift) { isOpeningGift = true }
                GoldButton(stringResource(R.string.open_gift_box), { isOpeningGift = true }, enabled = !isOpeningGift)
            }
            openedGiftDescription?.let {
                Text(stringResource(R.string.gift_received, it), color = Paper, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
            }
        }
        Spacer(Modifier.height(if (giftDropped) 16.dp else 38.dp))
        GoldButton(stringResource(if (captured) R.string.continue_hunt else R.string.find_another_ghost), onContinue)
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.back_to_menu), color = Mist, modifier = Modifier.clickable(onClick = onMenu))
    }
}

@Composable
private fun GiftBoxDrop(isOpening: Boolean, onOpen: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isOpening) 1.18f else 1f,
        animationSpec = tween(420, easing = FastOutSlowInEasing),
        label = "giftBoxScale",
    )
    Box(
        modifier = Modifier.size(112.dp).padding(vertical = 6.dp).clickable(enabled = !isOpening, onClick = onOpen),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.gift_glow),
            contentDescription = null,
            modifier = Modifier.size(if (isOpening) 112.dp else 96.dp).alpha(if (isOpening) 1f else .55f),
        )
        Image(
            painter = painterResource(if (isOpening) R.drawable.gift_box_open else R.drawable.gift_box_closed),
            contentDescription = stringResource(R.string.open_gift_box),
            modifier = Modifier.size(90.dp).graphicsLayer { scaleX = scale; scaleY = scale },
        )
        if (isOpening) {
            Image(
                painter = painterResource(R.drawable.gift_particles),
                contentDescription = null,
                modifier = Modifier.size(112.dp).graphicsLayer { scaleX = scale; scaleY = scale },
            )
        }
    }
}
