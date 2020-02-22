package linusfessler.alarmtiles.sleeptimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.core.App
import linusfessler.alarmtiles.databinding.FragmentSleepTimerConfigBinding
import javax.inject.Inject

class SleepTimerConfigFragment : Fragment() {
    @Inject
    lateinit var viewModel: SleepTimerViewModel

    private lateinit var binding: FragmentSleepTimerConfigBinding

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App)
                .sleepTimerComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSleepTimerConfigBinding.inflate(inflater, container, false)

        // Initialize view, afterwards it will always match the user input
        disposable.add(viewModel.sleepTimer
                .firstElement()
                .subscribe {
                    binding.decreaseVolume.isChecked = it.isDecreasingVolume
                    binding.decreaseVolume.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.dispatch(SleepTimerEvent.SetDecreasingVolume(isChecked))
                    }
                })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        binding.decreaseVolume.setOnCheckedChangeListener(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}