package com.tomo_app.memoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.tomo_app.memoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editMemo = pref.getString("MEMO", "")
        binding.textInputEdit.setText(editMemo)
    }

    //オプションmenuを設定
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_list, menu)
        return true
    }

    //menuのアイテム選択時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        when (item.itemId) {
            R.id.action_save -> {
                pref.edit {
                    putString("MEMO", binding.textInputEdit.text.toString())
                }
                Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
            }

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
                binding.textInputEdit.setText("")
                Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("CANCEL") { _, _ ->
            }
            .create()
        builder.show()
    }
}