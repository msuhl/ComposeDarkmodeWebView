package dk.isten.andro.mywebviewapplication

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log

class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d("Svend isNightMode on ", "${isNightMode(this)}")
    }
    fun isNightMode(app: Application): Boolean {
        return when(app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}