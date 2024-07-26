package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.utils.services.vibrate
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    private lateinit var switcherTheme : SwitchMaterial
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        settingsInteractor = Creator.provideSettingsInteractor(this)

        switcherTheme = findViewById<SwitchMaterial>(R.id.swTheme)

        switcherTheme.isChecked = settingsInteractor.checkDarkMode()

        switcherTheme.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            settingsInteractor.setDarkMode(checked)
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