package com.mylauncher.ui.view

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.content.res.Configuration
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.launchersdk.receivers.BroadCastReceiver
import com.mylauncher.R
import com.mylauncher.di.Injection
import com.mylauncher.ui.adapters.AppListAdapter
import com.mylauncher.ui.interfaces.OnItemClicked
import com.mylauncher.ui.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), OnItemClicked {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var mGridLayoutManager: GridLayoutManager

    private val GRID_COLUMNS_PORTRAIT = 1
    private val GRID_COLUMNS_LANDSCAPE = 2

    var adapter: AppListAdapter? = null
    var broadCastReceiver: BroadCastReceiver? = null

    override fun onStart() {
        super.onStart()
        initBroadCast()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var packageManager = getPackageManager()
        viewModel = ViewModelProviders.of(
            this,
            Injection.provideMainActivityViewModelFactory(packageManager, this)
        )
            .get(MainActivityViewModel::class.java)

        initRecyclerView()
        initSearch()

    }

    fun initRecyclerView() {

        configureRecyclerAdapter(resources.configuration.orientation)


        var packageManager = getPackageManager()

        viewModel.getAppList().observe(this, androidx.lifecycle.Observer {
            setupAdapter(it)
        })

        adapter = AppListAdapter(this@MainActivity, packageManager, this@MainActivity)
        recyclerView.adapter = adapter
    }

    fun setupAdapter(copyList: List<ResolveInfo>) {

        if (copyList.size > 0) {
            adapter?.listData = copyList as ArrayList<ResolveInfo>
        }
    }

    fun openApp(resolveInfo: ResolveInfo) {
        val activity: ActivityInfo = resolveInfo.activityInfo
        val name = ComponentName(
            activity.applicationInfo.packageName,
            activity.name
        )
        val i = Intent(Intent.ACTION_MAIN)

        i.addCategory(Intent.CATEGORY_LAUNCHER)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        i.component = name

        startActivity(i)
    }

    fun initSearch() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                    adapter.getFilter().filter(newText);
                adapter!!.filter.filter(newText)
                return false
            }
        })
    }

    fun initBroadCast() {
        broadCastReceiver = BroadCastReceiver()
        val intentFilter: IntentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package");
        registerReceiver(broadCastReceiver, intentFilter)

    }

    private fun configureRecyclerAdapter(orientation: Int) {
        val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT
        mGridLayoutManager = GridLayoutManager(
            this,
            if (isPortrait) GRID_COLUMNS_PORTRAIT else GRID_COLUMNS_LANDSCAPE
        )
        recyclerView.setLayoutManager(mGridLayoutManager)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (broadCastReceiver != null) {
            unregisterReceiver(broadCastReceiver);
            broadCastReceiver = null
        }
    }

    override fun onItemClicked(resolveInfo: ResolveInfo) {
        openApp(resolveInfo)
    }

}