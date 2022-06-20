package com.tomo_app.memoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.tomo_app.memoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedinstancestate: Bundle?) {
        super.onCreate(savedinstancestate)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //admob設定
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

    }
}