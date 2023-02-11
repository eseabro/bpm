package com.example.bpm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createPlaylist = findViewById<Button>(R.id.createPlaylist);
        val runningSpeed = findViewById<TextView>(R.id.runningSpeed);
        val runningSpeedDial = findViewById<SeekBar>(R.id.runningSpeedDial)

        createPlaylist.setOnClickListener {
            // TO DO:
            // - Get miles per hour
            // - Get the genre
            // - Get tempo using miles per hour
            // - Get reccommended songs based on genre and tempo
            // - Create a playlist
            // - Add recommended songs to playlist
            // - Sends use to playlist in spotify app
        }

        runningSpeedDial.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // TODO Auto-generated method stub
                runningSpeed.text = (progress.toFloat()/5).toString()
            }
        })
    }
}