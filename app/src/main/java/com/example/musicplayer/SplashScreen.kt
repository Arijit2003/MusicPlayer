package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import java.io.File


open class SplashScreen : AppCompatActivity() {
    companion object {
        var MusicList: ArrayList<Song> = arrayListOf()
    }

    private val storageResultLauncher:ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted->
            if(isGranted){
                MusicList.removeAll(MusicList.toSet())
                if(MusicList.isEmpty()) {
                    MusicList.addAll(getAllAudio())
                }
                val intent=Intent(this,MainActivity::class.java)
                val handler=Handler()
                handler.postDelayed(Runnable{
                    startActivity(intent)
                    finish()
                },3000)
            }
            else{
                showAlertDialog("Can't access music files", "You have denied storage access")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        launchPermission()


    }
    private fun launchPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            showAlertDialog("Can't access music files", "You have denied storage access")
        } else {
            storageResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
    private fun showAlertDialog(title: String, message: String) {
        val build: AlertDialog.Builder= AlertDialog.Builder(this)
        build.setTitle(title)
        build.setMessage(message)
        build.setPositiveButton("Cancel"){dialog,_->
            dialog.dismiss()
        }
        build.create().show()
    }
    @SuppressLint("Range")
    fun getAllAudio():ArrayList<Song> {
        var musicList:ArrayList<Song> = arrayListOf()
        val selection= MediaStore.Audio.Media.IS_MUSIC + " !=0"
        val projection= arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID)

        val cursor=this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,
            null,null)


        if(cursor!=null){

            if(cursor.moveToFirst()) {


                do {
                    val titleC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val artistC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val albumC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val pathC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC=
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri: Uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC= Uri.withAppendedPath(uri,albumIdC).toString()
                    val song = Song(
                        title = titleC,
                        artist = artistC,
                        id = idC,
                        album = albumC,
                        path = pathC,
                        duration = durationC,
                        artUri = artUriC)
                    val file = File(song.path)
                    if (file.exists()) {
                        musicList.add(song)
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }

        }

        return musicList

    }

}