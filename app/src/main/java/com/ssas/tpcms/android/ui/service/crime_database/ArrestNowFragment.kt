package com.ssas.tpcms.android.ui.service.crime_database

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.data.models.UploadImageModel
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileRecordModel
import com.ssas.tpcms.android.databinding.FragmentArrestNowBinding
import com.ssas.tpcms.android.repo.service.ServiceClickEvents
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.service.adapter.UploadMultiPhotosAdapter
import com.ssas.tpcms.android.utils.PermissionResultListener
import com.ssas.tpcms.android.utils.PermissionUtils
import com.ssas.tpcms.android.utils.Utils
import com.ssas.tpcms.android.utils.files.PickMediaHelper
import java.io.File

/**
 * Created by Jaspal on 12/13/2019.
 */
private const val ARG_CRIMINAL_DATA = "ARG_CRIMINAL_DATA"
private const val ARG_OFFICER_CODE = "ARG_OFFICER_CODE"

class ArrestNowFragment : BaseBottomSheetFragment<FragmentArrestNowBinding, ServiceVM>(),
    UploadMultiPhotosAdapter.ImageItemClickListener, PickMediaHelper.Callback,
    PermissionResultListener {

    private var criminalData: String = ""
    private var officerCode: String = ""
    lateinit var criminalDataModel: CrimnalProfileRecordModel
    private lateinit var uploadAdapter: UploadMultiPhotosAdapter
    private lateinit var mediaPicker: PickMediaHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        arguments?.let {
            officerCode = it.getString(ARG_OFFICER_CODE).toString()
            criminalData = it.getString(ARG_CRIMINAL_DATA).toString()
        }
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_arrest_now, ServiceVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!criminalData.isEmpty()) {
            criminalDataModel = Gson().fromJson<CrimnalProfileRecordModel>(
                criminalData,
                CrimnalProfileRecordModel::class.java
            )
            binding.item = criminalDataModel
            viewModel.crimeName.set(criminalDataModel.crimeName)
        }
        mediaPicker = PickMediaHelper(context!!, this)
        (context as CriminalDatabaseActivity).setPermissionResultListener(this)
        inflateCasePhotosList()
    }


    private fun inflateCasePhotosList() {
        uploadAdapter = UploadMultiPhotosAdapter()
        uploadAdapter.initAddMoreOption()
        uploadAdapter.addImageItemClickListener(this)
        createHorizontalList(binding.picturesList).adapter = uploadAdapter
    }

    private fun createHorizontalList(recyclerView: RecyclerView): RecyclerView {
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        return recyclerView
    }

    override fun subscribeToEvents(vm: ServiceVM) {
        binding.vm = vm

        vm.networkError.observe(this, Observer {
            if (it) {
                alertDialogShow(
                    context!!,
                    getString(R.string.no_network_title),
                    getString(R.string.no_network_connection)
                )
            }
        })

        vm.crimeLocationError.observe(this, Observer {
            alertDialogShow(context!!, getString(it))
        })

        vm.clickEvents.observe(this, Observer {
            when (it) {
                ServiceClickEvents.CANCEL_BUTTON_CLICK -> {
                    dismiss()
                }
                ServiceClickEvents.ARREST_NOW_CLICK -> {
                    vm.arrestNow(
                        uploadAdapter.getUploadImageList(),
                        officerCode,
                        criminalDataModel.criminalsId.toString()
                    )
                }
            }
        })

        vm.arrestNowResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showCircularProgress()
                }
                Status.ERROR -> {
                    dismissCircularProgress()
                    var error = Utils.errorHandlingWithStatus(context!!, it.error)
                    alertDialogShow(context!!, error.message)
                    Log.e("jaspal", "Exception Error " + error)
                }
                Status.SUCCESS -> {
                    dismissCircularProgress()
                    var response = it.response
                    if (response.responseCode == "OPS10017" && response.responseMsg.equals(
                            "Success",
                            false
                        )
                    ) {
                        alertDialogShow(
                            context!!,
                            getString(R.string.alert),
                            response.responseValue.toString(),
                            DialogInterface.OnClickListener { dialog, which ->
                                dismiss()
                            })

                    } else {
                        alertDialogShow(context!!, response.responseValue.toString())
                    }
                }
            }
        })
    }

    override fun onAddMoreImageClick(uploadMultiPhotosAdapter: UploadMultiPhotosAdapter) {
        mediaPicker.showDialogFromFragment(this)

    }

    override fun onRemoveImageClick(position: Int) {
        uploadAdapter.removeImage(position)
    }

    override fun onImageViewClick(
        uploadMultiPhotosAdapter: UploadMultiPhotosAdapter,
        position: Int
    ) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!mediaPicker!!.handleOnActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onImagePicked(path: String, request: Int) {
        var file = File(path)
        var image = UploadImageModel(1, file.name, path)
        if (uploadAdapter.getImageListSize() <= 5) {
            uploadAdapter.addImage(image)
        } else {
            alertDialogShow(
                context!!,
                getString(R.string.info),
                getString(R.string.criminal_image_limit_msg)
            )
        }
    }

    override fun onError(message: String) {

    }

    companion object {
        @JvmStatic
        fun newInstance(data: String, officerCode: String) =
            ArrestNowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CRIMINAL_DATA, data)
                    putString(ARG_OFFICER_CODE, officerCode)
                }
            }
    }

    override fun onPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mediaPicker.resultListener.onGranted(PermissionUtils.CAMERA_PERMISSION)
                } else {
                    alertDialogShow(context!!, getString(R.string.camera_permission_msg))
                }
            }
            PermissionUtils.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mediaPicker.resultListener.onGranted(PermissionUtils.GALLARY_PERMISSION)
                } else {
                    alertDialogShow(context!!, getString(R.string.camera_permission_msg))
                }
            }
        }
    }
}