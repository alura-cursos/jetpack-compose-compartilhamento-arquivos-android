package com.alura.concord.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.alura.concord.data.DownloadableContent
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadableContentDao {

    @Insert(onConflict = REPLACE)
    fun insert(downloadableContent: DownloadableContent)

    @Query("SELECT * FROM DownloadableContent WHERE id = :id")
    fun getById(id: Long): Flow<DownloadableContent?>
}