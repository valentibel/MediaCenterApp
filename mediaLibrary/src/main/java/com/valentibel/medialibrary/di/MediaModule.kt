package com.valentibel.medialibrary.di

import com.valentibel.datalibrary.network.adapter.NetworkCallAdapterFactory
import com.valentibel.medialibrary.api.MediaService
import com.valentibel.medialibrary.api.MediaServiceMockImpl
import com.valentibel.medialibrary.network.MediaServiceNetworkImpl
import com.valentibel.medialibrary.repository.MediaDataRepository
import com.valentibel.medialibrary.repository.MediaDataRepositoryImpl
import com.valentibel.medialibrary.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MediaServiceLocal

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MediaServiceNetwork

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ServiceModule {

    @MediaServiceLocal
    @Singleton
    @Binds
    abstract fun bindMediaServiceLocal(
        mediaServiceMockImpl: MediaServiceMockImpl
    ): MediaService

    @Singleton
    @Binds
    abstract fun bindMediaDataRepository(mediaDataRepository: MediaDataRepositoryImpl) : MediaDataRepository
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    internal fun providesNetworkCallAdapterFactory(): CallAdapter.Factory {
        return NetworkCallAdapterFactory()
    }

    @MediaServiceNetwork
    @Singleton
    @Provides
    internal fun provideMediaServiceNetwork(networkCallAdapterFactory: CallAdapter.Factory): MediaService {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(networkCallAdapterFactory)
            .build()
            .create(MediaServiceNetworkImpl::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}