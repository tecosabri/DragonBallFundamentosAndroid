package com.isabri.myapplication.ui.heroesList

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.isabri.myapplication.R
import com.isabri.myapplication.databinding.FragmentHeroesListItemBinding
import com.isabri.myapplication.domain.models.Hero
import com.isabri.myapplication.ui.battle.Battle


class MyItemRecyclerViewAdapter(private val values: List<Hero>) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentHeroesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: FragmentHeroesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = values[position]
            val idView: TextView = binding.itemNumber
            val contentView: TextView = binding.content
            idView.text = item.id.first().toString()
            contentView.text = item.name
            // Navigate to battle when click
            binding.root.setOnClickListener {
                Log.d(null, "Navigating to battle fragment")
                val activity = binding.root.context as AppCompatActivity
                activity.supportFragmentManager
                    .beginTransaction()
                    .addToBackStack("list")
                    .replace(R.id.container, Battle())
                    .commit()
            }
        }
    }

}