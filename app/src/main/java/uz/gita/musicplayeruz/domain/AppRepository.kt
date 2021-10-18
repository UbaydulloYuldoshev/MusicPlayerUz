package uz.gita.musicplayeruz.domain

import android.database.Cursor
import kotlinx.coroutines.flow.Flow
import uz.gita.musicplayeruz.app.App
import uz.gita.musicplayeruz.extensions.getMusicListCursor
import javax.inject.Inject

class AppRepository @Inject constructor() {

    fun loadMusics() : Flow<Cursor> = App.instance.getMusicListCursor()
}