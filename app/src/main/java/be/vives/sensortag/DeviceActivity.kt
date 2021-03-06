package be.vives.sensortag

import android.app.Activity
import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import be.vives.sensortag.databinding.ActivityDeviceBinding

class DeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device=intent.getParcelableExtra<Device>("device")
        Log.i("onCreate DeviceActivity","started deviceactivity with ble ${device?.device?.name}")
    }
}