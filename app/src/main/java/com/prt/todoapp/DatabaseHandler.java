package com.prt.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prt.todoapp.Model.ToDoModel;
import com.prt.todoapp.Model.completedTaskModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 7;
    private static final String NAME = "ToDoListDatabase";
    private static final String TODO_TABLE = "todoTable";
    private static final String COMPLETED_TASK_TABLE = "completedTaskTable";
    private static final String ID = "id";
    private static final String ID1 = "com_id";
    private static final String TASK = "task";
    private static final String COMPLETED_TASK = "completedTask";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + TASK + " TEXT )";
    private static final String CREATE_COMPLETED_TASK_TABLE = "CREATE TABLE " + COMPLETED_TASK_TABLE + "(" + ID1 + " INTEGER, "
            + COMPLETED_TASK + " TEXT )";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
        db.execSQL(CREATE_COMPLETED_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COMPLETED_TASK_TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(String task){
        String s = "INSERT INTO " + TODO_TABLE + " (" + TASK + ") VALUES ('" + task + "');";
        db.execSQL(s);

    }

    public List<ToDoModel> getAllTask(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(TODO_TABLE, null, null, null ,null , null, null, null );
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        taskList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return taskList;
    }

    public void updateTask(int id, String Task){
        ContentValues c = new ContentValues();
        c.put(TASK, Task);
        db.update(TODO_TABLE, c, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

    public List<completedTaskModel> getAllCompletedTask(){
        List<completedTaskModel> completedtaskList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(COMPLETED_TASK_TABLE, null, null, null ,null , null, null, null );
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        completedTaskModel completedTask = new completedTaskModel();
                        completedTask.setCompletedTask(cursor.getString(cursor.getColumnIndex(COMPLETED_TASK)));
                        completedTask.setId(cursor.getInt(cursor.getColumnIndex(ID1)));
                        completedtaskList.add(completedTask);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return completedtaskList;
    }

    public void updateCompletedTask(int id, String completedTask){
        ContentValues c = new ContentValues();
        c.put(COMPLETED_TASK, completedTask);
        db.update(COMPLETED_TASK_TABLE, c, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteCompletedTask(int id){
        String s = String.valueOf(id);
        db.delete(COMPLETED_TASK_TABLE, ID1 + "=?", new String[] {String.valueOf(id)});
    }

    public void addCompletedTask(int id){
        String s = String.valueOf(id);
        String query = "INSERT INTO " + COMPLETED_TASK_TABLE + " SELECT * FROM " + TODO_TABLE + " WHERE " + ID + " = " + s ;
        db.execSQL(query);
        deleteTask(id);
    }

    public void insertCompletedTask(String com_task){
        String s = "INSERT INTO " + COMPLETED_TASK_TABLE + " (" + COMPLETED_TASK + ") VALUES ('" + com_task + "');";
        db.execSQL(s);

    }

    public boolean task_isEmpty(){
        long no_Of_Row = DatabaseUtils.queryNumEntries(db,TODO_TABLE);
        if(no_Of_Row == 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean CompletedTask_isEmpty(){
        long no_Of_Row = DatabaseUtils.queryNumEntries(db,COMPLETED_TASK_TABLE);
        if(no_Of_Row == 0){
            return true;
        }else {
            return false;
        }
    }

}
