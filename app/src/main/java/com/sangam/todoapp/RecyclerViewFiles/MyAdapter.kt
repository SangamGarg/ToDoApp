package com.sangam.todoapp.RecyclerViewFiles

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sangam.todoapp.R

class MyAdapter(var list: List<DataClassTask>) : Adapter<MyAdapter.MyViewHolder>() {
    lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(
            task: String? = null,
            position: Int
        )
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }


    inner class MyViewHolder(itemView: View, listener: onItemClickListener) : ViewHolder(itemView) {
        val textViewTask = itemView.findViewById<TextView>(R.id.txtViewtask)
        val textViewDueDate = itemView.findViewById<TextView>(R.id.txtViewDueDate)
        val textViewCurrentdate = itemView.findViewById<TextView>(R.id.txtViewCurrentDate)
        val layoutColor = itemView.findViewById<RelativeLayout>(R.id.relativeLayout)

        init {
            itemView.setOnClickListener {
                val task = textViewTask.text.toString()
                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(task,position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (list[position].priority) {
            "Low" -> holder.layoutColor.setBackgroundColor(Color.parseColor("#DDAEF65B"))
            "Medium" -> holder.layoutColor.setBackgroundColor(Color.parseColor("#D2F4E456"))
            "High" -> holder.layoutColor.setBackgroundColor(Color.parseColor("#C8F46359"))

        }
        holder.textViewTask.text = list[position].task
        holder.textViewCurrentdate.text = list[position].currentDate
        holder.textViewDueDate.text = list[position].dueDate


    }

    override fun getItemCount(): Int {
        return list.size
    }
}