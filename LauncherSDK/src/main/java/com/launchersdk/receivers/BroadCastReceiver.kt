package com.launchersdk.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        var appName = intent.data?.encodedSchemeSpecificPart
        var action = intent.action
        when(action){
            Intent.ACTION_PACKAGE_REMOVED->{
                Toast.makeText(context,"$appName Uninstalled!", Toast.LENGTH_LONG).show()
            }
            Intent.ACTION_PACKAGE_ADDED->{
                Toast.makeText(context,"$appName Installed.", Toast.LENGTH_LONG).show()
            }
        }
    }
}