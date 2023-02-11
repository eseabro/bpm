package com.example.bpm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

class MainActivity : AppCompatActivity() {

    private val clientId = "cb9bed781f1b413e8878a3e93fa3da4a"
    private val redirectUri = "http://bpm-app.com/callback/"
    private var spotifyAppRemote: SpotifyAppRemote? = null

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
    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    private fun connected() {
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
            it.playerApi.play(playlistURI)
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("MainActivity", track.name + " by " + track.artist.name)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }
}