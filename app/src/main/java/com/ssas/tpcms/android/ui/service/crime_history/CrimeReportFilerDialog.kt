package com.ssas.tpcms.android.ui.service.crime_history

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ssas.dinarpay.android.network.Status
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.data.models.crimetype.CrimeTypeResponse
import com.ssas.tpcms.android.databinding.FragmentFilterCrimeReportBinding
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.dialogs.CrimeTypeFragment
import com.ssas.tpcms.android.utils.Utils

/**
 * Created by Jaspal on 12/18/2019.
 */
class CrimeReportFilerDialog :
    BaseBottomSheetFragment<FragmentFilterCrimeReportBinding, ServiceVM>() {
    private var crimeTypeList: ArrayList<CrimeTypeResponse> = ArrayList()
    private var crimeTypeId: String = ""
    private lateinit var listner: FilterCallbackListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_filter_crime_report, ServiceVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.crimeTypeEt.setOnClickListener {
            if (crimeTypeList.isEmpty()) {
                viewModel.getCrimeTypes()
            } else {
                showCrimeTyeDialog()
            }
        }
        binding.submitButton.setOnClickListener {
            listner.onFilterSubmit(
                binding.cimeNameEt.text.toString().trim(),
                binding.crimeLocationEt.text.toString().trim(),
                binding.crimeCityEt.text.toString().trim(),
                crimeTypeId
            )
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun showCrimeTyeDialog() {
        var crimeTypeDialog = CrimeTypeFragment.newInstance(crimeTypeList)
        crimeTypeDialog.setItemListener(object :
            CrimeTypeFragment.CrimeTypeItemListener {
            override fun selectedCrimeType(item: CrimeTypeResponse) {
                binding.crimeTypeEt.setText(item.crimenameEn)
                crimeTypeId = item.crimeTypeId.toString()

            }
        })
        crimeTypeDialog.show(fragmentManager, crimeTypeDialog.tag)
    }

    override fun subscribeToEvents(vm: ServiceVM) {
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
                            context!!,
                            getString(R.string.alert),
                            getString(R.string.exception_msg)
                        )
                    }
                }
                Status.ERROR -> {
                    dismissCircularProgress()
                    var error = Utils.errorHandlingWithStatus(context!!, it.error)
                    alertDialogShow(context!!, error.message)
                }
            }
        })
    }

    fun setCallBackListener(owner: FilterCallbackListener) {
        if (owner == null) {
            return
        }
        listner = owner
    }

    interface FilterCallbackListener {
        fun onFilterSubmit(
            crimeName: String,
            crimeLocation: String,
            crimeCity: String,
            crimeTypeId: String
        )
    }
}