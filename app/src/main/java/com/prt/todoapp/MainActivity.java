package com.prt.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prt.todoapp.Adapter.completedtask_recycler_Adapter;
import com.prt.todoapp.Adapter.recycler_Adapter;
import com.prt.todoapp.Model.ToDoModel;
import com.prt.todoapp.Model.completedTaskModel;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView taskList;
    private recycler_Adapter adapter;
    private completedtask_recycler_Adapter adapter1;
    private RecyclerView completedTaskList;
    private List<ToDoModel> todolist;
    private List<completedTaskModel> completedTaskModelList;
    private EditText addTaskEditText;
    private FloatingActionButton addTaskButton;
    private DatabaseHandler db;
    private RelativeLayout RelativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = findViewById(R.id.tasklist);
        addTaskEditText = findViewById(R.id.addTaskEditText);
        addTaskButton = findViewById(R.id.addTaskButton);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new recycler_Adapter(db, this);
        taskList.setAdapter(adapter);
        todolist = new ArrayList<>();

        db = new DatabaseHandler(this);
        db.openDatabase();
        adapter.setTodolist(db.getAllTask());
        db.close();

        completedTaskModelList = new ArrayList<>();
        completedTaskList = findViewById(R.id.completedTaskList);
        completedTaskList.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new completedtask_recycler_Adapter(this);
        completedTaskList.setAdapter(adapter1);

        db.openDatabase();
        adapter1.setCompletedTaskList(db.getAllCompletedTask());
        db.close();

        //add task operation
        addTaskButton.setOnClickListener(v -> {
            String task = String.valueOf(addTaskEditText.getText());
            Add_Task(task);
            addTaskEditText.setCursorVisible(false);
        });
        addTaskEditText.setOnClickListener(v -> {addTaskEditText.setCursorVisible(true);});
    }

    public void Add_Task(String task){

        db.openDatabase();
        if (task.isEmpty()) {
            Toast.makeText(MainActivity.this, "Can't Add empty task", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,"Added",Toast.LENGTH_SHORT).show();
            db.insertTask(task);
            adapter.setTodolist(db.getAllTask());
            addTaskEditText.setText("");
        }
        db.close();
    }
    public void restartTask() {
        db.openDatabase();
        adapter.setTodolist(db.getAllTask());
        db.close();
    }
    public void restartCompletedTask() {
        db.openDatabase();
        adapter1.setCompletedTaskList(db.getAllCompletedTask());
        db.close();
    }
}

