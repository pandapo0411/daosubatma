package com.vibecode.daosubatma.core.model

import androidx.annotation.StringRes
import com.vibecode.daosubatma.R
import kotlin.random.Random

enum class TaoistRank(@StringRes val titleRes: Int, val minimumLevel: Int) {
    DaoDong(R.string.rank_dao_dong, 1),
    PhapSu(R.string.rank_phap_su, 10),
    ThienSu(R.string.rank_thien_su, 20),
    ChanNhan(R.string.rank_chan_nhan, 35),
    DaoTo(R.string.rank_dao_to, 50),
}

data class TaoistProgress(
    val level: Int = 1,
    val experience: Int = 0,
    val pendingGiftBoxes: Int = 0,
    val unlockedSkills: Set<String> = emptySet(),
)

data class CaptureProgressAward(
    val progress: TaoistProgress,
    val inventory: PlayerInventory,
    val experienceGained: Int,
    val levelsGained: Int,
    val giftDropped: Boolean,
)

data class GiftOpening(
    val progress: TaoistProgress,
    val inventory: PlayerInventory,
    val descriptionKey: String,
)

fun GiftOpening.localizedDescription(context: android.content.Context): String = when (descriptionKey) {
    "red" -> context.getString(R.string.gift_talisman_reward, context.getString(R.string.talisman_red_name), 1)
    "purple" -> context.getString(R.string.gift_talisman_reward, context.getString(R.string.talisman_purple_name), 1)
    "exp_60" -> context.getString(R.string.gift_experience_reward, 60)
    "exp_90" -> context.getString(R.string.gift_experience_reward, 90)
    "skill_sight" -> context.getString(R.string.gift_skill_reward, context.getString(R.string.skill_sight))
    "skill_ward" -> context.getString(R.string.gift_skill_reward, context.getString(R.string.skill_ward))
    else -> context.getString(R.string.gift_skill_reward, context.getString(R.string.skill_step))
}

private data class ExperienceGrant(val progress: TaoistProgress, val inventory: PlayerInventory, val levelsGained: Int)

object TaoistProgression {
    fun rankFor(level: Int): TaoistRank = TaoistRank.entries.last { level >= it.minimumLevel }

    fun experienceForNextLevel(level: Int): Int = 75 + (level - 1) * 25

    fun experienceFromCapture(rarity: GhostRarity): Int = when (rarity) {
        GhostRarity.Common -> 20
        GhostRarity.Rare -> 45
        GhostRarity.Epic -> 85
        GhostRarity.Legendary -> 150
    }

    private fun giftDropChance(rarity: GhostRarity): Float = when (rarity) {
        GhostRarity.Common -> .12f
        GhostRarity.Rare -> .20f
        GhostRarity.Epic -> .35f
        GhostRarity.Legendary -> .60f
    }

    fun awardCapture(
        current: TaoistProgress,
        inventory: PlayerInventory,
        rarity: GhostRarity,
        random: Random = Random.Default,
    ): CaptureProgressAward {
        val gained = experienceFromCapture(rarity)
        val experienceGrant = addExperience(current, inventory, gained)
        val giftDropped = random.nextFloat() < giftDropChance(rarity)
        return CaptureProgressAward(
            progress = experienceGrant.progress.copy(pendingGiftBoxes = current.pendingGiftBoxes + if (giftDropped) 1 else 0),
            inventory = experienceGrant.inventory,
            experienceGained = gained,
            levelsGained = experienceGrant.levelsGained,
            giftDropped = giftDropped,
        )
    }

    fun openGift(current: TaoistProgress, inventory: PlayerInventory, random: Random = Random.Default): GiftOpening? {
        if (current.pendingGiftBoxes <= 0) return null
        val remaining = current.copy(pendingGiftBoxes = current.pendingGiftBoxes - 1)
        return when (random.nextInt(100)) {
            in 0..44 -> GiftOpening(remaining, inventory.copy(redTalismans = inventory.redTalismans + 1), "red")
            in 45..64 -> GiftOpening(remaining, inventory.copy(purpleTalismans = inventory.purpleTalismans + 1), "purple")
            in 65..79 -> {
                val grant = addExperience(remaining, inventory, 60)
                GiftOpening(grant.progress, grant.inventory, "exp_60")
            }
            else -> {
                val skill = listOf("skill_sight", "skill_ward", "skill_step").firstOrNull { it !in remaining.unlockedSkills }
                if (skill == null) {
                    val grant = addExperience(remaining, inventory, 90)
                    GiftOpening(grant.progress, grant.inventory, "exp_90")
                }
                else GiftOpening(remaining.copy(unlockedSkills = remaining.unlockedSkills + skill), inventory, skill)
            }
        }
    }

    private fun addExperience(current: TaoistProgress, inventory: PlayerInventory, amount: Int): ExperienceGrant {
        var level = current.level
        var experience = current.experience + amount
        var rewards = inventory
        var levelsGained = 0
        while (experience >= experienceForNextLevel(level)) {
            experience -= experienceForNextLevel(level)
            level += 1
            levelsGained += 1
            rewards = when (rankFor(level)) {
                TaoistRank.DaoDong, TaoistRank.PhapSu -> rewards.copy(redTalismans = rewards.redTalismans + 1)
                TaoistRank.ThienSu, TaoistRank.ChanNhan -> rewards.copy(purpleTalismans = rewards.purpleTalismans + 1)
                TaoistRank.DaoTo -> rewards.copy(vajraTalismans = rewards.vajraTalismans + 1)
            }
        }
        return ExperienceGrant(current.copy(level = level, experience = experience), rewards, levelsGained)
    }
}
