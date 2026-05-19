package com.example.kachow_cursach.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kachow_cursach.data.local.TokenManager
import com.example.kachow_cursach.data.network.ApiService
import com.example.kachow_cursach.data.repository.MainRepository
import com.example.kachow_cursach.di.AppModule.mainRepository
import com.example.kachow_cursach.presentation.viewmodel.AuthViewModel
import com.example.kachow_cursach.presentation.viewmodel.CarViewModel
import com.example.kachow_cursach.presentation.viewmodel.DealershipViewModel
import com.example.kachow_cursach.presentation.viewmodel.UserViewModel

object AppModule {
    private lateinit var tokenManager: TokenManager
    lateinit var mainRepository: MainRepository
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

class CarViewModelFactory(
    private val mainRepository: MainRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(CarViewModel::class.java) -> {
                CarViewModel(mainRepository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(mainRepository) as T
            }
            modelClass.isAssignableFrom(DealershipViewModel::class.java) -> {
                DealershipViewModel(mainRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

@Composable
fun provideCarViewModelFactory(): ViewModelProvider.Factory {
    return CarViewModelFactory(AppModule.mainRepository)
}

@Composable
fun getCarViewModel(): CarViewModel {
    return viewModel(factory = provideCarViewModelFactory())
}

fun provideUserViewModel(): UserViewModel {
    return UserViewModel(mainRepository)
}

class UserViewModelFactory(private val mainRepository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(mainRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

@Composable
fun getUserViewModel(): UserViewModel {
    return viewModel(factory = UserViewModelFactory(mainRepository))
}