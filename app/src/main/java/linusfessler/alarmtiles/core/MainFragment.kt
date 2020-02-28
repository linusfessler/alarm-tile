package linusfessler.alarmtiles.core

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.alarm.AlarmFragment
import linusfessler.alarmtiles.databinding.FragmentMainBinding
import linusfessler.alarmtiles.sleeptimer.SleepTimerConfigFragment
import linusfessler.alarmtiles.sleeptimer.SleepTimerFragment
import linusfessler.alarmtiles.stopwatch.StopwatchFragment
import linusfessler.alarmtiles.timer.TimerFragment


class MainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager
                .beginTransaction()
                .replace(R.id.sleep_timer_fragment_container, SleepTimerFragment())
                .replace(R.id.sleep_timer_config_fragment_container, SleepTimerConfigFragment())
                .replace(R.id.alarm_fragment_container, AlarmFragment())
                .replace(R.id.timer_fragment_container, TimerFragment())
                .replace(R.id.stopwatch_fragment_container, StopwatchFragment())
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