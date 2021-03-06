package be.vives.sensortag

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class DeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        val device=intent.getParcelableExtra<BluetoothDevice>("device")
        Log.i("onCreate DeviceActivity","started deviceactivity with ble ${device}")
    }
}