package com.esei.grvidal.nighttime.data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateListOf


data class ChatData(val id : Int, val userName: String, val lastMessage : String)
data class FullChat(val id : Int, val otherUserName : String, val initialConversation : List<Message> ){
    private val _messages: MutableList<Message> =
        mutableStateListOf(*initialConversation.toTypedArray())
    val messages: List<Message> = _messages

    fun addMessage(msg: Message) {
        _messages.add(msg)
    }
}

@Immutable
data class Message(val idUser: Int, val messageText :String, val timestamp: String, val image: Int? = null)