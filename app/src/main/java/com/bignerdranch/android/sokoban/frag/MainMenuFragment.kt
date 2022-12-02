package com.bignerdranch.android.sokoban.frag

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.sokoban.R
import com.bignerdranch.android.sokoban.db.Database
import com.bignerdranch.android.sokoban.databinding.FragmentMainMenuBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenuFragment : Fragment() {
    private var _binding: FragmentMainMenuBinding?= null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private var ruleDialog: Dialog? = null

    //bind with xml file
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = FragmentMainMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            //the new game
            newGameButton.setOnClickListener {
                findNavController().navigate(MainMenuFragmentDirections.actionChooseLevel())
            }
            //set up the info about the game rules
            gameRules.setOnClickListener {
                ruleDialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.help))
                    .setMessage(getString(R.string.help_msg))
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    .create()
                ruleDialog?.show()
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val gameState = Database.gameStateDao.getCurrentGame() ?: return@launch
                withContext(Dispatchers.Main) {
                    continueGameButton.setOnClickListener {
                        findNavController().navigate(
                            MainMenuFragmentDirections.actionContinueLevel(
                                false,
                                 gameState.levelNumber
                            )
                        )
                    }
                    continueGameButton.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        ruleDialog?.dismiss()
    }
}
