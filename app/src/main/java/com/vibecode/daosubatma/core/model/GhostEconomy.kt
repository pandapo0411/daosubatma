package com.vibecode.daosubatma.core.model

data class PlayerInventory(
    val redTalismans: Int = 3,
    val purpleTalismans: Int = 2,
    val vajraTalismans: Int = 1,
    val spiritEssence: Int = 0,
    val vajraFragments: Int = 0,
)

data class ExchangeReward(val spiritEssence: Int, val vajraFragments: Int = 0)

object GhostExchange {
    const val RED_TALISMAN_COST = 12
    const val PURPLE_TALISMAN_COST = 38
    const val PURPLE_TALISMAN_RED_COST = 1
    const val VAJRA_FRAGMENT_GOAL = 5

    fun rewardFor(rarity: GhostRarity): ExchangeReward = when (rarity) {
        GhostRarity.Common -> ExchangeReward(spiritEssence = 1)
        GhostRarity.Rare -> ExchangeReward(spiritEssence = 4)
        GhostRarity.Epic -> ExchangeReward(spiritEssence = 15)
        GhostRarity.Legendary -> ExchangeReward(spiritEssence = 60, vajraFragments = 1)
    }

    fun canRedeemRed(inventory: PlayerInventory): Boolean =
        inventory.spiritEssence >= RED_TALISMAN_COST

    fun canRedeemPurple(inventory: PlayerInventory): Boolean =
        inventory.spiritEssence >= PURPLE_TALISMAN_COST &&
            inventory.redTalismans >= PURPLE_TALISMAN_RED_COST
}
