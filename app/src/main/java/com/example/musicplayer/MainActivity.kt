package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import java.io.File

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
        val myAdapter:ViewPagerMessengerAdapter= ViewPagerMessengerAdapter(supportFragmentManager)
        viewPager.adapter=myAdapter
        tabLayout.setupWithViewPager(viewPager)
        songList.removeAll(songList.toSet())

        songList.addAll(SplashScreen.MusicList)

        val toolbar:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)



    }








}