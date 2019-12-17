package com.ssas.tpcms.android.ui.service.crime_history

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.BaseBottomSheetFragment
import com.ssas.tpcms.android.data.models.crime_report.CrimeReportDataItem
import com.ssas.tpcms.android.data.models.criminal_record.ImageModel
import com.ssas.tpcms.android.databinding.FragmentCrimeReportCardBinding
import com.ssas.tpcms.android.repo.service.ServiceClickEvents
import com.ssas.tpcms.android.repo.service.ServiceVM
import com.ssas.tpcms.android.ui.service.adapter.CasePhotosAdapter

/**
 * Created by Jaspal on 12/12/2019.
 */

private const val ARG_CRIME_REPORT = "ARG_CRIME_REPORT"

class CrimeReportCardFragment :
    BaseBottomSheetFragment<FragmentCrimeReportCardBinding, ServiceVM>() {

    lateinit var photosAdapter: CasePhotosAdapter

    private var crimeReportData: String = ""
    lateinit var crimeReportDataModel: CrimeReportDataItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

        arguments?.let {
            crimeReportData = it.getString(ARG_CRIME_REPORT).toString()
        }
    }


    override val fragmentBinding: FragmentBinding
        get() = FragmentBinding(R.layout.fragment_crime_report_card, ServiceVM::class.java)

    override fun onCreateViewFragment(savedInstanceState: Bundle?) {

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!crimeReportData.isEmpty()) {
            crimeReportDataModel = Gson().fromJson<CrimeReportDataItem>(
                crimeReportData,
                CrimeReportDataItem::class.java
            )
            if (!crimeReportDataModel.imageList?.isNullOrEmpty()!!) {
                inflateCasePhotosList(crimeReportDataModel.imageList)
            }
            binding.item = crimeReportDataModel
        }

    }

    private fun inflateCasePhotosList(imageList: ArrayList<ImageModel>?) {
        if (imageList == null)
            return
        photosAdapter = CasePhotosAdapter(imageList)
        createHorizontalList(binding.casePhotosList).adapter = photosAdapter
    }

    private fun createHorizontalList(recyclerView: RecyclerView): RecyclerView {
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        return recyclerView
    }

    companion object {
        @JvmStatic
        fun newInstance(data: String) =
            CrimeReportCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CRIME_REPORT, data)
                }
            }
    }

}