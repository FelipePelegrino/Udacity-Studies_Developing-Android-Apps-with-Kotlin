package com.gmail.devpelegrino.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()
    }

    private fun setListeners() {
        val clickableViews: List<View> = listOf(
            findViewById(R.id.text_box_one),
            findViewById(R.id.text_box_two),
            findViewById(R.id.text_box_three),
            findViewById(R.id.text_box_four),
            findViewById(R.id.text_box_five),
            findViewById(R.id.constraint_layout),
            findViewById(R.id.button_green),
            findViewById(R.id.button_red),
            findViewById(R.id.button_yellow)
        )

        for (item in clickableViews) {
            item.setOnClickListener { makeColored(it) }
        }
    }

    private fun makeColored(view: View) {
        when (view.id) {
            R.id.text_box_one -> view.setBackgroundColor(Color.DKGRAY)
            R.id.text_box_two -> view.setBackgroundColor(Color.RED)
            R.id.text_box_three -> view.setBackgroundResource(android.R.color.holo_green_light)
            R.id.text_box_four -> view.setBackgroundResource(android.R.color.holo_green_dark)
            R.id.text_box_five -> view.setBackgroundResource(android.R.color.holo_green_light)
            R.id.button_red -> view.setBackgroundResource(R.color.color_red)
            R.id.button_green -> view.setBackgroundResource(R.color.color_green)
            R.id.button_yellow -> view.setBackgroundResource(R.color.color_yellow)
            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}