package com.vibecode.daosubatma.core.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vibecode.daosubatma.R

enum class TalismanType(
    @DrawableRes val artworkRes: Int,
    @StringRes val nameRes: Int,
    @StringRes val detailRes: Int,
    val captureBonus: Float,
    val isUnlimited: Boolean = false,
) {
    Yellow(R.drawable.item_bua_vang, R.string.talisman_yellow_name, R.string.talisman_yellow_detail, 0f, true),
    Red(R.drawable.item_bua_do, R.string.talisman_red_name, R.string.talisman_red_detail, .15f),
    Purple(R.drawable.item_bua_tim, R.string.talisman_purple_name, R.string.talisman_purple_detail, .30f),
    Vajra(R.drawable.item_bua_kimcang, R.string.talisman_vajra_name, R.string.talisman_vajra_detail, .50f),
}

fun captureChance(ghost: GhostDefinition, talisman: TalismanType, score: Float): Float {
    if (score < .7f) return 0f
    val resonanceBonus = if (talisman.matches(ghost.rarity)) .10f else 0f
    val perfectSealBonus = if (score >= .9f) .10f else 0f
    return (
        ghost.rarity.captureRate +
            talisman.captureBonus +
            resonanceBonus +
            perfectSealBonus
        ).coerceAtMost(.98f)
}

fun TalismanType.matches(rarity: GhostRarity): Boolean = when (this) {
    TalismanType.Yellow -> rarity == GhostRarity.Common
    TalismanType.Red -> rarity == GhostRarity.Rare
    TalismanType.Purple -> rarity == GhostRarity.Epic
    TalismanType.Vajra -> rarity == GhostRarity.Legendary
}
