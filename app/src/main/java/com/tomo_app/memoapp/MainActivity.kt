package com.tomo_app.memoapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.tomo_app.memoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var adTappedCount: Int = 0

    override fun onCreate(savedinstancestate: Bundle?) {
        super.onCreate(savedinstancestate)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //admob設定
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.adView.adListener = object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                adTappedCount += 1
                when {
                    adTappedCount > 2 -> binding.adView.visibility = View.GONE
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            binding.adView.visibility = View.VISIBLE
            adTappedCount = 0
        }, 500L)
    }

}