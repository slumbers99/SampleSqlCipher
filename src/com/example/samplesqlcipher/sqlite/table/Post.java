/**
 * 
 */
package com.example.samplesqlcipher.sqlite.table;

import java.util.Calendar;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

/**
 * @author peko
 * 
 */
public class Post {

    @SuppressWarnings("unused")
    private static final String TAG = Post.class.getSimpleName();

    public static final String TABLE_NAME = Post.class.getSimpleName();

    private SQLiteDatabase mDatabase;

    public enum Column {
        // @formatter:off
        ID("_id", "INTEGER PRIMARY KEY AUTOINCREMENT"), // as long 
        TITLE("title", "TEXT"),
        BODY("body", "TEXT"),
        CREATED("created", "INTEGER"), // as long
        MODIFIED("modified", "INTEGER"), // as long
        ;
        // @formatter:on

        private String mActualColumnName;
        private String mType;

        /**
         * @param actualColumnnName
         * @param type
         */
        private Column(String actualColumnnName, String type) {
            this.mActualColumnName = actualColumnnName;
            this.mType = type;
        }

        /**
         * @return
         */
        public String getColumnName() {
            return this.mActualColumnName;
        }

        /**
         * @return
         */
        public String getType() {
            return this.mType;
        }
    }

    /**
     * @param database
     */
    public static void createTable(SQLiteDatabase database) {
        // @formatter:off
        StringBuilder builder = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(");
        // @formatter:on
        for (Column column : Column.values()) {
            builder.append(column.getColumnName()).append(" ").append(column.getType()).append(",");
        }
        builder.replace(builder.length() - 1, builder.length(), ")");
        String sql = builder.toString();
        database.execSQL(sql);
    }

    /**
     * @param database
     */
    public static void insertDummyValues(SQLiteDatabase database) {
        Post post = new Post(database);
        for (int i = 0; i < 100; i++) {
            post.insert("‚¾‚Ý[‚½‚¢‚Æ‚é" + i, "‚¾‚Ý[‚Ú‚Å[" + i);
        }
    }

    /**
     * @param database
     */
    public Post(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    /**
     * @param title
     * @param body
     * @return
     */
    public long insert(String title, String body) {

        long now = Calendar.getInstance().getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(Column.TITLE.getColumnName(), title);
        values.put(Column.BODY.getColumnName(), body);
        values.put(Column.CREATED.getColumnName(), now);
        values.put(Column.MODIFIED.getColumnName(), now);
        return this.mDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * @return
     */
    public Cursor selectAll() {
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        return this.mDatabase.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having,
                orderBy);
    }

}
