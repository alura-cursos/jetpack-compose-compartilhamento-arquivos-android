package com.alura.concord.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.alura.concord.database.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(messageEntity: MessageEntity)

    @Query("SELECT * FROM Message")
    fun getAll(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM Message WHERE chatId = :chatId")
    fun getByChatId(chatId: Long): Flow<List<MessageEntity>>

    @Query("SELECT * FROM Message WHERE id = :id")
    fun getById(id: Long): Flow<MessageEntity?>

    @Query("DELETE FROM Message WHERE id = :id")
    suspend fun delete(id: Long)


}