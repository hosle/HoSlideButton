package com.hosle.widget.slidebutton

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slide_button.setText("Slide to right >>")
        slide_button.setTextColor(Color.WHITE)
        slide_button.setTextSize(18f)

        slide_button.setOnClickListener {
            Toast.makeText(this@MainActivity,"On Click",Toast.LENGTH_SHORT).show()
            slide_button.slideToStartPos()
        }
    }
}
