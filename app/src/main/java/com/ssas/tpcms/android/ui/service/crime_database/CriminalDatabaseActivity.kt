package com.ssas.tpcms.android.ui.service.crime_database

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseActivity
import com.ssas.tpcms.android.base.ItemClickListener
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileRecordModel
import com.ssas.tpcms.android.databinding.ActivityCriminalDatabaseBinding
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.ConstantKeys
import com.ssas.tpcms.android.ui.service.adapter.CriminalDbAdapter
import com.ssas.tpcms.android.utils.Utils
import com.ssas.tpcms.android.widgets.GridSpacingItemDecoration

class CriminalDatabaseActivity : BaseActivity<ActivityCriminalDatabaseBinding, ServiceVM>(),
    ItemClickListener {

    private lateinit var criminalDbAdapter: CriminalDbAdapter
    var officerCode: String? = ""
    private var pageNumber = 0
    private var limit = 10
    private var loading = true
    private var isScrolling = false


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_criminal_database, ServiceVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        handleIntent(intent)
        initToolbar()
        inflaterServiceList()
        fetchCriminalRecords()
        swipeRefesh()
    }

    private fun fetchCriminalRecords() {
        viewModel.getCriminalProfileRecords(officerCode?:"", pageNumber, limit)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null) {
            officerCode = intent.getStringExtra(ConstantKeys.ARG_OFFICER_CODE)
        }
    }

    private fun initToolbar() {
        binding.toolbar.backViewBt.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.toolbarTitle.setText(R.string.criminalslist)
    }

    private fun inflaterServiceList() {
        criminalDbAdapter = CriminalDbAdapter()
        criminalDbAdapter.setItemClickListener(this)
        createGridView(binding.criminalList).adapter = criminalDbAdapter
    }

    private fun createGridView(recyclerView: RecyclerView): RecyclerView {
        var layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (criminalDbAdapter.getItemViewType(position)) {
                    1 -> 1
                    0 -> 2  //number of columns of the grid
                    else -> -1
                }
            }
        }

        var itemDecotation = GridSpacingItemDecoration(
            2,
            resources.getDimension(R.dimen.margin).toInt(), false
        )

        recyclerView.addItemDecoration(itemDecotation)
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (dy > 0) {
                    if (loading) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                            loading = false
                            pageNumber += 1
                            isScrolling = true
                            criminalDbAdapter!!.hideProgressBar(false)
                            fetchCriminalRecords()
                        }
                    }
                }
            }
        })
        return recyclerView
    }

    override fun onItemClick(adapter: Any?, position: Int) {
        var criminalData:CrimnalProfileRecordModel = criminalDbAdapter.getItem(position)
        var criminalDataSerial = Gson().toJson(criminalData)
        var crimnalCardDialog = CrimnalCardFragment.newInstance(criminalDataSerial)
        crimnalCardDialog.show(supportFragmentManager, crimnalCardDialog.tag)

    }

    override fun subscribeToEvents(vm: ServiceVM) {
        vm.crimnalProfileListResponse.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.SUCCESS -> {
                    dismissProgress()
                    stopRefeshingData()
                    if (it?.response.status?.responseCode == "OPS10005" && it?.response.status?.responseMsg.equals(
                            "Success",false
                        )
                    ) {
                        addCriminalProfileData(it?.response.profileList)
                    } else {
                        emptyDataLayout()
                        alertDialogShow(
                            this, it?.response.status?.responseValue.toString()
                        )
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    stopRefeshingData()
                    emptyDataLayout()
                    var error =
                        Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, error.message)
                }
            }
        })
    }

    private fun addCriminalProfileData(criminalProfileList: ArrayList<CrimnalProfileRecordModel>?) {
        if (criminalProfileList?.isNotEmpty()!!) {
            mainLayout()
            criminalDbAdapter.addData(criminalProfileList!!)
        } else {
            if (criminalDbAdapter.count() == 0) {
                emptyDataLayout()
            } else {
                criminalDbAdapter.hideProgressBar(true)
            }
        }
    }

    private fun swipeRefesh() {
        binding.swipeRefesh.setOnRefreshListener {
            startRefreshingData()
        }
    }

    private fun startRefreshingData() {
        criminalDbAdapter.clearData()
        isScrolling = false
        pageNumber = 0
        fetchCriminalRecords()
    }

    private fun stopRefeshingData() {
        if (binding.swipeRefesh.isRefreshing) {
            binding.swipeRefesh.isRefreshing = false
        }
    }


    private fun mainLayout() {
        criminalDbAdapter.hideProgressBar(true)
        loading = true
        binding.isEmptyLayout = false
    }

    private fun emptyDataLayout() {
        criminalDbAdapter.hideProgressBar(true)
        loading = false
        if (criminalDbAdapter.count() <= 0) {
            binding.isEmptyLayout = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionResultListener?.onPermissionResult(requestCode, permissions, grantResults)
    }
}
