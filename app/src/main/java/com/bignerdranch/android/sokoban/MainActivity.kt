package com.bignerdranch.android.sokoban

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import java.io.IOException

class MainActivity : FragmentActivity() {
    //set up the background sound
    private val audioUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3"
    var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //play the background sound
        playVideo()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
    }

    private fun playVideo(){
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try{
            mediaPlayer!!.setDataSource(audioUrl)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()

        } catch (e: IOException){
            e.printStackTrace()
        }
    }
}