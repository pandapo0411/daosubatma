package com.vibecode.daosubatma.core.data

import android.content.Context
import com.vibecode.daosubatma.core.model.GhostDefinition
import com.vibecode.daosubatma.core.model.GhostRarity
import com.vibecode.daosubatma.core.model.PlayerInventory
import com.vibecode.daosubatma.core.model.TaoistProgress
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class CapturedGhost(
    val id: String,
    val speciesId: String,
    val rarity: GhostRarity,
    val accuracy: Float,
    val capturedAtMillis: Long,
    val isTutorial: Boolean = false,
)

/** Each successful capture is an immutable card with a unique ID, including duplicates. */
class GhostStorage(context: Context) {
    private val preferences = context.getSharedPreferences("ghost_compendium", Context.MODE_PRIVATE)

    fun recordGhost(ghost: GhostDefinition, accuracy: Float) {
        val ghosts = loadCapturedGhosts().toMutableList()
        ghosts += CapturedGhost(
            id = UUID.randomUUID().toString(),
            speciesId = ghost.speciesId,
            rarity = ghost.rarity,
            accuracy = accuracy,
            capturedAtMillis = System.currentTimeMillis(),
        )
        save(ghosts)
    }

    fun loadCapturedGhosts(): List<CapturedGhost> = runCatching {
        val cards = JSONArray(preferences.getString(KEY_CARDS, "[]") ?: "[]")
        buildList {
            for (index in 0 until cards.length()) {
                val card = cards.getJSONObject(index)
                add(
                    CapturedGhost(
                        id = card.getString("id"),
                        speciesId = card.getString("speciesId"),
                        rarity = GhostRarity.valueOf(card.getString("rarity")),
                        accuracy = card.getDouble("accuracy").toFloat(),
                        capturedAtMillis = card.getLong("capturedAtMillis"),
                        isTutorial = card.optBoolean("isTutorial", false),
                    ),
                )
            }
        }.sortedByDescending { it.capturedAtMillis }
    }.getOrDefault(emptyList())

    fun removeGhost(cardId: String) {
        save(loadCapturedGhosts().filterNot { it.id == cardId })
    }

    /** Removes legacy tutorial cards that may have been written by an older MVP build. */
    fun removeLegacyTutorialGhosts() {
        val ghosts = loadCapturedGhosts()
        if (ghosts.any { it.isTutorial }) {
            save(ghosts.filterNot { it.isTutorial })
        }
    }

    fun loadInventory(): PlayerInventory = PlayerInventory(
        redTalismans = preferences.getInt(KEY_RED_TALISMANS, 3),
        purpleTalismans = preferences.getInt(KEY_PURPLE_TALISMANS, 2),
        vajraTalismans = preferences.getInt(KEY_VAJRA_TALISMANS, 1),
        spiritEssence = preferences.getInt(KEY_SPIRIT_ESSENCE, 0),
        vajraFragments = preferences.getInt(KEY_VAJRA_FRAGMENTS, 0),
    )

    fun saveInventory(inventory: PlayerInventory) {
        preferences.edit()
            .putInt(KEY_RED_TALISMANS, inventory.redTalismans)
            .putInt(KEY_PURPLE_TALISMANS, inventory.purpleTalismans)
            .putInt(KEY_VAJRA_TALISMANS, inventory.vajraTalismans)
            .putInt(KEY_SPIRIT_ESSENCE, inventory.spiritEssence)
            .putInt(KEY_VAJRA_FRAGMENTS, inventory.vajraFragments)
            .apply()
    }

    fun loadTaoistProgress(): TaoistProgress = TaoistProgress(
        level = preferences.getInt(KEY_TAOIST_LEVEL, 1),
        experience = preferences.getInt(KEY_TAOIST_EXPERIENCE, 0),
        pendingGiftBoxes = preferences.getInt(KEY_PENDING_GIFT_BOXES, 0),
        unlockedSkills = preferences.getStringSet(KEY_UNLOCKED_SKILLS, emptySet()) ?: emptySet(),
    )

    fun saveTaoistProgress(progress: TaoistProgress) {
        preferences.edit()
            .putInt(KEY_TAOIST_LEVEL, progress.level)
            .putInt(KEY_TAOIST_EXPERIENCE, progress.experience)
            .putInt(KEY_PENDING_GIFT_BOXES, progress.pendingGiftBoxes)
            .putStringSet(KEY_UNLOCKED_SKILLS, progress.unlockedSkills)
            .apply()
    }

    private fun save(ghosts: List<CapturedGhost>) {
        val cards = JSONArray()
        ghosts.forEach { ghost ->
            cards.put(
                JSONObject()
                    .put("id", ghost.id)
                    .put("speciesId", ghost.speciesId)
                    .put("rarity", ghost.rarity.name)
                    .put("accuracy", ghost.accuracy.toDouble())
                    .put("capturedAtMillis", ghost.capturedAtMillis)
                    .put("isTutorial", ghost.isTutorial),
            )
        }
        preferences.edit().putString(KEY_CARDS, cards.toString()).apply()
    }

    private companion object {
        const val KEY_CARDS = "captured_ghost_cards"
        const val KEY_RED_TALISMANS = "red_talismans"
        const val KEY_PURPLE_TALISMANS = "purple_talismans"
        const val KEY_VAJRA_TALISMANS = "vajra_talismans"
        const val KEY_SPIRIT_ESSENCE = "spirit_essence"
        const val KEY_VAJRA_FRAGMENTS = "vajra_fragments"
        const val KEY_TAOIST_LEVEL = "taoist_level"
        const val KEY_TAOIST_EXPERIENCE = "taoist_experience"
        const val KEY_PENDING_GIFT_BOXES = "pending_gift_boxes"
        const val KEY_UNLOCKED_SKILLS = "unlocked_skills"
    }
}
