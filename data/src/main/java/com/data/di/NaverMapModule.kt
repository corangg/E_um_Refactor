package com.data.di

import com.data.datasource.remote.NaverMapApi
import com.google.gson.Gson
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
import com.data.BuildConfig
import com.google.gson.GsonBuilder

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NaverMapUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NaverMapRetrofitUseConvertGson

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultNaverMapOkHttp

@Module
@InstallIn(SingletonComponent::class)
object NaverMapModule {
    @NaverMapUrl
    @Provides
    fun provideNaverMapUrl(): String = "https://maps.apigw.ntruss.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @NaverMapRetrofitUseConvertGson
    @Provides
    @Singleton
    fun provideRetrofitUseConvertGsonServer(
        @NaverMapUrl url: String,
        gson: Gson,
        @DefaultNaverMapOkHttp client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @DefaultNaverMapOkHttp
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_MAP_CLIENT_ID)
                    .header("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_MAP_CLIENT_SECRET)

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
    fun provideNaverMapApi(@NaverMapRetrofitUseConvertGson retrofit: Retrofit): NaverMapApi =
        retrofit.create(NaverMapApi::class.java)
}