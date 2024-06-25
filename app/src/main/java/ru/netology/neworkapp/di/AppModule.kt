package ru.netology.neworkapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.neworkapp.repository.UserRepository
import ru.netology.neworkapp.repository.PostRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

    @Provides
    fun providePostRepository(): PostRepository {
        return PostRepository()
    }
}