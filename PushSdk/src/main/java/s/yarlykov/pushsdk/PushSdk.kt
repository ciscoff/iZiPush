package s.yarlykov.pushsdk

import android.content.Context

interface PushSdk {
    companion object {
        fun create(context: Context) : PushSdk {
            return PushSdkImpl(context)
        }
    }
    fun printHello(): Unit
}