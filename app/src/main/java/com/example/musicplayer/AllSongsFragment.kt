package com.example.musicplayer

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AllSongsFragment : Fragment() {

    private var recyclerView: RecyclerView?=null
    companion object{
        var MyNewSongList:ArrayList<Song> = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        MyNewSongList.removeAll(MyNewSongList.toSet())
        MyNewSongList.addAll(SplashScreen.MusicList)
        val view:View= inflater.inflate(R.layout.fragment_all_songs, container, false)
        recyclerView=view.findViewById(R.id.recyclerView)
        val myAdapter:MyAdapter= MyAdapter(requireContext(), MyNewSongList)
        recyclerView!!.adapter=myAdapter
        recyclerView!!.layoutManager= LinearLayoutManager(context)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_song_menus,menu)
        val menuItemS:MenuItem=menu.findItem(R.id.search) as MenuItem
        val searchView: SearchView =menuItemS.actionView as SearchView
        searchView.onActionViewExpanded()
        searchView.queryHint="Search songs"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var arrayList:ArrayList<Song> = arrayListOf()
                for(i in MyNewSongList){
                    if(i.title.lowercase().contains(newText!!.lowercase())){
                        arrayList.add(i)
                    }
                }
                val adapter:MyAdapter2= MyAdapter2(requireContext(),arrayList)
                recyclerView!!.adapter=adapter
                recyclerView!!.layoutManager=LinearLayoutManager(context)

                return false
            }

        })
        //return true
        super.onCreateOptionsMenu(menu, inflater)
    }

}