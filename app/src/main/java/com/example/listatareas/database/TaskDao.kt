package com.example.listatareas.database

import androidx.room.*

@Dao
interface TaskDao {
    @Query("select * from task order by priority")
    fun loadAllTask(): List<TaskEntry>
    @Insert
    fun insertTask(taskEntry:TaskEntry)
    @Update(onConflict= OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry:TaskEntry)
    @Delete
    fun deleteTask(taskEntry:TaskEntry)
    @Query ("DELETE FROM task")
    fun deleteAll()
    @Query("SELECT * FROM task WHERE id=:id")
    fun loadTaskById(id:Long):TaskEntry
}