package com.malenea.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by conan on 11/01/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="ToDoListDb";
    private static final int DB_VER = 1;
    public static final String DB_TABLE="Task";
    public static final String DB_ID="id";
    public static final String DB_COLUMN_TITLE="TaskName";

    public static final String DB_COLUMN_YEAR="TaskYear";
    public static final String DB_COLUMN_MONTH="TaskMonth";
    public static final String DB_COLUMN_DAY="TaskDay";
    public static final String DB_COLUMN_HOUR="TaskHour";
    public static final String DB_COLUMN_MINUTE="TaskMinute";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DB_TABLE + " (" +
                DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DB_COLUMN_TITLE + " TEXT, " +
                DB_COLUMN_YEAR + " TEXT, " +
                DB_COLUMN_MONTH + " TEXT, " +
                DB_COLUMN_DAY + " TEXT, " +
                DB_COLUMN_HOUR + " TEXT, " +
                DB_COLUMN_MINUTE + " TEXT" +
                ");";
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
        values.put(DB_COLUMN_YEAR, String.valueOf(task.getTaskYear()));
        values.put(DB_COLUMN_MONTH, String.valueOf(task.getTaskMonth()));
        values.put(DB_COLUMN_DAY, String.valueOf(task.getTaskDay()));
        values.put(DB_COLUMN_HOUR, String.valueOf(task.getTaskHour()));
        values.put(DB_COLUMN_MINUTE, String.valueOf(task.getTaskMinute()));
        db.insert(DB_TABLE, null, values);
        db.close();
    }

    public void deleteTask(TaskClass task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_ID + " = ?",
                new String[]{String.valueOf(task.getTaskId())});
        db.close();
    }

    public ArrayList<TaskClass> getTaskList() {
        ArrayList<TaskClass> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DB_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TaskClass task = new TaskClass();
                task.setTaskId(Integer.parseInt(cursor.getString(0)));
                task.setTaskTitle(cursor.getString(1));
                task.setTaskYear(Integer.parseInt(cursor.getString(2)));
                task.setTaskYear(Integer.parseInt(cursor.getString(3)));
                task.setTaskYear(Integer.parseInt(cursor.getString(4)));
                task.setTaskYear(Integer.parseInt(cursor.getString(5)));
                task.setTaskYear(Integer.parseInt(cursor.getString(6)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }
}
