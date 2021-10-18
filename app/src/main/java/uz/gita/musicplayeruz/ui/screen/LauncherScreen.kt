package uz.gita.musicplayeruz.ui.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.musicplayeruz.R
import uz.gita.musicplayeruz.databinding.ScreenLauncherBinding

class LauncherScreen : Fragment(R.layout.screen_launcher) {
    private var _binding: ScreenLauncherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = ScreenLauncherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
//            findNavController().navigate(R.id.action_launcherScreen_to_mainScreen)
        }, 2500

        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
