package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MyAdapter2(val context:Context,private val songList:ArrayList<Song>): RecyclerView.Adapter<MyAdapter2.MyViewHolder2>() {

    //val newMusicList:ArrayList<Song> = songList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder2 {
        val layoutInflater:LayoutInflater=LayoutInflater.from(context)
        val view:View=layoutInflater.inflate(R.layout.list_item,parent,false)
        return MyViewHolder2(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        //holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,songList[position].image))
        //holder.imageView.setImageURI(songList[position].artUri.toUri())
        holder.songNTextView.text=songList[position].title
        holder.singerTextView.text=songList[position].album
        holder.durationTv.text= durationFormat(songList[position].duration)
        Glide.with(context).load(songList[position].artUri).apply(
            RequestOptions().placeholder(R.drawable.background2).centerCrop()).into(holder.imageView)
        holder.linearLayout.setOnClickListener(){
            val intent:Intent= Intent(context,PlaySong::class.java)
            intent.putExtra("id",songList[position].id)
            intent.putExtra("class","MyAdapter2")

            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    class MyViewHolder2(itemView:View):RecyclerView.ViewHolder(itemView){
        val imageView:ImageView=itemView.findViewById(R.id.image)
        val songNTextView:TextView=itemView.findViewById(R.id.songName)
        val singerTextView:TextView=itemView.findViewById(R.id.singer)
        val durationTv:TextView=itemView.findViewById(R.id.duration)
        val linearLayout:LinearLayout=itemView.findViewById(R.id.songItem)
    }

}
