package com.ssas.tpcms.android.ui.home

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.MApplication
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseActivity
import com.ssas.tpcms.android.base.ItemClickListener
import com.ssas.tpcms.android.data.prefs.PrefKeys
import com.ssas.tpcms.android.data.prefs.PrefMain
import com.ssas.tpcms.android.data.service.ServiceListModel
import com.ssas.tpcms.android.databinding.ActivityHomeBinding
import com.ssas.tpcms.android.repo.home.HomeVM
import com.ssas.tpcms.android.ui.ConstantKeys
import com.ssas.tpcms.android.ui.home.adapter.ServiceGridAdapter
import com.ssas.tpcms.android.ui.home.profile.UserProfileFragment
import com.ssas.tpcms.android.ui.home.qr.MissionCardFragment
import com.ssas.tpcms.android.ui.home.qr.OfficerCardFragment
import com.ssas.tpcms.android.ui.home.qr.QrScannerFragment
import com.ssas.tpcms.android.ui.home.qr.VehicleCardFragment
import com.ssas.tpcms.android.ui.service.crime_database.CriminalDatabaseActivity
import com.ssas.tpcms.android.ui.service.crime_history.CrimeHistoryReportActivity
import com.ssas.tpcms.android.ui.service.report_crime.ReportCrimeActivity
import com.ssas.tpcms.android.utils.PermissionUtils
import com.ssas.tpcms.android.utils.Utils
import com.ssas.tpcms.android.widgets.GridSpacingItemDecoration
import javax.inject.Inject


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeVM>(), ItemClickListener,
    QrScannerFragment.QrCallBackListener {

    /*
    * location crime type crime name
    * */

    lateinit var serviceAdapter: ServiceGridAdapter
    var profileData: String? = ""
    var officerCode: String? = ""
    private var qrCardType: String? = ""
    @Inject
    lateinit var prefMain: PrefMain


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_home, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        MApplication.appComponent.inject(this)
        handleIntent(intent)
        initListeners()
        inflaterServiceList()
        addDataRecords()
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null) {
            profileData = intent?.getStringExtra(ConstantKeys.ARG_PROFILE_DETAIL)
            officerCode = intent?.getStringExtra(ConstantKeys.ARG_OFFICER_CODE) ?: "LYeGOV02897TP"
        }
    }

    private fun initListeners() {
        binding.vertMore.setOnClickListener {
            createCardPopupMenu(binding.vertMore)
        }

        binding.seeAllBt.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(ConstantKeys.ARG_OFFICER_CODE, officerCode)
            Utils.jumpActivityWithData(this, NotificationActivity::class.java, bundle)
        }

        binding.qrScannerBt.setOnClickListener {
            createQROptionPopupMenu(binding.qrScannerBt)
        }

        binding.sosBtClick.setOnClickListener {
            sendSOS()
        }
    }

    private fun sendSOS() {
        if (PermissionUtils.hasCameraPermission(
                this,
                PermissionUtils.LOCATION_PERMISSION
            )
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                Log.e("jaspal", "SOS =====> " + location)
                if (location != null) {
                    viewModel.createSOS(officerCode ?: "", location.latitude, location.longitude)
                } else {
                    alertDialogShow(
                        this,
                        getString(R.string.alert),
                        getString(R.string.location_error)
                    )
                }
            }.addOnFailureListener { exception ->
                alertDialogShow(this, getString(R.string.alert), getString(R.string.location_error))
            }
        } else {
            PermissionUtils.requestPermissions(
                this,
                PermissionUtils.LOCATION_PERMISSION,
                PermissionUtils.PERMISSION_LOCATION_CODE
            )
        }
    }

    private fun createServiceData(): ArrayList<ServiceListModel> {
        val list = ArrayList<ServiceListModel>()
        list.add(
            ServiceListModel(
                R.drawable.ic_crimnal_db,
                getString(R.string.crimnal_database_search)
            )
        )
        list.add(ServiceListModel(R.drawable.ic_report_crime, getString(R.string.report_crime)))
        list.add(
            ServiceListModel(
                R.drawable.ic_crime_history,
                getString(R.string.crime_report_history)
            )
        )
        return list
    }


    private fun inflaterServiceList() {
        serviceAdapter = ServiceGridAdapter()
        serviceAdapter.setItemClickListener(this)
        createGridView(binding.serviceList).adapter = serviceAdapter
    }

    private fun addDataRecords() {
        serviceAdapter.addData(createServiceData())
    }

    private fun createGridView(recyclerView: RecyclerView): RecyclerView {
        var layoutManager = GridLayoutManager(this, 3)
        var itemDecotation = GridSpacingItemDecoration(
            3,
            resources.getDimension(R.dimen.margin).toInt(), false
        )
        recyclerView.addItemDecoration(itemDecotation)
        recyclerView.layoutManager = layoutManager
        return recyclerView
    }

    override fun onItemClick(adapter: Any?, position: Int) {
        when (position) {
            0 -> {
                var bundle = Bundle()
                bundle.putString(ConstantKeys.ARG_OFFICER_CODE, officerCode)
                Utils.jumpActivityWithData(this, CriminalDatabaseActivity::class.java, bundle)
            }

            1 -> {
                var bundle = Bundle()
                bundle.putString(ConstantKeys.ARG_OFFICER_CODE, officerCode)
                Utils.jumpActivityWithData(this, ReportCrimeActivity::class.java, bundle)
            }

            2 -> {
                var bundle = Bundle()
                bundle.putString(ConstantKeys.ARG_OFFICER_CODE, officerCode)
                Utils.jumpActivityWithData(this, CrimeHistoryReportActivity::class.java, bundle)
            }
        }
    }

    private fun createCardPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.home_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.myProfile -> {
                    viewModel.getQRProfileReponse(officerCode!!)
                }
                R.id.signOut -> {
                    alertDialogConfirmShow(this, getString(R.string.signout_msg),
                        DialogInterface.OnClickListener { dialog, which ->
                            onSignOut()
                        })
                }
            }
            true
        }
        popup.show()
    }

    private fun createQROptionPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.qr_option_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.officerCard -> {
                    var qrFragment =
                        QrScannerFragment.newInstance(
                            QrScannerFragment.ARG_OFFICE_CARD
                        )
                    qrFragment.setQRResultListener(this)
                    qrFragment.show(supportFragmentManager, qrFragment.tag)
                }
                R.id.vehicleCard -> {
                    var qrFragment = QrScannerFragment.newInstance(
                        QrScannerFragment.ARG_VEHICLE_CARD
                    )
                    qrFragment.setQRResultListener(this)
                    qrFragment.show(supportFragmentManager, qrFragment.tag)
                }
                R.id.MissionCard -> {
                    var qrFragment =
                        QrScannerFragment.newInstance(
                            QrScannerFragment.ARG_MISSION_CARD
                        )
                    qrFragment.setQRResultListener(this)
                    qrFragment.show(supportFragmentManager, qrFragment.tag)
                }
            }
            true
        }
        popup.show()
    }

    override fun subscribeToEvents(vm: HomeVM) {
        vm.qrProfileResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showCircularProgress()
                }
                Status.ERROR -> {
                    dismissCircularProgress()
                    var error = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, error.message)
                    Log.e("jaspal", "Exception Error " + error)
                }
                Status.SUCCESS -> {
                    dismissCircularProgress()
                    var response = it.response
                    if (response.status?.responseCode == "OPS10003" && response.status?.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        if (response?.profileResponse != null) {
                            var profileDataSerfial = Gson().toJson(response?.profileResponse)
                            var profileDialog = UserProfileFragment.newInstance(profileDataSerfial)
                            profileDialog.show(supportFragmentManager, profileDialog.tag)
                        } else {
                            alertDialogShow(this, response.status?.responseValue.toString())
                        }
                    } else {
                        alertDialogShow(this, response.status?.responseValue.toString())
                    }
                }
            }
        })

        vm.sosResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showCircularProgress()
                }
                Status.ERROR -> {
                    dismissCircularProgress()
                    var error = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, error.message)
                }
                Status.SUCCESS -> {
                    dismissCircularProgress()
                    if (it.response.responseCode == "OPS10014" && it.response.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        alertDialogShow(this, it.response.responseValue.toString())
                    } else {
                        alertDialogShow(this, it.response.responseValue.toString())
                    }
                }
            }
        })

        vm.qrScanResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showCircularProgress()
                }
                Status.ERROR -> {
                    dismissCircularProgress()
                    var error = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, error.message)
                    Log.e("jaspal", "Exception Error " + error)
                }
                Status.SUCCESS -> {
                    dismissCircularProgress()
                    var response = it.response
                    if (response.status?.responseCode == "OPS10003" && response.status?.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        if (qrCardType == QrScannerFragment.ARG_OFFICE_CARD) {
                            if (response?.profileResponse != null) {
                                var profileDataSerfial = Gson().toJson(response?.profileResponse)
                                openOfficerCard(profileDataSerfial)
                            } else {
                                alertDialogShow(
                                    this,
                                    response.status?.responseValue.toString()
                                )
                            }
                        }
                    } else if (response.status?.responseCode == "OPS10009" && response.status?.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        if (qrCardType == QrScannerFragment.ARG_VEHICLE_CARD) {
                            if (response?.vehicleModel != null) {
                                var vehicleDataSerfial = Gson().toJson(response?.vehicleModel)
                                openVehicleCard(vehicleDataSerfial)
                            } else {
                                alertDialogShow(
                                    this,
                                    response.status?.responseValue.toString()
                                )
                            }
                        }
                    } else if (response.status?.responseCode == "OPS10011" && response.status?.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        if (qrCardType == QrScannerFragment.ARG_MISSION_CARD) {
                            if (response?.officeMission != null) {
                                var missionSerfial = Gson().toJson(response?.officeMission)
                                var profileDataSerfial = Gson().toJson(response?.profileResponse)
                                openMissionCard(missionSerfial, profileDataSerfial)
                            } else {
                                alertDialogShow(
                                    this,
                                    response.status?.responseValue.toString()
                                )
                            }
                        }
                    } else {
                        alertDialogShow(this!!, response.status?.responseValue.toString())
                    }
                }
            }
        })

    }

    private fun openOfficerCard(profileDataSerfial: String?) {
        var officerCardDialog = OfficerCardFragment.newInstance(profileDataSerfial!!)
        officerCardDialog.show(supportFragmentManager, officerCardDialog.tag)
    }

    private fun openVehicleCard(vehicleDataSerfial: String) {
        var vehicleCardDialog = VehicleCardFragment.newInstance(vehicleDataSerfial!!)
        vehicleCardDialog.show(supportFragmentManager, vehicleCardDialog.tag)
    }

    private fun openMissionCard(
        missionDataSerfial: String,
        profileDataSerfial: String
    ) {
        var missionCardDialog =
            MissionCardFragment.newInstance(missionDataSerfial, profileDataSerfial)
        missionCardDialog.show(supportFragmentManager, missionCardDialog.tag)
    }


    override fun onQRResult(qrType: String, resultString: String) {
        qrCardType = qrType
        when (qrType) {
            QrScannerFragment.ARG_OFFICE_CARD -> {
                viewModel.getQRCodeScanResult(
                    officerCode,
                    true,
                    false,
                    false,
                    false,
                    resultString
                )
            }
            QrScannerFragment.ARG_MISSION_CARD -> {
                Log.e("jaspal", "Mission")
                viewModel.getQRCodeScanResult(
                    officerCode,
                    false,
                    false,
                    true,
                    false,
                    resultString
                )
            }
            QrScannerFragment.ARG_VEHICLE_CARD -> {
                viewModel.getQRCodeScanResult(
                    officerCode,
                    false,
                    true,
                    false,
                    false,
                    resultString
                )

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSOS()
                } else {
                    alertDialogShow(this, getString(R.string.location_permission_msg))
                }
            }
        }
    }

    private fun onSignOut() {
        prefMain.delete(PrefKeys.OFFICER_PROFILE)
        prefMain.delete(PrefKeys.OFFICER_CODE)
        finish()
    }
}
