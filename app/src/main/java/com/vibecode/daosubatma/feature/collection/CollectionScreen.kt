package com.vibecode.daosubatma.feature.collection

import android.content.Intent
import android.text.format.DateFormat
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vibecode.daosubatma.R
import com.vibecode.daosubatma.core.data.CapturedGhost
import com.vibecode.daosubatma.core.model.GhostCatalog
import com.vibecode.daosubatma.core.model.GhostExchange
import com.vibecode.daosubatma.core.model.GhostRarity
import com.vibecode.daosubatma.core.model.localizedName
import com.vibecode.daosubatma.core.model.localizedLore
import com.vibecode.daosubatma.core.model.PlayerInventory
import com.vibecode.daosubatma.ui.Gold
import com.vibecode.daosubatma.ui.GhostArtwork
import com.vibecode.daosubatma.ui.GoldButton
import com.vibecode.daosubatma.ui.Ink
import com.vibecode.daosubatma.ui.Mist
import com.vibecode.daosubatma.ui.Paper

private val CardShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)

@Composable
fun CollectionScreen(
    ghosts: List<CapturedGhost>,
    inventory: PlayerInventory,
    onBack: () -> Unit,
    onDissolveGhost: (CapturedGhost) -> Boolean,
    onRedeemRed: () -> Unit,
    onRedeemPurple: () -> Unit,
) {
    var selectedGhost by remember { mutableStateOf<CapturedGhost?>(null) }
    var showAlchemy by remember { mutableStateOf(false) }
    val sortedGhosts = ghosts.sortedWith(
        compareByDescending<CapturedGhost> { it.rarity.ordinal }
            .thenByDescending { it.capturedAtMillis },
    )
    BackHandler(enabled = selectedGhost != null) { selectedGhost = null }

    Box(Modifier.fillMaxSize().background(Ink)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 156.dp),
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                CollectionHeader(
                    onBack = onBack,
                    onOpenAlchemy = { showAlchemy = true },
                )
            }
            if (ghosts.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyCollection()
                }
            } else {
                items(sortedGhosts, key = { it.id }) { ghost ->
                    GhostCaptureCard(ghost, onClick = { selectedGhost = ghost })
                }
            }
        }
        selectedGhost?.let { ghost ->
            GhostCardDetail(
                ghost = ghost,
                onDismiss = { selectedGhost = null },
                isDuplicate = ghosts.count {
                    it.speciesId == ghost.speciesId && it.rarity == ghost.rarity
                } > 1,
                onDissolve = {
                    if (onDissolveGhost(ghost)) selectedGhost = null
                },
            )
        }
        if (showAlchemy) {
            AlchemyPanel(
                inventory = inventory,
                onDismiss = { showAlchemy = false },
                onRedeemRed = onRedeemRed,
                onRedeemPurple = onRedeemPurple,
            )
        }
    }
}

@Composable
private fun CollectionHeader(
    onBack: () -> Unit,
    onOpenAlchemy: () -> Unit,
) = Column {
    Text(
        stringResource(R.string.back),
        color = Paper,
        modifier = Modifier.clickable(onClick = onBack).padding(vertical = 10.dp),
        fontWeight = FontWeight.Bold,
    )
    Spacer(Modifier.height(12.dp))
    Text(
        stringResource(R.string.ghost_compendium),
        color = Gold,
        fontSize = 25.sp,
        fontWeight = FontWeight.Black,
    )
    Text(stringResource(R.string.collection_subtitle), color = Mist, fontSize = 14.sp)
    Spacer(Modifier.height(10.dp))
    GoldButton(
        label = stringResource(R.string.open_alchemy),
        onClick = onOpenAlchemy,
        modifier = Modifier.height(44.dp),
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun EmptyCollection() = Box(
    Modifier.fillMaxWidth().padding(vertical = 56.dp),
    contentAlignment = Alignment.Center,
) {
    Text(stringResource(R.string.collection_empty), color = Mist, textAlign = TextAlign.Center)
}

@Composable
private fun GhostCaptureCard(ghost: CapturedGhost, onClick: () -> Unit) {
    val context = LocalContext.current
    val definition = GhostCatalog.find(ghost.speciesId, ghost.rarity)
    val frameColor = ghost.rarity.frameColor()
    val capturedDate = DateFormat.getMediumDateFormat(context).format(ghost.capturedAtMillis)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(.45f)
            .border(2.dp, frameColor, CardShape)
            .background(Color(0xFF100D18), CardShape)
            .clip(CardShape)
            .clickable(onClick = onClick)
            .padding(9.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.ghost_card),
            color = frameColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.1.sp,
        )
        Text(
            "#" + ghost.id.take(4).uppercase() + " / 108",
            color = Mist,
            fontSize = 9.sp,
        )
        Spacer(Modifier.height(7.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.dp, frameColor.copy(alpha = .55f), CardShape)
                .background(ghostBackdrop(frameColor), CardShape),
            contentAlignment = Alignment.Center,
        ) {
            Text("✦", color = frameColor.copy(alpha = .25f), fontSize = 112.sp)
            GhostArtwork(definition, Modifier.fillMaxSize().padding(8.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(ghost.rarity.labelRes),
            modifier = Modifier
                .fillMaxWidth()
                .background(frameColor, androidx.compose.foundation.shape.RoundedCornerShape(50))
                .padding(vertical = 4.dp),
            color = Ink,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(7.dp))
        Text(
            definition.localizedName(context),
            color = Paper,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.height(38.dp),
        )
        Text(
            definition.localizedLore(context),
            color = Mist,
            fontSize = 9.sp,
            textAlign = TextAlign.Center,
            lineHeight = 11.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.height(27.dp),
        )
        GhostCardStatLine(
            label = stringResource(R.string.ghost_power),
            value = "+" + ghost.rarity.cardStats.power,
            compact = true,
        )
        GhostCardStatLine(
            label = stringResource(R.string.ghost_level),
            value = stringResource(R.string.ghost_level_value, ghost.rarity.cardStats.level),
            compact = true,
        )
        GhostCardStatLine(
            label = stringResource(R.string.ghost_true_rarity),
            value = stringResource(R.string.ghost_ownership, ghost.rarity.cardStats.ownershipPercent),
            compact = true,
        )
        Text(
            capturedDate,
            color = Mist,
            fontSize = 9.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 135.dp),
        )
    }
}

@Composable
private fun GhostCardDetail(
    ghost: CapturedGhost,
    onDismiss: () -> Unit,
    isDuplicate: Boolean,
    onDissolve: () -> Unit,
) {
    val context = LocalContext.current
    val definition = GhostCatalog.find(ghost.speciesId, ghost.rarity)
    val frameColor = ghost.rarity.frameColor()
    val capturedDate = DateFormat.getMediumDateFormat(context).format(ghost.capturedAtMillis)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.back),
            color = Paper,
            modifier = Modifier.align(Alignment.Start).clickable(onClick = onDismiss).padding(vertical = 10.dp),
            fontWeight = FontWeight.Bold,
        )
        Text(
            stringResource(R.string.ghost_card),
            color = frameColor,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
        )
        Text(
            "#" + ghost.id.take(8).uppercase() + " / 108",
            color = Mist,
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(2.dp, frameColor, CardShape)
                .background(ghostBackdrop(frameColor), CardShape),
            contentAlignment = Alignment.Center,
        ) {
            Text("✦", color = frameColor.copy(alpha = .25f), fontSize = 190.sp)
            GhostArtwork(definition, Modifier.fillMaxSize().padding(24.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text(
            stringResource(ghost.rarity.labelRes),
            modifier = Modifier
                .fillMaxWidth()
                .background(frameColor, androidx.compose.foundation.shape.RoundedCornerShape(50))
                .padding(vertical = 7.dp),
            color = Ink,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            definition.localizedName(context),
            color = Paper,
            fontSize = 27.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            definition.localizedLore(context),
            color = Mist,
            fontSize = 14.sp,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(8.dp))
        GhostCardStatLine(
            label = stringResource(R.string.ghost_power),
            value = "+" + ghost.rarity.cardStats.power,
        )
        GhostCardStatLine(
            label = stringResource(R.string.ghost_level),
            value = stringResource(R.string.ghost_level_value, ghost.rarity.cardStats.level),
        )
        GhostCardStatLine(
            label = stringResource(R.string.ghost_true_rarity),
            value = stringResource(R.string.ghost_ownership, ghost.rarity.cardStats.ownershipPercent),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.capture_accuracy, (ghost.accuracy * 100).toInt()),
            color = Mist,
            fontSize = 14.sp,
        )
        Text(
            stringResource(R.string.captured_at, capturedDate),
            color = Mist,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(18.dp))
        GoldButton(
            label = stringResource(R.string.share_ghost_card),
            onClick = {
                val shareText = context.getString(
                    R.string.share_ghost_message,
                    definition.localizedName(context),
                    context.getString(ghost.rarity.labelRes),
                )
                context.startActivity(
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, shareText),
                        context.getString(R.string.share_ghost_chooser),
                    ),
                )
            },
        )
        if (isDuplicate) {
            Spacer(Modifier.height(10.dp))
            Text(
                stringResource(
                    R.string.dissolve_duplicate,
                    GhostExchange.rewardFor(ghost.rarity).spiritEssence,
                ),
                color = Gold,
                modifier = Modifier.clickable(onClick = onDissolve).padding(8.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun AlchemyPanel(
    inventory: PlayerInventory,
    onDismiss: () -> Unit,
    onRedeemRed: () -> Unit,
    onRedeemPurple: () -> Unit,
) {
    var showTutorial by remember { mutableStateOf(false) }
    if (showTutorial) {
        AlchemyTutorial(onBack = { showTutorial = false })
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.back),
            color = Paper,
            modifier = Modifier.align(Alignment.Start).clickable(onClick = onDismiss).padding(vertical = 10.dp),
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(18.dp))
        Text(stringResource(R.string.alchemy_title), color = Gold, fontSize = 26.sp, fontWeight = FontWeight.Black)
        Text(
            stringResource(R.string.alchemy_hint),
            color = Mist,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(14.dp))
        GoldButton(
            label = stringResource(R.string.alchemy_tutorial),
            onClick = { showTutorial = true },
            modifier = Modifier.height(44.dp),
        )
        Spacer(Modifier.height(30.dp))
        AlchemyInventoryLine(stringResource(R.string.spirit_essence), inventory.spiritEssence.toString())
        AlchemyInventoryLine(
            stringResource(R.string.vajra_fragments),
            inventory.vajraFragments.toString() + " / " + GhostExchange.VAJRA_FRAGMENT_GOAL,
        )
        AlchemyInventoryLine(stringResource(R.string.talisman_red_name), "×" + inventory.redTalismans)
        AlchemyInventoryLine(stringResource(R.string.talisman_purple_name), "×" + inventory.purpleTalismans)
        Spacer(Modifier.height(26.dp))
        GoldButton(
            label = stringResource(R.string.redeem_red, GhostExchange.RED_TALISMAN_COST),
            onClick = onRedeemRed,
            enabled = GhostExchange.canRedeemRed(inventory),
        )
        Spacer(Modifier.height(12.dp))
        GoldButton(
            label = stringResource(
                R.string.redeem_purple,
                GhostExchange.PURPLE_TALISMAN_COST,
                GhostExchange.PURPLE_TALISMAN_RED_COST,
            ),
            onClick = onRedeemPurple,
            enabled = GhostExchange.canRedeemPurple(inventory),
        )
        Spacer(Modifier.height(18.dp))
        Text(stringResource(R.string.vajra_event_only), color = Mist, fontSize = 13.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun AlchemyTutorial(onBack: () -> Unit) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(Ink)
        .statusBarsPadding()
        .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Text(
        stringResource(R.string.back),
        color = Paper,
        modifier = Modifier.align(Alignment.Start).clickable(onClick = onBack).padding(vertical = 10.dp),
        fontWeight = FontWeight.Bold,
    )
    Spacer(Modifier.height(24.dp))
    Text(stringResource(R.string.alchemy_tutorial_title), color = Gold, fontSize = 25.sp, fontWeight = FontWeight.Black)
    Spacer(Modifier.height(16.dp))
    TutorialDuplicatePreview()
    Spacer(Modifier.height(24.dp))
    TutorialStep(1, stringResource(R.string.alchemy_tutorial_step_one))
    TutorialStep(2, stringResource(R.string.alchemy_tutorial_step_two))
    TutorialStep(3, stringResource(R.string.alchemy_tutorial_step_three))
    TutorialStep(4, stringResource(R.string.alchemy_tutorial_step_four))
    Spacer(Modifier.weight(1f))
    GoldButton(label = stringResource(R.string.alchemy_tutorial_done), onClick = onBack)
}

@Composable
private fun TutorialDuplicatePreview() {
    val ghost = remember { GhostCatalog.find("bong_ma", GhostRarity.Common) }
    Text(stringResource(R.string.tutorial_duplicate_example), color = Mist, fontSize = 13.sp)
    Spacer(Modifier.height(8.dp))
    Row(Modifier.fillMaxWidth().height(132.dp)) {
        repeat(2) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp)
                    .border(1.dp, GhostRarity.Common.frameColor(), CardShape)
                    .background(ghostBackdrop(GhostRarity.Common.frameColor()), CardShape),
                contentAlignment = Alignment.Center,
            ) {
                GhostArtwork(ghost, Modifier.fillMaxSize().padding(16.dp))
                Text(
                    stringResource(R.string.tutorial_sample_card, index + 1),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp),
                    color = Paper,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun TutorialStep(number: Int, text: String) = Row(
    Modifier.fillMaxWidth().padding(vertical = 12.dp),
    verticalAlignment = Alignment.Top,
) {
    Text(
        number.toString(),
        modifier = Modifier
            .background(Gold, androidx.compose.foundation.shape.RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        color = Ink,
        fontWeight = FontWeight.Black,
    )
    Text(
        text,
        color = Paper,
        fontSize = 15.sp,
        modifier = Modifier.padding(start = 14.dp, top = 4.dp),
    )
}

@Composable
private fun AlchemyInventoryLine(label: String, value: String) = Row(
    Modifier.fillMaxWidth().padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(label, color = Mist, fontSize = 16.sp, modifier = Modifier.weight(1f))
    Text(value, color = Paper, fontSize = 18.sp, fontWeight = FontWeight.Black)
}

@Composable
private fun GhostCardStatLine(label: String, value: String, compact: Boolean = false) {
    val textSize = if (compact) 9.sp else 14.sp
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Mist, fontSize = textSize, maxLines = 1, modifier = Modifier.weight(1f))
        Text(
            value,
            color = Paper,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.End,
        )
    }
}

private fun GhostRarity.frameColor(): Color = when (this) {
    GhostRarity.Common -> Color(0xFFAAA7AE)
    GhostRarity.Rare -> Color(0xFF73B7FF)
    GhostRarity.Epic -> Color(0xFFC58CFF)
    GhostRarity.Legendary -> Gold
}

private fun ghostBackdrop(frameColor: Color): Brush = Brush.radialGradient(
    colors = listOf(
        frameColor.copy(alpha = .52f),
        Color(0xFF2B2636),
        Color(0xFF100D18),
    ),
)
