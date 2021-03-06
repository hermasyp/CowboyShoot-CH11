package com.catnip.cowboyshoot.ui.game

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.catnip.cowboyshoot.R
import com.catnip.cowboyshoot.databinding.ActivityGameBinding
import com.catnip.cowboyshoot.enum.CharacterPosition
import com.catnip.cowboyshoot.enum.CharacterShootState
import com.catnip.cowboyshoot.enum.CharacterSide
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private val TAG = GameActivity::class.simpleName

    private lateinit var binding: ActivityGameBinding

    private var playerPosition: Int = CharacterPosition.MIDDLE.ordinal
    private var computerPosition: Int = CharacterPosition.MIDDLE.ordinal
    private var isGameFinished: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews()
        setInitialState()
        setClickListeners()

    }

    private fun bindViews() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    private fun setInitialState() {
        //set position player for idle
        setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.IDLE)

        //set position computer for idle
        setCharacterMovement(computerPosition, CharacterSide.COMPUTER, CharacterShootState.IDLE)

        //set text for button fire
        binding.tvStatusGame.text = getString(R.string.text_button_fire)
    }

    private fun setClickListeners() {
        binding.ivArrowUp.setOnClickListener {
            if (!isGameFinished && playerPosition > CharacterPosition.TOP.ordinal) {
                playerPosition--
                setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.IDLE)
            }
        }
        binding.ivArrowDown.setOnClickListener {
            if (!isGameFinished && playerPosition < CharacterPosition.BOTTOM.ordinal) {
                playerPosition++
                setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.IDLE)
            }
        }

        binding.tvStatusGame.setOnClickListener {
            if (isGameFinished) {
                resetGame()
            } else {
                startGame()
            }
        }
    }

    private fun startGame() {
        //set random pos for computer
        computerPosition = Random.nextInt(0, 3)
        //set character movement to shoot
        setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.SHOOT)
        setCharacterMovement(computerPosition, CharacterSide.COMPUTER, CharacterShootState.SHOOT)
        // proceed winner
        if (playerPosition == computerPosition) {
            //PLAYER LOSE
            Log.d(TAG, "startGame: Player Lose")
            setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.DEAD)
            setCharacterMovement(
                computerPosition,
                CharacterSide.COMPUTER,
                CharacterShootState.SHOOT
            )
            binding.tvWinnerGame.text = getString(R.string.text_player_lose)
        } else {
            //PLAYER WIN
            Log.d(TAG, "startGame: Player Wins")
            setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.SHOOT)
            setCharacterMovement(computerPosition, CharacterSide.COMPUTER, CharacterShootState.DEAD)
            binding.tvWinnerGame.text = getString(R.string.text_player_win)
        }
        isGameFinished = true
        binding.tvStatusGame.text = getString(R.string.text_restart)
    }

    private fun resetGame() {
        Log.d(TAG, "resetGame: $isGameFinished")
        isGameFinished = false
        playerPosition = CharacterPosition.MIDDLE.ordinal
        computerPosition = CharacterPosition.MIDDLE.ordinal
        binding.tvWinnerGame.text = ""
        setInitialState()
        Log.d(TAG, "resetGame: $isGameFinished")

    }


    private fun setCharacterMovement(
        position: Int,
        characterSide: CharacterSide,
        characterShootState: CharacterShootState
    ) {
        val ivCharTop: ImageView?
        val ivCharMiddle: ImageView?
        val ivCharBottom: ImageView?
        var imgResCharacter: Drawable?

        if (characterSide == CharacterSide.PLAYER) {
            ivCharTop = binding.ivPlayerLeft0
            ivCharMiddle = binding.ivPlayerLeft1
            ivCharBottom = binding.ivPlayerLeft2
            imgResCharacter = ContextCompat.getDrawable(
                this,
                when (characterShootState) {
                    CharacterShootState.IDLE -> R.drawable.ic_cowboy_left_shoot_false
                    CharacterShootState.SHOOT -> R.drawable.ic_cowboy_left_shoot_true
                    CharacterShootState.DEAD -> R.drawable.ic_cowboy_left_dead
                }
            )
        } else {
            ivCharTop = binding.ivPlayerRight0
            ivCharMiddle = binding.ivPlayerRight1
            ivCharBottom = binding.ivPlayerRight2
            imgResCharacter = ContextCompat.getDrawable(
                this,
                when (characterShootState) {
                    CharacterShootState.IDLE -> R.drawable.ic_cowboy_right_shoot_false
                    CharacterShootState.SHOOT -> R.drawable.ic_cowboy_right_shoot_true
                    CharacterShootState.DEAD -> R.drawable.ic_cowboy_right_dead
                }
            )
        }

        when (CharacterPosition.values()[position]) {
            CharacterPosition.TOP -> {
                ivCharTop.visibility = View.VISIBLE
                ivCharMiddle.visibility = View.INVISIBLE
                ivCharBottom.visibility = View.INVISIBLE
                ivCharTop.setImageDrawable(imgResCharacter)
            }
            CharacterPosition.MIDDLE -> {
                ivCharTop.visibility = View.INVISIBLE
                ivCharMiddle.visibility = View.VISIBLE
                ivCharBottom.visibility = View.INVISIBLE
                ivCharMiddle.setImageDrawable(imgResCharacter)

            }
            CharacterPosition.BOTTOM -> {
                ivCharTop.visibility = View.INVISIBLE
                ivCharMiddle.visibility = View.INVISIBLE
                ivCharBottom.visibility = View.VISIBLE
                ivCharBottom.setImageDrawable(imgResCharacter)
            }
        }
    }

}