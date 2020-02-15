package linusfessler.alarmtiles.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import linusfessler.alarmtiles.R;
import linusfessler.alarmtiles.alarm.AlarmFragment;
import linusfessler.alarmtiles.databinding.FragmentMainBinding;
import linusfessler.alarmtiles.sleeptimer.SleepTimerConfigFragment;
import linusfessler.alarmtiles.sleeptimer.SleepTimerFragment;
import linusfessler.alarmtiles.stopwatch.StopwatchFragment;
import linusfessler.alarmtiles.timer.TimerFragment;

public class MainFragment extends Fragment {

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.sleep_timer_fragment_container, new SleepTimerFragment())
                .replace(R.id.sleep_timer_config_fragment_container, new SleepTimerConfigFragment())
                .replace(R.id.alarm_fragment_container, new AlarmFragment())
                .replace(R.id.timer_fragment_container, new TimerFragment())
                .replace(R.id.stopwatch_fragment_container, new StopwatchFragment())
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final FragmentMainBinding binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
