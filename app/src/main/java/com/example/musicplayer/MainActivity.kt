package com.example.musicplayer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.core.widget.NestedScrollView

import androidx.viewpager.widget.ViewPager

import com.google.android.material.tabs.TabLayout


open class MainActivity : AppCompatActivity() {
    companion object{
        var songList:ArrayList<Song> = arrayListOf()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nestedScrollView:NestedScrollView=findViewById(R.id.nestedScrollView)
        val tabLayout:TabLayout=findViewById(R.id.tabLayout)
        val viewPager:ViewPager=findViewById(R.id.viewPager)
        nestedScrollView.isFillViewport=true
        val myAdapter= ViewPagerMessengerAdapter(supportFragmentManager)
        viewPager.adapter=myAdapter
        tabLayout.setupWithViewPager(viewPager)
        songList.removeAll(songList.toSet())

        songList.addAll(SplashScreen.MusicList)

        val toolbar:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)



    }








}