package com.up.bc.myapplicationproject

import android.util.Log
import com.up.bc.myapplicationproject.Data.PetEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CalendarAlgorithmLibrary {

    fun load(arr: ArrayList<PetEvent>, year: Int, month: Int, day: Int): Map<String, java.util.ArrayList<PetEvent>> {

        val map = HashMap<String, ArrayList<PetEvent>>()

        arr.forEach {
            if (it.loop) {
                val a = Calendar.getInstance()

                a.set(Calendar.YEAR, year)
                a.set(Calendar.MONTH, month)
                a.set(Calendar.DAY_OF_MONTH, day)

                val staY = a.get(Calendar.YEAR)


                a.add(Calendar.YEAR, it.startYear)
                a.add(Calendar.MONTH, it.startMonth)
                a.add(Calendar.DAY_OF_YEAR, it.startDay)


                while (a.get(Calendar.YEAR) <= staY + 5) {

                    val c = a.time

                    val df = SimpleDateFormat("yyyy-MM-dd")
                    val formattedDate = df.format(c)

                    val z = map[formattedDate]
                    if (z == null) {
                        val b = ArrayList<PetEvent>()
                        b.add(it)
                        map[formattedDate] = b
                    } else {
                        z.add(it)
                        map[formattedDate] = z
                    }

                    a.add(Calendar.YEAR, it.loopYear)
                    a.add(Calendar.MONTH, it.loopMonth)
                    a.add(Calendar.DAY_OF_YEAR, it.loopDay)

                }

            } else {
                val a = Calendar.getInstance()

                a.set(Calendar.YEAR, year)
                a.set(Calendar.MONTH, month)
                a.set(Calendar.DAY_OF_MONTH, day)

                a.add(Calendar.YEAR, it.startYear)
                a.add(Calendar.MONTH, it.startMonth)
                a.add(Calendar.DAY_OF_YEAR, it.startDay)


                val c = a.time

                val df = SimpleDateFormat("yyyy-MM-dd")
                val formattedDate = df.format(c)

                val z = map[formattedDate]
                if (z == null) {
                    val b = ArrayList<PetEvent>()
                    b.add(it)
                    map[formattedDate] = b
                } else {
                    z.add(it)
                    map[formattedDate] = z
                }

            }

        }


        return sorting(map)

    }

    fun sorting(map :HashMap<String, ArrayList<PetEvent>>) : Map<String, java.util.ArrayList<PetEvent>> {

        val result = map.toList().sortedBy { (value, _) -> value}.toMap()
        return result
    }

    fun getStringFromMap(map :Map<String, java.util.ArrayList<PetEvent>>) :ArrayList<String>{
        val arrayList = ArrayList<String>()

        map.forEach {
            arrayList.add(it.key)

            //Log.e("Date",it.key)
        }

        return arrayList

    }

    fun getArrayEventFromMap(map :Map<String, java.util.ArrayList<PetEvent>>,string: String) :ArrayList<PetEvent>{
        //val arrayList = ArrayList<PetEvent>()


        return map[string]!!

    }

}