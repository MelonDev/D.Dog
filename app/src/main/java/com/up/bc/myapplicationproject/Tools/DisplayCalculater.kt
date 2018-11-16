package th.ac.up.se.thaicardgame.Tools

import android.content.Context

class DisplayCalculater(var context: Context){

    fun getDpi() : Int{
        val metrics = context.resources.displayMetrics
        val densityDpi = (metrics.density * 160f).toInt()
        return densityDpi
    }

}