package com.example.listatareas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.RadioGroup
import com.example.listatareas.database.AppDatabase
import com.example.listatareas.database.TaskEntry
import com.example.listatareas.helper.doAsync
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    companion object {
        val PRIORITY_HIGH=1
        val PRIORITY_MEDIUM=2
        val PRIORITY_LOW=3
        val EXTRA_TASK_ID="extraTaskId"
        val INSTANCE_TASK_ID="instanceTaskId"
        private val DEFAULT_TASK_ID=-1
        private val TAG= AddTaskActivity::class.java.simpleName

    }

    private var mTaskId=DEFAULT_TASK_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        saveButton.setOnClickListener{
            onSaveButtonClicked()
        }

        if (savedInstanceState !=null && savedInstanceState.containsKey(INSTANCE_TASK_ID)){
            mTaskId=savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID)
        }
        val intent=intent
        if(intent !=null && intent.hasExtra(EXTRA_TASK_ID)){
            saveButton.text=getString(R.string.update_button).toString()
            if (mTaskId === DEFAULT_TASK_ID){
                mTaskId=intent.getLongExtra(EXTRA_TASK_ID,DEFAULT_TASK_ID.toLong()).toInt()
                doAsync{
                    val task= AppDatabase.getInstance(this@AddTaskActivity)?.taskDao()?.loadTaskById(mTaskId.toLong())
                    runOnUiThread{
                        populateUI(task!!)
                    }

                }.execute()
            }
        }
    }
    override fun onSaveInstanceState(outState:Bundle?, outPersistentState: PersistableBundle?){
        outState?.putInt(INSTANCE_TASK_ID, mTaskId)
        super.onSaveInstanceState(outState, outPersistentState)
    }
    private fun populateUI(task: TaskEntry){
        if (task==null) return
        editTextTaskDescription.setText(task.description)
        setPriorityInViews(task.priority)
    }

    fun onSaveButtonClicked(){
        val description=editTextTaskDescription.text.toString()
        val prioridad=getPriorityFromViews()
        val taskEntry=TaskEntry(description=description,priority=prioridad,updatedAt = Date())
        doAsync{
            if (mTaskId == DEFAULT_TASK_ID){
                AppDatabase.getInstance(this)!!.taskDao().insertTask(taskEntry)
            }else{
                taskEntry.id=mTaskId.toLong()
                AppDatabase.getInstance(this)!!.taskDao().updateTask(taskEntry)
            }
            finish()
        }.execute()
    }
    fun getPriorityFromViews():Int{
        var priority=1
        val checkedId=(findViewById<View>(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
        when(checkedId){
            R.id.radButton1->priority= PRIORITY_HIGH
            R.id.radButton2->priority= PRIORITY_MEDIUM
            R.id.radButton3->priority= PRIORITY_LOW
        }
        return priority
    }
    fun setPriorityInViews(priority:Int){
        when(priority){
            PRIORITY_HIGH->radButton1.isChecked=true
            PRIORITY_MEDIUM->radButton2.isChecked=true
            PRIORITY_LOW->radButton3.isChecked=true
        }
    }
}
