package com.catnip.cowboyshoot.ui.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.catnip.cowboyshoot.R
import com.catnip.cowboyshoot.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews()
    }
    private fun bindViews(){
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}