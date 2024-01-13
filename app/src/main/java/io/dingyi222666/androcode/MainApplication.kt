package io.dingyi222666.androcode

import android.app.Application
import io.dingyi222666.androcode.api.AndroCodeContext

class MainApplication : Application() {

    lateinit var ctx: AndroCodeContext
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun initIDEContext() {
        ctx = AndroCodeContext()
    }


    companion object {
        lateinit var instance: MainApplication
    }

}