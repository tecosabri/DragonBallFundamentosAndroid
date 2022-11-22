package com.isabri.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.isabri.myapplication.databinding.ActivityBattleGroundBinding

class BattleGroundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBattleGroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}