package com.malenea.todolist;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
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
import java.util.TimeZone;

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

    private static boolean order_choice = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/MyFont.otf");

        TextView tx = (TextView) findViewById(R.id.program_title);
        tx.setTypeface(custom_font);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrderChoice();
            }
        });

        TextView tx_info = (TextView) findViewById(R.id.information);
        tx_info.setTextColor(Color.RED);
        tx_info.setText("");

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
        if (loadTaskList(null).isEmpty()) {
            tx_info.setText("Task list is empty");
        } else {
            tx_info.setText("");
        }
    }

    private void changeOrderChoice() {
        TextView title = (TextView) findViewById(R.id.program_title);
        order_choice = order_choice ? false : true;
        title.setText(!order_choice ? "! tsiL odoT yM" : "My Todo List !");
        loadTaskList(null);
    }

    private ArrayList<TaskClass> loadTaskList(String search) {
        mRecyclerView = (RecyclerView) findViewById(R.id.listTask);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyRecyclerViewAdapter(getDataSet(status_state_choice, cat_state_choice,
                search));
        listHandler = new ArrayList<>(getDataSet(status_state_choice, cat_state_choice, search));

        mRecyclerView.setAdapter(mAdapter);
        return listHandler;
    }

    private ArrayList<TaskClass> getDataSet(int stat, int cat, String search) {
        ArrayList<TaskClass> results = dbHelper.getTaskList(stat, cat, search, order_choice);
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

        TextView tx_at = (TextView) promptView.findViewById(R.id.text_at);
        tx_at.setTypeface(custom_font);
        TextView tx_time_begin = (TextView) promptView.findViewById(R.id.popup_txtTime_Begin);
        tx_time_begin.setTypeface(custom_font);
        TextView tx_to = (TextView) promptView.findViewById(R.id.text_to);
        tx_to.setTypeface(custom_font);
        TextView tx_time_end = (TextView) promptView.findViewById(R.id.popup_txtTime_End);
        tx_time_end.setTypeface(custom_font);

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
                taskEditText.setText(taskList.get(position).getTaskTitle());
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

        if (taskList.get(position).getTaskYear() == 9999 ||
                taskList.get(position).getTaskMonth() == 99 ||
                taskList.get(position).getTaskDay() == 99) {
            tx_date.setText("No date set yet.");
        } else {
            tx_date.setText("For the : " +
                    taskList.get(position).getTaskDay() + "/" +
                    taskList.get(position).getTaskMonth() + "/" +
                    taskList.get(position).getTaskYear());
        }
        if (taskList.get(position).getTaskHourBegin() == 99 ||
                taskList.get(position).getTaskMinuteBegin() == 99) {
            tx_time_begin.setText("??:??");
        } else {
            tx_time_begin.setText(String.format(Locale.US, "%02d:%02d",
                    taskList.get(position).getTaskHourBegin(),
                    taskList.get(position).getTaskMinuteBegin()));
        }
        if (taskList.get(position).getTaskHourEnd() == 99 ||
                taskList.get(position).getTaskMinuteEnd() == 99) {
            tx_time_end.setText("??:??");
        } else {
            tx_time_end.setText(String.format(Locale.US, "%02d:%02d",
                    taskList.get(position).getTaskHourEnd(),
                    taskList.get(position).getTaskMinuteEnd()));
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
                int ret = dbHelper.updateTask(taskList.get(position));
                Log.i(LOG_TAG, "Updated status : " + ret);
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
                int ret = dbHelper.updateTask(taskList.get(position));
                Log.i(LOG_TAG, "Updated category : " + ret);
            }
        });

        ImageButton btnDel = (ImageButton) promptView.findViewById(R.id.popup_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(taskList, position);
                Toast.makeText(MainActivity.this, "Deleted task", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        final Calendar c = Calendar.getInstance();

        ImageButton btnCal = (ImageButton) promptView.findViewById(R.id.popup_calendar_btn);
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskList.get(position).getTaskYear() == 9999 ||
                        taskList.get(position).getTaskMonth() == 99 ||
                        taskList.get(position).getTaskDay() == 99) {
                    Toast.makeText(MainActivity.this,
                            "Please enter a valid date before continuing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (taskList.get(position).getTaskHourBegin() == 99 ||
                        taskList.get(position).getTaskMinuteBegin() == 99) {
                    Toast.makeText(MainActivity.this,
                            "Please enter a valid start time before continuing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (taskList.get(position).getTaskHourEnd() == 99 ||
                        taskList.get(position).getTaskMinuteEnd() == 99) {
                    Toast.makeText(MainActivity.this,
                            "Please enter a valid end time before continuing",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Put into the Calendar")
                        .setMessage("Do you want to add the todo task to your calendar ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addToCalendar(taskList, position);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
            }
        });

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
                                Toast.makeText(MainActivity.this, "Updated date",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                int ret = dbHelper.updateTask(taskList.get(position));
                Log.i(LOG_TAG, "Updated date : " + ret);
                dateDialog.show();

            }
        });

        tx_time_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView txtTime = (TextView) promptView.findViewById(R.id.popup_txtTime_Begin);

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectMinute) {
                                // Set time on db
                                if (taskList.get(position).getTaskHourEnd() != 99 &&
                                        taskList.get(position).getTaskMinuteEnd() != 99 &&
                                        (taskList.get(position).getTaskHourEnd() < selectedHour ||
                                                (taskList.get(position).getTaskHourEnd()
                                                        == selectedHour &&
                                                        taskList.get(position).getTaskMinuteEnd()
                                                                < selectMinute))) {
                                    Toast.makeText(MainActivity.this,
                                            "Set start time cannot be before end time",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    txtTime.setText(String.format(Locale.US, "%02d:%02d",
                                            selectedHour, selectMinute));
                                    taskList.get(position).setTaskHourBegin(selectedHour);
                                    taskList.get(position).setTaskMinuteBegin(selectMinute);
                                    Toast.makeText(MainActivity.this, "Updated start time",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, hour, minute, true);
                int ret = dbHelper.updateTask(taskList.get(position));
                Log.i(LOG_TAG, "Updated start time : " + ret);
                timeDialog.show();
            }
        });

        tx_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView txtTime = (TextView) promptView.findViewById(R.id.popup_txtTime_End);

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectMinute) {
                                // Set time on db
                                if (taskList.get(position).getTaskHourBegin() != -1 &&
                                        taskList.get(position).getTaskMinuteBegin() != -1 &&
                                        (taskList.get(position).getTaskHourBegin() > selectedHour ||
                                                (taskList.get(position).getTaskHourBegin()
                                                        == selectedHour &&
                                                        taskList.get(position).getTaskMinuteBegin()
                                                                > selectMinute))) {
                                    Toast.makeText(MainActivity.this,
                                            "Set end time cannot be before start time",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    txtTime.setText(String.format(Locale.US, "%02d:%02d",
                                            selectedHour, selectMinute));
                                    taskList.get(position).setTaskHourEnd(selectedHour);
                                    taskList.get(position).setTaskMinuteEnd(selectMinute);
                                    Toast.makeText(MainActivity.this, "Updated end time",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, hour, minute, true);
                int ret = dbHelper.updateTask(taskList.get(position));
                Log.i(LOG_TAG, "Updated end time : " + ret);
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
                Toast.makeText(MainActivity.this, "Updated task", Toast.LENGTH_SHORT).show();
                loadTaskList(null);
                dialog.dismiss();
            }
        });

        dialog.setView(promptView);
        dialog.show();
    }

    private void addToCalendar(ArrayList<TaskClass> taskList, int position) {
        /*
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        calendarIntent.setType("vnd.android.cursor.item/event");
        */
        final Calendar c_begin = Calendar.getInstance();
        c_begin.set(taskList.get(position).getTaskYear(),
                taskList.get(position).getTaskMonth(),
                taskList.get(position).getTaskDay(),
                taskList.get(position).getTaskHourBegin(),
                taskList.get(position).getTaskMinuteBegin());
        final Calendar c_end = Calendar.getInstance();
        c_end.set(taskList.get(position).getTaskYear(),
                taskList.get(position).getTaskMonth(),
                taskList.get(position).getTaskDay(),
                taskList.get(position).getTaskHourEnd(),
                taskList.get(position).getTaskMinuteEnd());
        /*
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                c_begin.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                c_end.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE,
                taskList.get(position).getTaskTitle());
        calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION,
                taskList.get(position).getTaskDesc());
        startActivity(calendarIntent);
        */
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, c_begin.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, c_end.getTimeInMillis());
        values.put(CalendarContract.Events.TITLE, taskList.get(position).getTaskTitle());
        values.put(CalendarContract.Events.DESCRIPTION, taskList.get(position).getTaskDesc());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            final int MY_PERMISSIONS_WRITE_CALENDAR = 0;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_WRITE_CALENDAR);
        } else {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Long eventID = Long.parseLong(uri.getLastPathSegment());
            taskList.get(position).setTaskCalId(eventID);
            dbHelper.updateTask(taskList.get(position));

            Log.i(LOG_TAG, "Added task to calendar : "
                    + taskList.get(position).getTaskTitle() + " with id : " + eventID);
        }
    }

    private void deleteFromCalendar(ArrayList<TaskClass> taskList, int position) {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;

        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI,
                taskList.get(position).getTaskCalId());
        int rows = getContentResolver().delete(deleteUri, null, null);
        Log.i(LOG_TAG, "Rows deleted : " + rows);
    }

    public void searchItem(final View view) {
        ImageButton srchbtn = (ImageButton) findViewById(R.id.search);
        srchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView tx_info = (TextView) findViewById(R.id.information);
                final EditText taskEditText = new EditText(v.getContext());
                final AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Search for a todo task")
                        .setMessage("What is the title ?")
                        .setView(taskEditText)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                if (loadTaskList(task).isEmpty()) {
                                    tx_info.setTextColor(Color.RED);
                                    tx_info.setText("Task list is empty");
                                    tx_info.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (loadTaskList(null).isEmpty()) {
                                                tx_info.setTextColor(Color.RED);
                                                tx_info.setText("Task list is empty");
                                            } else {
                                                tx_info.setText("");
                                            }
                                        }
                                    });
                                } else {
                                    tx_info.setText("");
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
    }

    public void changeStatValue(View view) {
        final TextView tx_info = (TextView) findViewById(R.id.information);
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
        if (loadTaskList(null).isEmpty()) {
            tx_info.setTextColor(Color.RED);
            tx_info.setText("Task list is empty");
        } else {
            tx_info.setText("");
        }
    }

    // Change the search value for put
    public void changeCatValue(View view) {
        final TextView tx_info = (TextView) findViewById(R.id.information);
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
        if (loadTaskList(null).isEmpty()) {
            tx_info.setTextColor(Color.RED);
            tx_info.setText("Task list is empty");
        } else {
            tx_info.setText("");
        }
    }

    // Add a new task to the list and the db by prompting an input window
    public void addTask(View view) {
        final TextView tx_info = (TextView) findViewById(R.id.information);
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
                        tmp.setTaskYear(9999);
                        tmp.setTaskMonth(99);
                        tmp.setTaskDay(99);
                        tmp.setTaskHourBegin(99);
                        tmp.setTaskMinuteBegin(99);
                        tmp.setTaskHourEnd(99);
                        tmp.setTaskMinuteEnd(99);
                        tmp.setTaskCalId(-1L);
                        tmp.setTaskCat(0);
                        tmp.setTaskStatus(0);
                        Log.i(LOG_TAG, "Created new task : " + tmp.getTaskTitle());
                        dbHelper.insertNewTask(tmp);
                        if (loadTaskList(null).isEmpty()) {
                            tx_info.setTextColor(Color.RED);
                            tx_info.setText("Task list is empty");
                        } else {
                            tx_info.setText("");
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    // Delete the current task from list and db
    public void deleteTask(ArrayList<TaskClass> list, int position) {
        final TextView tx_info = (TextView) findViewById(R.id.information);
        dbHelper.deleteTask(list.get(position));
        if (list.get(position).getTaskCalId() > 0) {
            deleteFromCalendar(list, position);
        }
        if (loadTaskList(null).isEmpty()) {
            tx_info.setTextColor(Color.RED);
            tx_info.setText("Task list is empty");
        } else {
            tx_info.setText("");
        }
    }

}
