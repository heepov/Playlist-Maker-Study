package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var vibrator: Vibrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

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

        findViewById<ImageView>(R.id.iwBack).setOnClickListener {
            vibrate()
            finish()
        }
    }

    private fun vibrate() {
        if (vibrator.hasVibrator()) {
            // set vibration on 50 milliseconds with default vibrate
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    override fun onDestroy() {
        // destroy vibrator
        vibrator.cancel()
        super.onDestroy()
    }
}