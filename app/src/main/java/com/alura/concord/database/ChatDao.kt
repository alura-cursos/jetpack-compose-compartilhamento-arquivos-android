package com.alura.concord.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.alura.concord.data.Chat
import com.alura.concord.data.ChatWithLastMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(chat: Chat)

    @Query("SELECT * FROM Chat")
    fun getAll(): Flow<List<Chat>>

    @Transaction
    @Query(
        """
    SELECT chat.*, message.content AS lastMessage, message.date AS dateLastMessage 
    FROM Chat LEFT JOIN Message ON chat.id = message.chatId 
    WHERE message.id = ( SELECT MAX(id) FROM Message
    WHERE chatId = chat.id )
    """
    )
    fun getAllWithLastMessage(): Flow<List<ChatWithLastMessage>>

    @Query("SELECT * FROM Chat WHERE id = :id")
    fun getById(id: Long): Flow<Chat?>

    @Query("DELETE FROM Chat WHERE id = :id")
    suspend fun delete(id: Long)
}