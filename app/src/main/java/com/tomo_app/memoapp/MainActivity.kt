package com.tomo_app.memoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.tomo_app.memoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //adMob設定
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editMemo = pref.getString("MEMO", "")
        binding.textInputEdit.setText(editMemo)
    }

    //menuレイアウトを設定
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_list, menu)
        return true
    }

    //menuのアイテム選択時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                createDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //アラートダイアログ生成
    private fun createDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("WARNING!")
            .setMessage("Delete all text?")
            .setPositiveButton("DONE") { _, _ ->
                actionDelete()
            }
            .setNegativeButton("CANCEL") { _, _ ->
            }
            .create()
        builder.show()
    }

    //テキストリセット
    private fun actionDelete() {
        binding.textInputEdit.setText("")
        Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
    }

    //データ変更をコミットするタイミングはonPauseで行う
    override fun onPause() {
        super.onPause()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            putString("MEMO", binding.textInputEdit.text.toString())
        }
    }

}