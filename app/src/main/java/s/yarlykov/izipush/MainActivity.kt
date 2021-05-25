package s.yarlykov.izipush

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import s.yarlykov.pushsdk.PushSdk
import s.yarlykov.pushsdk.utils.logIt
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var fabEx: ExtendedFloatingActionButton
    private lateinit var animator: FabAnimationHelper

    private val pushSdk = PushSdk.create(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        fabEx = findViewById(R.id.fabEx)

        animator = FabAnimationHelper(fabEx, R.drawable.ic_send, R.drawable.ic_motion)

        lifecycleScope.launch {
            logIt("FCM token: ${fetchToken()}")
            logIt("App ID: ${fetchApiId()}")
        }
        rotateFab()

        fabEx.setOnClickListener {
            lifecycleScope.launch {
                animator.shrinkAndRotate()
//                delay(5000)
//                animator.cancelImmediately()
            }
        }

        fab.setOnClickListener {
            animator.cancelImmediately()
        }
    }

    private fun rotateFab() {
        fab.animation?.apply {
            cancel()
            reset()
        }

        fab.rotation = 0f
        fab.animate()
            .rotation(360f)
            .setDuration(240)
            .withEndAction(::rotateFab)
            .start()
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