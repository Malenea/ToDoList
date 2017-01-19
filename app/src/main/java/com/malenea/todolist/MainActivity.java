package com.malenea.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
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
import android.view.MotionEvent;
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
import android.widget.ScrollView;
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
    private static String LOG_TAG = "MainActivity";

    private static ArrayList<TaskClass> listHandler;

    private static int cat_state_choice = 0;
    private static int status_state_choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/MyFont.otf");

        TextView tx = (TextView) findViewById(R.id.program_title);
        tx.setTypeface(custom_font);

        TextView tx_stat = (TextView) findViewById(R.id.status_state);
        tx_stat.setTypeface(custom_font);
        if (status_state_choice == 0) {
            tx_stat.setTextColor(Color.GRAY);
            tx_stat.setText("All");
        } else if (status_state_choice == 1) {
            tx_stat.setTextColor(Color.BLACK);
            tx_stat.setText("?");
        } else if (status_state_choice == 2) {
            tx_stat.setTextColor(Color.RED);
            tx_stat.setText("TBD");
        } else {
            tx_stat.setTextColor(Color.GREEN);
            tx_stat.setText("Done");
        }

        TextView tx_cat = (TextView) findViewById(R.id.cat_state);
        tx_cat.setTypeface(custom_font);
        if (cat_state_choice == 0) {
            tx_cat.setTextColor(Color.GRAY);
            tx_cat.setText("All");
        } else if (cat_state_choice == 1) {
            tx_cat.setTextColor(Color.BLACK);
            tx_cat.setText("?");
        } else if (cat_state_choice == 2) {
            tx_cat.setTextColor(Color.RED);
            tx_cat.setText("Work");
        } else if (cat_state_choice == 3) {
            tx_cat.setTextColor(Color.BLUE);
            tx_cat.setText("Perso");
        } else if (cat_state_choice == 4) {
            tx_cat.setTextColor(Color.CYAN);
            tx_cat.setText("Errands");
        } else {
            tx_cat.setTextColor(Color.GREEN);
            tx_cat.setText("Hobbies");
        }

        dbHelper = new DbHelper(this);
        loadTaskList(status_state_choice, cat_state_choice);
    }

    private void loadTaskList(int stat, int cat) {
        mRecyclerView = (RecyclerView) findViewById(R.id.listTask);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyRecyclerViewAdapter(getDataSet(stat, cat));
        listHandler = new ArrayList<>(getDataSet(stat, cat));

        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<TaskClass> getDataSet(int stat, int cat) {
        ArrayList<TaskClass> results = dbHelper.getTaskList(stat, cat);
        return results;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on " + listHandler.get(position) + " [" + position + "]");
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

        final TextView tx_title = (TextView) promptView.findViewById(R.id.popup_txt);
        tx_title.setTypeface(custom_font);

        TextView tx_date = (TextView) promptView.findViewById(R.id.popup_txtDate);
        tx_date.setTypeface(custom_font);

        TextView tx_time = (TextView) promptView.findViewById(R.id.popup_txtTime);
        tx_time.setTypeface(custom_font);

        TextView tx_status = (TextView) promptView.findViewById(R.id.popup_status);
        tx_status.setTypeface(custom_font);

        TextView tx_cat = (TextView) promptView.findViewById(R.id.popup_cat);
        tx_cat.setTypeface(custom_font);

        final EditText tx_desc = (EditText) promptView.findViewById(R.id.popup_desc);
        tx_desc.setTypeface(custom_font);

        tx_title.setText(taskList.get(position).getTaskTitle());
        tx_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(v.getContext());
                final AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Edit the todo task")
                        .setMessage("What todo task is it ?")
                        .setView(taskEditText)
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                tx_title.setText(task);
                                taskList.get(position).setTaskTitle(task);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        tx_desc.setText(taskList.get(position).getTaskDesc());

        if (taskList.get(position).getTaskStatus() == 0) {
            tx_status.setTextColor(Color.BLACK);
            tx_status.setText("Unknown");
        } else if (taskList.get(position).getTaskStatus() == 1) {
            tx_status.setTextColor(Color.RED);
            tx_status.setText("To be done");
        } else {
            tx_status.setTextColor(Color.GREEN);
            tx_status.setText("Done");
        }

        if (taskList.get(position).getTaskCat() == 0) {
            tx_cat.setTextColor(Color.BLACK);
            tx_cat.setText("?");
        } else if (taskList.get(position).getTaskCat() == 1) {
            tx_cat.setTextColor(Color.RED);
            tx_cat.setText("Work");
        } else if (taskList.get(position).getTaskCat() == 2) {
            tx_cat.setTextColor(Color.BLUE);
            tx_cat.setText("Perso");
        } else if (taskList.get(position).getTaskCat() == 3) {
            tx_cat.setTextColor(Color.CYAN);
            tx_cat.setText("Errands");
        } else {
            tx_cat.setTextColor(Color.MAGENTA);
            tx_cat.setText("Hobbies");
        }

        if (taskList.get(position).getTaskYear() == -1 ||
                taskList.get(position).getTaskMonth() == -1 ||
                taskList.get(position).getTaskDay() == -1) {
            tx_date.setText("No date set yet.");
        } else {
            tx_date.setText("For the : " +
                    taskList.get(position).getTaskDay() + "/" +
                    taskList.get(position).getTaskMonth() + "/" +
                    taskList.get(position).getTaskYear());
        }
        if (taskList.get(position).getTaskHour() == -1 ||
                taskList.get(position).getTaskMinute() == -1) {
            tx_time.setText("No time set yet.");
        } else {
            tx_time.setText(String.format(Locale.US, "At : %02d:%02d",
                    taskList.get(position).getTaskHour(),
                    taskList.get(position).getTaskMinute()));
        }

        tx_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView txtStatus = (TextView) promptView.findViewById(R.id.popup_status);

                if (taskList.get(position).getTaskStatus() == 0) {
                    taskList.get(position).setTaskStatus(1);
                    txtStatus.setTextColor(Color.RED);
                    txtStatus.setText("To be done");
                } else if (taskList.get(position).getTaskStatus() == 1) {
                    taskList.get(position).setTaskStatus(2);
                    txtStatus.setTextColor(Color.GREEN);
                    txtStatus.setText("Done");
                } else {
                    taskList.get(position).setTaskStatus(0);
                    txtStatus.setTextColor(Color.BLACK);
                    txtStatus.setText("Unknown");
                }
            }
        });

        tx_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView txtCat = (TextView) promptView.findViewById(R.id.popup_cat);

                if (taskList.get(position).getTaskCat() == 0) {
                    taskList.get(position).setTaskCat(1);
                    txtCat.setTextColor(Color.RED);
                    txtCat.setText("Work");
                } else if (taskList.get(position).getTaskCat() == 1) {
                    taskList.get(position).setTaskCat(2);
                    txtCat.setTextColor(Color.BLUE);
                    txtCat.setText("Perso");
                } else if (taskList.get(position).getTaskCat() == 2) {
                    taskList.get(position).setTaskCat(3);
                    txtCat.setTextColor(Color.CYAN);
                    txtCat.setText("Errands");
                } else if (taskList.get(position).getTaskCat() == 3) {
                    taskList.get(position).setTaskCat(4);
                    txtCat.setTextColor(Color.GREEN);
                    txtCat.setText("Hobbies");
                } else {
                    taskList.get(position).setTaskCat(0);
                    txtCat.setTextColor(Color.BLACK);
                    txtCat.setText("?");
                }
            }
        });

        ImageButton btnDel = (ImageButton) promptView.findViewById(R.id.popup_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(taskList, position);
                dialog.dismiss();
            }
        });

        final Calendar c = Calendar.getInstance();

        tx_date.setOnClickListener(new View.OnClickListener() {
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
                                txtDate.setText("For the : "
                                        + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                // Set date on db
                                taskList.get(position).setTaskYear(year);
                                taskList.get(position).setTaskMonth(monthOfYear + 1);
                                taskList.get(position).setTaskDay(dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                dateDialog.show();

            }
        });

        tx_time.setOnClickListener(new View.OnClickListener() {
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
                                taskList.get(position).setTaskHour(selectedHour);
                                taskList.get(position).setTaskMinute(selectMinute);
                            }
                        }, hour, minute, true);
                timeDialog.show();
            }
        });

        ImageButton btnBack = (ImageButton) promptView.findViewById(R.id.popup_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = tx_desc.getText().toString();
                if (tmp != null) {
                    taskList.get(position).setTaskDesc(tmp);
                }
                int ret = dbHelper.updateTask(taskList.get(position));
                Log.i(LOG_TAG, "Update returned : " + ret);
                loadTaskList(status_state_choice, cat_state_choice);
                dialog.dismiss();
            }
        });

        dialog.setView(promptView);
        dialog.show();
    }

    public void changeStatValue(View view) {
        TextView tx_stat = (TextView) findViewById(R.id.status_state);
        if (status_state_choice == 0) {
            status_state_choice = 1;
            tx_stat.setTextColor(Color.BLACK);
            tx_stat.setText("?");
        } else if (status_state_choice == 1) {
            status_state_choice = 2;
            tx_stat.setTextColor(Color.RED);
            tx_stat.setText("TBD");
        } else if (status_state_choice == 2) {
            status_state_choice = 3;
            tx_stat.setTextColor(Color.GREEN);
            tx_stat.setText("Done");
        } else {
            status_state_choice = 0;
            tx_stat.setTextColor(Color.GRAY);
            tx_stat.setText("All");
        }
        loadTaskList(status_state_choice, cat_state_choice);
    }

    // Change the search value for put
    public void changeCatValue(View view) {
        TextView tx_cat = (TextView) findViewById(R.id.cat_state);
        if (cat_state_choice == 0) {
            cat_state_choice = 1;
            tx_cat.setTextColor(Color.BLACK);
            tx_cat.setText("?");
        } else if (cat_state_choice == 1) {
            cat_state_choice = 2;
            tx_cat.setTextColor(Color.RED);
            tx_cat.setText("Work");
        } else if (cat_state_choice == 2) {
            cat_state_choice = 3;
            tx_cat.setTextColor(Color.BLUE);
            tx_cat.setText("Perso");
        } else if (cat_state_choice == 3) {
            cat_state_choice = 4;
            tx_cat.setTextColor(Color.CYAN);
            tx_cat.setText("Errands");
        } else if (cat_state_choice == 4) {
            cat_state_choice = 5;
            tx_cat.setTextColor(Color.GREEN);
            tx_cat.setText("Hobbies");
        } else {
            cat_state_choice = 0;
            tx_cat.setTextColor(Color.GRAY);
            tx_cat.setText("All");
        }
        loadTaskList(status_state_choice, cat_state_choice);
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
                        tmp.setTaskDesc("No note associated to this todo task yet.");
                        tmp.setTaskYear(-1);
                        tmp.setTaskMonth(-1);
                        tmp.setTaskDay(-1);
                        tmp.setTaskHour(-1);
                        tmp.setTaskMinute(-1);
                        tmp.setTaskCat(0);
                        tmp.setTaskStatus(0);
                        Log.i(LOG_TAG, "Created new task : " + tmp.getTaskTitle());
                        dbHelper.insertNewTask(tmp);
                        loadTaskList(status_state_choice, cat_state_choice);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    // Delete the current task from list and db
    public void deleteTask(ArrayList<TaskClass> list, int position) {
        dbHelper.deleteTask(list.get(position));
        loadTaskList(status_state_choice, cat_state_choice);
    }
}
