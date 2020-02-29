package linusfessler.alarmtiles.tiles.sleeptimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.databinding.FragmentSleepTimerTileBinding
import linusfessler.alarmtiles.shared.App
import javax.inject.Inject

class SleepTimerTileFragment : Fragment() {
    @Inject
    lateinit var viewModel: SleepTimerViewModel

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    private lateinit var binding: FragmentSleepTimerTileBinding
    private lateinit var startDialog: SleepTimerStartDialog
    private lateinit var descriptionDialog: AlertDialog

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App)
                .sleepTimerComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSleepTimerTileBinding.inflate(inflater, container, false)
        startDialog = SleepTimerStartDialog(requireContext(), inputMethodManager, viewModel)
        descriptionDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.sleep_timer)
                .setMessage(R.string.sleep_timer_description)
                .setCancelable(true)
                .create()

        binding.sleepTimer.setOnClickListener {
            disposable.add(viewModel.sleepTimer
                    .firstElement()
                    .subscribe {
                        if (it.isEnabled) {
                            viewModel.dispatch(SleepTimerEvent.Cancel())
                        } else {
                            startDialog.show()
                        }
                    })
        }

        binding.sleepTimer.setOnLongClickListener {
            descriptionDialog.show()
            true
        }

        disposable.add(viewModel.sleepTimer
                .subscribe {
                    binding.sleepTimer.isEnabled = it.isEnabled
                })

        disposable.add(viewModel.timeLeft
                .subscribe {
                    binding.sleepTimer.setSubtitle(it)
                })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        binding.sleepTimer.setOnClickListener(null)
        binding.sleepTimer.setOnLongClickListener(null)
        startDialog.dismiss()
        descriptionDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}