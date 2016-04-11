package com.netanelshilo.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by netanels on 09/03/2016.
 */
public class TaskAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mTasks;

    public TaskAdapter(Context context,List<ParseObject> tasks)
    {
        super(context,R.layout.task_custom_row,tasks);
        mContext = context;
        mTasks = tasks;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.task_custom_row, null);
            holder = new ViewHolder();
            holder.tasknamecustom = (TextView)convertView.findViewById(R.id.tvTaskName);
            holder.taskstatuscustom = (TextView)convertView.findViewById(R.id.tvTaskStatus);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject taskObject = mTasks.get(position);
        String taskname = taskObject.getString("location");
        holder.tasknamecustom.setText(taskname);

        String status = taskObject.getString("taskStatus");
        holder.taskstatuscustom.setText(status);

        return convertView;
    }

    public static class ViewHolder
    {
        TextView tasknamecustom;
        TextView taskstatuscustom;
    }
}
