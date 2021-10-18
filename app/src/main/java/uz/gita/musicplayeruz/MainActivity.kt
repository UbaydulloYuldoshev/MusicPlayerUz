package uz.gita.musicplayeruz

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main)
/*
            val intent = Intent(this, ForegroundService::class.java)

        findViewById<Button>(R.id.startService).setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("data", ActionEnum.PLAY)
            intent.putExtras(bundle)
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(intent)
            } else startService(intent)
        }

        findViewById<Button>(R.id.stopService).setOnClickListener {
            stopService(intent)

        }

        EventBus.eventBusLiveData.observe(this, androidx.lifecycle.Observer<ActionEnum> {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
        })*/