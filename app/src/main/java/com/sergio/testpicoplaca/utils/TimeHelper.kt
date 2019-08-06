package com.sergio.testpicoplaca.utils

import android.annotation.SuppressLint
import android.content.Context
import java.util.*
import android.text.format.DateUtils



class TimeHelper {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        val monh = arrayOf(
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )
        fun getStyleDate(dayOfMonth:Int,monthOfYear:Int, year:Int):String{
            return "${dayOfMonth}-${monh[monthOfYear]}-${year}"
        }

        fun inMiddle(currentHour:String, minHour:String, maxHour:String):Boolean{
            //val actual = "18:01:23"
            //val limit = "00:16:23"

            var parts = minHour.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val minH = Calendar.getInstance()
            minH.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]))
            minH.set(Calendar.MINUTE, Integer.parseInt(parts[1]))
            //cal1.set(Calendar.SECOND, Integer.parseInt(parts[2]))

            parts = maxHour.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val maxH = Calendar.getInstance()
            maxH.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]))
            maxH.set(Calendar.MINUTE, Integer.parseInt(parts[1]))
            //cal2.set(Calendar.SECOND, Integer.parseInt(parts[2]))

            parts = currentHour.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val current = Calendar.getInstance()
            current.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]))
            current.set(Calendar.MINUTE, Integer.parseInt(parts[1]))
            //cal2.set(Calendar.SECOND, Integer.parseInt(parts[2]))

            // Add 1 day because you mean 00:16:23 the next day
            // cal2.add(Calendar.DATE, 1)

            if (current.before(maxH) && current.after(minH)) {
                return true
            }
            return false
        }
        fun setContext(context:Context): Companion {
            this.context=context
            return this
        }
        fun formatDate(dateString:String):String{
            val parts = dateString.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val partsDate = parts[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (context!=null){
                val cal = GregorianCalendar(Integer.parseInt(partsDate[0]), Integer.parseInt(partsDate[1])-1, Integer.parseInt(partsDate[2]))
                var date = DateUtils.formatDateTime(context, cal.timeInMillis, DateUtils.FORMAT_SHOW_DATE)
                // date == "December 20"
                /*date = DateUtils.formatDateTime(
                    context,
                    cal.timeInMillis,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_YEAR
                )*/
                // date == "12/20/2013"
                /*
                * date = DateUtils.formatDateTime(
                    context,
                    cal.timeInMillis,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_TIME
                )*/
                // date == "00:00, 12/20/2013"
                return date
            }
            return ""
        }
        fun getDayOfWeak(year: Int, month: Int, day: Int): Int {
            val gregorianCalendar = GregorianCalendar(year, month, day - 1)
            return gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) // 1= Monday ... 7 = sunday
        }
    }
}