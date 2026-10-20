package com.vibecode.daosubatma.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import com.vibecode.daosubatma.core.ads.InterstitialAdController
import com.vibecode.daosubatma.core.ads.PersistentBannerAd
import com.vibecode.daosubatma.core.ads.findActivity
import com.vibecode.daosubatma.core.audio.GameAudio
import com.vibecode.daosubatma.core.audio.MusicTrack
import com.vibecode.daosubatma.core.audio.SoundEffect
import com.vibecode.daosubatma.core.data.GhostStorage
import com.vibecode.daosubatma.core.model.TalismanType
import com.vibecode.daosubatma.core.model.GhostCatalog
import com.vibecode.daosubatma.core.model.GhostExchange
import com.vibecode.daosubatma.core.model.GhostRarity
import com.vibecode.daosubatma.core.model.PlayerInventory
import com.vibecode.daosubatma.core.model.TaoistProgression
import com.vibecode.daosubatma.core.model.localizedDescription
import com.vibecode.daosubatma.feature.collection.CollectionScreen
import com.vibecode.daosubatma.feature.hunt.GameScreen
import com.vibecode.daosubatma.feature.menu.MenuScreen
import com.vibecode.daosubatma.feature.result.ResultScreen
import com.vibecode.daosubatma.feature.splash.SplashScreen
import com.vibecode.daosubatma.feature.talisman.TalismanScreen

private enum class Destination { Splash, Menu, Hunt, Talisman, Result, Collection }

@Composable
fun DaoSuBatMaApp() {
    val context = LocalContext.current
    val ghostStorage = remember(context) { GhostStorage(context) }
    val interstitialAdController = remember(context) {
        InterstitialAdController(context.applicationContext)
    }
    val gameAudio = remember(context) { GameAudio(context.applicationContext) }
    DisposableEffect(gameAudio) { onDispose { gameAudio.release() } }
    var destinationName by rememberSaveable { mutableStateOf(Destination.Splash.name) }
    var score by rememberSaveable { mutableFloatStateOf(0f) }
    var captured by rememberSaveable { mutableStateOf(false) }
    var collectionRevision by rememberSaveable { mutableStateOf(0) }
    var playerInventory by remember { mutableStateOf(ghostStorage.loadInventory()) }
    var taoistProgress by remember { mutableStateOf(ghostStorage.loadTaoistProgress()) }
    var resultExperienceGained by rememberSaveable { mutableStateOf(0) }
    var resultGiftDropped by rememberSaveable { mutableStateOf(false) }
    var openedGiftDescription by rememberSaveable { mutableStateOf<String?>(null) }
    var activeTalismanName by rememberSaveable { mutableStateOf(TalismanType.Yellow.name) }
    var activeGhostSpeciesId by rememberSaveable { mutableStateOf("bong_ma") }
    var activeGhostRarityName by rememberSaveable { mutableStateOf(GhostRarity.Common.name) }
    LaunchedEffect(destinationName) {
        gameAudio.playMusic(
            when (Destination.valueOf(destinationName)) {
                Destination.Hunt, Destination.Talisman, Destination.Result -> MusicTrack.Hunt
                else -> MusicTrack.Menu
            },
        )
    }
    val activeTalisman = TalismanType.valueOf(activeTalismanName)
    val activeGhost = GhostCatalog.find(activeGhostSpeciesId, GhostRarity.valueOf(activeGhostRarityName))
    val talismanInventory = mapOf(
        TalismanType.Yellow to -1,
        TalismanType.Red to playerInventory.redTalismans,
        TalismanType.Purple to playerInventory.purpleTalismans,
        TalismanType.Vajra to playerInventory.vajraTalismans,
    )

    fun updateInventory(transform: (PlayerInventory) -> PlayerInventory) {
        playerInventory = transform(playerInventory)
        ghostStorage.saveInventory(playerInventory)
    }

    fun consumeTalisman(talisman: TalismanType) {
        updateInventory { inventory ->
            when (talisman) {
                TalismanType.Yellow -> inventory
                TalismanType.Red -> inventory.copy(redTalismans = (inventory.redTalismans - 1).coerceAtLeast(0))
                TalismanType.Purple -> inventory.copy(purpleTalismans = (inventory.purpleTalismans - 1).coerceAtLeast(0))
                TalismanType.Vajra -> inventory.copy(vajraTalismans = (inventory.vajraTalismans - 1).coerceAtLeast(0))
            }
        }
    }
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            when (Destination.valueOf(destinationName)) {
        Destination.Splash -> SplashScreen { destinationName = Destination.Menu.name }
        Destination.Menu -> MenuScreen(
            onStart = {
                gameAudio.play(SoundEffect.ScanPulse)
                destinationName = Destination.Hunt.name
            },
            onOpenCollection = { destinationName = Destination.Collection.name },
        )
        Destination.Hunt -> GameScreen(
            inventory = talismanInventory,
            taoistProgress = taoistProgress,
            audio = gameAudio,
            onBack = { destinationName = Destination.Menu.name },
            onUseTalisman = { talisman, ghost ->
                activeTalismanName = talisman.name
                activeGhostSpeciesId = ghost.speciesId
                activeGhostRarityName = ghost.rarity.name
                destinationName = Destination.Talisman.name
            },
        )
        Destination.Talisman -> TalismanScreen(
            talisman = activeTalisman,
            ghost = activeGhost,
            audio = gameAudio,
            onBack = { destinationName = Destination.Hunt.name },
            onCompleted = { attemptScore, wasCaptured ->
                score = attemptScore
                captured = wasCaptured
                consumeTalisman(activeTalisman)
                resultExperienceGained = 0
                resultGiftDropped = false
                openedGiftDescription = null
                if (wasCaptured) {
                    ghostStorage.recordGhost(activeGhost, attemptScore)
                    val award = TaoistProgression.awardCapture(taoistProgress, playerInventory, activeGhost.rarity)
                    taoistProgress = award.progress
                    playerInventory = award.inventory
                    ghostStorage.saveTaoistProgress(taoistProgress)
                    ghostStorage.saveInventory(playerInventory)
                    resultExperienceGained = award.experienceGained
                    resultGiftDropped = award.giftDropped
                    collectionRevision += 1
                }
                interstitialAdController.preload()
                destinationName = Destination.Result.name
            },
        )
        Destination.Result -> ResultScreen(
            activeGhost,
            score,
            captured,
            audio = gameAudio,
            experienceGained = resultExperienceGained,
            giftDropped = resultGiftDropped,
            openedGiftDescription = openedGiftDescription,
            onOpenGift = {
                TaoistProgression.openGift(taoistProgress, playerInventory)?.let { gift ->
                    taoistProgress = gift.progress
                    playerInventory = gift.inventory
                    ghostStorage.saveTaoistProgress(taoistProgress)
                    ghostStorage.saveInventory(playerInventory)
                    openedGiftDescription = gift.localizedDescription(context)
                    resultGiftDropped = false
                }
            },
            onContinue = {
                val activity = context.findActivity()
                if (activity == null) {
                    destinationName = Destination.Hunt.name
                } else {
                    gameAudio.play(SoundEffect.AdTransition)
                    interstitialAdController.showAfterResult(activity) {
                        destinationName = Destination.Hunt.name
                    }
                }
            },
            onMenu = { destinationName = Destination.Menu.name },
        )
        Destination.Collection -> {
            collectionRevision
            ghostStorage.removeLegacyTutorialGhosts()
            val capturedGhosts = ghostStorage.loadCapturedGhosts()
            CollectionScreen(
                ghosts = capturedGhosts,
                inventory = playerInventory,
                onBack = { destinationName = Destination.Menu.name },
                onDissolveGhost = { ghost ->
                    val isDuplicate = capturedGhosts.count {
                        it.speciesId == ghost.speciesId && it.rarity == ghost.rarity
                    } > 1
                    if (!isDuplicate) {
                        false
                    } else {
                        val reward = GhostExchange.rewardFor(ghost.rarity)
                        ghostStorage.removeGhost(ghost.id)
                        gameAudio.play(SoundEffect.Alchemy)
                        updateInventory {
                            it.copy(
                                spiritEssence = it.spiritEssence + reward.spiritEssence,
                                vajraFragments = it.vajraFragments + reward.vajraFragments,
                            )
                        }
                        gameAudio.play(SoundEffect.Alchemy)
                        collectionRevision += 1
                        true
                    }
                },
                onRedeemRed = {
                    if (GhostExchange.canRedeemRed(playerInventory)) {
                        updateInventory {
                            it.copy(
                                redTalismans = it.redTalismans + 1,
                                spiritEssence = it.spiritEssence - GhostExchange.RED_TALISMAN_COST,
                            )
                        }
                    }
                },
                onRedeemPurple = {
                    if (GhostExchange.canRedeemPurple(playerInventory)) {
                        updateInventory {
                            it.copy(
                                redTalismans = it.redTalismans - GhostExchange.PURPLE_TALISMAN_RED_COST,
                                purpleTalismans = it.purpleTalismans + 1,
                                spiritEssence = it.spiritEssence - GhostExchange.PURPLE_TALISMAN_COST,
                            )
                        }
                    }
                },
            )
        }
        }
        }
        PersistentBannerAd(Modifier.fillMaxWidth())
    }
}
