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
import java.util.Calendar;

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

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        android.support.v7.widget.CardView bg =
                (android.support.v7.widget.CardView) holder.itemView.findViewById(R.id.card_view);
        if (mDataset.get(position).getTaskYear() == 9999 ||
                mDataset.get(position).getTaskMonth() == 99 ||
                mDataset.get(position).getTaskDay() == 99) {
            bg.setCardBackgroundColor(Color.parseColor("#df816f"));
        } else if (mDataset.get(position).getTaskYear() < year ||
                (mDataset.get(position).getTaskYear() == year &&
                mDataset.get(position).getTaskMonth() < month) ||
                (mDataset.get(position).getTaskYear() == year &&
                mDataset.get(position).getTaskMonth() == month &&
                mDataset.get(position).getTaskDay() < day) ||
                (mDataset.get(position).getTaskYear() == year &&
                mDataset.get(position).getTaskMonth() == month &&
                mDataset.get(position).getTaskDay() == day &&
                mDataset.get(position).getTaskHourBegin() < hour) ||
                (mDataset.get(position).getTaskYear() == year &&
                mDataset.get(position).getTaskMonth() == month &&
                mDataset.get(position).getTaskDay() == day &&
                mDataset.get(position).getTaskHourBegin() == hour &&
                mDataset.get(position).getTaskMinuteBegin() < minute)
                ) {
            Log.i(LOG_TAG, "Got : " + year + "/" + month + "/" + day + " - " + hour + ":" + minute);
            bg.setCardBackgroundColor(Color.parseColor("#e1a966"));
        }

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

        if (mDataset.get(position).getTaskDay() == 99 ||
                mDataset.get(position).getTaskMonth() == 99 ||
                mDataset.get(position).getTaskYear() == 9999) {
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
