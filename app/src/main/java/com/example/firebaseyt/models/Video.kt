package com.example.firebaseyt.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Video(var id: String? = "", var name: String? = "", var link: String? = "", var rank: String? = "", var reason: String? = "") {
    override fun toString(): String {
        return "$name rank $rank"
    }
}