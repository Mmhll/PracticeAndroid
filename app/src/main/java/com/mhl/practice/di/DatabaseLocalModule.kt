package com.mhl.practice.di

import android.app.Application
import androidx.room.Room
import com.mhl.practice.model.DatabaseDao
import com.mhl.practice.model.LocalDatabase
import com.mhl.practice.model.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseLocalModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(application, LocalDatabase::class.java, "local_db")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(database: LocalDatabase): DatabaseDao{
        return database.getDao()
    }

    @Provides
    @Singleton
    @Named("localRepository")
    fun provideLocalRepository(dao: DatabaseDao): LocalRepository = LocalRepository(dao)
}