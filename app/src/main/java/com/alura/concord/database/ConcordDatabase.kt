package com.alura.concord.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.alura.concord.data.Chat
import com.alura.concord.data.DownloadableContent
import com.alura.concord.data.Message

@Database(
    entities = [Chat::class, Message::class, DownloadableContent::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1, to = 2
        ),
    ]
)

abstract class ConcordDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun downloadableContentDao(): DownloadableContentDao
}
