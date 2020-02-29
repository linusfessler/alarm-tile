package linusfessler.alarmtiles.tiles.stopwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.databinding.FragmentStopwatchTileBinding
import linusfessler.alarmtiles.shared.App
import javax.inject.Inject

class StopwatchTileFragment : Fragment() {
    @Inject
    lateinit var viewModel: StopwatchViewModel

    private lateinit var descriptionDialog: AlertDialog

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App)
                .stopwatchComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentStopwatchTileBinding.inflate(inflater, container, false)
        descriptionDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.stopwatch)
                .setMessage(R.string.stopwatch_description)
                .setCancelable(true)
                .create()

        binding.stopwatch.setOnClickListener {
            viewModel.dispatch(StopwatchEvent.Toggle())
        }

        binding.stopwatch.setOnLongClickListener {
            descriptionDialog.show()
            true
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
        descriptionDialog.dismiss()
    }
}