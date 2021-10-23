package uz.gita.musicplayeruz.service

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import uz.gita.musicplayeruz.SingleLiveEvent
import uz.gita.musicplayeruz.data.MusicData

object EventBus {
    val eventBusLiveData = MutableLiveData<ActionEnum>()
    val eventFinishBusLiveData = SingleLiveEvent<ActionEnum>()

    lateinit var  currentMusic : MusicData
    lateinit var  media : MediaPlayer

    var currentPosition  = 0
    var position = 0
    lateinit var data: MusicData
    var isPlaying = true

}