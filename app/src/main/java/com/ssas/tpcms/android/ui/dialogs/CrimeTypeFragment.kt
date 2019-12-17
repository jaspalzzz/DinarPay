package com.ssas.tpcms.android.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.base.ItemClickListener
import com.ssas.tpcms.android.data.models.crimetype.CrimeTypeResponse
import com.ssas.tpcms.android.databinding.FragmentCrimeTypeBinding
import com.ssas.tpcms.android.ui.service.adapter.CrimeTypeAdapter

/**
 * Created by Jaspal on 12/17/2019.
 */

const val ARG_CRIME_LIST = "ARG_CRIME_LIST"

class CrimeTypeFragment : BottomSheetDialogFragment(), ItemClickListener {

    lateinit var binding: FragmentCrimeTypeBinding
    var crimeTypeList: ArrayList<CrimeTypeResponse> = ArrayList()
    lateinit var crimeTypeAdapter: CrimeTypeAdapter
    lateinit var listner: CrimeTypeItemListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crime_type, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            crimeTypeList = it.getSerializable(ARG_CRIME_LIST) as ArrayList<CrimeTypeResponse>
        }
        inflateCrimeTypeList(crimeTypeList)
    }

    private fun inflateCrimeTypeList(crimeTypeList: ArrayList<CrimeTypeResponse>) {
        var layoutManager = LinearLayoutManager(context)
        binding.crimeTypeList.layoutManager = layoutManager
        val itemDecorator =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.white_divider
                )
            }!!
        )
        binding.crimeTypeList.addItemDecoration(itemDecorator)
        crimeTypeAdapter = CrimeTypeAdapter(crimeTypeList)
        binding.crimeTypeList.adapter = crimeTypeAdapter
        crimeTypeAdapter.setItemClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(data: ArrayList<CrimeTypeResponse>) =
            CrimeTypeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CRIME_LIST, data)
                }
            }
    }

    override fun onItemClick(adapter: Any?, position: Int) {
        listner.selectedCrimeType(crimeTypeAdapter.getitem(position))
        dismiss()
    }

    fun setItemListener(owner: CrimeTypeItemListener) {
        if (owner == null) {
            return
        }
        listner = owner
    }

    interface CrimeTypeItemListener {
        fun selectedCrimeType(item: CrimeTypeResponse)
    }
}