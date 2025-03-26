package com.data.di

import com.data.BuildConfig
import com.data.datasource.remote.NaverMapApi
import com.data.datasource.remote.NaverSearchApi
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
annotation class NaverSearchUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NaverSearchRetrofitUseConvertGson

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultNaverSearchOkHttp

@Module
@InstallIn(SingletonComponent::class)
object NaverSearchModule {
    @NaverSearchUrl
    @Provides
    fun provideNaverSearchUrl(): String = "https://openapi.naver.com/"

    @NaverSearchRetrofitUseConvertGson
    @Provides
    @Singleton
    fun provideRetrofitUseConvertGsonServer(
        @NaverSearchUrl url: String,
        gson: Gson,
        @DefaultNaverSearchOkHttp client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @DefaultNaverSearchOkHttp
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("X-Naver-Client-Id", BuildConfig.NAVER_SEARCH_CLIENT_ID)
                    .header("X-Naver-Client-Secret", BuildConfig.NAVER_SEARCH_CLIENT_SECRET)

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
    fun provideNaverSearchApi(@NaverSearchRetrofitUseConvertGson retrofit: Retrofit): NaverSearchApi =
        retrofit.create(NaverSearchApi::class.java)
}