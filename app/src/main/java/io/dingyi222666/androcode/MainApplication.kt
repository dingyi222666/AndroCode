package io.dingyi222666.androcode

import android.app.Application
import io.dingyi222666.androcode.api.AndroCodeContext

class MainApplication : Application() {

    lateinit var androCode: AndroCodeContext
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun initIDEContext() {
        androCode = AndroCodeContext()
    }


    companion object {
        lateinit var instance: MainApplication
    }

}