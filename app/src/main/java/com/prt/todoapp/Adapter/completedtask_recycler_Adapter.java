package com.prt.todoapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.prt.todoapp.DatabaseHandler;
import com.prt.todoapp.MainActivity;
import com.prt.todoapp.Model.ToDoModel;
import com.prt.todoapp.Model.completedTaskModel;
import com.prt.todoapp.R;

import java.util.List;

public class completedtask_recycler_Adapter extends RecyclerView.Adapter<completedtask_recycler_Adapter.ViewHolder> {

    List<completedTaskModel> completedTaskList;
    private MainActivity activity;
    private DatabaseHandler db;

    public completedtask_recycler_Adapter(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_tasklist_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.completedTask.setText(completedTaskList.get(position).getCompletedTask());
        holder.deleteCompletedTask.setOnClickListener(v -> {DeleteCompletedTask(completedTaskList.get(position).getId());});
    }

    public void setCompletedTaskList(List<completedTaskModel> completedTaskList) {
        this.completedTaskList = completedTaskList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return completedTaskList.size();
    }

    public void DeleteCompletedTask(int id){
        db = new DatabaseHandler(activity);
        db.openDatabase();
        db.deleteCompletedTask(id);
        db.close();
        activity.restartCompletedTask();
        Toast.makeText(activity,"Deleted Completed Task!",Toast.LENGTH_SHORT).show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView completedTask;
        MaterialButton deleteCompletedTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            completedTask = itemView.findViewById(R.id.completedTask);
            deleteCompletedTask = itemView.findViewById(R.id.deleteCompletedTask);
        }
    }

}
