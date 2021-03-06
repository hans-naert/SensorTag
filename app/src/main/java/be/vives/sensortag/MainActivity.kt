package be.vives.sensortag

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import be.vives.sensortag.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
private const val LOCATION_PERMISSION_REQUEST_CODE = 2

class MainActivity : AppCompatActivity() {

    val deviceList= mutableListOf<Device>();

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val indexQuery = deviceList.indexOfFirst { it.bluetoothDevice.address == result.device.address }
            if (indexQuery == -1) {
                with(result.device) {
                    Log.i("ScanCallback", "Found BLE device Name: ${name ?: "Unnamed"}, address: $address")
                    deviceList.add(Device(this/*, name ?: "Unnamed", address*/))
                    deviceAdapter.notifyItemInserted(deviceList.size - 1)
                }
            }
        }
    }

    val deviceClicked = { device: Device ->

        Toast.makeText(this, "Clicked on device Name: ${device.bluetoothDevice.name ?: "Unnamed"}, address: ${device.bluetoothDevice.address}",Toast.LENGTH_SHORT).show()
        if(isScanning)
            stopBleScan()
        intent = Intent(this,DeviceActivity::class.java)
        intent.putExtra("device",device)
        startActivity(intent)
    }

    private fun startBleScan() {
        deviceList.clear()
        deviceAdapter.notifyDataSetChanged()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted) {
            requestLocationPermission()
        } else {
            bleScanner.startScan(null, scanSettings, scanCallback)
            isScanning = true
        }
    }

    private fun stopBleScan() {
        bleScanner.stopScan(scanCallback)
        isScanning = false
    }

    private var isScanning = false
        set(value) {
            field = value
            runOnUiThread { binding.scanButton.text = if (value) "Stop Scan" else "Start Scan" }
        }

    val isLocationPermissionGranted
        get() = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private lateinit var binding: ActivityMainBinding
    private lateinit var deviceAdapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        deviceAdapter = DeviceAdapter(deviceList, deviceClicked)
        binding.devicesRecyclerView.adapter=deviceAdapter
        binding.scanButton.setOnClickListener {
            Toast.makeText(this,"Clicked on SCAN button", Toast.LENGTH_LONG).show()
            if (isScanning) {
                stopBleScan()
            } else {
                startBleScan()
            }
        }
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

     /**
     * Requests the [android.Manifest.permission.ACCESS_FINE_LOCATION] permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestLocationPermission() {
        // Permission has not been granted and must be requested.
         if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
             Snackbar.make(
                     this,
                     binding.layout,
                     "fine location permission is needed for using BLE", Snackbar.LENGTH_INDEFINITE)
                     .show()

             requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            Snackbar.make(
                this,
                binding.layout,
                "fine location permission not enabled",
                Snackbar.LENGTH_SHORT)
                .show()

            // Request the permission. The result will be received in onRequestPermissionResult().
             requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(
                        this,
                        binding.layout,
                        "fine location permission is granted",
                        Snackbar.LENGTH_SHORT)
                        .show()
            } else {
                // Permission request was denied.
                Snackbar.make(
                        this,
                        binding.layout,
                        "fine location permission was denied",
                        Snackbar.LENGTH_SHORT)
                        .show()
            }
        }
    }

}