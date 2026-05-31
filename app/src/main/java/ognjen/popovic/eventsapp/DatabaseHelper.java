package ognjen.popovic.eventsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventsApp.db";
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_USERS = "users";

    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "lozinka";

    public static final String TABLE_EVENTS = "events";

    public static final String COLUMN_EVENT_ID = "id";
    public static final String COLUMN_EVENT_SERVER_ID = "serverEventId";
    public static final String COLUMN_EVENT_NAME = "naziv";
    public static final String COLUMN_EVENT_DESCRIPTION = "opis";
    public static final String COLUMN_EVENT_LOCATION = "lokacija";
    public static final String COLUMN_EVENT_DATE_TIME = "datumVreme";
    public static final String COLUMN_EVENT_CATEGORY = "kategorija";
    public static final String COLUMN_EVENT_PROMOTED = "promoted";
    public static final String COLUMN_EVENT_CAPACITY = "kapacitet";
    public static final String COLUMN_EVENT_ATTENDING_COUNT = "brojPrisutnih";
    public static final String COLUMN_EVENT_AVERAGE_RATING = "prosecnaOcena";
    public static final String COLUMN_EVENT_RATING_COUNT = "brojOcena";

    public static final String TABLE_ATTENDANCE = "attendance";

    public static final String COLUMN_ATTENDANCE_ID = "id";
    public static final String COLUMN_ATTENDANCE_USER_ID = "userId";
    public static final String COLUMN_ATTENDANCE_EVENT_ID = "eventId";
    public static final String COLUMN_ATTENDANCE_STATUS = "prisustvo";

    public static final String STATUS_INTERESTED = "ZAINTERESOVAN";
    public static final String STATUS_ATTENDING = "PRISUSTVUJE";

    public static final String TABLE_RATINGS = "ratings";

    public static final String COLUMN_RATING_ID = "id";
    public static final String COLUMN_RATING_USER_ID = "userId";
    public static final String COLUMN_RATING_EVENT_ID = "eventId";
    public static final String COLUMN_RATING_VALUE = "rating";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL" +
                    ");";

    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_SERVER_ID + " TEXT, " +
                    COLUMN_EVENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                    COLUMN_EVENT_LOCATION + " TEXT NOT NULL, " +
                    COLUMN_EVENT_DATE_TIME + " TEXT NOT NULL, " +
                    COLUMN_EVENT_CATEGORY + " TEXT NOT NULL, " +
                    COLUMN_EVENT_PROMOTED + " INTEGER DEFAULT 0 CHECK(" +
                    COLUMN_EVENT_PROMOTED + " IN (0, 1)), " +
                    COLUMN_EVENT_CAPACITY + " INTEGER DEFAULT 0 CHECK(" +
                    COLUMN_EVENT_CAPACITY + " >= 0), " +
                    COLUMN_EVENT_ATTENDING_COUNT + " INTEGER DEFAULT 0 CHECK(" +
                    COLUMN_EVENT_ATTENDING_COUNT + " >= 0), " +
                    COLUMN_EVENT_AVERAGE_RATING + " REAL DEFAULT 0 CHECK(" +
                    COLUMN_EVENT_AVERAGE_RATING + " BETWEEN 0 AND 5), " +
                    COLUMN_EVENT_RATING_COUNT + " INTEGER DEFAULT 0 CHECK(" +
                    COLUMN_EVENT_RATING_COUNT + " >= 0), " +
                    "CHECK(" + COLUMN_EVENT_PROMOTED + " = 0 OR " +
                    COLUMN_EVENT_CAPACITY + " > 0), " +
                    "CHECK(" + COLUMN_EVENT_PROMOTED + " = 0 OR " +
                    COLUMN_EVENT_ATTENDING_COUNT + " <= " +
                    COLUMN_EVENT_CAPACITY + ")" +
                    ");";

    private static final String CREATE_TABLE_ATTENDANCE =
            "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                    COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ATTENDANCE_USER_ID + " INTEGER NOT NULL, " +
                    COLUMN_ATTENDANCE_EVENT_ID + " INTEGER NOT NULL, " +
                    COLUMN_ATTENDANCE_STATUS + " TEXT NOT NULL CHECK(" +
                    COLUMN_ATTENDANCE_STATUS + " IN ('" +
                    STATUS_INTERESTED + "', '" +
                    STATUS_ATTENDING + "')), " +
                    "FOREIGN KEY(" + COLUMN_ATTENDANCE_USER_ID + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_ATTENDANCE_EVENT_ID + ") REFERENCES " +
                    TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "), " +
                    "UNIQUE(" + COLUMN_ATTENDANCE_USER_ID + ", " +
                    COLUMN_ATTENDANCE_EVENT_ID + ")" +
                    ");";

    private static final String CREATE_TABLE_RATINGS =
            "CREATE TABLE " + TABLE_RATINGS + " (" +
                    COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RATING_USER_ID + " INTEGER NOT NULL, " +
                    COLUMN_RATING_EVENT_ID + " INTEGER NOT NULL, " +
                    COLUMN_RATING_VALUE + " INTEGER NOT NULL CHECK(" +
                    COLUMN_RATING_VALUE + " BETWEEN 1 AND 5), " +
                    "FOREIGN KEY(" + COLUMN_RATING_USER_ID + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_RATING_EVENT_ID + ") REFERENCES " +
                    TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "), " +
                    "UNIQUE(" + COLUMN_RATING_USER_ID + ", " +
                    COLUMN_RATING_EVENT_ID + ")" +
                    ");";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_EVENTS);
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_RATINGS);
        insertInitialEvents(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
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

    public boolean insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = createEventContentValues(event);

        long result = db.insert(TABLE_EVENTS, null, values);

        return result != -1;
    }

    public boolean insertOrUpdateServerEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (event.getServerEventId() == null || event.getServerEventId().isEmpty()) {
            return insertEvent(event);
        }

        Cursor cursor = db.query(
                TABLE_EVENTS,
                new String[]{COLUMN_EVENT_ID},
                COLUMN_EVENT_SERVER_ID + " = ?",
                new String[]{event.getServerEventId()},
                null,
                null,
                null
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();

        ContentValues values = createEventContentValues(event);

        if (exists) {
            int result = db.update(
                    TABLE_EVENTS,
                    values,
                    COLUMN_EVENT_SERVER_ID + " = ?",
                    new String[]{event.getServerEventId()}
            );

            return result > 0;
        } else {
            long result = db.insert(TABLE_EVENTS, null, values);

            return result != -1;
        }
    }

    private ContentValues createEventContentValues(Event event) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_EVENT_SERVER_ID, event.getServerEventId());
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());
        values.put(COLUMN_EVENT_LOCATION, event.getLocation());
        values.put(COLUMN_EVENT_DATE_TIME, event.getDateTime());
        values.put(COLUMN_EVENT_CATEGORY, event.getCategory());
        values.put(COLUMN_EVENT_PROMOTED, event.isPromoted() ? 1 : 0);
        values.put(COLUMN_EVENT_CAPACITY, event.getCapacity());
        values.put(COLUMN_EVENT_ATTENDING_COUNT, event.getAttendingCount());
        values.put(COLUMN_EVENT_AVERAGE_RATING, event.getAverageRating());
        values.put(COLUMN_EVENT_RATING_COUNT, event.getRatingCount());

        return values;
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_EVENT_PROMOTED + " DESC"
        );

        while (cursor.moveToNext()) {
            events.add(createEventFromCursor(cursor));
        }

        cursor.close();

        return events;
    }

    public ArrayList<Event> getEventsByCategory(String category) {
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                COLUMN_EVENT_CATEGORY + " = ?",
                new String[]{category},
                null,
                null,
                COLUMN_EVENT_PROMOTED + " DESC"
        );

        while (cursor.moveToNext()) {
            events.add(createEventFromCursor(cursor));
        }

        cursor.close();

        return events;
    }

    public Event findEventByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                COLUMN_EVENT_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            Event event = createEventFromCursor(cursor);
            cursor.close();

            return event;
        }

        cursor.close();

        return null;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                true,
                TABLE_EVENTS,
                new String[]{COLUMN_EVENT_CATEGORY},
                null,
                null,
                null,
                null,
                COLUMN_EVENT_CATEGORY + " ASC",
                null
        );

        while (cursor.moveToNext()) {
            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_EVENT_CATEGORY)
            );

            categories.add(category);
        }

        cursor.close();

        return categories;
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_USERNAME + " = ?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_ID)
            );

            cursor.close();

            return userId;
        }

        cursor.close();

        return -1;
    }

    public int getEventIdByName(String eventName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_EVENTS,
                new String[]{COLUMN_EVENT_ID},
                COLUMN_EVENT_NAME + " = ?",
                new String[]{eventName},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int eventId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)
            );

            cursor.close();

            return eventId;
        }

        cursor.close();

        return -1;
    }

    public String getAttendanceStatus(String username, String eventName) {
        int userId = getUserIdByUsername(username);
        int eventId = getEventIdByName(eventName);

        if (userId == -1 || eventId == -1) {
            return "";
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ATTENDANCE,
                new String[]{COLUMN_ATTENDANCE_STATUS},
                COLUMN_ATTENDANCE_USER_ID + " = ? AND " +
                        COLUMN_ATTENDANCE_EVENT_ID + " = ?",
                new String[]{
                        String.valueOf(userId),
                        String.valueOf(eventId)
                },
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String status = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_ATTENDANCE_STATUS)
            );

            cursor.close();

            return status;
        }

        cursor.close();

        return "";
    }

    public boolean addInterested(String username, String eventName) {
        int userId = getUserIdByUsername(username);
        int eventId = getEventIdByName(eventName);

        if (userId == -1 || eventId == -1) {
            return false;
        }

        String currentStatus =
                getAttendanceStatus(username, eventName);

        if (!currentStatus.isEmpty()) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ATTENDANCE_USER_ID, userId);
        values.put(COLUMN_ATTENDANCE_EVENT_ID, eventId);
        values.put(COLUMN_ATTENDANCE_STATUS, STATUS_INTERESTED);

        long result = db.insert(TABLE_ATTENDANCE, null, values);

        return result != -1;
    }

    public boolean addAttending(String username, String eventName) {
        int userId = getUserIdByUsername(username);
        int eventId = getEventIdByName(eventName);

        if (userId == -1 || eventId == -1) {
            return false;
        }

        Event event = findEventByName(eventName);

        if (event == null) {
            return false;
        }

        if (event.isPromoted()
                && event.getAttendingCount() >= event.getCapacity()) {
            return false;
        }

        String currentStatus =
                getAttendanceStatus(username, eventName);

        if (STATUS_ATTENDING.equals(currentStatus)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {
            if (STATUS_INTERESTED.equals(currentStatus)) {

                ContentValues updateValues = new ContentValues();
                updateValues.put(COLUMN_ATTENDANCE_STATUS, STATUS_ATTENDING);

                db.update(
                        TABLE_ATTENDANCE,
                        updateValues,
                        COLUMN_ATTENDANCE_USER_ID + " = ? AND " +
                                COLUMN_ATTENDANCE_EVENT_ID + " = ?",
                        new String[]{
                                String.valueOf(userId),
                                String.valueOf(eventId)
                        }
                );

            } else {

                ContentValues insertValues = new ContentValues();
                insertValues.put(COLUMN_ATTENDANCE_USER_ID, userId);
                insertValues.put(COLUMN_ATTENDANCE_EVENT_ID, eventId);
                insertValues.put(COLUMN_ATTENDANCE_STATUS, STATUS_ATTENDING);

                db.insert(TABLE_ATTENDANCE, null, insertValues);
            }

            ContentValues eventValues = new ContentValues();
            eventValues.put(
                    COLUMN_EVENT_ATTENDING_COUNT,
                    event.getAttendingCount() + 1
            );

            db.update(
                    TABLE_EVENTS,
                    eventValues,
                    COLUMN_EVENT_ID + " = ?",
                    new String[]{String.valueOf(eventId)}
            );

            db.setTransactionSuccessful();

            return true;

        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Event> getInterestedEvents(String username) {
        return getEventsByAttendanceStatus(username, STATUS_INTERESTED);
    }

    public ArrayList<Event> getAttendingEvents(String username) {
        return getEventsByAttendanceStatus(username, STATUS_ATTENDING);
    }

    private ArrayList<Event> getEventsByAttendanceStatus(String username,
                                                         String status) {
        ArrayList<Event> events = new ArrayList<>();

        int userId = getUserIdByUsername(username);

        if (userId == -1) {
            return events;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT e.* FROM " + TABLE_EVENTS + " e " +
                        "INNER JOIN " + TABLE_ATTENDANCE + " a ON e." +
                        COLUMN_EVENT_ID + " = a." +
                        COLUMN_ATTENDANCE_EVENT_ID + " " +
                        "WHERE a." + COLUMN_ATTENDANCE_USER_ID + " = ? " +
                        "AND a." + COLUMN_ATTENDANCE_STATUS + " = ? " +
                        "ORDER BY e." + COLUMN_EVENT_PROMOTED + " DESC";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{
                        String.valueOf(userId),
                        status
                }
        );

        while (cursor.moveToNext()) {
            events.add(createEventFromCursor(cursor));
        }

        cursor.close();

        return events;
    }

    public boolean addRating(String username, String eventName, int rating) {
        if (rating < 1 || rating > 5) {
            return false;
        }

        int userId = getUserIdByUsername(username);
        int eventId = getEventIdByName(eventName);

        if (userId == -1 || eventId == -1) {
            return false;
        }

        if (hasUserRatedEvent(userId, eventId)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues ratingValues = new ContentValues();
            ratingValues.put(COLUMN_RATING_USER_ID, userId);
            ratingValues.put(COLUMN_RATING_EVENT_ID, eventId);
            ratingValues.put(COLUMN_RATING_VALUE, rating);

            long insertResult =
                    db.insert(TABLE_RATINGS, null, ratingValues);

            if (insertResult == -1) {
                return false;
            }

            updateEventRatingData(db, eventId);

            db.setTransactionSuccessful();

            return true;

        } finally {
            db.endTransaction();
        }
    }

    private boolean hasUserRatedEvent(int userId, int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RATINGS,
                null,
                COLUMN_RATING_USER_ID + " = ? AND " +
                        COLUMN_RATING_EVENT_ID + " = ?",
                new String[]{
                        String.valueOf(userId),
                        String.valueOf(eventId)
                },
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

    private void updateEventRatingData(SQLiteDatabase db, int eventId) {
        String query =
                "SELECT AVG(" + COLUMN_RATING_VALUE + ") AS averageRating, " +
                        "COUNT(" + COLUMN_RATING_VALUE + ") AS ratingCount " +
                        "FROM " + TABLE_RATINGS + " " +
                        "WHERE " + COLUMN_RATING_EVENT_ID + " = ?";

        Cursor cursor =
                db.rawQuery(
                        query,
                        new String[]{String.valueOf(eventId)}
                );

        if (cursor.moveToFirst()) {
            double averageRating =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("averageRating")
                    );

            int ratingCount =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("ratingCount")
                    );

            ContentValues values = new ContentValues();
            values.put(COLUMN_EVENT_AVERAGE_RATING, averageRating);
            values.put(COLUMN_EVENT_RATING_COUNT, ratingCount);

            db.update(
                    TABLE_EVENTS,
                    values,
                    COLUMN_EVENT_ID + " = ?",
                    new String[]{String.valueOf(eventId)}
            );
        }

        cursor.close();
    }

    private Event createEventFromCursor(Cursor cursor) {
        String serverEventId = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_SERVER_ID)
        );

        String name = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)
        );

        String description = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)
        );

        String location = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_LOCATION)
        );

        String dateTime = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE_TIME)
        );

        String category = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_CATEGORY)
        );

        int promotedValue = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_PROMOTED)
        );

        int capacity = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_CAPACITY)
        );

        int attendingCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_ATTENDING_COUNT)
        );

        double averageRating = cursor.getDouble(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_AVERAGE_RATING)
        );

        int ratingCount = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_EVENT_RATING_COUNT)
        );

        boolean isPromoted = promotedValue == 1;

        int imageResId = getImageResIdByCategory(category);

        return new Event(
                serverEventId,
                name,
                description,
                location,
                dateTime,
                category,
                imageResId,
                isPromoted,
                capacity,
                attendingCount,
                averageRating,
                ratingCount
        );
    }

    private int getImageResIdByCategory(String category) {
        if (category.equals("Party")) {
            return R.drawable.party1;
        }

        if (category.equals("Festival")) {
            return R.drawable.festival1;
        }

        if (category.equals("Concert")) {
            return R.drawable.concert1;
        }

        if (category.equals("Stand-Up & Theater")) {
            return R.drawable.theater1;
        }

        if (category.equals("Exhibition")) {
            return R.drawable.exhibition1;
        }

        return R.drawable.exit;
    }

    private void insertInitialEvents(SQLiteDatabase db) {
        insertInitialEvent(db, "Rooftop Party", "Summer rooftop party", "Belgrade",
                "20.07.2026 21:00", "Party", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Beach Party", "Night beach event", "Budva",
                "15.08.2026 22:00", "Party", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "EXIT Festival", "Biggest music festival", "Novi Sad",
                "10.07.2026 18:00", "Festival", 1, 50000, 0, 0, 0);

        insertInitialEvent(db, "Beer Fest", "Beer and music festival", "Belgrade",
                "25.08.2026 17:00", "Festival", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Rock Concert", "Live rock concert", "Nis",
                "12.06.2026 20:00", "Concert", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Drake Live", "World tour concert", "Budapest",
                "05.09.2026 20:00", "Concert", 1, 70000, 0, 0, 0);

        insertInitialEvent(db, "Stand-Up Night", "Comedy evening", "Novi Sad",
                "18.05.2026 19:00", "Stand-Up & Theater", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Hamlet", "Theater performance", "Belgrade",
                "22.06.2026 20:00", "Stand-Up & Theater", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Art Expo", "Modern art exhibition", "Novi Sad",
                "10.06.2026 12:00", "Exhibition", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Photography Expo", "Photography exhibition", "Belgrade",
                "15.07.2026 14:00", "Exhibition", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Old Concert", "Past concert", "Belgrade",
                "10.01.2024 20:00", "Concert", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Winter Party", "Past party", "Kopaonik",
                "01.02.2024 21:00", "Party", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Old Festival", "Past festival", "Novi Sad",
                "15.03.2024 18:00", "Festival", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Jazz Night", "Jazz concert", "Belgrade",
                "15.09.2026 20:00", "Concert", 0, 0, 0, 0, 0);

        insertInitialEvent(db, "Tech Expo", "Technology exhibition", "Novi Sad",
                "11.10.2026 11:00", "Exhibition", 0, 0, 0, 0, 0);
    }

    private void insertInitialEvent(SQLiteDatabase db,
                                    String name,
                                    String description,
                                    String location,
                                    String dateTime,
                                    String category,
                                    int promoted,
                                    int capacity,
                                    int attendingCount,
                                    double averageRating,
                                    int ratingCount) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_SERVER_ID, "");
        values.put(COLUMN_EVENT_NAME, name);
        values.put(COLUMN_EVENT_DESCRIPTION, description);
        values.put(COLUMN_EVENT_LOCATION, location);
        values.put(COLUMN_EVENT_DATE_TIME, dateTime);
        values.put(COLUMN_EVENT_CATEGORY, category);
        values.put(COLUMN_EVENT_PROMOTED, promoted);
        values.put(COLUMN_EVENT_CAPACITY, capacity);
        values.put(COLUMN_EVENT_ATTENDING_COUNT, attendingCount);
        values.put(COLUMN_EVENT_AVERAGE_RATING, averageRating);
        values.put(COLUMN_EVENT_RATING_COUNT, ratingCount);

        db.insert(TABLE_EVENTS, null, values);
    }
}