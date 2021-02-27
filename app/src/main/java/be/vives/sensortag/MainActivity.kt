package be.vives.sensortag

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import be.vives.sensortag.databinding.ActivityMainBinding

private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.scanButton.setOnClickListener { Toast.makeText(this,"Clicked on SCAN button", Toast.LENGTH_LONG).show() }
    }

    override fun onResume() {
        super.onResume()
        //check if bluetooth is enabled
        if (!bluetoothAdapter.isEnabled) {
            promptEnableBluetooth()
        }
    }

   //request to enable bluetooth
    private fun promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== ENABLE_BLUETOOTH_REQUEST_CODE)
        {
            Log.v("ENABLE BT", when(resultCode) { android.app.Activity.RESULT_OK -> "BT enabled" else -> "BT not enabled"})
        }
    }
}