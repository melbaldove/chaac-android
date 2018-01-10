package org.mb.m3r.chaac.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.mb.m3r.chaac.data.source.remote.ChaacAPI
import org.mb.m3r.chaac.di.scopes.PerApplication
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

/**
 * @author Melby Baldove
 * melqbaldove@gmail.com
 */
@Module
class NetworkModule {
    @Provides
    @PerApplication
    fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(ChuckInterceptor(context))
                .addInterceptor(StethoInterceptor())
                .build()
    }

    @Provides
    @PerApplication
    @Named("chaac")
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return retrofitCreator(client, ChaacAPI.URL)
    }

    fun retrofitCreator(client: OkHttpClient, baseUrl: String): Retrofit {
        val gson = GsonBuilder()
                .create()
        return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    @Provides
    @PerApplication
    fun provideChaacAPI(@Named("chaac") retrofit: Retrofit): ChaacAPI {
        return retrofit.create(ChaacAPI::class.java)
    }
}