package com.mhl.practice.di

import com.mhl.practice.model.UsersRepository
import com.mhl.practice.model.UsersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationRemoteModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseURL(): String = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideRetrofit(@Named("BASE_URL") BASE_URL: String): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("authService")
    fun provideService(@Named("authRetrofit") retrofit: Retrofit): UsersService = retrofit.create(UsersService::class.java)

    @Provides
    @Singleton
    @Named("authRepository")
    fun provideRepository(@Named("authService") service: UsersService): UsersRepository =
        UsersRepository(service)
}