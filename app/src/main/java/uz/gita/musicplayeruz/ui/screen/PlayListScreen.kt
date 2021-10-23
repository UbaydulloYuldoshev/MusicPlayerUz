package uz.gita.musicplayeruz.ui.screen

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.musicplayeruz.R
import uz.gita.musicplayeruz.databinding.ScreenPlayListBinding
import uz.gita.musicplayeruz.extensions.checkPermissions
import uz.gita.musicplayeruz.extensions.scope
import uz.gita.musicplayeruz.service.ActionEnum
import uz.gita.musicplayeruz.service.EventBus
import uz.gita.musicplayeruz.service.ForegroundService
import uz.gita.musicplayeruz.ui.adapter.PlayListAdapter
import uz.gita.musicplayeruz.viewmodel.PlayListViewModel


@AndroidEntryPoint
class PlayListScreen : Fragment(R.layout.screen_play_list) {
    private val binding by viewBinding(ScreenPlayListBinding::bind)
    private val viewModel by viewModels<PlayListViewModel>()
    private val adapter = PlayListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        playList.layoutManager = LinearLayoutManager(requireContext())
        playList.adapter = adapter


        adapter.setEventListener {pos,data ->
            val intent = Intent(requireContext(), ForegroundService::class.java)
            EventBus.data = data
            val bundle = Bundle()
            bundle.putSerializable("data", ActionEnum.PLAY)
            intent.putExtras(bundle)
            EventBus.position = pos
            if (Build.VERSION.SDK_INT >= 26) {
                requireActivity().startForegroundService(intent)
            } else requireActivity().startService(intent)
        }


        viewModel.playListLiveData.observe(viewLifecycleOwner, playListObserver)
        requireActivity().checkPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            viewModel.loadMusics()
        }
    }
    private val playListObserver = Observer<Cursor> {
        adapter.cursor = it
        adapter.notifyDataSetChanged()
    }
}