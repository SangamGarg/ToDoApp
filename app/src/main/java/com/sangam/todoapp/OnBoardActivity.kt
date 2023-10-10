package com.sangam.todoapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class OnBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        supportActionBar?.hide()

    }
    private fun onBoardFinished(): Boolean {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

}