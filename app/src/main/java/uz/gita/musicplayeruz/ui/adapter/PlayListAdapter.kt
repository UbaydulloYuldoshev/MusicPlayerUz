package uz.gita.musicplayeruz.ui.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.musicplayeruz.R
import uz.gita.musicplayeruz.app.App
import uz.gita.musicplayeruz.data.MusicData
import uz.gita.musicplayeruz.extensions.songArt

class PlayListAdapter constructor(var cursor : Cursor?=null): RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder>() {
    private var eventMusicListener : ((MusicData) -> Unit)?=null
    private var eventListener:((Int,MusicData)->Unit)? = null

    inner class PlayListViewHolder(view : View)  : RecyclerView.ViewHolder(view) {
        private val imageMusic = view.findViewById<ImageView>(R.id.imageMusic)
        private val textMusicName = view.findViewById<TextView>(R.id.textMusicName)
        private val textMusicDescription = view.findViewById<TextView>(R.id.textMusicDescription)

        init {
            itemView.setOnClickListener {
                eventMusicListener?.invoke(getMusicDataByPos(absoluteAdapterPosition))
            }
            itemView.setOnClickListener {
                eventListener?.invoke(absoluteAdapterPosition,getMusicDataByPos(absoluteAdapterPosition))
            }
        }

        fun bind() {
            val data = getMusicDataByPos(absoluteAdapterPosition)
            data.imageUri?.let {
                imageMusic.setImageURI(it)
            }
            textMusicName.text = data.title
            textMusicDescription.text = data.artist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        PlayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_music,parent,false))

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int =
        cursor?.count ?: 0

    private fun getMusicDataByPos(pos : Int) : MusicData{
        cursor!!.moveToPosition(pos)
        return MusicData(
            cursor!!.getInt(0),
            cursor!!.getString(1),
            cursor!!.getString(2),
            cursor!!.getString(3),
            cursor!!.getString(4),
            cursor!!.getLong(5),
            App.instance.songArt(cursor!!.getLong(6))
        )
    }

    fun setEventMusicListener (block : (MusicData) -> Unit) {
        eventMusicListener = block
    }
    fun setEventListener (block : (Int,MusicData) -> Unit) {
        eventListener = block
    }
}