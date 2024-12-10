import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atech.android.R
import com.atech.android.data.dtos.DeviceDataDtoItem

class BleDeviceAdapter(
    private val callback: (DeviceDataDtoItem) -> Unit
) : ListAdapter<DeviceDataDtoItem, BleDeviceAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<DeviceDataDtoItem>() {
        override fun areItemsTheSame(oldItem: DeviceDataDtoItem, newItem: DeviceDataDtoItem): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: DeviceDataDtoItem, newItem: DeviceDataDtoItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val root: ConstraintLayout = itemView.findViewById(R.id.root)
        private val nameText: TextView = itemView.findViewById(R.id.txt_name)
        private val addressText: TextView = itemView.findViewById(R.id.txt_address)
        private val signalText: TextView = itemView.findViewById(R.id.txt_signal)

        fun bind(device: DeviceDataDtoItem, callback: (DeviceDataDtoItem) -> Unit) {
            nameText.text = device.name
            addressText.text = device.address
            signalText.text = device.signal

            root.setOnClickListener {
                callback.invoke(device)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.ble_device_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val device = getItem(position)
        viewHolder.bind(device, callback)
    }
}