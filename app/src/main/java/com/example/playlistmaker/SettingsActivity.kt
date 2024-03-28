package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //я знаю что свитчер работает некорректно при первом запуске
        val switcherTheme = findViewById<Switch>(R.id.swTheme)
        switcherTheme.isChecked =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            vibrate()
            finish()
        }
    }

}