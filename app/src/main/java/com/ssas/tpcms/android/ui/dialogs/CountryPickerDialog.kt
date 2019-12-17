package com.ssas.tpcms.android.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.utils.countrycode.Country
import com.ssas.tpcms.android.utils.countrycode.CountryUtils
import kotlinx.android.synthetic.main.dialog_country_list.*


class CountryPickerDialog : DialogFragment() {
    private val ARG_TYPE="ARG_TYPE"
    private lateinit var adapter: CountryPickerAdapter
    private val arrCountryList = ArrayList<Country>()
    lateinit var mContext: Context
    lateinit var callback: CountrySelect
    lateinit var type:String

    fun setListener(callback: CountrySelect) {
        this.callback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.dialog_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            if(arguments!=null){
                arguments.let {
                    type= it?.getString(ARG_TYPE).toString()
                }
            }
        setUpRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrCountryList.clear()
        arrCountryList.addAll(CountryUtils.getAllCountries(mContext))
        adapter.setData(arrCountryList)
        setTextWatcher()
        setBackPress()
    }

    fun setBackPress(){
        setTitle()
        backIconBt.setOnClickListener {
            dismiss()
        }
    }

    fun setTitle(){
        if(type.equals(TYPE_COUNTRY_CODE)){
            toolbarTitle.setText(getString(R.string.select_country_code))
        }else{
            toolbarTitle.setText(getString(R.string.country_hint))
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(mContext)

        mRvCountry.layoutManager = layoutManager
        val itemDecorator = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.white_divider)!!)
        mRvCountry.addItemDecoration(itemDecorator)
        adapter = CountryPickerAdapter(mContext,type)
        adapter.setListener(callback)
        mRvCountry.adapter = adapter
    }

    private fun setTextWatcher() {
        if (txt_search == null) return
        txt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                adapter.getFilter1().filter(s.toString()) { count ->
                    if (count == 0) {
                        mTvNoResult.visibility = View.VISIBLE
                    } else {
                        mTvNoResult.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    interface CountrySelect {
        fun onCountryClick(country: Country)
    }

    companion object {
        val TYPE_COUNTRY_NAME="TYPE_COUNTRY_NAME"
        val TYPE_COUNTRY_CODE="TYPE_COUNTRY_CODE"
        @JvmStatic
        fun newInstance(type: String) =
            CountryPickerDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                }
            }
    }


}
