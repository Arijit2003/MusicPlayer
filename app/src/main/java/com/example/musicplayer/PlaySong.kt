package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager

import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

import kotlin.collections.ArrayList

class PlaySong : AppCompatActivity(),ServiceConnection{
    companion object {
        var musicList: ArrayList<Song> ?=null
        var songPosition: Int = 0
        //var mediaPlayer:MediaPlayer?=null
        var musicService:MusicService?=null
        var playPause:FloatingActionButton?=null
        @SuppressLint("StaticFieldLeak")
        var  endingTime:TextView?=null
        @SuppressLint("StaticFieldLeak")
        var albumImage:ImageView?=null
        @SuppressLint("StaticFieldLeak")
        var ongoingTime:TextView?=null
        @SuppressLint("StaticFieldLeak")
        var seekbar:SeekBar?=null
        @SuppressLint("StaticFieldLeak")
        var songName:TextView?=null
        var isClicked:Boolean=false
        @SuppressLint("StaticFieldLeak")
        var getContext: Context ?=null


    }

    private var newSongPosition:Int=0
    private var autoPlay:ImageButton?=null
    private lateinit var runnable: Runnable
    private var volumeBtn:ImageButton?=null


    private var share:ImageButton?=null
    private var shuffle:ImageButton?=null
    private var previous:FloatingActionButton?=null
    private var next:FloatingActionButton?=null
    private var minus10Sec:ImageButton?=null
    private var add10Sec:ImageButton?=null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)
        val audioManager=applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        //Starting the service
        val newIntent=Intent(this,MusicService::class.java)
        bindService(newIntent,this, BIND_AUTO_CREATE)
        startService(newIntent)
        initializeAllViews()
        getContext=this@PlaySong

        seekbar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean,
            ) {
                if (fromUser) {
                    musicService!!.mediaPlayer!!.seekTo(progress)
                    if (musicService!!.mediaPlayer!!.currentPosition == musicService!!.mediaPlayer!!.duration) {
                        playPause!!.setImageResource(R.drawable.ic_play)
                    }
                    if(musicService!!.mediaPlayer!!.isPlaying){
                        musicService!!.showNotification(R.drawable.ic_pause,1F)
                    }
                    else if(!musicService!!.mediaPlayer!!.isPlaying){
                        musicService!!.showNotification(R.drawable.ic_play,0F)
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        playPause!!.setOnClickListener(){
            if(musicService!!.mediaPlayer!!.isPlaying){
                musicService!!.showNotification(R.drawable.ic_play,0F)
                playPause!!.setImageResource(R.drawable.ic_play)
                musicService!!.mediaPlayer!!.pause()

            }
            else{
                if(musicService!!.mediaPlayer!!.currentPosition== musicService!!.mediaPlayer!!.duration){
                    musicService!!.mediaPlayer!!.stop()
                    musicService!!.mediaPlayer!!.reset()
                    musicService!!.mediaPlayer!!.setDataSource(musicList!![songPosition].path)
                    playPause!!.setImageResource(R.drawable.ic_pause)
                    musicService!!.showNotification(R.drawable.ic_pause,1F)
                    musicService!!.mediaPlayer!!.prepare()
                    musicService!!.mediaPlayer!!.start()

                }
                else {
                    playPause!!.setImageResource(R.drawable.ic_pause)
                    musicService!!.showNotification(R.drawable.ic_pause,1F)
                    musicService!!.mediaPlayer!!.start()
                    setSeekAndOnGoingText()
                }

            }
        }

        next!!.setOnClickListener(){
            playNextSong()
            musicService!!.showNotification(R.drawable.ic_pause,1F)
        }

        previous!!.setOnClickListener(){
            playPreviousSong()
            musicService!!.showNotification(R.drawable.ic_pause,1F)
        }

        volumeBtn!!.setOnClickListener(){
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI)
        }

        shuffle!!.setOnClickListener(){
            val previousSong:Song= musicList!![songPosition]
            musicList!!.shuffle()

            while(previousSong.path== musicList!![songPosition].path){
                musicList!!.shuffle()
            }

            musicService!!.mediaPlayer!!.stop()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicList!![songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            Glide.with(this).load(musicList!![songPosition].artUri).apply(
                RequestOptions().placeholder(R.drawable.img).centerCrop()).into(albumImage!!)
            playPause!!.setImageResource(R.drawable.ic_pause)
            musicService!!.mediaPlayer!!.start()
            musicService!!.showNotification(R.drawable.ic_pause,1F)
            songName!!.text= musicList!![songPosition].title

            endingTime!!.text= durationFormat(musicList!![songPosition].duration)
            seekbar!!.progress=0
            setSeekAndOnGoingText()
            seekbar!!.max= musicService!!.mediaPlayer!!.duration
        }

        add10Sec!!.setOnClickListener(){
            if((musicService!!.mediaPlayer!!.duration-musicService!!.mediaPlayer!!.currentPosition)<=10) {
                musicService!!.mediaPlayer!!.seekTo(musicService!!.mediaPlayer!!.duration)
                setSeekAndOnGoingText()
            }

            else {
                musicService!!.mediaPlayer!!.seekTo(musicService!!.mediaPlayer!!.currentPosition + 10000)
                setSeekAndOnGoingText()
            }
        }

        minus10Sec!!.setOnClickListener(){
            if(musicService!!.mediaPlayer!!.currentPosition<=10000) {
                musicService!!.mediaPlayer!!.seekTo(0)
                setSeekAndOnGoingText()
            }
            else {
                musicService!!.mediaPlayer!!.seekTo(musicService!!.mediaPlayer!!.currentPosition - 10000)
                setSeekAndOnGoingText()
            }
        }

        share!!.setOnClickListener(){
            val shareIntent=Intent()
            shareIntent.action=Intent.ACTION_SEND
            shareIntent.type="audio/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(musicList!![songPosition].path))
            startActivity(Intent.createChooser(shareIntent, musicList!![songPosition].title))

        }

        autoPlay!!.setOnClickListener(){
            if(isClicked){
                isClicked=false
                autoPlay!!.setBackgroundColor(android.R.attr.selectableItemBackground)
            }
            else{
                isClicked=true
                autoPlay!!.setBackgroundColor(R.color.black)
            }
        }

    }

    private fun playPreviousSong() {
        songPosition--
        if (songPosition < 0) {
            songPosition = musicList!!.size - 1
        }
        musicService!!.mediaPlayer!!.stop()
        musicService!!.mediaPlayer!!.reset()

        musicService!!.mediaPlayer!!.setDataSource(musicList!![songPosition].path)
        musicService!!.mediaPlayer!!.prepare()
        musicService!!.mediaPlayer!!.start()
        songName!!.text = musicList!![songPosition].title
        Glide.with(this).load(musicList!![songPosition].artUri).apply(
            RequestOptions().placeholder(R.drawable.img).centerCrop()).into(albumImage!!)
        playPause!!.setImageResource(R.drawable.ic_pause)
        endingTime!!.text = durationFormat(musicList!![songPosition].duration)
        seekbar!!.progress = 0
        setSeekAndOnGoingText()
        seekbar!!.max = musicService!!.mediaPlayer!!.duration
    }

    private fun playNextSong() {
        songPosition++
        if (songPosition > musicList!!.size - 1) {
            songPosition = 0
        }
        musicService!!.mediaPlayer!!.stop()
        musicService!!.mediaPlayer!!.reset()
        musicService!!.mediaPlayer!!.setDataSource(musicList!![songPosition].path)
        musicService!!.mediaPlayer!!.prepare()
        Glide.with(this).load(musicList!![songPosition].artUri).apply(
            RequestOptions().placeholder(R.drawable.img).centerCrop()).into(albumImage!!)
        playPause!!.setImageResource(R.drawable.ic_pause)
        musicService!!.mediaPlayer!!.start()
        songName!!.text = musicList!![songPosition].title
        seekbar!!.progress = 0
        setSeekAndOnGoingText()
        endingTime!!.text = durationFormat(musicList!![songPosition].duration)
        seekbar!!.max = musicService!!.mediaPlayer!!.duration
    }

    private fun initializeAllViews() {
        endingTime = findViewById(R.id.endingTime)
        musicList = arrayListOf()
        volumeBtn=findViewById(R.id.volumeBtn)
        albumImage = findViewById(R.id.albumImage)
        shuffle = findViewById(R.id.shuffle)
        share=findViewById(R.id.share)
        previous = findViewById(R.id.previousBtn)
        next = findViewById(R.id.nextBtn)
        songName = findViewById(R.id.songName)
        autoPlay = findViewById(R.id.autoPlay)
        songName!!.isSelected = true
        seekbar = findViewById(R.id.seekbar)
        ongoingTime = findViewById(R.id.ongoingTime)
        minus10Sec = findViewById(R.id.minus10Sec)
        add10Sec = findViewById(R.id.add10Sec)
        playPause = findViewById(R.id.playPause)

        // newSongPosition is the previous songPosition
        newSongPosition= songPosition

        when (intent.getStringExtra("class")) {
            "MyAdapter" -> {
                songPosition = intent.getIntExtra("index", 0)
                musicList!!.addAll(MainActivity.songList)
            }

            "MyAdapter2" -> {
                val sID: String = intent.getStringExtra("id")!!
                if (musicList!!.isNotEmpty()) {
                    musicList!!.removeAll(musicList!!.toSet())
                }
                musicList!!.addAll(AllSongsFragment.MyNewSongList)
                for (i in ((0..musicList!!.size)-1)) {
                    if (musicList!![i].id == sID) {
                        songPosition = i
                        break
                    }
                }
            }
        }
    }
    private fun playOngoingSong(){
        playPause!!.setImageResource(R.drawable.ic_pause)

        endingTime!!.text= durationFormat(musicService!!.mediaPlayer!!.duration.toLong())
        Glide.with(this).load(musicList!![songPosition].artUri).apply(
            RequestOptions().placeholder(R.drawable.img).centerCrop()).into(albumImage!!)
        seekbar!!.progress=0
        seekbar!!.max= musicService!!.mediaPlayer!!.duration
        setSeekAndOnGoingText()
        songName!!.text= musicList!![songPosition].title



    }

    private fun createMediaPlayer() {
        if(musicService!!.mediaPlayer!=null){
            musicService!!.mediaPlayer!!.stop()
        }
        musicService!!.mediaPlayer= MediaPlayer()

        musicService!!.mediaPlayer!!.reset()
        musicService!!.mediaPlayer!!.setDataSource(musicList!![songPosition].path)
        musicService!!.mediaPlayer!!.prepare()
        musicService!!.mediaPlayer!!.start()
        songName!!.text = musicList!![songPosition].title
        endingTime!!.text = durationFormat(musicList!![songPosition].duration)
        seekbar!!.max = musicService!!.mediaPlayer!!.duration
        seekbar!!.progress = 0
        setSeekAndOnGoingText()
        Glide.with(this).load(musicList!![songPosition].artUri).apply(
            RequestOptions().placeholder(R.drawable.img).centerCrop()).into(albumImage!!)
    }

    private fun setSeekAndOnGoingText(){
        runnable=Runnable{
            ongoingTime!!.text= durationFormat(musicService!!.mediaPlayer!!.currentPosition.toLong())
            seekbar!!.progress= musicService!!.mediaPlayer!!.currentPosition
            if(seekbar!!.progress==seekbar!!.max){
                playPause!!.setImageResource(R.drawable.ic_play)
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                    musicService!!.showNotification(R.drawable.ic_play,0F)
                }
                if (isClicked){

                    playNewSong()
                }
            }

            Handler(Looper.getMainLooper()).postDelayed(runnable,1000)

        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)


    }

    private fun playNewSong(){

        songPosition++
        if(songPosition> musicList!!.size-1){
            songPosition=0
        }
        musicService!!.mediaPlayer!!.stop()
        musicService!!.mediaPlayer!!.reset()
        musicService!!.mediaPlayer!!.setDataSource(musicList!![songPosition].path)
        musicService!!.mediaPlayer!!.prepare()
        musicService!!.mediaPlayer!!.start()
        playPause!!.setImageResource(R.drawable.ic_pause)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            musicService!!.showNotification(R.drawable.ic_pause,1F)
        }

        Glide.with(this).load(musicList!![songPosition].artUri).apply(RequestOptions().placeholder(R.drawable.img).centerCrop()).into(albumImage!!)
        songName!!.text= musicList!![songPosition].title
        endingTime!!.text= durationFormat(musicList!![songPosition].duration)
        seekbar!!.max= musicService!!.mediaPlayer!!.duration
        seekbar!!.progress=0
        setSeekAndOnGoingText()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val myBinder= service as MusicService.MyBinder
        musicService=myBinder.currentService()

        if(musicService!!.mediaPlayer!=null){
            if(newSongPosition== songPosition && musicService!!.mediaPlayer!!.isPlaying){
                playOngoingSong()
            }
            else if(newSongPosition== songPosition && !(musicService!!.mediaPlayer!!.isPlaying)){
                musicService!!.mediaPlayer!!.start()
                playOngoingSong()
            }
            else{
                createMediaPlayer()
            }
        }
        else{
            createMediaPlayer()
        }

        musicService!!.showNotification(R.drawable.ic_pause,1F)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService=null
    }

}