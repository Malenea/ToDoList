package com.malenea.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.R.attr.datePickerDialogTheme;
import static android.R.attr.typeface;


public class MainActivity extends AppCompatActivity {
    DbHelper dbHelper;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "TaskViewActivity";

    private static ArrayList<TaskClass> listHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tx = (TextView) findViewById(R.id.program_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/MyFont.otf");
        tx.setTypeface(custom_font);

        dbHelper = new DbHelper(this);
        loadTaskList();

    }

    private void loadTaskList() {
        mRecyclerView = (RecyclerView) findViewById(R.id.listTask);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        listHandler = new ArrayList<>(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<TaskClass> getDataSet() {
        ArrayList<TaskClass> results = dbHelper.getTaskList();
        return results;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on " + listHandler.get(position) + " " + position);
                showPopup(listHandler, position);
            }
        });
    }

    // To show the des popup of a task
    public void showPopup(final ArrayList<TaskClass> taskList, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.popup, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/MyFont.otf");

        TextView tx_title = (TextView) promptView.findViewById(R.id.popup_txt);
        tx_title.setTypeface(custom_font);

        TextView tx_date = (TextView) promptView.findViewById(R.id.popup_txtDate);
        tx_date.setTypeface(custom_font);

        TextView tx_time = (TextView) promptView.findViewById(R.id.popup_txtTime);
        tx_time.setTypeface(custom_font);

        tx_title.setText(taskList.get(position).getTaskTitle());

        ImageButton btnDel = (ImageButton) promptView.findViewById(R.id.popup_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(taskList, position);
                dialog.dismiss();
            }
        });

        final Calendar c = Calendar.getInstance();

        ImageButton btnCal = (ImageButton) promptView.findViewById(R.id.popup_cal);
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView txtDate = (TextView) promptView.findViewById(R.id.popup_txtDate);

                DatePickerDialog dateDialog;

                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dateDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view,
                                                  int year, int monthOfYear, int dayOfMonth) {
                                txtDate.setText("This todo task is planned on the : "
                                        + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                // Set date on db
                            }
                        }, mYear, mMonth, mDay);

                dateDialog.show();

            }
        });

        ImageButton btnTim = (ImageButton) promptView.findViewById(R.id.popup_clock);
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView txtTime = (TextView) promptView.findViewById(R.id.popup_txtTime);

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectMinute) {
                                txtTime.setText(String.format(Locale.US, "At : %02d:%02d",
                                        selectedHour, selectMinute));
                                // Set time on db
                            }
                        }, hour, minute, true);

                timeDialog.show();
            }
        });

        ImageButton btnBack = (ImageButton) promptView.findViewById(R.id.popup_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setView(promptView);
        dialog.show();
    }

    // Add a new task to the list and the db by prompting an input window
    public void addTask(View view) {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a todo task")
                .setMessage("What todo task is it ?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskClass tmp = new TaskClass();
                        String task = String.valueOf(taskEditText.getText());
                        tmp.setTaskTitle(task);
                        tmp.setTaskYear(0);
                        tmp.setTaskMonth(0);
                        tmp.setTaskDay(0);
                        tmp.setTaskHour(0);
                        tmp.setTaskMinute(0);
                        Log.i(LOG_TAG, "Created new task : " + tmp.getTaskTitle());
                        dbHelper.insertNewTask(tmp);
                        loadTaskList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    // Delete the current task from list and db
    public void deleteTask(ArrayList<TaskClass> list, int position) {
        dbHelper.deleteTask(list.get(position));
        loadTaskList();
    }
}
