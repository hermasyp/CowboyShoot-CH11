package com.catnip.cowboyshoot.ui.game

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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

    private var gamePlayMode: Int = GAMEPLAY_MODE_VS_COMPUTER
    private var playTurn : CharacterSide = CharacterSide.PLAYER

    companion object {
        private const val EXTRAS_GAME_MODE = "EXTRAS_GAME_MODE"
        const val GAMEPLAY_MODE_VS_COMPUTER = 0
        const val GAMEPLAY_MODE_VS_PLAYER = 1

        fun startActivity(context: Context, gameplayMode: Int) {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra(EXTRAS_GAME_MODE, gameplayMode)
            context.startActivity(intent)
        }
    }

    private fun getIntentData() {
        gamePlayMode = intent.extras?.getInt(EXTRAS_GAME_MODE, 0) ?: 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews()
        getIntentData()
        setInitialState()
        setClickListeners()

    }

    private fun showUIPlayer(characterSide: CharacterSide,isVisible : Boolean){
        if(characterSide == CharacterSide.PLAYER){
            binding.llPlayerLeft.isVisible = isVisible
        }else{
            binding.llPlayerRight.isVisible = isVisible
        }
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
        setCharacterMovement(computerPosition, CharacterSide.ENEMY, CharacterShootState.IDLE)

        //checking game mode when init state
        if(gamePlayMode == GAMEPLAY_MODE_VS_PLAYER){
            Toast.makeText(this, getString(R.string.text_toast_player_1_turn), Toast.LENGTH_SHORT).show()
            showUIPlayer(CharacterSide.PLAYER,true)
            showUIPlayer(CharacterSide.ENEMY,false)
            //show lock Player 1
            binding.tvStatusGame.text = getString(R.string.text_lock_player_1)
        }else{
            //set text for button fire
            binding.tvStatusGame.text = getString(R.string.text_button_fire)
        }

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
                //starting the game
                if(gamePlayMode == GAMEPLAY_MODE_VS_PLAYER){
                    //vs player
                    //first condition when player lock the position
                    //after that, we change the turn to Enemy
                    if(playTurn == CharacterSide.PLAYER){
                        //change the turn
                        playTurn = CharacterSide.ENEMY
                        //hide who should be visible
                        showUIPlayer(CharacterSide.PLAYER,false)
                        showUIPlayer(CharacterSide.ENEMY,true)
                        //set text to fire
                        binding.tvStatusGame.text = getString(R.string.text_button_fire)
                    }else{
                        //if both player already lock their position, then we can start the game
                        startGame()
                    }
                }else{
                    //vs computer, we can just start the game
                    startGame()
                }
            }
        }
    }

    private fun startGame() {
        //set random pos for computer
        computerPosition = Random.nextInt(0, 3)
        //set character movement to shoot
        setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.SHOOT)
        setCharacterMovement(computerPosition, CharacterSide.ENEMY, CharacterShootState.SHOOT)
        // proceed winner
        if (playerPosition == computerPosition) {
            //PLAYER LOSE
            Log.d(TAG, "startGame: Player Lose")
            setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.DEAD)
            setCharacterMovement(
                computerPosition,
                CharacterSide.ENEMY,
                CharacterShootState.SHOOT
            )
            binding.tvWinnerGame.text = getString(R.string.text_player_lose)
        } else {
            //PLAYER WIN
            Log.d(TAG, "startGame: Player Wins")
            setCharacterMovement(playerPosition, CharacterSide.PLAYER, CharacterShootState.SHOOT)
            setCharacterMovement(computerPosition, CharacterSide.ENEMY, CharacterShootState.DEAD)
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