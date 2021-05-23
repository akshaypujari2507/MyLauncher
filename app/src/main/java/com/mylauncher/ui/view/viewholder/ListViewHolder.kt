package com.mylauncher.ui.view.viewholder

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mylauncher.R
import com.mylauncher.ui.interfaces.OnItemClicked
import kotlinx.android.synthetic.main.list_item.view.*

class ListViewHolder(
    itemView: View,
    val listener: OnItemClicked,
    val packageManager: PackageManager,
    val context: Context
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val iv_app_icon: ImageView = itemView.iv_app_icon
    val tv_app_name: TextView = itemView.tv_app_name
    val tv_packag_name: TextView = itemView.tv_packag_name
    val tv_version_code: TextView = itemView.tv_version_code
    val tv_version_name: TextView = itemView.tv_version_name
    val tv_main_activit_class: TextView = itemView.tv_main_activit_class

    private var item: ResolveInfo? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun bindNowShowingData(item: ResolveInfo?, listSize: Int, position: Int) {
        if (item == null) {
            return
        } else {

            this.item = item

            tv_app_name.text =
                "${context.resources.getString(R.string.name)!!} ${item.loadLabel(packageManager)}"
            tv_packag_name.text =
                "${context.resources.getString(R.string.package_name)!!} ${item.activityInfo.packageName}"

            val packageInfo: PackageInfo =
                packageManager.getPackageInfo(item.activityInfo.packageName, 0)
            tv_main_activit_class.text =
                "${context.resources.getString(R.string.activity_name)!!} ${item.activityInfo.name.replace(
                    item.activityInfo.packageName + ".",
                    ""
                )}"
            tv_version_code.text =
                "${context.resources.getString(R.string.version_code)!!} ${packageInfo.versionCode.toString()}"
            tv_version_name.text =
                "${context.resources.getString(R.string.version_name)!!} ${packageInfo.versionName}"
            try {

                Glide.with(context!!)
                    .load(item.loadIcon(packageManager))
                    .thumbnail(0.1f)
                    .into(iv_app_icon);

            } catch (e: RuntimeException) {

            }
        }
    }


    override fun onClick(v: View?) {
        val position: Int = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            listener.onItemClicked(item!!)
        }

    }
}
