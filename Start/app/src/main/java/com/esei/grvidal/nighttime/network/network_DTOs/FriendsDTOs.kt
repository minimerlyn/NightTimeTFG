package com.esei.grvidal.nighttime.network.network_DTOs

import androidx.compose.ui.graphics.ImageAsset
import com.esei.grvidal.nighttime.network.AnswerOptions

data class UserFriendViewAPI(
    var friendshipId: Long,
    var userId: Long,
    var userNickname: String,
    var state: String,
    var image: Boolean
)

data class UserFriendView(
    var friendshipId: Long,
    var userId: Long,
    var userNickname: String,
    var state: String,
    val hasImage: Boolean,
    var image: ImageAsset? = null
)

data class FriendshipUpdateDTO(
    val id: Long,
    val answer: AnswerOptions
)