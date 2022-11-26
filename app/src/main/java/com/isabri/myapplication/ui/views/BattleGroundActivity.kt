package com.isabri.myapplication.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.isabri.myapplication.databinding.ActivityBattleGroundBinding
import com.isabri.myapplication.ui.viewModels.BattleGroundViewModel
import com.isabri.myapplication.ui.views.heroesList.HeroesList

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