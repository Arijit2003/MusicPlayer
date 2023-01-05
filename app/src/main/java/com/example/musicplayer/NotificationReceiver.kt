package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReceiver:BroadcastReceiver() {
    lateinit var newRunnable:Runnable
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS-> {
                playPreviousSong()
                Toast.makeText(context,"Previous clicked",Toast.LENGTH_SHORT).show()

            }
            ApplicationClass.PLAY-> {
                if(PlaySong.musicService!!.mediaPlayer!!.isPlaying){
                    pauseSong()
                }
                else{
                    playSong()
                }
                Toast.makeText(context,"Play clicked",Toast.LENGTH_SHORT).show()
            }

            ApplicationClass.NEXT-> {
                playNextSong()
                Toast.makeText(context,"Next clicked",Toast.LENGTH_SHORT).show()
            }

            ApplicationClass.EXIT-> {
                PlaySong.musicService!!.stopForeground(true)
                PlaySong.musicService=null
                exitProcess(1)

            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun playSong(){
        PlaySong.musicService!!.mediaPlayer!!.start()
        PlaySong.musicService!!.showNotification(R.drawable.ic_pause,1F)
        PlaySong.playPause!!.setImageResource(R.drawable.ic_pause)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun pauseSong(){
        PlaySong.musicService!!.mediaPlayer!!.pause()
        PlaySong.musicService!!.showNotification(R.drawable.ic_play,0F)
        PlaySong.playPause!!.setImageResource(R.drawable.ic_play)

    }

    private fun playNextSong(){
        PlaySong.songPosition++
        if(PlaySong.songPosition>PlaySong.musicList!!.size-1){
            PlaySong.songPosition=0
        }

        PlaySong.musicService!!.mediaPlayer!!.stop()
        PlaySong.musicService!!.mediaPlayer!!.reset()
        PlaySong.musicService!!.mediaPlayer=null
        PlaySong.musicService!!.mediaPlayer= MediaPlayer()
        PlaySong.musicService!!.mediaPlayer!!.setDataSource(PlaySong.musicList!![PlaySong.songPosition].path)

        PlaySong.musicService!!.mediaPlayer!!.prepare()
        PlaySong.musicService!!.mediaPlayer!!.start()
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        PlaySong.musicService!!.showNotification(R.drawable.ic_pause,1F)
        PlaySong.playPause!!.setImageResource(R.drawable.ic_pause)
        PlaySong.endingTime!!.text= durationFormat(PlaySong.musicService!!.mediaPlayer!!.duration.toLong())
        PlaySong.songName!!.text=PlaySong.musicList!![PlaySong.songPosition].title
        setSeekAndOnGoingText()
        Glide.with(PlaySong.getContext!!).load(PlaySong.musicList!![PlaySong.songPosition].artUri).apply(
            RequestOptions().placeholder(R.drawable.img).centerCrop()).into(PlaySong.albumImage!!)

    }
    private fun playNewSong(){
        PlaySong.songPosition++
        if(PlaySong.songPosition > PlaySong.musicList!!.size-1){
            PlaySong.songPosition =0
        }
        PlaySong.musicService!!.mediaPlayer!!.stop()
        PlaySong.musicService!!.mediaPlayer!!.reset()
        PlaySong.musicService!!.mediaPlayer!!.setDataSource(PlaySong.musicList!![PlaySong.songPosition].path)
        PlaySong.musicService!!.mediaPlayer!!.prepare()
        PlaySong.musicService!!.mediaPlayer!!.start()
        PlaySong.playPause!!.setImageResource(R.drawable.ic_pause)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            PlaySong.musicService!!.showNotification(R.drawable.ic_pause,1F)
        }

        Glide.with(PlaySong.getContext!!).load(PlaySong.musicList!![PlaySong.songPosition].artUri).apply(
            RequestOptions().placeholder(R.drawable.img).centerCrop()).into(PlaySong.albumImage!!)
        PlaySong.songName!!.text= PlaySong.musicList!![PlaySong.songPosition].title
        PlaySong.endingTime!!.text= durationFormat(PlaySong.musicList!![PlaySong.songPosition].duration)
        PlaySong.seekbar!!.max= PlaySong.musicService!!.mediaPlayer!!.duration
        PlaySong.seekbar!!.progress=0
        setSeekAndOnGoingText()

    }
    private fun setSeekAndOnGoingText(){
        newRunnable=Runnable{
            PlaySong.ongoingTime!!.text= durationFormat(PlaySong.musicService!!.mediaPlayer!!.currentPosition.toLong())
            PlaySong.seekbar!!.progress= PlaySong.musicService!!.mediaPlayer!!.currentPosition
            if(PlaySong.seekbar!!.progress== PlaySong.seekbar!!.max){
                PlaySong.playPause!!.setImageResource(R.drawable.ic_play)
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                    PlaySong.musicService!!.showNotification(R.drawable.ic_play,0F)
                }
                if (PlaySong.isClicked){
                    playNewSong()
                }
            }

            Handler(Looper.getMainLooper()).postDelayed(newRunnable,1000)

        }
        Handler(Looper.getMainLooper()).postDelayed(newRunnable,0)


    }

    private fun playPreviousSong(){
        PlaySong.songPosition--
        if(PlaySong.songPosition<0){
            PlaySong.songPosition=PlaySong.musicList!!.size-1
        }
        PlaySong.musicService!!.mediaPlayer!!.stop()
        PlaySong.musicService!!.mediaPlayer!!.reset()
        PlaySong.musicService!!.mediaPlayer=null
        PlaySong.musicService!!.mediaPlayer= MediaPlayer()
        PlaySong.musicService!!.mediaPlayer!!.setDataSource(PlaySong.musicList!![PlaySong.songPosition].path)
        PlaySong.musicService!!.mediaPlayer!!.prepare()
        PlaySong.musicService!!.mediaPlayer!!.start()
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            PlaySong.musicService!!.showNotification(R.drawable.ic_pause,1F)
        PlaySong.playPause!!.setImageResource(R.drawable.ic_pause)
        PlaySong.endingTime!!.text= durationFormat(PlaySong.musicService!!.mediaPlayer!!.duration.toLong())
        PlaySong.songName!!.text=PlaySong.musicList!![PlaySong.songPosition].title
        setSeekAndOnGoingText()
        Glide.with(PlaySong.getContext!!).load(PlaySong.musicList!![PlaySong.songPosition].artUri).apply(
            RequestOptions().placeholder(R.drawable.img).centerCrop()).into(PlaySong.albumImage!!)

    }



}
