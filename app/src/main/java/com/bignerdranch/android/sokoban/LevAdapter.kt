package com.bignerdranch.android.sokoban

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.sokoban.db.Database
import com.bignerdranch.android.sokoban.databinding.ListItemLevelBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//create a level holder
class LevelHolder(val binding: ListItemLevelBinding)
    :RecyclerView.ViewHolder(binding.root)
{
    fun bind(level: Int, onLevelClicked: (lev: Int) -> Unit){
        //set the text of the button to indicate the level
        binding.levelButton.setText(String.format(binding.levelButton.context.getString(R.string.level), level))

        binding.root.setOnClickListener{
            onLevelClicked(level)
        }
    }
}

//create Level holders
class LevelIconsAdapter(private val size: Int, private val onLevelClicked: (lev: Int) -> Unit) : RecyclerView.Adapter<LevelHolder>() {

    //bind it to list_item_level xml file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemLevelBinding.inflate(inflater, parent, false)
        return LevelHolder(binding)
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onBindViewHolder(holder: LevelHolder, position: Int) {
        val levelNumber = position + 1
        holder.apply {
            holder.bind(levelNumber, onLevelClicked)
        }
    }

 /*   override fun onViewAttachedToWindow(holder: LevelHolder) {
        super.onViewAttachedToWindow(holder)

        val button = holder.getButton()

        button.findFragment<Fragment>().lifecycleScope.launch(Dispatchers.IO) {
            val solvedLevels = Database.highScoreDao.solvedLevels()
            val levelNumber = holder.adapterPosition + 1
            if (levelNumber in solvedLevels) {
                withContext(Dispatchers.Main) {
                }
            }
        }
    }*/

    //get the size
    override fun getItemCount() = size
}