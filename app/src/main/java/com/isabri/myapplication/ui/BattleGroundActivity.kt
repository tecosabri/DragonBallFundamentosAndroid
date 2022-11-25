package com.isabri.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.isabri.myapplication.databinding.ActivityBattleGroundBinding
import com.isabri.myapplication.ui.heroesList.HeroesList

class BattleGroundActivity : AppCompatActivity() {

    lateinit var binding: ActivityBattleGroundBinding
    private val viewModel: BattleGroundViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBattleGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            // Set token received from login activity
            intent.getStringExtra("token")?.let {
                viewModel.token = it
            }

            // Navigate to fragment
            Log.d("MyLog", "Navigating to heroes list")
            supportFragmentManager.beginTransaction()
                .add(binding.container.id, HeroesList())
                .commit()
        }
    }
}