package linusfessler.alarmtiles.tiles.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import linusfessler.alarmtiles.R
import linusfessler.alarmtiles.databinding.FragmentAlarmTileBinding
import linusfessler.alarmtiles.shared.App
import linusfessler.alarmtiles.shared.alarm.AlarmEvent
import javax.inject.Inject
import javax.inject.Named

class AlarmTileFragment : Fragment() {
    @Inject
    lateinit var viewModel: AlarmViewModel

    @JvmField
    @field:[Inject Named("is24Hours")]
    var is24Hours = false

    private lateinit var binding: FragmentAlarmTileBinding
    private lateinit var startDialog: AlarmStartDialog
    private lateinit var descriptionDialog: AlertDialog

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App)
                .alarmComponent
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAlarmTileBinding.inflate(inflater, container, false)
        startDialog = AlarmStartDialog(requireContext(), viewModel, is24Hours)
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
                            viewModel.dispatch(AlarmEvent.Cancel())
                        } else {
                            startDialog.show()
                        }
                    })
        }

        binding.alarm.setOnLongClickListener {
            descriptionDialog.show()
            true
        }

        disposable.add(viewModel.alarm
                .subscribe {
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
        startDialog.dismiss()
        descriptionDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}