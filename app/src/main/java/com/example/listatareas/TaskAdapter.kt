package com.example.listatareas

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.listatareas.database.TaskEntry
import kotlinx.android.synthetic.main.task_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class TaskAdapter(private var mTaskEntries:List<TaskEntry>, private val mContext: Context, private val clickListener: (TaskEntry) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return TaskViewHolder(layoutInflater.inflate(R.layout.task_layout, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(mTaskEntries[position], mContext, clickListener)
    }

    override fun getItemCount(): Int = mTaskEntries.size


    fun setTask(taskEntries: List<TaskEntry>){
        mTaskEntries = taskEntries
        notifyDataSetChanged()
    }

    fun getTasks():List<TaskEntry> = mTaskEntries


    class TaskViewHolder (itemView:View) :RecyclerView.ViewHolder(itemView) {

        fun bind (task:TaskEntry, context: Context, clickListener: (TaskEntry) -> Unit){

            itemView.taskDescription.text = task.description
            itemView.taskUpdatedAt.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(task.updatedAt).toString()

            itemView.priorityTextView.text = task.priority.toString()
            val priorityCircle = itemView.priorityTextView.background as GradientDrawable

            val priorityColor = getPriorityColor(task.priority,context)
            priorityCircle.setColor(priorityColor)

            itemView.setOnClickListener{ clickListener(task)}
        }


        fun getPriorityColor(priority: Int, mContext: Context): Int {
            var priorityColor = 0

            when (priority) {
                1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
                2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
                3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
                else -> {
                }
            }
            return priorityColor
        }
    }

}
