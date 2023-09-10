package com.berkaksoy.weatherapp.module

import android.content.Context
import com.berkaksoy.weatherapp.Constants
import com.berkaksoy.weatherapp.service.WeatherWebService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext appContext: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(appContext.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun providesOkHttpClientBuilder(): OkHttpClient.Builder {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(100, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(100, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(100, TimeUnit.SECONDS)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)

        httpClientBuilder.addInterceptor { chain: Interceptor.Chain ->

            val original = chain.request()
            val builder = original.newBuilder()
            builder.method(original.method, original.body)

            val request = builder.build()
            val response = chain.proceed(request)

            response
        }
        return httpClientBuilder
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        builder: OkHttpClient.Builder,
        okHttpCache: Cache?
    ): OkHttpClient {
        builder.cache(okHttpCache)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideGsonConverter(gson: Gson?): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        converter: Converter.Factory?,
        okHttpClient: OkHttpClient?
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converter)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherWebService(retrofit: Retrofit): WeatherWebService {
        return retrofit.create(WeatherWebService::class.java)
    }
}