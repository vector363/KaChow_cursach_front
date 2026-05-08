package com.example.kachow_cursach.di

import android.content.Context
import com.example.kachow_cursach.data.local.TokenManager
import com.example.kachow_cursach.data.network.ApiService
import com.example.kachow_cursach.data.repository.MainRepository
import com.example.kachow_cursach.presentation.viewmodel.AuthViewModel
import com.example.kachow_cursach.presentation.viewmodel.CarViewModel
import com.example.kachow_cursach.presentation.viewmodel.DealershipViewModel

object AppModule {
    private lateinit var tokenManager: TokenManager
    private lateinit var mainRepository: MainRepository
    private lateinit var apiService: ApiService

    fun init(context: Context) {
        tokenManager = TokenManager(context)
        apiService = ApiService()
        mainRepository = MainRepository(apiService, tokenManager)
    }

    fun provideAuthViewModel(): AuthViewModel {
        return AuthViewModel(mainRepository)
    }

    fun provideCarViewModel(): CarViewModel {
        return CarViewModel(mainRepository)
    }

    fun provideDealershipViewModel(): DealershipViewModel {
        return DealershipViewModel(mainRepository)
    }
}