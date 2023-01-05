package com.example.musicplayer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


class MusicService: Service() {
    private var myBinder=MyBinder()
    var mediaPlayer:MediaPlayer?=null

    private lateinit var mediaSession: MediaSessionCompat
    override fun onBind(intent: Intent?): IBinder {
        mediaSession=MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }
    inner class MyBinder:Binder(){
        fun currentService():MusicService{
            return this@MusicService
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(icon:Int,playbackSpeed:Float){
        val prevIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent=PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_IMMUTABLE)
        val playIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_IMMUTABLE)


        val nextIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_IMMUTABLE)

        val exitIntent=Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_IMMUTABLE)


        val notification=NotificationCompat.Builder(baseContext,ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlaySong.musicList!![PlaySong.songPosition].title)
            .setContentText(PlaySong.musicList!![PlaySong.songPosition].artist)
            .setSmallIcon(R.drawable.bakground)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.bakground))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_backward,"previous",prevPendingIntent)
            .addAction(icon,"play",playPendingIntent)
            .addAction(R.drawable.ic_forward,"next",nextPendingIntent)
            .addAction(R.drawable.ic_exit,"exit",exitPendingIntent)
            .build()

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
            mediaSession.setMetadata(MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,mediaPlayer!!.duration.toLong())
                .build())
            mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING,mediaPlayer!!.currentPosition.toLong(),playbackSpeed)
                //.setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build())

        }

        startForeground(15,notification)


    }
}