package com.example.android_krud_app.dto

import android.app.AlertDialog
import android.content.Context
import com.example.android_krud_app.R
import kotlinx.android.synthetic.main.activity_create_post.*

enum class AttachmentType {
    IMAGE, AUDIO, VIDEO
}

data class AttachmentModel(val id: String, val url: String, val type: AttachmentType)

enum class PostType {
    POST, REPOST
}

data class PostModel(
  val id: Long,
  val source: PostModel? = null,
  val ownerId: Long,
  val ownerName: String,
  val created: Int,
  var content: String? = null,
  var likes: Int = 0,
  var likedByMe: Boolean = false,
  var reposts: Int = 0,
  var repostedByMe: Boolean = false,
  val link: String? = null,
  val type: PostType = PostType.POST,
  val attachment: AttachmentModel?
) {
    var likeActionPerforming = false
    var repostActionPerforming = false

    fun updateLikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        likedByMe = updatedModel.likedByMe
    }

    fun updatePost(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes
        likedByMe = updatedModel.likedByMe
        content = updatedModel.content
        reposts = updatedModel.reposts
        repostedByMe = updatedModel.repostedByMe
    }
}
