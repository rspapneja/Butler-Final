package com.butlerschocolates.app.viewmodels.baseviewmodel


import android.view.WindowManager
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel

import com.butlerschocolates.app.respostiory.AppRepository

abstract class BaseViewModel: ViewModel() {
  val appRepository: AppRepository = AppRepository()
  val isLoading = ObservableBoolean(false)

  fun setIsLoading(isLoading: Boolean) {
       this.isLoading.set(isLoading)
  }

    fun setIsLoading(isLoading: Boolean,activity :FragmentActivity) {
        if(isLoading)
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        else
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        this.isLoading.set(isLoading)

    }
}