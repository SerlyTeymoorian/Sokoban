package com.bignerdranch.android.sokoban.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.sokoban.R
import com.bignerdranch.android.sokoban.databinding.FragmentChooseLevelBinding
import com.bignerdranch.android.sokoban.LevelIconsAdapter

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding?= null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseLevelBinding.inflate(layoutInflater, container, false)
        binding.gridView.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    //bind to the adapter passing the number of levels
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.gridView.adapter =
            LevelIconsAdapter(resources.getInteger(R.integer.number_of_levels)){ lev ->
                findNavController().navigate(
                    ChooseLevelFragmentDirections.actionStartLevel(true, lev)
                )
            }

    }
}
