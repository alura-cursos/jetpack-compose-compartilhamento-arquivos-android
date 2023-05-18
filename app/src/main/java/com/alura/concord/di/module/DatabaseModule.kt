package com.alura.concord.di.module

import android.content.Context
import androidx.room.Room
import com.alura.concord.database.ChatDao
import com.alura.concord.database.ConcordDatabase
import com.alura.concord.database.DownloadableFileDao
import com.alura.concord.database.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "concord.db"


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ConcordDatabase {
        return Room.databaseBuilder(
            context,
            ConcordDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset("database/concord.db")
            .build()
    }

    @Provides
    fun provideChatDao(db: ConcordDatabase): ChatDao {
        return db.chatDao()
    }

    @Provides
    fun provideMessageDao(db: ConcordDatabase): MessageDao {
        return db.messageDao()
    }

    @Provides
    fun provideDownloadableFileDao(db: ConcordDatabase): DownloadableFileDao {
        return db.downloadableFileDao()
    }

}