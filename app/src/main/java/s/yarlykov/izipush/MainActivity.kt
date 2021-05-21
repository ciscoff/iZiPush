package s.yarlykov.izipush

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import s.yarlykov.pushsdk.utils.logIt
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv : TextView = TextView(this)
        tv.animation.reset()

        lifecycleScope.launch {
            logIt("FCM token: ${fetchToken()}")
            logIt("App ID: ${fetchApiId()}")
        }
    }

    private suspend fun fetchApiId(): String = suspendCoroutine { cont ->
        val task: Task<String> = FirebaseInstallations.getInstance().id
        task.addOnSuccessListener { appId -> cont.resume(appId) }
    }

    private suspend fun fetchToken(): String = suspendCoroutine { cont ->

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    cont.resumeWithException(Throwable("Something's wrong"))
                }

                task.result?.let(cont::resume) ?: run {
                    cont.resumeWithException(Throwable("Token is null"))
                }
            }
    }
}