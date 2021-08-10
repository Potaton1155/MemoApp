package com.tomo_app.memoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_action, menu)
        return true
    }

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
                pref.edit {
                    
                }
                Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
