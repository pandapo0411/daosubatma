package com.vibecode.daosubatma.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.RawRes
import com.vibecode.daosubatma.R

enum class MusicTrack(@RawRes val resourceId: Int) {
    Menu(R.raw.bgm_splash_loop),
    Hunt(R.raw.bgm_hunt_loop),
}

enum class SoundEffect(@RawRes val resourceId: Int) {
    ButtonTalisman(R.raw.sfx_button_talisman),
    ScanPulse(R.raw.sfx_scan_pulse),
    GhostAppear(R.raw.sfx_ghost_appear),
    GhostFloat(R.raw.sfx_ghost_float),
    ChooseTalisman(R.raw.sfx_choose_talisman),
    DrawStart(R.raw.sfx_draw_start),
    SealLoop(R.raw.sfx_seal_loop),
    SealSuccess(R.raw.sfx_seal_success),
    SealFail(R.raw.sfx_seal_fail),
    Timeout(R.raw.sfx_timeout),
    CardReveal(R.raw.sfx_card_reveal),
    Alchemy(R.raw.sfx_alchemy),
    AdTransition(R.raw.sfx_ad_transition),
    GiftOpen(R.raw.sfx_gift_open),
}

class GameAudio(private val context: Context) {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()
    private val effects = SoundEffect.entries.associateWith { soundPool.load(context, it.resourceId, 1) }
    private var musicPlayer: MediaPlayer? = null
    private var currentTrack: MusicTrack? = null
    private var sealLoopStream = 0

    fun play(effect: SoundEffect, volume: Float = 1f) {
        effects[effect]?.let { soundPool.play(it, volume, volume, 1, 0, 1f) }
    }

    fun startSealLoop() {
        if (sealLoopStream != 0) return
        effects[SoundEffect.SealLoop]?.let {
            sealLoopStream = soundPool.play(it, .42f, .42f, 1, -1, 1f)
        }
    }

    fun stopSealLoop() {
        if (sealLoopStream != 0) soundPool.stop(sealLoopStream)
        sealLoopStream = 0
    }

    fun playMusic(track: MusicTrack) {
        if (currentTrack == track && musicPlayer?.isPlaying == true) return
        musicPlayer?.release()
        currentTrack = track
        musicPlayer = MediaPlayer.create(context, track.resourceId)?.apply {
            isLooping = true
            setVolume(.45f, .45f)
            start()
        }
    }

    fun release() {
        stopSealLoop()
        musicPlayer?.release()
        musicPlayer = null
        soundPool.release()
    }
}
