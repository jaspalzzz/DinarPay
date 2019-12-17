package com.ssas.tpcms.android.ui.service.crime_database

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.data.models.UploadImageModel
import com.ssas.tpcms.android.databinding.FragmentArrestNowBinding
import com.ssas.tpcms.android.repo.service.ServiceClickEvents
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.service.adapter.UploadMultiPhotosAdapter
import com.ssas.tpcms.android.utils.PermissionResultListener
import com.ssas.tpcms.android.utils.PermissionUtils
import com.ssas.tpcms.android.utils.files.PickMediaHelper
import java.io.File

/**
 * Created by Jaspal on 12/13/2019.
 */
class ArrestNowFragment : BaseBottomSheetFragment<FragmentArrestNowBinding, ServiceVM>(),
    UploadMultiPhotosAdapter.ImageItemClickListener, PickMediaHelper.Callback,
    PermissionResultListener {

    lateinit var uploadAdapter: UploadMultiPhotosAdapter
    lateinit var mediaPicker: PickMediaHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_arrest_now, ServiceVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        vm.clickEvents.observe(this, Observer {
            when (it) {
                ServiceClickEvents.CANCEL_BUTTON_CLICK -> {
                    dismiss()
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
        uploadAdapter.addImage(image)
    }

    override fun onError(message: String) {

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