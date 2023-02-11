package com.example.bpm

import android.app.DownloadManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import org.json.JSONArray


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createPlaylist = findViewById<Button>(R.id.createPlaylist);
        val runningSpeed = findViewById<TextView>(R.id.runningSpeed);
        val runningSpeedDial = findViewById<SeekBar>(R.id.runningSpeedDial)
        val genreSelect = findViewById<Spinner>(R.id.genreSelect)


        fun spotifyConnect(){
            private val clientId = "4073955ddd87480fa0ab26ec705705e9"
            private val redirectUri = "https://com.spotify.android.bpm/callback"
            private var spotifyAppRemote: SpotifyAppRemote? = null
            private val client = OkHttpClient()
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
                fun get_recs(url: String): JSONArray{
                    spotifyAppRemote?.let {
                        val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
                        it.playerApi.play(playlistURI)
                        it.playerApi.subscribeToPlayerState().setEventCallback {
                            val track: Track = it.track
                            Log.d("MainActivity", track.name + " by " + track.artist.name)
                        }
                    }

                }

            })

        }

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.spinner_options,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            genreSelect.adapter = adapter
//        }


        createPlaylist.setOnClickListener {
            val mph: Double = runningSpeed.text.toString().toDouble();
            val genre =  genreSelect.getSelectedItem().toString();
            fun mphToBpm(mph: Double): Double {
                return (1.456876456876459 * mph * mph) - (42.027972027972076 * mph) + 438.99766899766934
            }
            val BPM = mphToBpm(mph)
//            val recommendedSongs = spotify.Get

            // TO DO:
            // - Get reccommended songs based on genre and tempo
            // - Create a playlist
            // - Add recommended songs to playlist
            // - Sends use to playlist in spotify app
        }
    }
}