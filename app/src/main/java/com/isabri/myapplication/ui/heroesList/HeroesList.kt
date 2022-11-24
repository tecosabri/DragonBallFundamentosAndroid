package com.isabri.myapplication.ui.heroesList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.isabri.myapplication.databinding.FragmentHeroesListListBinding
import com.isabri.myapplication.domain.models.Hero
import com.isabri.myapplication.ui.BattleGroundActivity
import com.isabri.myapplication.ui.BattleGroundViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration


interface HeroesListItemInterface {
    fun onClick(hero: Hero)
}

/**
 * A fragment representing a list of Items.
 */
class HeroesList : Fragment(), HeroesListItemInterface {

    private val viewModel: BattleGroundViewModel by activityViewModels()
    private lateinit var binding: FragmentHeroesListListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getHeroes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Navigation", "On create view")
        binding = FragmentHeroesListListBinding.inflate(inflater)
        with(binding.root) {
            observeFetchingState(this)
            activity?.let { viewModel.checkWinner(it.baseContext) }
            this.layoutManager = LinearLayoutManager(context)
            return this
        }
    }

    private fun observeFetchingState(view: RecyclerView) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is BattleGroundViewModel.HeroesListState.Success -> {
                    // Within the main thread as the heroes list gets updated asynchronously
                    lifecycleScope.launch(Dispatchers.Main) {
                        view.adapter = MyItemRecyclerViewAdapter(viewModel.heroesList, viewModel = viewModel)
                    }
                }
                is BattleGroundViewModel.HeroesListState.Failure -> {
                    view.adapter = MyItemRecyclerViewAdapter(listOf(Hero("", "", "Error fetching heroes")))
                }
                is BattleGroundViewModel.HeroesListState.Loading -> {
                    // A list of one hero provides only one cell to show the loading status
                    view.adapter = MyItemRecyclerViewAdapter(listOf(Hero("", "", "")), true)
                }
            }
        }
    }

    override fun onClick(hero: Hero) {
        viewModel.setFightingHeroes(hero)
    }
}