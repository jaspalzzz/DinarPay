package com.ssas.tpcms.android.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

class ContextWrapper(base: Context?) : android.content.ContextWrapper(base) {
    companion object {
        fun wrap(
            context: Context,
            language: String
        ): ContextWrapper {
            var context = context
            val res = context.resources
            val configuration = res.configuration

            val newLocale = Locale(language)
            Locale.setDefault(newLocale)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                configuration.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
                context = context.createConfigurationContext(configuration)
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(newLocale)
                context = context.createConfigurationContext(configuration)
            } else {
                configuration.locale = newLocale
                res.updateConfiguration(configuration, res.displayMetrics)
            }

            return ContextWrapper(context)
        }
    }
}