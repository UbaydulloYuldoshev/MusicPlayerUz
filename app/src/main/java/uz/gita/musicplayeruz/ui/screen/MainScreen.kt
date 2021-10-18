package uz.gita.musicplayeruz.ui.screen

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import uz.gita.musicplayeruz.R
import uz.gita.musicplayeruz.databinding.ScreenMainBinding
import uz.gita.musicplayeruz.service.ActionEnum
import uz.gita.musicplayeruz.service.EventBus
import uz.gita.musicplayeruz.service.ForegroundService

class MainScreen : Fragment(R.layout.screen_main) {

    private var _viewBinding: ScreenMainBinding? = null
    private val viewBinding get() = _viewBinding!!
    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer get() = _mediaPlayer!!
    private val service = ForegroundService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _viewBinding = ScreenMainBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val intent = Intent(requireContext(), ForegroundService::class.java)
        _mediaPlayer = MediaPlayer.create(requireContext(), R.raw.music)

        viewBinding.startService.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("data", ActionEnum.PLAY)
            intent.putExtras(bundle)
            if (Build.VERSION.SDK_INT >= 26) {
                requireContext().startForegroundService(intent)
            } else
                requireContext().startService(intent)
        }

        service.setItemListener {
            requireActivity().finish()
        }

        viewBinding.stopService.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("data", ActionEnum.PAUSE)
            intent.putExtras(bundle)
            if (Build.VERSION.SDK_INT >= 26) {
                requireContext().startForegroundService(intent)
            } else
                requireContext().startService(intent)
        }

        EventBus.eventBusLiveData.observe(requireActivity(),
            Observer<ActionEnum> {
                Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
            })
        EventBus.eventFinishBusLiveData.observe(requireActivity(),
            Observer<ActionEnum> {
                requireActivity().finish()
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _mediaPlayer = null
        _viewBinding = null
    }
}