package com.ssas.tpcms.android.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseActivity
import com.ssas.tpcms.android.base.ItemClickListener
import com.ssas.tpcms.android.data.models.notification.NotificationDataModel
import com.ssas.tpcms.android.databinding.ActivityNotificationBinding
import com.ssas.tpcms.android.repo.home.HomeVM
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.ConstantKeys
import com.ssas.tpcms.android.ui.home.adapter.NotificationListAdapter
import com.ssas.tpcms.android.ui.service.adapter.CrimeHistoryReportAdapter
import com.ssas.tpcms.android.utils.Utils

class NotificationActivity : BaseActivity<ActivityNotificationBinding, HomeVM>(),
    ItemClickListener {

    private lateinit var notificationAdapter: NotificationListAdapter
    var officerCode: String? = ""
    var pageNumber = 0
    var limit = 10
    private var loading = true
    private var isScrolling = false


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_notification, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        handleIntent(intent)
        initToolbar()
        inflateNotificationList()
        swipeRefesh()
        fetchNotifications()
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null) {
            officerCode = intent.getStringExtra(ConstantKeys.ARG_OFFICER_CODE)
        }
    }

    private fun fetchNotifications() {
        viewModel.getNotifications(officerCode ?: "", limit, pageNumber)
    }

    private fun initToolbar() {
        binding.toolbar.backViewBt.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.toolbarTitle.setText(R.string.dialy_notifications_text)
    }

    private fun inflateNotificationList() {
        var layoutManager = LinearLayoutManager(this)
        binding.notificationList.layoutManager = layoutManager
        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.white_divider
            )!!
        )
        binding.notificationList.addItemDecoration(itemDecorator)
        notificationAdapter = NotificationListAdapter()
        notificationAdapter.setItemClickListener(this)
        binding.notificationList.adapter = notificationAdapter

        binding.notificationList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            notificationAdapter!!.hideProgressBar(false)
                            fetchNotifications()
                        }
                    }
                }
            }
        })
    }


    override fun subscribeToEvents(vm: HomeVM) {
        vm.notificationResponse.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.SUCCESS -> {
                    dismissProgress()
                    stopRefeshingData()
                    if (it?.response.status.responseCode == "OPS10013" && it?.response.status.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        notificationData(it?.response.notificationList)
                    } else {
                        emptyDataLayout()
                        alertDialogShow(
                            this, it?.response.status.responseValue.toString()
                        )
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    stopRefeshingData()
                    emptyDataLayout()
                    var error = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, error.message)
                }
            }
        })
    }

    private fun mainLayout() {
        notificationAdapter.hideProgressBar(true)
        loading = true
        binding.isEmptyLayout = false
    }

    private fun emptyDataLayout() {
        notificationAdapter.hideProgressBar(true)
        loading = false
        if (notificationAdapter.count() <= 0) {
            binding.isEmptyLayout = true
        }
    }

    private fun swipeRefesh() {
        binding.swipeRefesh.setOnRefreshListener {
            startRefreshingData()
        }
    }

    private fun startRefreshingData() {
        notificationAdapter.clearData()
        isScrolling = false
        pageNumber = 0
        fetchNotifications()
    }

    private fun stopRefeshingData() {
        if (binding.swipeRefesh.isRefreshing) {
            binding.swipeRefesh.isRefreshing = false
        }
    }

    private fun notificationData(notificationList: ArrayList<NotificationDataModel>?) {
        if (notificationList?.isNotEmpty()!!) {
            mainLayout()
            notificationAdapter.addData(notificationList!!)
        } else {
            if (notificationAdapter.count() == 0) {
                emptyDataLayout()
            } else {
                notificationAdapter.hideProgressBar(true)
            }
        }
    }

    override fun onItemClick(adapter: Any?, position: Int) {

    }
}
