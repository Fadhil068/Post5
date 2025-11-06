package com.fadhil_068.post5

import android.net.Uri

data class Post(
    val id: Int,
    var username: String,
    val userImageUrl: Int,
    var postImageUrl: Any,
    var caption: String,
    var likes: Int = 0
)
