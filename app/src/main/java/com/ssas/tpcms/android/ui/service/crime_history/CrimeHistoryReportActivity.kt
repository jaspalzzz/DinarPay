package com.ssas.tpcms.android.ui.service.crime_history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseActivity
import com.ssas.tpcms.android.base.ItemClickListener
import com.ssas.tpcms.android.data.models.crime_report.CrimeReportDataItem
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileRecordModel
import com.ssas.tpcms.android.databinding.ActivityCrimeHistoryReportBinding
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.ConstantKeys
import com.ssas.tpcms.android.ui.service.adapter.CrimeHistoryReportAdapter
import com.ssas.tpcms.android.ui.service.crime_database.CrimnalCardFragment
import com.ssas.tpcms.android.utils.Utils

class CrimeHistoryReportActivity : BaseActivity<ActivityCrimeHistoryReportBinding, ServiceVM>(),
    ItemClickListener {

    private lateinit var historyAdapter: CrimeHistoryReportAdapter
    var officerCode: String? = ""
    private var pageNumber = 0
    private var limit = 10
    private var loading = true
    private var isScrolling = false


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_crime_history_report, ServiceVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        handleIntent(intent)
        initToolbar()
        inflateCrimeHistoryReportList()
        fetchCrimeReports()
        swipeRefesh()
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
        binding.toolbar.toolbarTitle.setText(R.string.crime_report_history)
    }

    private fun fetchCrimeReports() {
        viewModel.getCrimeReports(officerCode ?: "", pageNumber, limit)
    }

    private fun inflateCrimeHistoryReportList() {
        var layoutManager = LinearLayoutManager(this)
        binding.crimieHirtoryList.layoutManager = layoutManager
        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.white_divider
            )!!
        )
        binding.crimieHirtoryList.addItemDecoration(itemDecorator)
        historyAdapter = CrimeHistoryReportAdapter()
        historyAdapter.setItemClickListener(this)
        binding.crimieHirtoryList.adapter = historyAdapter

        binding.crimieHirtoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            historyAdapter!!.hideProgressBar(false)
                            fetchCrimeReports()
                        }
                    }
                }
            }
        })
    }

    override fun subscribeToEvents(vm: ServiceVM) {
        vm.crimeReportResponse.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.SUCCESS -> {
                    dismissProgress()
                    stopRefeshingData()
                    if (it?.response.status?.responseCode == "OPS10007" && it?.response.status?.responseMsg.equals(
                            "Success", false
                        )
                    ) {
                        addCrimeReports(it?.response.crimeReportList)
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

    private fun addCrimeReports(crimeReportList: ArrayList<CrimeReportDataItem>?) {
        if (crimeReportList?.isNotEmpty()!!) {
            mainLayout()
            historyAdapter.addData(crimeReportList!!)
        } else {
            if (historyAdapter.count() == 0) {
                emptyDataLayout()
            } else {
                historyAdapter.hideProgressBar(true)
            }
        }
    }

    private fun swipeRefesh() {
        binding.swipeRefesh.setOnRefreshListener {
            startRefreshingData()
        }
    }

    private fun startRefreshingData() {
        historyAdapter.clearData()
        isScrolling = false
        pageNumber = 0
        fetchCrimeReports()
    }

    private fun stopRefeshingData() {
        if (binding.swipeRefesh.isRefreshing) {
            binding.swipeRefesh.isRefreshing = false
        }
    }


    private fun mainLayout() {
        historyAdapter.hideProgressBar(true)
        loading = true
        binding.isEmptyLayout = false
    }

    private fun emptyDataLayout() {
        historyAdapter.hideProgressBar(true)
        loading = false
        if (historyAdapter.count() <= 0) {
            binding.isEmptyLayout = true
        }
    }

    override fun onItemClick(adapter: Any?, position: Int) {
        var crimeReportData: CrimeReportDataItem = historyAdapter.getItem(position)
        var crimeReportDataSerial = Gson().toJson(crimeReportData)
        var crimeReportDialog = CrimeReportCardFragment.newInstance(crimeReportDataSerial)
        crimeReportDialog.show(supportFragmentManager, crimeReportDialog.tag)
    }
}
