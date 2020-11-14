package com.esei.grvidal.nighttime.data

import androidx.lifecycle.ViewModel

class User(val name: String) : ViewModel() {
    val id = 0//user ID

    var nextDate: MyDate? = null

    var city: City = City(0,"Ourense")

    fun getChats() : List<ChatData>{
        return listOf(
            ChatData(0,userName = "Nuria Sotelo Domarco", "Dicen que hoy abre el Lokal"),
            ChatData(1,userName = "Maria Jose", "Ya he hecho lentejas"),
            ChatData(2,userName = "Pablo Pablito", "Hoy he clavado un clavo"),
            ChatData(3,userName = "Elma RockStar", "Concierto en aquel sitio!!, cuento contigo" +
                    " espero que no te pongas enfermo ocmo la ultima vez en aquel lugar, te acuerdas?\n" +
                    "Fue increible pero ojala no repetirlo nunca entiendes?"),
            ChatData(0,userName = "Nuria Sotelo Domarco", "Dicen que hoy abre el Lokal"),
            ChatData(1,userName = "Maria Jose", "Ya he hecho lentejas"),
            ChatData(2,userName = "Pablo Pablito", "Hoy he clavado un clavo"),
            ChatData(3,userName = "Elma RockStar", "Concierto en aquel sitio!!, cuento contigo" +
                    " espero que no te pongas enfermo ocmo la ultima vez en aquel lugar, te acuerdas?\n" +
                    "Fue increible pero ojala no repetirlo nunca entiendes?"),
            ChatData(0,userName = "Nuria Sotelo Domarco", "Dicen que hoy abre el Lokal"),
            ChatData(1,userName = "Maria Jose", "Ya he hecho lentejas"),
            ChatData(2,userName = "Pablo Pablito", "Hoy he clavado un clavo"),
            ChatData(3,userName = "Elma RockStar", "Concierto en aquel sitio!!, cuento contigo" +
                    " espero que no te pongas enfermo ocmo la ultima vez en aquel lugar, te acuerdas?\n" +
                    "Fue increible pero ojala no repetirlo nunca entiendes?"),
            ChatData(0,userName = "Nuria Sotelo Domarco", "Dicen que hoy abre el Lokal"),
            ChatData(1,userName = "Maria Jose", "Ya he hecho lentejas"),
            ChatData(2,userName = "Pablo Pablito", "Hoy he clavado un clavo"),
            ChatData(3,userName = "Elma RockStar", "Concierto en aquel sitio!!, cuento contigo" +
                    " espero que no te pongas enfermo ocmo la ultima vez en aquel lugar, te acuerdas?\n" +
                    "Fue increible pero ojala no repetirlo nunca entiendes?"),
        )
    }
    fun getChatConversation(idChat : Int): FullChat{
        return allChats[idChat]
    }

    private var allChats : List<FullChat> = listOf(
        FullChat(0, "Nuria Sotelo Domarco", listOf(
            Message(0,"hey que tal?"),
            Message(1,"Bien, llegando a casa y tu?"),
            Message(0,"Acabando de trabjar"),
            Message(0,"Te apetece hacer algo hoy?"),
            Message(1,"Dicen que hoy abre el Lokal")
        )),
        FullChat(1, "Maria Jose", listOf(
            Message(3,"Hola hijo"),
            Message(3,"Estas bien?"),
            Message(0,"Con algo de hambre"),
            Message(3,"Yo me encargo"),
            Message(3,"Ya he hecho lentejas")
        )),
                FullChat(2, "Pablo Pablito", listOf(
            Message(3,"Hola hijo"),
            Message(3,"Estas bien?"),
            Message(0,"Con algo de hambre"),
            Message(3,"Yo me encargo"),
            Message(3,"Hoy he clavado un clavo")
        )),
        FullChat(3, "Elma RockStar", listOf(
            Message(3,"Hola hijo"),
            Message(3,"Estas bien?"),
            Message(0,"Con algo de hambre"),
            Message(3,"Yo me encargo"),
            Message(3,"Concierto en aquel sitio!!, cuento contigo" +
                     " espero que no te pongas enfermo ocmo la ultima vez en aquel lugar, te acuerdas?\n" +
                    "Fue increible pero ojala no repetirlo nunca entiendes?")
        ))
    )


}

data class ChatData(val id : Int, val userName: String, val lastMessage : String)
data class FullChat(val id : Int, val otherUserName : String, val conversation : List<Message> )

data class Message(val idUser: Int, val messageText :String)

data class DatePeople(val amigos: Int, val total: Int)
object CalendarDao {

    fun getPeopleOnDate(cityId: Int, date: MyDate): DatePeople {

        return when {
            date.day % 5 == 0 -> {
                DatePeople(35, 144)
            }
            date.day % 2 == 0 -> {
                DatePeople(14, 20)
            }
            else -> DatePeople(0, 5)
        }
    }

    fun getFriends(cityId: Int, date: MyDate): List<User?> {
        //todo this is hardcoded
        return if (date.day == 1) {
            listOf()
        } else listOf(
            User(name = "Nuria"),
            User(name = "Miguel"),
            User(name = "Maria"),
            User(name = "Marcos"),
            User(name = "Laura"),
            User(name = "Sara"),
            User(name = "Julio"),
            User(name = "Juan"),
            User(name = "Pedro"),
            User(name = "Salva"),
            User(name = "Gabriel"),
            User(name = "Jose"),
            User(name = "Emma"),
            User(name = "Santi"),
            User(name = "Filo"),
            User(name = "Nuria"),
            User(name = "Miguel"),
            User(name = "Maria"),
            User(name = "Marcos"),
            User(name = "Laura"),
            User(name = "Sara"),
            User(name = "Julio"),
            User(name = "Juan"),
            User(name = "Pedro"),
            User(name = "Salva"),
            User(name = "Gabriel"),
            User(name = "Jose"),
            User(name = "Emma"),
            User(name = "Santi"),
            User(name = "Filo")
        )
    }
}