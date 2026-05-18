package ognjen.popovic.eventsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventsApp.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "users";

    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "lozinka";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL" +
                    ");";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        String hashedPassword = PasswordHasher.hashPassword(password);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_USERS, null, values);

        return result != -1;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USER_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USER_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_PASSWORD},
                COLUMN_USER_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)
            );

            cursor.close();

            return PasswordHasher.verifyPassword(password, storedPassword);
        }

        cursor.close();

        return false;
    }

    public String getEmailByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_EMAIL},
                COLUMN_USER_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)
            );

            cursor.close();

            return email;
        }

        cursor.close();

        return "";
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        String hashedPassword = PasswordHasher.hashPassword(newPassword);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, hashedPassword);

        int result = db.update(
                TABLE_USERS,
                values,
                COLUMN_USER_USERNAME + " = ?",
                new String[]{username}
        );

        return result > 0;
    }
}