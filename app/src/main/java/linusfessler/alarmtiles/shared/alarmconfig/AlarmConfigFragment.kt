package linusfessler.alarmtiles.shared.alarmconfig

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import linusfessler.alarmtiles.databinding.FragmentAlarmConfigBinding

class AlarmConfigFragment : Fragment() {
    private lateinit var binding: FragmentAlarmConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (requireActivity().applicationContext as App)
//                .sleepTimerComponent
//                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAlarmConfigBinding.inflate(inflater, container, false)
        return binding.root
    }
}