package com.mylauncher.ui.interfaces

import android.content.pm.ResolveInfo


interface OnItemClicked {
    fun onItemClicked(resolveInfo: ResolveInfo)
}