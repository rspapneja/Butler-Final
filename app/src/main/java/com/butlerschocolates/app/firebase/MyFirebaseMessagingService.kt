package com.butlerschocolates.app.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.butlerschocolates.app.R
import com.butlerschocolates.app.activities.MainActivity
import com.butlerschocolates.app.util.Console
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "Tag MyFirebaseMessagingService"
    var notificationBuilder: NotificationCompat.Builder?=null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Console.Log(TAG, "From: " + remoteMessage!!.from)
        val data = remoteMessage.data

        Console.Log(TAG, "Message_Notification_Body_Map: " + data.toString())

        sendNotification(remoteMessage.data["title"],remoteMessage.data["body"],JSONObject(remoteMessage.data["extra"]))
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *  @param messageBody FCM message body received.
     */

     private fun sendNotification(
        title: String?,
        body: String?,
        jsonObject: JSONObject
    ) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("ityp",jsonObject.getString("ityp"))

        if(jsonObject.getString("ityp").equals("order"))
        {
            intent.putExtra("oid",jsonObject.getString("oid"))
        }
        else if(jsonObject.getString("ityp").equals("support")) {
            intent.putExtra("tid",jsonObject.getString("tid"))
        }
        else if(jsonObject.getString("ityp").equals("store")) {
            intent.putExtra("id",jsonObject.getString("id"))
            intent.putExtra("title",title)
            intent.putExtra("body",body)
         }
        else if(jsonObject.getString("ityp").equals("app_store")) {
            intent.putExtra("id",jsonObject.getString("id"))
            intent.putExtra("title",title)
            intent.putExtra("body",body)

        }
        else if(jsonObject.getString("ityp").equals("general")) {
            intent.putExtra("id",jsonObject.getString("id"))
            intent.putExtra("title",title)
            intent.putExtra("body",body)
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder!!.setSmallIcon(R.drawable.ic_logo)
        notificationBuilder!!.setContentTitle(title)
        notificationBuilder!!.setContentText(body)
        notificationBuilder!!.setAutoCancel(true)
        notificationBuilder!!.setSound(defaultSoundUri)
        notificationBuilder!!.setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      notificationBuilder?.setChannelId(getString(R.string.default_notification_channel_id))

       val channel = NotificationChannel(getString(R.string.default_notification_channel_id),
          getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder!!.build())
    }

    object Events {
        val serviceEvent: MutableLiveData<String> by lazy {
            MutableLiveData<String>()
        }
    }
}