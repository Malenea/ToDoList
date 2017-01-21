package com.malenea.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by conan on 11/01/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="ToDoListDb.db";
    private static final int DB_VER = 1;
    public static final String DB_TABLE="Task";

    public static final String DB_ID="id";
    public static final String DB_COLUMN_TITLE="TaskName";
    public static final String DB_COLUMN_DESC="TaskDesc";

    public static final String DB_COLUMN_YEAR="TaskYear";
    public static final String DB_COLUMN_MONTH="TaskMonth";
    public static final String DB_COLUMN_DAY="TaskDay";
    public static final String DB_COLUMN_HOUR_BEGIN="TaskHourBegin";
    public static final String DB_COLUMN_MINUTE_BEGIN="TaskMinuteBegin";
    public static final String DB_COLUMN_HOUR_END="TaskHourEnd";
    public static final String DB_COLUMN_MINUTE_END="TaskMinuteEnd";
    public static final String DB_COLUMN_CALENDAR_ID="TaskCalId";

    public static final String DB_COLUMN_CATEGORY="TaskCategory";
    public static final String DB_COLUMN_STATUS="TaskStatus";

    private static String LOG_TAG = "dbHelper";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DB_TABLE + " (" +
                DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_COLUMN_TITLE + " TEXT NOT NULL, " +
                DB_COLUMN_DESC + " TEXT, " +
                DB_COLUMN_YEAR + " INTEGER, " +
                DB_COLUMN_MONTH + " INTEGER, " +
                DB_COLUMN_DAY + " INTEGER, " +
                DB_COLUMN_HOUR_BEGIN + " INTEGER, " +
                DB_COLUMN_MINUTE_BEGIN + " INTEGER, " +
                DB_COLUMN_HOUR_END + " INTEGER, " +
                DB_COLUMN_MINUTE_END + " INTEGER, " +
                DB_COLUMN_CALENDAR_ID + " BIGINT, " +
                DB_COLUMN_CATEGORY + " INTEGER, " +
                DB_COLUMN_STATUS + " INTEGER" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    public void insertNewTask(TaskClass task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_TITLE, task.getTaskTitle());
        values.put(DB_COLUMN_DESC, task.getTaskDesc());
        values.put(DB_COLUMN_YEAR, task.getTaskYear());
        values.put(DB_COLUMN_MONTH, task.getTaskMonth());
        values.put(DB_COLUMN_DAY, task.getTaskDay());
        values.put(DB_COLUMN_HOUR_BEGIN, task.getTaskHourBegin());
        values.put(DB_COLUMN_MINUTE_BEGIN, task.getTaskMinuteBegin());
        values.put(DB_COLUMN_HOUR_END, task.getTaskHourEnd());
        values.put(DB_COLUMN_MINUTE_END, task.getTaskMinuteEnd());
        values.put(DB_COLUMN_CALENDAR_ID, task.getTaskCalId());
        values.put(DB_COLUMN_CATEGORY, task.getTaskCat());
        values.put(DB_COLUMN_STATUS, task.getTaskStatus());
        db.insert(DB_TABLE, null, values);
        db.close();
    }

    public void deleteTask(TaskClass task) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(LOG_TAG, "Deleting id : " + task.getTaskId());
        db.delete(DB_TABLE, DB_ID + " = " + task.getTaskId(), null);
        db.close();
    }

    public int updateTask(TaskClass task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_TITLE, task.getTaskTitle());
        values.put(DB_COLUMN_DESC, task.getTaskDesc());
        values.put(DB_COLUMN_YEAR, task.getTaskYear());
        values.put(DB_COLUMN_MONTH, task.getTaskMonth());
        values.put(DB_COLUMN_DAY, task.getTaskDay());
        values.put(DB_COLUMN_HOUR_BEGIN, task.getTaskHourBegin());
        values.put(DB_COLUMN_MINUTE_BEGIN, task.getTaskMinuteBegin());
        values.put(DB_COLUMN_HOUR_END, task.getTaskHourEnd());
        values.put(DB_COLUMN_MINUTE_END, task.getTaskMinuteEnd());
        values.put(DB_COLUMN_CALENDAR_ID, task.getTaskCalId());
        values.put(DB_COLUMN_CATEGORY, task.getTaskCat());
        values.put(DB_COLUMN_STATUS, task.getTaskStatus());
        Log.i(LOG_TAG, "Updating id : " + task.getTaskId());
        return db.update(DB_TABLE, values, DB_ID + " = " + task.getTaskId(), null);
    }

    public ArrayList<TaskClass> getTaskList(int stat, int cat, String search) {
        ArrayList<TaskClass> taskList = new ArrayList<>();
        String selectQuery;
        if (search == null) {
            if (stat == 0) {
                if (cat == 0) {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                } else {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " WHERE " + DB_COLUMN_CATEGORY + " = " + (cat - 1) +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                }
            } else {
                if (cat == 0) {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " WHERE " + DB_COLUMN_STATUS + " = " + (stat - 1) +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                } else {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " WHERE " +
                            DB_COLUMN_CATEGORY + " = " + (cat - 1) +
                            " AND " +
                            DB_COLUMN_STATUS + " = " + (stat - 1) +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                }
            }
        } else {
            if (stat == 0) {
                if (cat == 0) {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " WHERE " + DB_COLUMN_TITLE + " LIKE \"%" + search + "%\"" +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                } else {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " WHERE " + DB_COLUMN_CATEGORY + " = " + (cat - 1) +
                            " AND " + DB_COLUMN_TITLE + " LIKE \"%" + search + "%\"" +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                }
            } else {
                if (cat == 0) {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " WHERE " + DB_COLUMN_STATUS + " = " + (stat - 1) +
                            " AND " + DB_COLUMN_TITLE + " LIKE \"%" + search + "%\"" +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                } else {
                    selectQuery = "SELECT * FROM " + DB_TABLE +
                            " WHERE " +
                            DB_COLUMN_CATEGORY + " = " + (cat - 1) +
                            " AND " +
                            DB_COLUMN_STATUS + " = " + (stat - 1) +
                            " AND " + DB_COLUMN_TITLE + " LIKE\"%" + search + "%\"" +
                            " ORDER BY " + DB_COLUMN_YEAR + " DESC, " +
                            DB_COLUMN_MONTH + " DESC, " +
                            DB_COLUMN_DAY + " DESC, " +
                            DB_COLUMN_HOUR_BEGIN + " DESC, " +
                            DB_COLUMN_MINUTE_BEGIN + " DESC";
                }
            }
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TaskClass task = new TaskClass();
                task.setTaskId(cursor.getInt(cursor.getColumnIndex(DB_ID)));
                task.setTaskTitle(cursor.getString(cursor.getColumnIndex(DB_COLUMN_TITLE)));
                task.setTaskDesc(cursor.getString(cursor.getColumnIndex(DB_COLUMN_DESC)));
                task.setTaskYear(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_YEAR)));
                task.setTaskMonth(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_MONTH)));
                task.setTaskDay(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_DAY)));
                task.setTaskHourBegin(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_HOUR_BEGIN)));
                task.setTaskMinuteBegin(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_MINUTE_BEGIN)));
                task.setTaskHourEnd(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_HOUR_END)));
                task.setTaskMinuteEnd(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_MINUTE_END)));
                task.setTaskCalId(cursor.getLong(cursor.getColumnIndex(DB_COLUMN_CALENDAR_ID)));
                task.setTaskCat(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_CATEGORY)));
                task.setTaskStatus(cursor.getInt(cursor.getColumnIndex(DB_COLUMN_STATUS)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return taskList;
    }
}
