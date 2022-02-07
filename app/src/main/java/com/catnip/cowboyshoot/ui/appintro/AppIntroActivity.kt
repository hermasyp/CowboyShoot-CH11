package com.catnip.cowboyshoot.ui.appintro

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.catnip.cowboyshoot.R
import com.catnip.cowboyshoot.ui.appintro.inputname.InputNameFragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment

class AppIntroActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        isSkipButtonEnabled = false
        addSlide(
            AppIntroFragment.createInstance(
                title = getString(R.string.text_title_slide_1).uppercase(),
                description = getString(R.string.text_desc_slide_1).uppercase(),
                imageDrawable = R.drawable.ic_cowboy_left_shoot_true,
                titleTypefaceFontRes = R.font.pixelated_font,
                descriptionTypefaceFontRes = R.font.pixelated_font,
                backgroundDrawable = R.drawable.bg_game,
                descriptionColorRes = R.color.white,
                titleColorRes = R.color.white,
            )
        )

        addSlide(
            AppIntroFragment.createInstance(
                title = getString(R.string.text_title_slide_2).uppercase(),
                description = getString(R.string.text_desc_slide_2).uppercase(),
                imageDrawable = R.drawable.ic_cowboy_right_shoot_true,
                titleTypefaceFontRes = R.font.pixelated_font,
                descriptionTypefaceFontRes = R.font.pixelated_font,
                backgroundDrawable = R.drawable.bg_game,
                descriptionColorRes = R.color.white,
                titleColorRes = R.color.white,
            )
        )

        addSlide(InputNameFragment())
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        if(currentFragment is InputNameFragment){
            currentFragment.navigateToMenuGame()
        }
    }

}