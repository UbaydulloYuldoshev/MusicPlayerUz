package uz.gita.musicplayeruz.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import uz.gita.musicplayeruz.R
import uz.gita.musicplayeruz.app.App
import uz.gita.musicplayeruz.data.MusicData
import java.io.File
import java.net.URI

class ForegroundService : Service(){

    private val scope = CoroutineScope(Dispatchers.IO )
    private var listener: (() -> Unit)? = null


    override fun onBind(intent: Intent?): IBinder? = null
    private val CHANNEL_ID = "My Music Player"
    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer get() = _mediaPlayer!!
    private  var data : MusicData? = null

    private val notification by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("My Music")
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(createRemoteView())
            .build()
    }

    private fun createRemoteView(): RemoteViews {
        val remoteView = RemoteViews(this.packageName, R.layout.notification_layout)
        remoteView.setOnClickPendingIntent(R.id.playButton, createPendingIntent(ActionEnum.PLAY))
        remoteView.setOnClickPendingIntent(R.id.pauseButton, createPendingIntent(ActionEnum.PAUSE))
        remoteView.setOnClickPendingIntent(R.id.nextButton, createPendingIntent(ActionEnum.NEXT))
        remoteView.setOnClickPendingIntent(R.id.closeButton, createPendingIntent(ActionEnum.CLOSE))
        remoteView.setOnClickPendingIntent(R.id.prevButton, createPendingIntent(ActionEnum.NEXT))
        return remoteView
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createPendingIntent(action: ActionEnum): PendingIntent {
        val intent = Intent(this, ForegroundService::class.java)
        intent.putExtra("data", action)
        intent.putExtra("music",data)
        return PendingIntent.getService(this,
            action.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate() {
        createChannel()

        startForeground(1, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(CHANNEL_ID,
                "My Music Player",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.extras?.getSerializable("data") as? ActionEnum
        data = intent?.extras?.getSerializable("music") as? MusicData

        if (_mediaPlayer != null) mediaPlayer.stop()
        _mediaPlayer = MediaPlayer.create(this, Uri.fromFile(File(data?.data)))  // Uri
        doneCommand(command)
        return START_NOT_STICKY
    }

    private fun doneCommand(command: ActionEnum?) {
        when (command) {
            ActionEnum.PAUSE -> {
                mediaPlayer.pause()
            }

            ActionEnum.PLAY -> {
                if( !mediaPlayer.isPlaying )
                mediaPlayer.start()
            }

            ActionEnum.CLOSE -> {
                mediaPlayer.stop()
                stopForeground(true)
//                EventBus.eventFinishBusLiveData.value = command

            }
            ActionEnum.NEXT -> {
                EventBus.eventBusLiveData.value = command
            }
            ActionEnum.PREV -> {
                EventBus.eventBusLiveData.value = command
            }
        }
    }

    override fun onDestroy() {
        _mediaPlayer = null
        scope.cancel()
    }
        fun setItemListener(f: () -> Unit) {
        listener = f
    }

}