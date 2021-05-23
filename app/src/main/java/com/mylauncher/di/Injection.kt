package com.mylauncher.di

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import com.mylauncher.data.Repository
import com.mylauncher.ui.viewmodel.factory.ViewModelMainActivityFactory

object Injection {

    var repo: Repository? = null

    //repo provider
    public fun provideRepository(packageManager: PackageManager, context: Context): Repository {

        if (repo == null) {
            synchronized(Repository::class.java) {
                if (repo == null) {
                    repo = Repository(packageManager, context)
                }
            }
        }
        return repo!!
    }

    // main activity viewmodel provider
    fun provideMainActivityViewModelFactory(
        packageManager: PackageManager,
        context: Context
    ): ViewModelProvider.Factory {
        return ViewModelMainActivityFactory(
            provideRepository(
                packageManager, context
            )
        )
    }

}