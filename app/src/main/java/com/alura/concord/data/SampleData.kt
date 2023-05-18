package com.alura.concord.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.alura.concord.database.entities.Author
import com.alura.concord.database.entities.MessageEntity
import com.alura.concord.util.getRandomDate

val messageEntityListSamples = mutableListOf(
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = "Olá",
        author = Author.USER,
        date = getRandomDate(),
    ),
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = LoremIpsum(2).values.first(),
        author = Author.OTHER,
        date = getRandomDate()
    ),
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = LoremIpsum(13).values.first(),
        author = Author.USER,
        date = getRandomDate(),
    ),
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = LoremIpsum(14).values.last(),
        author = Author.OTHER
    ),
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = LoremIpsum(8).values.first(),
        author = Author.USER,
        date = getRandomDate(),
        mediaLink = "/data/user/0/com.alura.concord/app_temImages/94f077ed-a81f-4a74-ac5b-6e44130e80b1"
    ),
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = LoremIpsum(13).values.first(),
        author = Author.USER,
        date = getRandomDate(),
        mediaLink = "/storage/self/primary/Android/data/com.alura.concord/Emoji 6 Jelly Bean.png"
    ),
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = LoremIpsum(1).values.first(),
        author = Author.USER,
        date = getRandomDate(),
        mediaLink = "/data/data/com.alura.concord/files/aumenta o som.png"
    ),
    MessageEntity(
        id = 3L,
        chatId = 3L,
        content = LoremIpsum(7).values.first(),
        author = Author.OTHER
    ),
)

val chatListSample = mutableListOf(
    Chat(
        id = 1L,
        owner = "João",
        profilePicOwner = "https://picsum.photos/id/1015/200/200",
        lastMessage = "Olá, tudo bem?",
        dateLastMessage = "09:00"
    ),
    Chat(
        id = 2L,
        owner = "Maria",
        profilePicOwner = "https://picsum.photos/id/1020/200/200",
        lastMessage = "Sim, e com você?",
        dateLastMessage = "10:15"
    ),
    Chat(
        id = 3L,
        owner = "Grupo de Estudos",
        profilePicOwner = "https://picsum.photos/id/1030/200/200",
        lastMessage = "A reunião será na sexta-feira",
        dateLastMessage = "11:30"
    ),
    Chat(
        id = 4L,
        owner = "Pedro",
        profilePicOwner = "https://picsum.photos/id/1040/200/200",
        lastMessage = "O que você está fazendo?",
        dateLastMessage = "12:45"
    ),
    Chat(
        id = 5L,
        owner = "Grupo de Família",
        profilePicOwner = "https://picsum.photos/id/1050/200/200",
        lastMessage = "Feliz Aniversário, tio!",
        dateLastMessage = "13:00"
    ),
    Chat(
        id = 6L,
        owner = "Ana",
        profilePicOwner = "https://picsum.photos/id/1060/200/200",
        lastMessage = "Estou no caminho",
        dateLastMessage = "14:30"
    ),
    Chat(
        id = 7L,
        owner = "Lucas",
        profilePicOwner = "https://picsum.photos/id/1070/200/200",
        lastMessage = "Vamos ao cinema hoje à noite?",
        dateLastMessage = "15:00"
    ),
    Chat(
        id = 8L,
        owner = "Grupo de Trabalho",
        profilePicOwner = "https://picsum.photos/id/1080/200/200",
        lastMessage = "Precisamos terminar o relatório",
        dateLastMessage = "16:00"
    ),
    Chat(
        id = 9L,
        owner = "Fernanda",
        profilePicOwner = "https://picsum.photos/id/1090/200/200",
        lastMessage = "Obrigado pelo presente!",
        dateLastMessage = "17:30"
    ),
    Chat(
        id = 10L,
        owner = "Grupo de Amigos",
        profilePicOwner = "https://picsum.photos/id/1100/200/200",
        lastMessage = "Vamos marcar um encontro",
        dateLastMessage = "18:45"
    )
)
