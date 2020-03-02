package linusfessler.alarmtiles.shared.alarm.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.databinding.FragmentAlarmConfigBinding
import linusfessler.alarmtiles.shared.App
import javax.inject.Inject

class AlarmConfigFragment : Fragment() {
    @Inject
    lateinit var viewModel: AlarmConfigViewModel

    private lateinit var binding: FragmentAlarmConfigBinding

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App)
                .sharedComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAlarmConfigBinding.inflate(inflater, container, false)

        viewModel.config.apply {
            binding.vibrate.isChecked = this.vibrate
            binding.flashlight.isChecked = this.flashlight
            binding.snoozeDuration.time = this.snoozeDuration.time
            binding.snoozeDuration.timeUnit = this.snoozeDuration.timeUnit
        }

        binding.vibrate.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setVibrate(isChecked)
        }

        binding.flashlight.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setFlashlight(isChecked)
        }

        disposable.add(binding.snoozeDuration.timeObservable
                .subscribe {
                    viewModel.setSnoozeTime(it)
                })

        disposable.add(binding.snoozeDuration.timeUnitObservable
                .subscribe {
                    viewModel.setSnoozeTimeUnit(it)
                })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}