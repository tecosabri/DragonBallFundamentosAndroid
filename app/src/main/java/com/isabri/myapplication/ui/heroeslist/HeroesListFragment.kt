package com.isabri.myapplication.ui.heroeslist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.isabri.myapplication.BlankFragment
import com.isabri.myapplication.R
import com.isabri.myapplication.databinding.FragmentHeroesListBinding

class HeroesListFragment : Fragment() {

    companion object {
        fun newInstance() = HeroesListFragment()
    }

    val viewModel: HeroesListViewModel by viewModels()
    private lateinit var binding: FragmentHeroesListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            binding.button.setOnClickListener {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(binding.heroesList.id, BlankFragment())
                transaction.commit()
            }
        }

    }
}