package com.example.firebaseyt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.firebaseyt.handlers.VideoHandler
import com.example.firebaseyt.models.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var nameEditText: EditText
    lateinit var linkEditText: EditText
    lateinit var rankEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var addEditButton: Button
    lateinit var videoHandler: VideoHandler
    lateinit var videos: ArrayList<Video>
    lateinit var videoListView: ListView
    lateinit var videoGettingEditted: Video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        linkEditText = findViewById(R.id.linkEditText)
        rankEditText = findViewById(R.id.rankEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        addEditButton = findViewById(R.id.addEditButton)
        videoHandler = VideoHandler()
        videos = ArrayList()
        videoListView = findViewById(R.id.videoListView)

        addEditButton.setOnClickListener{
            val name = nameEditText.text.toString()
            val link = linkEditText.text.toString()
            val rank = rankEditText.text.toString()
            val reason = reasonEditText.text.toString()
            if(addEditButton.text.toString()=="Add"){
                val video = Video(name = name, link = link, rank = rank, reason = reason)
                if(videoHandler.create(video)){
                    Toast.makeText(applicationContext, "Video added", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
            else if (addEditButton.text.toString() == "Update") {
                val video = Video(
                    id = videoGettingEditted.id,
                    name = name,
                    link = link,
                    rank = rank,
                    reason = reason
                )
                if (videoHandler.update(video)) {
                    Toast.makeText(applicationContext, "Video updated", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
        }
        registerForContextMenu(videoListView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.video_options, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when(item.itemId){
            R.id.edit_video -> {
                videoGettingEditted = videos[info.position]
                nameEditText.setText(videoGettingEditted.name)
                linkEditText.setText(videoGettingEditted.link)
                rankEditText.setText(videoGettingEditted.rank)
                reasonEditText.setText(videoGettingEditted.reason)
                addEditButton.setText("Update")
                true
            }
            R.id.delete_video -> {

                videoHandler.delete(videos[info.position])
                Toast.makeText(applicationContext, "Video deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    override fun onStart() {
        super.onStart()
        videoHandler.videoRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                videos.clear()
                snapshot.children.forEach{
                        it -> val video = it.getValue(Video::class.java)
                        videos.add(video!!)
                }
                val adapter = ArrayAdapter<Video>(applicationContext, android.R.layout.simple_list_item_1, videos)
                videoListView.adapter = adapter

            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
    }
    fun clearFields(){
        nameEditText.text.clear()
        linkEditText.text.clear()
        rankEditText.text.clear()
        reasonEditText.text.clear()
        addEditButton.setText("Add")
    }
}