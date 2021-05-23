package com.launchersdk.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo

object AppListUtil{

    fun getListOfApps(context: Context):List<ResolveInfo>{
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = context!!.packageManager.queryIntentActivities(mainIntent, 0)
        return pkgAppsList
    }

}