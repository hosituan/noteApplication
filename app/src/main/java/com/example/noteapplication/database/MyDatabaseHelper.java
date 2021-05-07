package com.example.noteapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.noteapplication.bean.Category;
import com.example.noteapplication.bean.Note;
import com.example.noteapplication.bean.Priority;
import com.example.noteapplication.bean.Status;
import com.example.noteapplication.bean.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Note_Manager";

    // Table name: Note.
    private static final String TABLE_NOTE = "Note";

    private static final String COLUMN_NOTE_TITLE ="Note_Title";
    private static final String COLUMN_NOTE_CATEGORY ="Category";
    private static final String COLUMN_NOTE_PRIORITY ="Priority";
    private static final String COLUMN_NOTE_STATUS ="Status";
    private static final String COLUMN_NOTE_DATE ="Date";

    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_EMAIL = "Email";
    private static final String COLUMN_USER_PASSWORD = "Password";
    private static final String COLUMN_USER_NAME = "Name";

    private static final String TABLE_CATEGORY = "Category";
    private static final String COLUMN_CATEGORY_TITLE = "Title";
    private static final String COLUMN_CATEGORY_DATE = "Date";

    private static final String TABLE_PRIORITY = "Priority";
    private static final String COLUMN_PRIORITY_TITLE = "Title";
    private static final String COLUMN_PRIORITY_DATE = "Date";

    private static final String TABLE_STATUS = "Status";
    private static final String COLUMN_STATUS_TITLE = "Title";
    private static final String COLUMN_STATUS_DATE = "Date";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    SQLiteDatabase db;
    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script_note = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_TITLE + " TEXT PRIMARY KEY,"
                +  COLUMN_NOTE_CATEGORY + " TEXT, " + COLUMN_NOTE_PRIORITY + " TEXT," + COLUMN_NOTE_STATUS + " TEXT," + COLUMN_NOTE_DATE + " TEXT)";
        // Execute Script.
        db.execSQL(script_note);

        String script_user = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_EMAIL + " TEXT PRIMARY KEY," + COLUMN_USER_PASSWORD + " TEXT, "+ COLUMN_USER_NAME + " TEXT)";
        // Execute Script.
        db.execSQL(script_user);

        String script_category = "CREATE TABLE " + TABLE_CATEGORY + "("
                + COLUMN_CATEGORY_TITLE + " TEXT PRIMARY KEY," + COLUMN_CATEGORY_DATE + " TEXT)";

        db.execSQL(script_category);

        String script_priority = "CREATE TABLE " + TABLE_PRIORITY + "("
                + COLUMN_PRIORITY_TITLE + " TEXT PRIMARY KEY," + COLUMN_PRIORITY_DATE + " TEXT)";

        db.execSQL(script_priority);

        String script_status= "CREATE TABLE " + TABLE_STATUS + "("
                + COLUMN_STATUS_TITLE + " TEXT PRIMARY KEY," + COLUMN_STATUS_DATE + " TEXT)";

        db.execSQL(script_status);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        // Create tables again
        onCreate(db);
    }


    // Note --



    public void addNote(Note note) {
        this.db = getWritableDatabase();
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + note.getNoteTitle());
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getNoteTitle());
        values.put(COLUMN_NOTE_CATEGORY, note.getCategory());
        values.put(COLUMN_NOTE_PRIORITY, note.getPriority());
        values.put(COLUMN_NOTE_STATUS, note.getStatus());
        values.put(COLUMN_NOTE_DATE, note.getDate().toString());

        // Inserting Row
        db.insert(TABLE_NOTE, null, values);

        // Closing database connection
        db.close();
    }

    public boolean deleteNote(Note note) {
        this.db = getWritableDatabase();
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + note.getNoteTitle() );
        boolean result = db.delete(TABLE_NOTE, COLUMN_NOTE_DATE + " = ?",
                new String[] { String.valueOf(note.getDate()) }) > 0;
        db.close();
        return  result;
    }

    public boolean updateNote(Note note) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getNoteTitle());
        values.put(COLUMN_NOTE_CATEGORY, note.getCategory());
        values.put(COLUMN_NOTE_PRIORITY, note.getPriority());
        values.put(COLUMN_NOTE_STATUS, note.getStatus());
        boolean result = db.update(TABLE_NOTE, values,COLUMN_NOTE_DATE + " = ?",
                new String[] { String.valueOf(note.getDate()) }) > 0;
        db.close();
        return  result;
    }

    public List<Note> getAllNotes() throws ParseException {
        this.db = getWritableDatabase();
        List<Note> noteList = new ArrayList<Note>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String dateStr = cursor.getString(4);
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateStr);
                Note note = new Note(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), date);
                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " +  cursor.getCount());
        // return note list
        return noteList;
    }

    // User --

    public  void addUser(User user) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        Log.i(TAG, "Added user");
        db.close();
    }


    public Boolean checkUser(User user) {
        this.db = getWritableDatabase();
        String checkQuery = "SELECT  * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + "='" + user.getEmail() + "' AND " + COLUMN_USER_PASSWORD + "='" + user.getPassword() + "'";
        Cursor cursor = db.rawQuery(checkQuery, null);

        Log.i(TAG, "MyDatabaseHelper.getNote ... " + cursor.getCount());
        if (cursor.getCount() == 1) {
            return  true;
        }
        return  false;
    }

    public boolean changePassword(User user) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        boolean result = db.update(TABLE_USER, values,  COLUMN_USER_EMAIL +"='" + user.getEmail() + "'", null) > 0;
        db.close();
        return  result;
    }


    public User getUser(String email) {
        this.db = getWritableDatabase();
        String checkQuery = "SELECT  * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + "='" + email +"'";
        Cursor cursor = db.rawQuery(checkQuery, null);

        Log.i(TAG, "MyDatabaseHelper.getNote ... " + cursor.getCount());
        if (cursor.moveToFirst()) {
            String password = cursor.getString(1);
            String name = cursor.getString(2);
            return new User(email, password, name);
        }
        return  null;
    }

    public  boolean changeName(User user) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        boolean result = db.update(TABLE_USER, values,  COLUMN_USER_EMAIL +"= '" + user.getEmail() + "'", null) > 0;
        db.close();
        return  result;
    }





    //  -- Category

    public  void addCategory(Category category) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_TITLE, category.getTitle());
        values.put(COLUMN_CATEGORY_DATE, category.getDate().toString());
        db.insert(TABLE_CATEGORY, null, values);
        Log.i(TAG, "Added category");
        db.close();
    }

    public boolean deleteCategory(Category category) {
        this.db = getWritableDatabase();
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " +category.getTitle());
        boolean result = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_TITLE + " = ?",
                new String[] { String.valueOf(category.getTitle())}) > 0;
        boolean result_note = db.delete(TABLE_NOTE, COLUMN_NOTE_CATEGORY + " = ?",
                new String[] { String.valueOf(category.getTitle())}) > 0;
        db.close();
        return  result;
    }

    public boolean updateCategory(Category category) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_TITLE, category.getTitle());
        boolean result = db.update(TABLE_CATEGORY, values,COLUMN_CATEGORY_DATE + " = ?",
                new String[] { String.valueOf(category.getDate()) }) > 0;
        db.close();
        Log.i(TAG, String.valueOf(category.getDate()));
        return  result;
    }

    public List<Category> getAllCategory() throws ParseException {
        this.db = getWritableDatabase();
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<Category> categoryList = new ArrayList<Category>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String dateStr = cursor.getString(1);
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateStr);
                Category cate = new Category(cursor.getString(0), date);
                // Adding note to list
                categoryList.add(cate);
            } while (cursor.moveToNext());
        }

        // return note list
        return categoryList;
    }


    // -- Priority
    public  void addPriority(Priority priority) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CATEGORY_TITLE, priority.getTitle());
        values.put(COLUMN_CATEGORY_DATE, priority.getDate().toString());
        db.insert(TABLE_PRIORITY, null, values);
        Log.i(TAG, "Added priority");
        db.close();
    }

    public boolean deletePriority(Priority priority) {
        this.db = getWritableDatabase();
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " +priority.getTitle());
        boolean result = db.delete(TABLE_PRIORITY, COLUMN_PRIORITY_TITLE + " = ?",
                new String[] { String.valueOf(priority.getTitle())}) > 0;
        boolean result_note = db.delete(TABLE_NOTE, COLUMN_NOTE_PRIORITY + " = ?",
                new String[] { String.valueOf(priority.getTitle())}) > 0;
        db.close();
        return  result;
    }

    public boolean updatePriority(Priority priority) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRIORITY_TITLE, priority.getTitle());
        boolean result = db.update(TABLE_PRIORITY, values,COLUMN_PRIORITY_DATE + " = ?",
                new String[] { String.valueOf(priority.getDate()) }) > 0;
        db.close();
        return  result;
    }

    public List<Priority> getAllPriority() throws ParseException {
        this.db = getWritableDatabase();
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<Priority> categoryList = new ArrayList<Priority>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRIORITY;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String dateStr = cursor.getString(1);
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateStr);
                Priority cate = new Priority(cursor.getString(0), date);
                // Adding note to list
                categoryList.add(cate);
            } while (cursor.moveToNext());
        }

        // return note list
        return categoryList;
    }

    // -- Status

    public  void addStatus(Status status) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_TITLE, status.getTitle());
        values.put(COLUMN_CATEGORY_DATE, status.getDate().toString());
        db.insert(TABLE_STATUS, null, values);
        Log.i(TAG, "Added status");
        db.close();
    }

    public boolean deleteStatus(Status status) {
        this.db = getWritableDatabase();
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " +status.getTitle());
        boolean result = db.delete(TABLE_STATUS, COLUMN_STATUS_TITLE + " = ?",
                new String[] { String.valueOf(status.getTitle())}) > 0;
        boolean result_note = db.delete(TABLE_NOTE, COLUMN_NOTE_STATUS + " = ?",
                new String[] { String.valueOf(status.getTitle())}) > 0;
        db.close();
        return  result;
    }

    public boolean updateStatus(Status status) {
        this.db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS_TITLE, status.getTitle());
        boolean result = db.update(TABLE_STATUS, values,COLUMN_STATUS_DATE + " = ?",
                new String[] { String.valueOf(status.getDate()) }) > 0;
        db.close();
        return  result;
    }

    public List<Status> getAllStatus() throws ParseException { ;
        this.db = getWritableDatabase();
        List<Status> categoryList = new ArrayList<Status>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STATUS;

        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String dateStr = cursor.getString(1);
                Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateStr);
                Status cate = new Status(cursor.getString(0), date);
                // Adding note to list
                categoryList.add(cate);
            } while (cursor.moveToNext());
        }

        // return note list
        return categoryList;
    }


    public int countStatus(String status){
        int count = 0;
        this.db = getWritableDatabase();
        //String selectQuery = "SELECT COUNT(*) FROM " + TABLE_NOTE + " WHERE " + COLUMN_NOTE_STATUS +" ='Done' ";
        String selectQuery = "SELECT COUNT(*)FROM Note WHERE Status ='" + status +"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                count=cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return count;
    }

}