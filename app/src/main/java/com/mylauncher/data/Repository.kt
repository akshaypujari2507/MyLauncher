package com.mylauncher.data

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.lifecycle.MutableLiveData
import com.launchersdk.utils.AppListUtil
import java.util.*
import kotlin.collections.ArrayList

class Repository(val packageManager: PackageManager, val context: Context) {

    private var appList: MutableLiveData<List<ResolveInfo>> = MutableLiveData()
    var listResolver: List<ResolveInfo>? = null

    fun getAppList(): MutableLiveData<List<ResolveInfo>> {
        if (appList.value == null) {
            try {
                listResolver = AppListUtil.getListOfApps(context)
                Collections.sort(
                    listResolver,
                    ResolveInfo.DisplayNameComparator(packageManager)
                )
                var copyList: List<ResolveInfo> = ArrayList(listResolver!!)

                appList?.value = copyList

            } catch (e: Exception) {
                println(e)
            }
        }
        return appList
    }

}