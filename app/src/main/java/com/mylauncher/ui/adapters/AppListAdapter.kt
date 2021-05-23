package com.mylauncher.ui.adapters

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.mylauncher.R
import com.mylauncher.ui.interfaces.OnItemClicked
import com.mylauncher.ui.view.viewholder.ListViewHolder
import java.util.*

class AppListAdapter(
    context: Context,
    packageManager: PackageManager,
    private val listener: OnItemClicked
) : RecyclerView.Adapter<ListViewHolder>(), Filterable {

    public var filteredList: MutableList<ResolveInfo> = arrayListOf()

    var listData: ArrayList<ResolveInfo>? = null
        set(value) {
            field = value
            filteredList.addAll(field!!)
            notifyDataSetChanged()
        }

    var context: Context = context
    var packageManager: PackageManager = packageManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view, listener, packageManager, context)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var currentItem = filteredList!![position]

        if (filteredList != null) {
            val viewHolder = holder as ListViewHolder
            viewHolder.bindNowShowingData(currentItem, filteredList!!.size, position)
        } else {
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredList.clear()
                if (charSearch.isEmpty()) {
                    filteredList.addAll(listData!!)
                } else {
                    for (row in listData!!) {
                        if (row.loadLabel(packageManager)!!.toString().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            filteredList.add(row)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }

}
