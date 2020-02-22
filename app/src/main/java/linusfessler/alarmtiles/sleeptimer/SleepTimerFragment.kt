package linusfessler.alarmtiles.sleeptimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.core.App
import linusfessler.alarmtiles.databinding.FragmentSleepTimerBinding
import javax.inject.Inject

class SleepTimerFragment : Fragment() {
    @Inject
    lateinit var viewModel: SleepTimerViewModel

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    private lateinit var binding: FragmentSleepTimerBinding
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
        binding = FragmentSleepTimerBinding.inflate(inflater, container, false)
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
                    if (it.isEnabled) {
                        // It's possible that the sleep timer was enabled through the quick settings while the start dialog is shown, dismiss it in this case
                        startDialog.dismiss()
                    }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        startDialog.dismiss()
        descriptionDialog.dismiss()
    }
}