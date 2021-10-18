package uz.gita.musicplayeruz.service

import androidx.lifecycle.MutableLiveData
import uz.gita.musicplayeruz.data.MusicData

object EventBus {
    val eventBusLiveData = MutableLiveData<ActionEnum>()
    val eventFinishBusLiveData = MutableLiveData<ActionEnum>()
    val eventStartActivityBusLiveData = MutableLiveData<ActionEnum>()

    val setSelectListener : ((MusicData) -> Unit)? = null
}