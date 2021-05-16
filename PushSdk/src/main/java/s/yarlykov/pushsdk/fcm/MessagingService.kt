package s.yarlykov.pushsdk.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import s.yarlykov.pushsdk.utils.logIt
import java.lang.Exception

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        logIt("onMessageReceived")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        logIt("onNewToken: $token")
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
    }

    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
    }
}