package linusfessler.alarmtiles.core

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import linusfessler.alarmtiles.R

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}