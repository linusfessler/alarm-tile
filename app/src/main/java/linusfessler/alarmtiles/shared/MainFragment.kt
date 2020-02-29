package linusfessler.alarmtiles.shared

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.databinding.FragmentMainBinding
import linusfessler.alarmtiles.tiles.alarm.AlarmTileFragment
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerConfigFragment
import linusfessler.alarmtiles.tiles.sleeptimer.SleepTimerTileFragment
import linusfessler.alarmtiles.tiles.stopwatch.StopwatchTileFragment
import linusfessler.alarmtiles.tiles.timer.TimerTileFragment


class MainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager
                .beginTransaction()
                .replace(R.id.sleep_timer_fragment_container, SleepTimerTileFragment())
                .replace(R.id.sleep_timer_config_fragment_container, SleepTimerConfigFragment())
                .replace(R.id.alarm_fragment_container, AlarmTileFragment())
                .replace(R.id.timer_fragment_container, TimerTileFragment())
                .replace(R.id.stopwatch_fragment_container, StopwatchTileFragment())
                .commit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_open_source_licenses -> {
                    OssLicensesMenuActivity.setActivityTitle(getString(R.string.menu_open_source_licenses));
                    startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
            }

            false
        }

        return binding.root
    }
}