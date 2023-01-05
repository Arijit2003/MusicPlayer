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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.io.File

open class MainActivity : AppCompatActivity() {
    companion object{
        var songList:ArrayList<Song> = arrayListOf()
    }
//    private var recyclerView:RecyclerView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songList.removeAll(songList.toSet())

        songList.addAll(SplashScreen.MusicList)
        val bottomNavigationView:BottomNavigationView=findViewById(R.id.bottomNavView)
        val toolbar:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar)
        val fragmentManager:FragmentManager=supportFragmentManager
        val allSongs:AllSongsFragment= AllSongsFragment()
        val favourites:FavouritesFragment= FavouritesFragment()
        val playLists:PlayListsFragment= PlayListsFragment()
        setSupportActionBar(toolbar)


//        recyclerView=findViewById(R.id.recyclerView)

//        val myAdapter:MyAdapter= MyAdapter(this,songList)
//        recyclerView!!.adapter=myAdapter
//        recyclerView!!.layoutManager=LinearLayoutManager(this)

        bottomNavigationView.setOnItemSelectedListener { item:MenuItem->Boolean
            when(item.itemId){
                R.id.AllSongs->{
                    Toast.makeText(this,"All Songs Clicked",Toast.LENGTH_SHORT).show()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView,allSongs)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit()
                }
                R.id.playlists->{
                    Toast.makeText(this,"Playlists Clicked",Toast.LENGTH_SHORT).show()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView,playLists)
                        .setReorderingAllowed(false)
                        .addToBackStack("name")
                        .commit()
                }
                R.id.favourites->{
                    Toast.makeText(this,"Favourites Clicked",Toast.LENGTH_SHORT).show()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView,favourites)
                        .setReorderingAllowed(false)
                        .addToBackStack("name")
                        .commit()
                }
            }
            true
        }
    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater:MenuInflater=getMenuInflater()
        menuInflater.inflate(R.menu.all_song_menus,menu)
        // Here we are creating the reference variable for the menu item
        val menuItemS:MenuItem=menu?.findItem(R.id.search) as MenuItem
        val searchView:SearchView=menuItemS.actionView as SearchView
        searchView.onActionViewExpanded()
        searchView.queryHint="Search songs"
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var arrayList:ArrayList<Song> = arrayListOf()
                for(i in songList){
                    if(i.title.lowercase().contains(newText!!.lowercase())){
                        arrayList.add(i)
                    }
                }
                val adapter:MyAdapter2= MyAdapter2(this@MainActivity,arrayList)
                recyclerView!!.adapter=adapter
                recyclerView!!.layoutManager=LinearLayoutManager(this@MainActivity)

                return false
            }

        })
        return true

        //return super.onCreateOptionsMenu(menu)
    }

 */







}