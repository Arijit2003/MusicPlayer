package com.example.musicplayer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerMessengerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        if(position==0){
            return AllSongsFragment()
        }
        else if(position==1){
            return FavouritesFragment()
        }
        else{
            return PlayListsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence{
        return when(position){
            0-> "All Songs"
            1-> "Favourites"
            else-> "Playlists"
        }
    }
}