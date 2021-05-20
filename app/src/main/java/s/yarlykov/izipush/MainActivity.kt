package s.yarlykov.izipush

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import s.yarlykov.pushsdk.utils.logIt


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showToken()
        showAppId()
    }

    private fun showAppId() {
        val task: Task<String> = FirebaseInstallations.getInstance().id

        task.addOnSuccessListener { appId ->
            logIt(" Firebase app installation: $appId")
        }
    }

    private fun showToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                logIt("token: $token")
            })
    }


}