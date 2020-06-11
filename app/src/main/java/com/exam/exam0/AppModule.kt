package com.exam.exam0

import android.app.Application
import com.exam.exam0.database.AppDatabase
import com.exam.exam0.page.ConnectivityLiveData
import com.exam.exam0.page.AppViewModel
import com.exam.exam0.remote.RemoteProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModules {
    private val viewModels = module {
        single { ConnectivityLiveData(get<Application>()) }
        viewModel { AppViewModel(get(), get(), get()) }
    }

    private val roomModule = module {
        single { AppDatabase.getInstance(androidApplication()) }
        single { get<AppDatabase>().scheduleDAO() }
    }

    private val remoteModule = module {
        single { RemoteProvider() }
    }
    val modules = viewModels + remoteModule + roomModule
}
