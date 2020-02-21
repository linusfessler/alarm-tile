package linusfessler.alarmtiles.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.core.App
import linusfessler.alarmtiles.databinding.FragmentStopwatchBinding
import javax.inject.Inject

class StopwatchFragment : Fragment() {
    @Inject
    lateinit var viewModel: StopwatchViewModel

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App)
                .stopwatchComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        binding.stopwatch.setOnClickListener {
            viewModel.dispatch(StopwatchEvent.Toggle())
        }
        disposable.add(viewModel.stopwatch
                .subscribe {
                    binding.stopwatch.isEnabled = it.isEnabled
                })

        disposable.add(viewModel.elapsedTime
                .subscribe {
                    binding.stopwatch.setSubtitle(it)
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