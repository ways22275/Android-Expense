package com.tom.expense;

import android.Manifest;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnRecyclerViewItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_CONTACT = 110;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 5;
    private ExpenseAdapter adapter;
    private String sorting;
    BroadcastReceiver lastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AddExpenseService.ACTION_LAST_INSERT.equals(intent.getAction())){
                getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //read default sorting column
        sorting = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("pref_sorting", ExpenseContract.COL_DATE);
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString("pref_sorting", sorting)
                .apply();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(
                        new Intent(MainActivity.this, AddActivity.class));
            }
        });
        //setup RecyclerView
        setupRecyclerView();
        //Testing
        //
        /*
        if (ActivityCompat.checkSelfPermission(this, READ_CONTACTS)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_CONTACTS}, REQUEST_CONTACT);
        }else{
            readContacts();
        }
        */
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(lastReceiver,
                new IntentFilter(AddExpenseService.ACTION_LAST_INSERT));
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(lastReceiver);
    }

    private void readContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor =
                resolver.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CONTACT){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readContacts();
            }
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        DBHelper helper = new DBHelper(this, "expense.db", null, 1);
//        Uri uri = ContentUris.withAppendedId(ExpenseContract.CONTENT_URI, 2);
//        Cursor cursor =
//            getContentResolver().query(
//                    uri,
//                    ExpenseContract.CONTENT_URI,
//                    null, null, null, null
//            );
//        Log.d(TAG, "count: " + cursor.getCount());
//                helper.getReadableDatabase()
//                .query("exp", null, null, null, null, null, null);

        adapter = new ExpenseAdapter();
        adapter.setOnRecyclerViewItemClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(
                    this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_sort){
            sorting = (ExpenseContract.COL_DATE.equals(sorting))?
                    ExpenseContract.COL_INFO : ExpenseContract.COL_DATE;
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(View view, Expense expense) {
        Log.d(TAG, "onItemClick: " + expense.getInfo());
        //TODO: start detail
        Intent detail = new Intent(this, DetailActivity.class);
        detail.putExtra("EXPENSE", expense);
        startActivity(detail);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                ExpenseContract.CONTENT_URI,
                null,
                null, null,
                sorting);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,
                               Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}
