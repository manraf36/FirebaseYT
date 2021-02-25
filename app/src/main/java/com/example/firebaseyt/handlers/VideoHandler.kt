package com.example.firebaseyt.handlers

import com.example.firebaseyt.models.Video
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VideoHandler {

    var database: FirebaseDatabase
    var videoRef: DatabaseReference

    init{
        database = FirebaseDatabase.getInstance()
        videoRef = database.getReference("videos")
    }
    fun create(video: Video): Boolean {
        val id = videoRef.push().key
        video.id = id

        videoRef.child(id!!).setValue(video)
        return true
    }
    fun update(video: Video): Boolean {
        videoRef.child(video.id!!).setValue(video)
        return true
    }
    fun delete(video: Video): Boolean {
        video.id?.let { videoRef.child(it).removeValue() }
        return true
    }
}