package com.malenea.todolist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by conan on 14/01/17.
 */

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<TaskClass> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        TextView statusState;
        TextView catType;

        public DataObjectHolder(View itemView) {
            super(itemView);

            Context context = itemView.getContext();
            label = (TextView) itemView.findViewById(R.id.row_task_title);
            dateTime = (TextView) itemView.findViewById(R.id.row_task_date);
            statusState = (TextView) itemView.findViewById(R.id.row_task_status);
            catType = (TextView) itemView.findViewById(R.id.row_task_cat);

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),
                    "fonts/MyFont.otf");
            label.setTypeface(custom_font);
            dateTime.setTypeface(custom_font);
            statusState.setTypeface(custom_font);
            catType.setTypeface(custom_font);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<TaskClass> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getTaskTitle());

        if (mDataset.get(position).getTaskStatus() == 0) {
            holder.statusState.setTextColor(Color.BLACK);
            holder.statusState.setText("?");
        } else if (mDataset.get(position).getTaskStatus() == 1) {
            holder.statusState.setTextColor(Color.RED);
            holder.statusState.setText("TBD");
        } else {
            holder.statusState.setTextColor(Color.GREEN);
            holder.statusState.setText("D");
        }

        if (mDataset.get(position).getTaskCat() == 0) {
            holder.catType.setTextColor(Color.BLACK);
            holder.catType.setText("?");
        } else if (mDataset.get(position).getTaskCat() == 1) {
            holder.catType.setTextColor(Color.RED);
            holder.catType.setText("Work");
        } else if (mDataset.get(position).getTaskCat() == 2) {
            holder.catType.setTextColor(Color.BLUE);
            holder.catType.setText("Perso");
        } else if (mDataset.get(position).getTaskCat() == 3) {
            holder.catType.setTextColor(Color.CYAN);
            holder.catType.setText("Errands");
        } else {
            holder.catType.setTextColor(Color.GREEN);
            holder.catType.setText("Hobbies");
        }

        if (mDataset.get(position).getTaskDay() == -1 ||
                mDataset.get(position).getTaskMonth() == -1 ||
                mDataset.get(position).getTaskYear() == -1) {
            holder.dateTime.setText("No date set yet.");
        } else {
            holder.dateTime.setText(mDataset.get(position).getTaskDay()
                    + "/" + mDataset.get(position).getTaskMonth()
                    + "/" + mDataset.get(position).getTaskYear());
        }
    }

    public void addItem(TaskClass dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
