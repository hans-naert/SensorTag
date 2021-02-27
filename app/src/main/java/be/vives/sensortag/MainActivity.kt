package be.vives.sensortag

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import be.vives.sensortag.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
private const val LOCATION_PERMISSION_REQUEST_CODE = 2

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
        binding.scanButton.setOnClickListener {
            Toast.makeText(this,"Clicked on SCAN button", Toast.LENGTH_LONG).show()
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
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
         if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
             Snackbar.make(
                     this,
                     binding.layout,
                     "fine location permission is needed for using BLE", Snackbar.LENGTH_INDEFINITE)
                     .show()

             ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            Snackbar.make(
                this,
                binding.layout,
                "fine location permission not enabled",
                Snackbar.LENGTH_SHORT)
                .show()

            // Request the permission. The result will be received in onRequestPermissionResult().
             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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