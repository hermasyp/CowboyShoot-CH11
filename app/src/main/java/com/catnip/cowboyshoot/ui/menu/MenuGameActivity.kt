package com.catnip.cowboyshoot.ui.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.catnip.cowboyshoot.R
import com.catnip.cowboyshoot.databinding.ActivityMenuGameBinding
import com.catnip.cowboyshoot.preference.UserPreference

class MenuGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setNameOnTitle()
        setMenuClickListeners()
    }

    private fun setMenuClickListeners() {
        binding.ivMenuVsComputer.setOnClickListener {
            //todo : navigate to game, menu vs computer
        }
        binding.ivMenuVsPlayer.setOnClickListener {
            //todo : navigate to game, menu vs player
        }
    }

    private fun setNameOnTitle() {
        binding.tvTitleMenu.text =
            getString(
                R.string.placeholder_title_menu_game,
                UserPreference(this).name
            )
    }
}