package com.example.bpm
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.beust.klaxon.Klaxon
import android.os.Bundle
import kotlinx.coroutines.*
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.internal.bind.TypeAdapters.URL
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import org.json.JSONArray
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val clientId = "4073955ddd87480fa0ab26ec705705e9"
    private val redirectUri = "https://com.spotify.android.BPMile/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val client: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createPlaylist = findViewById<Button>(R.id.createPlaylist);
        val runningSpeed = findViewById<TextView>(R.id.runningSpeed);
        val runningSpeedDial = findViewById<SeekBar>(R.id.runningSpeedDial)
        val genreSelect = findViewById<Spinner>(R.id.genreSelect)

        createPlaylist.setOnClickListener {
            val mph: Double = runningSpeed.text.toString().toDouble();
            val genre =  genreSelect.getSelectedItem().toString();
            fun mphToBpm(mph: Double): Double {
                return (1.456876456876459 * mph * mph) - (42.027972027972076 * mph) + 438.99766899766934
            }
            val BPM = mphToBpm(mph)

        }

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
                getPlaylist()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
            }
        })
    }
    private fun getPlaylist() {

        fun getRequest(sUrl: String): String? {
            var result: String? = null
            try {
                val url = URL(sUrl)
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                result = response.body?.string()
            } catch (err: Error) {
                print("Error when executing get request: " + err.localizedMessage)
            }
            return result
        }

    }
    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

    }
}