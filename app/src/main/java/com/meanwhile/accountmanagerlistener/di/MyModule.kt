package com.meanwhile.accountmanagerlistener.di

import android.accounts.AccountManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MyModule {

    @Provides
    fun provideAccountManager(@ApplicationContext context: Context): AccountManager = AccountManager.get(context)
}