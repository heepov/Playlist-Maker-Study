package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val preferences = getSharedPreferences(SETTINGS_PREFERENCE, MODE_PRIVATE)
        val switcherTheme = findViewById<SwitchMaterial>(R.id.swTheme)
        switcherTheme.isChecked = preferences.getBoolean(THEME_SWITCHER_KEY, false)

        switcherTheme.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            preferences.edit().putBoolean(THEME_SWITCHER_KEY, checked).apply()
        }

        findViewById<ImageView>(R.id.iwShareApp).setOnClickListener {
            Intent(Intent.ACTION_SEND).run {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.shareLink))
                startActivity(this)
            }
            vibrate()
        }

        findViewById<ImageView>(R.id.iwWriteSupport).setOnClickListener {
            Intent(Intent.ACTION_SENDTO).run {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmailAddress)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.supportMailSubject)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.supportMailText)
                )
                startActivity(this)
            }
            vibrate()
        }
        findViewById<ImageView>(R.id.iwTerms).setOnClickListener {
            Intent(Intent.ACTION_VIEW).run {
                setData(Uri.parse(getString(R.string.termsLink)))
                startActivity(this)
            }
            vibrate()
        }

        findViewById<Toolbar>(R.id.toolBar).setNavigationOnClickListener {
            vibrate()
            finish()
        }
    }

}