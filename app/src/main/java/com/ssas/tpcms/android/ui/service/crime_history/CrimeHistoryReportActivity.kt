package com.ssas.tpcms.android.ui.service.crime_history

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.widget.SearchView
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
import com.ssas.tpcms.android.databinding.ActivityCrimeHistoryReportBinding
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.ConstantKeys
import com.ssas.tpcms.android.ui.service.adapter.CrimeHistoryReportAdapter
import com.ssas.tpcms.android.utils.Utils


class CrimeHistoryReportActivity : BaseActivity<ActivityCrimeHistoryReportBinding, ServiceVM>(),
    ItemClickListener, CrimeReportFilerDialog.FilterCallbackListener {

    private lateinit var historyAdapter: CrimeHistoryReportAdapter
    var officerCode: String? = ""
    private var pageNumber = 0
    private var limit = 10
    private var loading = true
    private var isScrolling = false
    /***
     * Searh and filter params
     * */
    private var crimeName: String = ""
    private var crimeLocation: String = ""
    private var crimeType: String = ""
    private var crimeCity: String = ""

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_crime_history_report, ServiceVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        handleIntent(intent)
        initToolbar()
        inflateCrimeHistoryReportList()
        fetchCrimeReports()
        swipeRefesh()
        handleSearchFilterView()
    }

    private fun handleSearchFilterView() {

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s)) {
                    hideSoftKeyboard(this@CrimeHistoryReportActivity)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        binding.searchEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                crimeName = binding.searchEt.text.toString().trim()
                clearOldData()
                fetchCrimeReports()
                return@OnEditorActionListener true
            }
            false
        })

        binding.filterBt.setOnClickListener {
            var filterDilaog = CrimeReportFilerDialog()
            filterDilaog.setCallBackListener(this)
            filterDilaog.show(supportFragmentManager, filterDilaog.tag)
        }

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
        viewModel.getCrimeReports(
            officerCode ?: "",
            pageNumber,
            limit,
            crimeName,
            crimeLocation,
            crimeType,
            crimeCity
        )
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

        vm.networkError.observe(this, Observer {
            if (it) {
                emptyDataLayout()
                stopRefeshingData()
                alertDialogShow(
                    this,
                    getString(R.string.no_network_title),
                    getString(R.string.no_network_connection)
                )
            }
        })

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
                    } else if (it?.response.status?.responseCode == "OPE10009" && it?.response.status?.responseMsg.equals(
                            "Failure",
                            false
                        )
                    ) {
                        emptyDataLayout()
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
        clearOldData()
        clearFilterData()
        fetchCrimeReports()
    }

    private fun clearFilterData() {
        crimeName = ""
        crimeLocation = ""
        crimeType = ""
        crimeCity = ""
    }

    private fun clearOldData() {
        historyAdapter.clearData()
        isScrolling = false
        pageNumber = 0
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

    override fun onFilterSubmit(
        crimeName: String,
        crimeLocation: String,
        crimeCity: String,
        crimeTypeId: String
    ) {
        this.crimeName = crimeName
        this.crimeLocation = crimeLocation
        this.crimeCity = crimeCity
        this.crimeType = crimeType
        clearOldData()
        fetchCrimeReports()
    }
}
