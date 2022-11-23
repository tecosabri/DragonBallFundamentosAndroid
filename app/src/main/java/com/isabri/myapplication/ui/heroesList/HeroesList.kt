package com.isabri.myapplication.ui.heroesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.isabri.myapplication.R
import com.isabri.myapplication.databinding.FragmentHeroesListListBinding
import com.isabri.myapplication.domain.models.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class HeroesList : Fragment() {

    private val viewModel: HeroesListViewModel by viewModels()
    private lateinit var binding: FragmentHeroesListListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getHeroes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesListListBinding.inflate(inflater)
        with(binding.root) {
            observeFetchingState(this)
            this.layoutManager = LinearLayoutManager(context)
            return this
        }
    }

    private fun observeFetchingState(view: RecyclerView) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is HeroesListViewModel.HeroesListState.Success -> {
                    // Within main thread as the heroes list gets updated asynchronously
                    lifecycleScope.launch(Dispatchers.Main) {
                        view.adapter = MyItemRecyclerViewAdapter(viewModel.heroesList)
                    }
                }
                is HeroesListViewModel.HeroesListState.Failure -> {
                    view.adapter = MyItemRecyclerViewAdapter(listOf(Hero("", "", "Error fetching heroes")))
                }
                is HeroesListViewModel.HeroesListState.Loading -> {
                    // A list of one hero provides only one cell to show the loading status
                    view.adapter = MyItemRecyclerViewAdapter(listOf(Hero("", "", "")), true)
                }
            }
        }
    }
}