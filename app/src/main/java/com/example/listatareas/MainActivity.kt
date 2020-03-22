package com.example.listatareas

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listatareas.database.AppDatabase
import com.example.listatareas.database.TaskEntry
import com.example.listatareas.helper.doAsync

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

   private lateinit var viewAdapter: TaskAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    val taskList: List<TaskEntry> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

  viewManager=LinearLayoutManager(this)
  viewAdapter= TaskAdapter(taskList,this,{ task:TaskEntry->onItemClickListener(task)})

 recyclerViewTasks.apply{
     setHasFixedSize(true)
     layoutManager=viewManager
     adapter=viewAdapter
     addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
 }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {


                doAsync{
                    val position = viewHolder.adapterPosition
                    val tasks = viewAdapter.getTasks()
                    AppDatabase.getInstance(this@MainActivity)?.taskDao()?.deleteTask(tasks[position])
                    retrieveTasks()
                }.execute()


            }
        }).attachToRecyclerView(recyclerViewTasks)

        fab.setOnClickListener { view ->

     val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
     startActivity(addTaskIntent)
 }
}

override fun onCreateOptionsMenu(menu: Menu): Boolean {

 menuInflater.inflate(R.menu.menu_main, menu)
 return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {

 return when (item.itemId) {
     R.id.action_settings -> true
     else -> super.onOptionsItemSelected(item)
 }
}
    private fun onItemClickListener(task:TaskEntry){
        val intent=Intent(this,AddTaskActivity::class.java)
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID,task.id)
        startActivity(intent)

    }

    override fun onResume(){
        super.onResume()
        retrieveTasks()
    }

    private fun retrieveTasks(){
        doAsync{
            val tasks=AppDatabase.getInstance(this@MainActivity)?.taskDao()?.loadAllTask()
            runOnUiThread{
                viewAdapter.setTask(tasks!!)
            }

        }.execute()
    }
}
