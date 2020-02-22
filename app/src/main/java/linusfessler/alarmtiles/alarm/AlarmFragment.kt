package linusfessler.alarmtiles.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.core.App
import linusfessler.alarmtiles.databinding.FragmentAlarmBinding
import javax.inject.Inject

class AlarmFragment : Fragment() {
    @Inject
    lateinit var viewModel: AlarmViewModel

    private lateinit var binding: FragmentAlarmBinding
    //    private lateinit var startDialog: AlarmStartDialog
    private lateinit var descriptionDialog: AlertDialog

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App)
                .alarmComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
//        startDialog = AlarmStartDialog(requireContext(), inputMethodManager, viewModel)
        descriptionDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.alarm)
                .setMessage(R.string.alarm_description)
                .setCancelable(true)
                .create()

        binding.alarm.setOnClickListener {
            disposable.add(viewModel.alarm
                    .firstElement()
                    .subscribe {
                        if (it.isEnabled) {
                            viewModel.dispatch(AlarmEvent.Disable())
                        } else {
                            viewModel.dispatch(AlarmEvent.Enable(8, 0))
//                            startDialog.show()
                        }
                    })
        }

        binding.alarm.setOnLongClickListener {
            descriptionDialog.show()
            true
        }

        disposable.add(viewModel.alarm
                .subscribe {
                    if (it.isEnabled) {
                        // It's possible that the alarm was set through the quick settings while the start dialog is shown, dismiss it in this case
//                        startDialog.dismiss()
                    }
                    binding.alarm.isEnabled = it.isEnabled
                })

        disposable.add(viewModel.timeLeft
                .subscribe {
                    binding.alarm.setSubtitle(it)
                })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        binding.alarm.setOnClickListener(null)
        binding.alarm.setOnLongClickListener(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
//        startDialog.dismiss()
        descriptionDialog.dismiss()
    }
}