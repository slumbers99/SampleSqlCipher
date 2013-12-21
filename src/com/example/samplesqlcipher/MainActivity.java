package com.example.samplesqlcipher;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.widget.ListView;

import com.example.samplesqlcipher.sqlite.OpenHelper;
import com.example.samplesqlcipher.sqlite.table.Post;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    private CursorAdapter mAdapter;

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] from = new String[] { Post.Column.TITLE.getColumnName(),
                Post.Column.BODY.getColumnName(), };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        this.mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
                from, to, 0);
        ListView listView = (ListView) this.findViewById(android.R.id.list);
        listView.setAdapter(this.mAdapter);

        LoaderManager loaderManager = this.getSupportLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
     */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

        return new ListItemLoader(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
     */
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        this.mAdapter.swapCursor(arg1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
     */
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        this.mAdapter.swapCursor(null);
    }

    /**
     * @author peko
     * 
     */
    private static class ListItemLoader extends AsyncTaskLoader<Cursor> {

        public ListItemLoader(Context context) {
            super(context);
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.support.v4.content.Loader#onStartLoading()
         */
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            this.forceLoad();
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
         */
        @Override
        public Cursor loadInBackground() {
            Context context = this.getContext();
            OpenHelper openHelper = OpenHelper.getInstance(context);
            SQLiteDatabase database = openHelper.getWritableDatabase();
            Post post = new Post(database);
            return post.selectAll();
        }
    }

}
