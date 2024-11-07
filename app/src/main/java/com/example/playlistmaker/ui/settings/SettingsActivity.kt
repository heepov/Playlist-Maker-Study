package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.presentation.settings.view_model.SettingsViewModel
import com.example.playlistmaker.utils.services.vibrate
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(application)
        ).get(SettingsViewModel::class.java)

        setupObservers()

        binding.iwShareApp.setOnClickListener {
            Intent(Intent.ACTION_SEND).run {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.shareLink))
                startActivity(this)
            }
            vibrate()
        }

        binding.iwWriteSupport.setOnClickListener {
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
        binding.iwTerms.setOnClickListener {
            Intent(Intent.ACTION_VIEW).run {
                setData(Uri.parse(getString(R.string.termsLink)))
                startActivity(this)
            }
            vibrate()
        }

        binding.toolBar.setNavigationOnClickListener {
            vibrate()
            finish()
        }
    }

    fun setupObservers() {
        viewModel.themeStateLiveData.observe(this) { state ->
            binding.swTheme.isChecked = state
        }
        binding.swTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.setThemeState(checked)
        }
    }

}