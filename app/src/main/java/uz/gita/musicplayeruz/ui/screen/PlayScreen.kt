package uz.gita.musicplayeruz.ui.screen

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.gita.musicplayeruz.R
import uz.gita.musicplayeruz.data.MusicData
import uz.gita.musicplayeruz.databinding.ScreenPlayBinding
import uz.gita.musicplayeruz.extensions.showToast
import java.io.File

class PlayScreen : Fragment(R.layout.screen_play) {
    private var _viewBinding: ScreenPlayBinding? = null
    private val viewBinding get() = _viewBinding!!
    private lateinit var data: MusicData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _viewBinding = ScreenPlayBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            data = it.getSerializable("music") as MusicData
            viewBinding.tvSongName.text = data.title
            viewBinding.seekBar.progress = data.duration?.toInt()!!
        }
        viewBinding.ivPlayPauseDetail.setOnClickListener {


//            val mediaPlayer =
//                MediaPlayer.create(requireContext(), Uri.fromFile(File(data.imageUri ?: "")))
//            mediaPlayer.setOnCompletionListener {
//                showToast("Finish")
//            }
//            mediaPlayer.start()
        }
    }
}