package linusfessler.alarmtiles.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.databinding.FragmentSettingsBinding
import linusfessler.alarmtiles.shared.alarm.config.AlarmConfigFragment
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerConfigFragment

class SettingsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager
                .beginTransaction()
                .replace(R.id.sleep_timer_config_fragment_container, SleepTimerConfigFragment())
                .replace(R.id.alarm_config_fragment_container, AlarmConfigFragment())
                .commit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
}