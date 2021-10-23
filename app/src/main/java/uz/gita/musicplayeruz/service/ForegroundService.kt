package uz.gita.musicplayeruz.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.gita.musicplayeruz.R
import uz.gita.musicplayeruz.app.App
import uz.gita.musicplayeruz.data.MusicData
import uz.gita.musicplayeruz.extensions.getMusicListCursor
import uz.gita.musicplayeruz.extensions.songArt
import java.io.File

class ForegroundService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private var listener: (() -> Unit)? = null


    override fun onBind(intent: Intent?): IBinder? = null
    private val CHANNEL_ID = "My Music Player"
    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer get() = _mediaPlayer!!
    private var data: MusicData? = null
    private lateinit var remoteView: RemoteViews
    private lateinit var cursor: Cursor
    private var isPause = true


    private val notification by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("My Music")
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setSilent(true)
            .setCustomContentView(createRemoteView())
            .build()
    }

    private fun createRemoteView(): RemoteViews {
        remoteView = RemoteViews(this.packageName, R.layout.notification_layout)
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
        return PendingIntent.getService(this,
            action.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate() {
        _mediaPlayer = mediaPlayer
        controlMusic()
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
        createChannel()
        startForeground(1, notification)
        val command = intent?.extras?.getSerializable("data") as? ActionEnum
        doneCommand(command)
        return START_NOT_STICKY
    }

    private fun doneCommand(command: ActionEnum?) {
        when (command) {

            ActionEnum.PLAY -> {
                if (mediaPlayer.isPlaying) mediaPlayer.stop()
                _mediaPlayer = MediaPlayer.create(this, Uri.fromFile(File(EventBus.data.data)))
                mediaPlayer.start()
                remoteView.setImageViewResource(R.id.pauseButton, R.drawable.ic_pause)
                isPause = true
            }

            ActionEnum.PAUSE -> {
                isPause = if (isPause) {
                    remoteView.setImageViewResource(R.id.pauseButton, R.drawable.ic_play)
                    mediaPlayer.pause()
                    EventBus.isPlaying = false
                    false
                } else {
                    remoteView.setImageViewResource(R.id.pauseButton, R.drawable.ic_pause)
                    mediaPlayer.start()
                    EventBus.isPlaying = true
                    true
                }
            }

            ActionEnum.CLOSE -> {
                mediaPlayer.stop()
                stopForeground(true)
//                EventBus.eventFinishBusLiveData.value = command

            }
            ActionEnum.NEXT -> {
                EventBus.position += 1
                control(EventBus.position)
                if (mediaPlayer.isPlaying)
                    mediaPlayer.stop()
                _mediaPlayer =
                    MediaPlayer.create(App.instance, Uri.fromFile(File(EventBus.data.data)))
            }
            ActionEnum.PREV -> {
                EventBus.eventBusLiveData.value = command
            }
        }
    }

    private fun controlMusic() {
        scope.launch(Dispatchers.Default) {
            this@ForegroundService.getMusicListCursor().collect {
                cursor = it
            }
        }
    }

    private fun control(pos: Int) {
        cursor.moveToPosition(pos)
        EventBus.data = MusicData(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getLong(5),
            App.instance.songArt(cursor.getLong(6))
        )
    }

    private fun loadMusics(): Flow<Cursor> = App.instance.getMusicListCursor()
    override fun onDestroy() {
        _mediaPlayer = null
        scope.cancel()
    }

    fun setItemListener(f: () -> Unit) {
        listener = f
    }

}