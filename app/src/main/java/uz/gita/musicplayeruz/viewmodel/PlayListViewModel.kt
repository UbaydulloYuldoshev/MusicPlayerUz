package uz.gita.musicplayeruz.viewmodel

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.musicplayeruz.domain.AppRepository
import javax.inject.Inject


@HiltViewModel
class PlayListViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    private val _playListLiveData = MutableLiveData<Cursor>()
    val playListLiveData: LiveData<Cursor> get() = _playListLiveData


    fun loadMusics() {
        repository.loadMusics().onEach {
            _playListLiveData.value = it
        }.launchIn(viewModelScope)
    }
}

