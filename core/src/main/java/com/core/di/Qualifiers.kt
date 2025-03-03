package com.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnconfinedDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalDataSources

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteDataSources

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnSplashUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EmojiUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleFontUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnSplashRetrofitUseConvertGson

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EmojiRetrofitUseConvertGson

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleFontRetrofitUseConvertGson

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultUnSplashOkHttp

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultEmojiOkHttp

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultGoogleFontOkHttp