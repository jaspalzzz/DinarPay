package com.ssas.tpcms.android.utils.countrycode

class Country(val iso: String, val phoneCode: String, val name: String) {

    /**
     * If country have query word in name or name code or phone code, this will return true.
     */
    internal fun isEligibleForQuery(query: String): Boolean {
        var query = query
        query = query.toLowerCase()
        return (name.toLowerCase().contains(query)
                || iso.toLowerCase().contains(query)
                || phoneCode.toLowerCase().contains(query))
    }
}