package net.thetoo.myshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

class CustomAdapter(private val context: Context, private val items: List<Triple<Int, String, String>>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val numberView = view.findViewById<TextView>(R.id.tvNumber)
        val itemView = view.findViewById<TextView>(R.id.tvItem)
        val costView = view.findViewById<TextView>(R.id.tvCost)

        val (number, item, cost) = items[position]

        numberView.text = number.toString()
        itemView.text = item
        costView.text = cost

        return view
    }
}
