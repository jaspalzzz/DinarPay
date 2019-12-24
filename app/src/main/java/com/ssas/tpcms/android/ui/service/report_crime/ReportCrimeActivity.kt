package com.ssas.tpcms.android.ui.service.report_crime

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseActivity
import com.ssas.tpcms.android.data.models.UploadImageModel
import com.ssas.tpcms.android.data.models.crimetype.CrimeTypeResponse
import com.ssas.tpcms.android.databinding.ActivityReportCrimeBinding
import com.ssas.tpcms.android.repo.service.ServiceClickEvents
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.ConstantKeys
import com.ssas.tpcms.android.ui.dialogs.CountryPickerDialog
import com.ssas.tpcms.android.ui.dialogs.CrimeTypeFragment
import com.ssas.tpcms.android.ui.service.adapter.UploadMultiPhotosAdapter
import com.ssas.tpcms.android.utils.PermissionUtils
import com.ssas.tpcms.android.utils.Utils
import com.ssas.tpcms.android.utils.countrycode.Country
import com.ssas.tpcms.android.utils.countrycode.CountryUtils
import com.ssas.tpcms.android.utils.files.PickMediaHelper
import java.io.File

class ReportCrimeActivity : BaseActivity<ActivityReportCrimeBinding, ServiceVM>(),
    UploadMultiPhotosAdapter.ImageItemClickListener, PickMediaHelper.Callback {

    private lateinit var uploadAdapter: UploadMultiPhotosAdapter
    private lateinit var mediaPicker: PickMediaHelper
    private var countryCodePicker: CountryPickerDialog? = null
    private var officerCode: String? = ""
    private var crimeTypeList: ArrayList<CrimeTypeResponse> = ArrayList()


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_report_crime, ServiceVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        handleIntent(intent)
        initToolbar()
        mediaPicker = PickMediaHelper(this, this)
        inflateCasePhotosList()
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
        binding.toolbar.toolbarTitle.setText(R.string.report_crime)
    }

    private fun inflateCasePhotosList() {
        uploadAdapter = UploadMultiPhotosAdapter()
        uploadAdapter.initAddMoreOption()
        uploadAdapter.addImageItemClickListener(this)
        createHorizontalList(binding.uploadImageList).adapter = uploadAdapter
    }

    private fun createHorizontalList(recyclerView: RecyclerView): RecyclerView {
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        return recyclerView
    }

    private fun fetchCurrentLocation() {
        if (PermissionUtils.hasCameraPermission(
                this,
                PermissionUtils.LOCATION_PERMISSION
            )
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                var address = Utils.fetchAddressFromLocation(this, location)
                viewModel.crimeLocation.set(address ?: "")
            }
        } else {
            PermissionUtils.requestPermissions(
                this,
                PermissionUtils.LOCATION_PERMISSION,
                PermissionUtils.PERMISSION_LOCATION_CODE
            )
        }
    }

    override fun subscribeToEvents(vm: ServiceVM) {
        binding.vm = vm

        vm.networkError.observe(this, Observer {
            if (it) {
                alertDialogShow(
                    this,
                    getString(R.string.no_network_title),
                    getString(R.string.no_network_connection)
                )
            }
        })

        vm.crimeLocationError.observe(this, Observer {
            alertDialogShow(this, getString(R.string.alert), getString(it))
        })

        vm.crimeTypeError.observe(this, Observer {
            alertDialogShow(this, getString(R.string.alert), getString(it))
        })

        vm.crimeNameError.observe(this, Observer {
            alertDialogShow(this, getString(R.string.alert), getString(it))
        })

        vm.clickEvents.observe(this, Observer {
            when (it) {
                ServiceClickEvents.CRIME_TYPE_CLICK -> {
                    if (crimeTypeList.isEmpty()) {
                        vm.getCrimeTypes()
                    } else {
                        showCrimeTyeDialog()
                    }
                }

                ServiceClickEvents.GET_CURRENT_LOCATION_CLICK -> {
                    fetchCurrentLocation()
                }

                ServiceClickEvents.MAIK_A_REPORT_CLICK -> {
                    vm.reportACrime(uploadAdapter.getUploadImageList(), officerCode ?: "")
                }
                ServiceClickEvents.COUNTRY_CODE_CLICK -> {
                    if (countryCodePicker == null) {
                        countryCodePicker =
                            CountryPickerDialog.newInstance(CountryPickerDialog.TYPE_COUNTRY_CODE)
                    }
                    countryCodePicker?.setListener(object : CountryPickerDialog.CountrySelect {
                        override fun onCountryClick(country: Country) {
                            countryCodePicker?.dismiss()
                            vm.countryCode.set(country.phoneCode)
                            val image = CountryUtils.getFlagDrawableResId(country)
                            vm.countryFlag.set(image)
                        }
                    })
                    if (!countryCodePicker?.isAdded!!) {
                        countryCodePicker?.show(supportFragmentManager, countryCodePicker?.tag)
                    }
                }
            }
        })

        vm.reportCrimeResponse.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.SUCCESS -> {
                    dismissProgress()
                    if (it?.response.responseCode == "OPS10006" && it?.response.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        alertDialogShow(this, it?.response.responseValue.toString(),
                            DialogInterface.OnClickListener { dialog, which ->
                                finish()
                            })
                    } else {
                        alertDialogShow(this, it?.response.responseValue.toString())
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    var error = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, error.message)
                }
            }
        })

        vm.crimeTypeResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showCircularProgress()
                }
                Status.SUCCESS -> {
                    dismissCircularProgress()
                    if (it?.response != null) {
                        crimeTypeList = it?.response
                        showCrimeTyeDialog()
                    } else {
                        alertDialogShow(
                            this,
                            getString(R.string.alert),
                            getString(R.string.exception_msg)
                        )
                    }
                }
                Status.ERROR -> {
                    dismissCircularProgress()
                    var error = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, error.message)
                }
            }
        })
    }

    private fun showCrimeTyeDialog() {
        var crimeTypeDialog = CrimeTypeFragment.newInstance(crimeTypeList)
        crimeTypeDialog.setItemListener(object :
            CrimeTypeFragment.CrimeTypeItemListener {
            override fun selectedCrimeType(item: CrimeTypeResponse) {
                viewModel.crimeType.set(item.crimenameEn)
                viewModel.crimeTypeId.set(item.crimeTypeId)
            }
        })
        crimeTypeDialog.show(supportFragmentManager, crimeTypeDialog.tag)
    }


    override fun onAddMoreImageClick(uploadMultiPhotosAdapter: UploadMultiPhotosAdapter) {
        mediaPicker.showDialogFromActivity(this)
    }

    override fun onRemoveImageClick(position: Int) {
        uploadAdapter.removeImage(position)
    }

    override fun onImageViewClick(
        uploadMultiPhotosAdapter: UploadMultiPhotosAdapter,
        position: Int
    ) {

    }

    override fun onImagePicked(path: String, request: Int) {
        var file = File(path)
        var image = UploadImageModel(1, file.name, path)
        if (uploadAdapter.getImageListSize() <= 10) {
            uploadAdapter.addImage(image)
        } else {
            alertDialogShow(
                this,
                getString(R.string.info),
                getString(R.string.criminal_upload_limit_msg)
            )
        }
    }

    override fun onError(message: String) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!mediaPicker!!.handleOnActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mediaPicker.resultListener.onGranted(PermissionUtils.CAMERA_PERMISSION)
                } else {
                    alertDialogShow(this, getString(R.string.camera_permission_msg))
                }
            }
            PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mediaPicker.resultListener.onGranted(PermissionUtils.GALLARY_PERMISSION)
                } else {
                    alertDialogShow(this, getString(R.string.camera_permission_msg))
                }
            }

            PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchCurrentLocation()
                } else {
                    alertDialogShow(this, getString(R.string.location_permission_msg))
                }
            }
        }
    }
}
