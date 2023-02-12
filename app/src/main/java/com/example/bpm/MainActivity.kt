package com.example.bpm

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import kotlin.math.*

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val clientId = "cb9bed781f1b413e8878a3e93fa3da4a"
    private val redirectUri = "http://bpm-app.com/callback/"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    val playlists = JSONObject("""{
        "190.0": "https://open.spotify.com/playlist/37i9dQZF1EIcID9rq1OAoH?si=1a26bce7353b4197",
        "180.0": "https://open.spotify.com/playlist/37i9dQZF1EIerWUrjG2OiJ?si=8232f95c918d4090",
        "170.0": "https://open.spotify.com/playlist/37i9dQZF1EIgfIackHptHl?si=e44d63f43b084e71",
        "160.0": "https://open.spotify.com/playlist/37i9dQZF1EIdYV92VKrjuC?si=67a53f7157a3434f",
        "150.0": "https://open.spotify.com/playlist/37i9dQZF1EIgrZKdA44WQK?si=9fb005947d5e44dd",
        "140.0": "https://open.spotify.com/playlist/37i9dQZF1EIgOKtiospcqN?si=dc13d60f07fa44f8"
    }""")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createPlaylist = findViewById<Button>(R.id.createPlaylist);
        val runningSpeed = findViewById<TextView>(R.id.runningSpeed);
        val runningSpeedDial = findViewById<SeekBar>(R.id.runningSpeedDial)

        createPlaylist.setOnClickListener {
            val mph: Double = runningSpeed.text.toString().toDouble();

            fun mphToBpm(mph: Double): Double {
                return (-0.35174437271856374 * mph * mph +  27.50030269012755 * mph + 38.98561871213088)
            }

            val BPM = Math.round(mphToBpm(mph) / 10.0) * 10.0;

            // take BPM and return "playlist" JSONobject key
            var playlistLink = playlists.get(BPM.toString());

            val i = Intent(Intent.ACTION_VIEW);
            i.data = Uri.parse(playlistLink.toString());
            startActivity(i);


            // nothing below 140
            // nothing above 190

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
                runningSpeed.text = ((progress.toFloat()/10)+4).toString()
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
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }
}