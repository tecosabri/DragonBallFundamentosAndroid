package com.isabri.myapplication.ui.battle

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isabri.myapplication.R

class Battle : Fragment() {

    companion object {
        fun newInstance() = Battle()
    }

    private lateinit var viewModel: BattleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_battle, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BattleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}