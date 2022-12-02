package com.bignerdranch.android.sokoban.frag

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.sokoban.R
import com.bignerdranch.android.sokoban.db.Database
import com.bignerdranch.android.sokoban.databinding.FragmentGameBinding
import com.bignerdranch.android.sokoban.GameModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import androidx.lifecycle.Observer
import com.bignerdranch.android.sokoban.GameModelViewModelFactory

class GameFragment : Fragment()
{
    private val args: GameFragmentArgs by navArgs()
    private var timer: Timer? = null

   private var _binding: FragmentGameBinding?= null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private lateinit var gameModel : GameModel

    //create the binding with the xml file
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            val  mod : GameModel by viewModels(){
                GameModelViewModelFactory(container.context)
            }
            gameModel = mod
        }
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        setupGameModel()

        return binding.root
    }

    //if undo or reset buttons are pressed
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            resetButton.setOnClickListener {
                gameModel.onResetLevel()
            }

            undoButton.setOnClickListener {
                gameModel.onUndoMove()
            }
        }
    }

    private fun resetTimer() {
        timer?.let {
            it.cancel()
            timer = null
        }
    }

    private fun setupGameModel() {
        //creating a new game
        if (args.newGame) {
            gameModel.startLevel(args.levelNumber)
        }
        //continuing a game
        else {
            lifecycleScope.launchWhenStarted {
                when (val gameState = Database.gameStateDao.getCurrentGame()) {
                    //get the level number
                    null -> gameModel.startLevel(args.levelNumber)
                    else -> {
                        //start the game with the previous saved level number and time
                        gameModel.startLevel(gameState.levelNumber)
                        gameModel.setTime(gameState.time)
                        gameModel.makeMoves(gameState.movesList)
                    }
                }
            }
        }
        //printing the status of the game
        //Level # and the time spent on that level
        binding.apply {
            gameModel.statsLive.observe(
                viewLifecycleOwner,
                Observer { highScore ->
                    if (highScore == null) return@Observer
                    val levelNumber = gameModel.levelNumber()
                    val minutes = highScore.time / 60
                    val seconds = highScore.time % 60
                    statusTextView.text = String.format(
                        getString(R.string.level_status),
                        levelNumber, minutes, seconds
                    )
                }
            )
        }

        gameModel.movesLive.observe(
            viewLifecycleOwner,
            Observer {
                val gameState = gameModel.gameState()
                lifecycleScope.launch(Dispatchers.IO) {
                    //we save the current state of the game in the database
                    Database.gameStateDao.setCurrentGame(gameState)
                }
            }
        )
        gameModel.wonLive.observe(
            viewLifecycleOwner,
            Observer {
                resetTimer()
            }
        )
        gameModel.levelNumberLive.observe(
            viewLifecycleOwner,
            Observer {
                timer = Timer()
                timer?.schedule(
                    object : TimerTask() {
                        override fun run() {
                            activity?.runOnUiThread {
                                gameModel.onSecondElapsed()
                            }
                        }
                    },
                    0, 1000
                )
            }
        )
    }
    //reset timer when destroyed
    override fun onDestroy() {
        resetTimer()
        super.onDestroy()
    }

}