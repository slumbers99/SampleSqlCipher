/**
 * 
 */
package com.example.samplesqlcipher.sqlite;

import java.util.UUID;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.samplesqlcipher.sqlite.table.Post;

/**
 * @author peko
 * 
 */
public final class OpenHelper extends SQLiteOpenHelper {

    @SuppressWarnings("unused")
    private static final String TAG = OpenHelper.class.getSimpleName();

    private static final String DB_NAME = "app.db";
    private static final int DB_VERSION = 1;

    private static volatile OpenHelper sInstance;

    private static final String PREF_PARAM_ENCRYPT_KEY = "pref_param_encrypt_key";

    private String mEncryptKey;

    /**
     * @param context
     * @return
     */
    public synchronized static OpenHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new OpenHelper(context);
        }
        return sInstance;
    }

    /**
     * @param context
     */
    private OpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase.loadLibs(context);
        this.generateEncryptKey(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sqlcipher.database.SQLiteOpenHelper#onCreate(net.sqlcipher.database.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase database) {

        Post.createTable(database);
        Post.insertDummyValues(database);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sqlcipher.database.SQLiteOpenHelper#onUpgrade(net.sqlcipher.database.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    /**
     * @param context
     */
    private void generateEncryptKey(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        this.mEncryptKey = sharedPreferences.getString(PREF_PARAM_ENCRYPT_KEY, null);
        if (TextUtils.isEmpty(this.mEncryptKey)) {
            this.mEncryptKey = UUID.randomUUID().toString();
            Editor editor = sharedPreferences.edit();
            editor.putString(PREF_PARAM_ENCRYPT_KEY, this.mEncryptKey);
            editor.commit();
        }
    }

    /**
     * @return
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        return this.getReadableDatabase(this.mEncryptKey);
    }

    /**
     * @return
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        return this.getWritableDatabase(this.mEncryptKey);
    }

}
