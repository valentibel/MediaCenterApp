package com.valentibel.medialibrary.di

import com.valentibel.medialibrary.api.MediaService
import com.valentibel.medialibrary.api.MediaServiceImpl
import com.valentibel.medialibrary.repository.MediaDataRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.contracts.contract

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ServiceModule {
    @Singleton
    @Binds
    abstract fun bindMediaService(
        mediaServiceImpl: MediaServiceImpl
    ): MediaService
}


@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Singleton
    @Provides
    fun provideMediaDataRepository(service: MediaService) = MediaDataRepository(service)
}