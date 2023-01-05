package com.example.musicplayer

import android.animation.TimeInterpolator
import android.net.Uri
import androidx.core.graphics.convertTo
import java.sql.Time
import java.util.concurrent.TimeUnit

data class Song(val title:String,val artist:String,val id:String,val album:String,
                val path:String,
                val duration:Long,
                val artUri:String
                )
fun durationFormat(duration: Long):String{
    val minutes=TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
    val seconds=TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS)-TimeUnit.SECONDS.convert(minutes,TimeUnit.MINUTES)
    return String.format("%02d:%02d",minutes,seconds)



}