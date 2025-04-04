package com.data.di

import com.data.BuildConfig
import com.data.datasource.remote.NaverMapApi
import com.data.datasource.remote.NaverSearchApi
import com.data.datasource.remote.TMapCarApi
import com.data.datasource.remote.TMapPublicTransportApi
import com.data.datasource.remote.TMapWalkApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TMapUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TMapRetrofitUseConvertGson

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultTMapOkHttp

@Module
@InstallIn(SingletonComponent::class)
object TMapModule {
    @TMapUrl
    @Provides
    fun provideTMapUrl(): String = "https://apis.openapi.sk.com/"

    @TMapRetrofitUseConvertGson
    @Provides
    @Singleton
    fun provideRetrofitUseConvertGsonServer(
        @TMapUrl url: String,
        gson: Gson,
        @DefaultTMapOkHttp client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @DefaultTMapOkHttp
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("appKey", BuildConfig.TMAP_API_KEY)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideTMapPublicTransportApi(@TMapRetrofitUseConvertGson retrofit: Retrofit): TMapPublicTransportApi =
        retrofit.create(TMapPublicTransportApi::class.java)

    @Provides
    @Singleton
    fun provideTMapCarApi(@TMapRetrofitUseConvertGson retrofit: Retrofit): TMapCarApi =
        retrofit.create(TMapCarApi::class.java)

    @Provides
    @Singleton
    fun provideTMapWalkApi(@TMapRetrofitUseConvertGson retrofit: Retrofit): TMapWalkApi =
        retrofit.create(TMapWalkApi::class.java)
}