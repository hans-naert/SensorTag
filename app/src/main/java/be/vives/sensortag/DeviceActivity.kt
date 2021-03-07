package be.vives.sensortag

import android.bluetooth.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import be.vives.sensortag.databinding.ActivityDeviceBinding
import java.util.*

class DeviceActivity : AppCompatActivity() {

    private val UUID_CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    private val UUID_BAROMETER_DATA = UUID.fromString("f000aa41-0451-4000-b000-000000000000")
    private val UUID_BAROMETER_CONFIGURATION = UUID.fromString("f000aa42-0451-4000-b000-000000000000")
    private val UUID_BAROMETER_SERVICE = UUID.fromString("f000aa40-0451-4000-b000-000000000000")

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private lateinit var binding: ActivityDeviceBinding;

    private lateinit var bleDevice: BluetoothDevice;

    private var bleGatt : BluetoothGatt? = null;

    private var bleService : BluetoothGattService? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<Device>("device")
        bleDevice = device?.bluetoothDevice!!;
        Log.i("onCreate DeviceActivity", "started deviceactivity with ble ${device?.bluetoothDevice?.name}")

        binding.connectButton.setOnClickListener {
            bleDevice.connectGatt(this, false, mGattCallback)
        }

        binding.enableBarometerButton.setOnClickListener {
            bleService = bleGatt?.getService(UUID_BAROMETER_SERVICE)
            val characteristic = bleService?.getCharacteristic(UUID_BAROMETER_CONFIGURATION)
            val setValueSuccess = characteristic?.setValue(byteArrayOf(0x01))
            Log.i("BAROMETER_CONF_LOCAL", setValueSuccess.toString())
            val writeSuccess = bleGatt?.writeCharacteristic(characteristic)
            Log.i("BAROMETER_CONF_WRITE", writeSuccess.toString())
        }

        binding.barometerNotificationsButton.setOnClickListener {
            val characteristic = bleService?.getCharacteristic(UUID_BAROMETER_DATA)
            bleGatt?.setCharacteristicNotification(characteristic, true)

            val descriptor = characteristic?.getDescriptor(UUID_CLIENT_CHARACTERISTIC_CONFIG)
            descriptor?.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            bleGatt?.writeDescriptor(descriptor)
        }
    }

    private val mGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("DeviceActivity", "Connected to GATT server.");
                bleGatt=gatt!!;
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
               Log.i("DeviceActivity", "Services discovered succesfully")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)

            if(characteristic?.uuid==UUID_BAROMETER_DATA) {
                Log.i("DeviceActivity", "characteristic changed ${characteristic?.value?.size}")
                val temp=characteristic?.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16,0)
                Log.i("DeviceActivity", "temp is $temp")
                binding.barometerTextView.text=(temp!!/100.0).toString()
            }
        }
    }
}