package linusfessler.alarmtiles.tiles.alarmtimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.databinding.FragmentAlarmTimerTileBinding
import linusfessler.alarmtiles.shared.App
import javax.inject.Inject

class AlarmTimerTileFragment : Fragment() {
    @Inject
    lateinit var viewModel: AlarmTimerViewModel

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    private lateinit var binding: FragmentAlarmTimerTileBinding
    private lateinit var startDialog: AlarmTimerStartDialog
    private lateinit var descriptionDialog: AlertDialog

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App)
                .alarmTimerComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAlarmTimerTileBinding.inflate(inflater, container, false)
        startDialog = AlarmTimerStartDialog(requireContext(), inputMethodManager, viewModel)
        descriptionDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.alarm_timer)
                .setMessage(R.string.alarm_timer_description)
                .setCancelable(true)
                .create()

        binding.alarmTimer.setOnClickListener {
            disposable.add(viewModel.alarmTimer
                    .firstElement()
                    .subscribe {
                        if (it.isEnabled) {
                            viewModel.dispatch(AlarmTimerEvent.Disable())
                        } else {
                            startDialog.show()
                        }
                    })
        }

        binding.alarmTimer.setOnLongClickListener {
            descriptionDialog.show()
            true
        }

        disposable.add(viewModel.alarmTimer
                .subscribe {
                    binding.alarmTimer.isEnabled = it.isEnabled
                })

        disposable.add(viewModel.timeLeft
                .subscribe {
                    binding.alarmTimer.setSubtitle(it)
                })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        binding.alarmTimer.setOnClickListener(null)
        binding.alarmTimer.setOnLongClickListener(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        startDialog.dismiss()
        descriptionDialog.dismiss()
    }
}