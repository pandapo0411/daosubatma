package com.vibecode.daosubatma.core.ads

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vibecode.daosubatma.BuildConfig

private object AdUnitIds {
    private const val productionBanner = "ca-app-pub-4846873668995611/7435186122"
    private const val productionInterstitial = "ca-app-pub-4846873668995611/9064137571"

    // Google-provided test IDs protect the AdMob account during local/debug testing.
    private const val testBanner = "ca-app-pub-3940256099942544/6300978111"
    private const val testInterstitial = "ca-app-pub-3940256099942544/1033173712"

    val banner: String get() = if (BuildConfig.DEBUG) testBanner else productionBanner
    val interstitial: String get() = if (BuildConfig.DEBUG) testInterstitial else productionInterstitial
}

@Composable
fun PersistentBannerAd(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val adView = remember {
        AdView(context).apply {
            adUnitId = AdUnitIds.banner
            setAdSize(AdSize.BANNER)
            loadAd(AdRequest.Builder().build())
        }
    }
    DisposableEffect(adView) {
        onDispose { adView.destroy() }
    }
    AndroidView(
        factory = { adView },
        modifier = modifier,
    )
}

class InterstitialAdController(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    fun preload() {
        if (interstitialAd != null || isLoading) return
        isLoading = true
        InterstitialAd.load(
            context,
            AdUnitIds.interstitial,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    isLoading = false
                }
            },
        )
    }

    fun showAfterResult(activity: Activity, onFinished: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            preload()
            onFinished()
            return
        }
        interstitialAd = null
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            private var finished = false

            override fun onAdDismissedFullScreenContent() = finishOnce()

            override fun onAdFailedToShowFullScreenContent(adError: AdError) = finishOnce()

            private fun finishOnce() {
                if (finished) return
                finished = true
                preload()
                onFinished()
            }
        }
        ad.show(activity)
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}
