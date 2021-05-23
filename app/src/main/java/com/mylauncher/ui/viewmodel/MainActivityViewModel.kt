package com.mylauncher.ui.viewmodel

import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mylauncher.data.Repository

class MainActivityViewModel(val repo: Repository) : ViewModel() {

    private var appList: LiveData<List<ResolveInfo>>? = null

    fun getAppList(): LiveData<List<ResolveInfo>> {
        if (appList == null) {
            try {
                appList = repo.getAppList()
            } catch (e: Exception) {
                println(e)
            }
        }
        return appList!!
    }

}