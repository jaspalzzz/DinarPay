package com.ssas.tpcms.android.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.tpcms.android.R
import com.ssas.tpcms.android.databinding.ItemCountryBinding
import com.ssas.tpcms.android.utils.countrycode.Country
import java.util.*


class CountryPickerAdapter(internal var context: Context,var type: String) :
    RecyclerView.Adapter<CountryPickerAdapter.Holder>() {

    var filteredData: ArrayList<Country> = ArrayList()
    var response: ArrayList<Country> = ArrayList()
    private val mFilter = ItemFilter()

    lateinit var callback: CountryPickerDialog.CountrySelect

    fun setListener(callback: CountryPickerDialog.CountrySelect) {
        this.callback = callback
    }

    fun setData(response: ArrayList<Country>?) {
        this.filteredData.addAll(response!!)
        this.response.addAll(response)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemCountryBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_country, p0, false)
        return Holder(
            binding
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.isCountryView = type.equals(CountryPickerDialog.TYPE_COUNTRY_NAME)
        holder.binding.data = filteredData[position]
        holder.binding.callBack = callback
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    fun clearData() {
        filteredData.clear()
        notifyDataSetChanged()
    }

    class Holder(var binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root)

    fun getFilter1(): ItemFilter {
        return mFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterString = constraint.toString().toLowerCase()
            val results = FilterResults()
            val list = response
            val count = list.size
            val nlist = ArrayList<Country>(count)
            var filterableString: Country

            for (i in 0 until count) {
                filterableString = list.get(i)
                if (filterableString.name.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString)
                }
            }
            results.values = nlist
            results.count = nlist.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filteredData = results.values as ArrayList<Country>
            notifyDataSetChanged()
        }

    }
}