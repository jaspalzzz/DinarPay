package com.ssas.tpcms.android.ui.service.crime_database

import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.data.models.criminal_record.CrimnalProfileRecordModel
import com.ssas.tpcms.android.databinding.FragmentCrimnalCardBinding
import com.ssas.tpcms.android.repo.service.ServiceClickEvents
import com.ssas.tpcms.android.repo.service.ServiceVM

/**
 * Created by Jaspal on 12/12/2019.
 */

private const val ARG_CRIMINAL_DATA = "ARG_CRIMINAL_DATA"
private const val ARG_OFFICER_CODE = "ARG_OFFICER_CODE"

class CrimnalCardFragment : BaseBottomSheetFragment<FragmentCrimnalCardBinding, ServiceVM>() {
    private var criminalData: String = ""
    private var officerCode: String = ""
    lateinit var criminalDataModel: CrimnalProfileRecordModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        arguments?.let {
            officerCode = it.getString(ARG_OFFICER_CODE).toString()
            criminalData = it.getString(ARG_CRIMINAL_DATA).toString()
        }
    }

    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_crimnal_card, ServiceVM::class.java)

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
        }
    }


    override fun subscribeToEvents(vm: ServiceVM) {
        binding.vm = vm
        vm.clickEvents.observe(this, Observer {
            when (it) {
                ServiceClickEvents.CANCEL_BUTTON_CLICK -> {
                    dismiss()
                }

                ServiceClickEvents.ARREST_NOW_CLICK -> {
                    moveArresNowDialog()
                }
            }
        })
    }

    private fun moveArresNowDialog() {
        var arrestDialog = ArrestNowFragment.newInstance(criminalData, officerCode)
        arrestDialog.show(fragmentManager, arrestDialog.tag)
    }

    companion object {
        @JvmStatic
        fun newInstance(data: String, officerCode: String) =
            CrimnalCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CRIMINAL_DATA, data)
                    putString(ARG_OFFICER_CODE, officerCode)
                }
            }
    }
}