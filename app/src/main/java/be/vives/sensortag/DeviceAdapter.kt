package be.vives.sensortag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(val deviceList: Array<Device>) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>(){

    class DeviceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val device_name: TextView = itemView.findViewById(R.id.device_name_text_view)
        private val address: TextView = itemView.findViewById(R.id.address_text_view)


        fun bind(device: Device) {
            device_name.text = device.name
            address.text= device.address
        }
    }


    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.device_item, parent, false)

        return DeviceViewHolder(view)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return deviceList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(deviceList[position])
    }

}