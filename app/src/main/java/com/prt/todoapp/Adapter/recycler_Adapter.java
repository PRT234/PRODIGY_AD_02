package com.prt.todoapp.Adapter;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.prt.todoapp.DatabaseHandler;
import com.prt.todoapp.MainActivity;
import com.prt.todoapp.Model.ToDoModel;
import com.prt.todoapp.R;

import java.util.List;

public class recycler_Adapter extends RecyclerView.Adapter<recycler_Adapter.ViewHolder> {
    private List<ToDoModel> todolist;
    private MainActivity activity;
    private DatabaseHandler db;
    ToDoModel todomodel = new ToDoModel();

    public recycler_Adapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasklist_view,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoModel item = todolist.get(position);
        holder.Task.setText(item.getTask());
        holder.deleteTask.setOnClickListener(v -> {DeleteTask(todolist.get(position).getId());});
        holder.taskComplete.setOnClickListener(v -> {CompleteTask(todolist.get(position).getId());});
        holder.Task.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String s = holder.Task.getText().toString();
                    updateTask(todolist.get(position).getId(),s);
                }

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public void setTodolist(List<ToDoModel> todolist) {
        this.todolist = todolist;
        notifyDataSetChanged();
    }

    public void DeleteTask(int id){
        db = new DatabaseHandler(activity);
        db.openDatabase();
        db.deleteTask(id);
        db.close();
        activity.restartTask();
        Toast.makeText(activity,"Deleted !",Toast.LENGTH_SHORT).show();
    }

    public void CompleteTask(int id){
        db = new DatabaseHandler(activity);
        db.openDatabase();
        db.addCompletedTask(id);
        db.close();
        activity.restartTask();
        activity.restartCompletedTask();
        Toast.makeText(activity,"Task Completed !",Toast.LENGTH_SHORT).show();
    }

    public void updateTask(int id, String Task){
        db.openDatabase();
        db.updateTask(id,Task);
        db.close();
        activity.restartTask();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText Task;
        MaterialButton deleteTask;
        MaterialButton taskComplete;
        ViewHolder(View view){
            super(view);
            Task = view.findViewById(R.id.Task);
            deleteTask = view.findViewById(R.id.deleteTask);
            taskComplete = view.findViewById(R.id.taskComplete);
        }
    }
}
