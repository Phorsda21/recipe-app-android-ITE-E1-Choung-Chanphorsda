package com.example.mad_lab2.di

import android.content.Context
import androidx.room.Room
import com.example.mad_lab2.data.local.RecipeDatabase
import com.example.mad_lab2.data.local.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(
        @ApplicationContext context: Context
    ): RecipeDatabase {
        return Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "recipe_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: RecipeDatabase): FavoriteDao {
        return database.favoriteDao()
    }
}