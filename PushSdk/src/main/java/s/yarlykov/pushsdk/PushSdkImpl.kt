package s.yarlykov.pushsdk

import android.content.Context
import s.yarlykov.pushsdk.utils.logIt

class PushSdkImpl(val context: Context) : PushSdk {
    override fun printHello() {
        logIt("Hello")
    }
}