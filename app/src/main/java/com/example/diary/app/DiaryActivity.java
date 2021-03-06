package com.example.diary.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.diary.app.model.Diary;
import com.example.diary.app.model.DiaryAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class



DiaryActivity extends AppCompatActivity {

    ListView listView;
    SQLiteHelper db;
    ArrayList<Diary> arrayList;
    ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Integer> unDeleteSelect = new ArrayList<Integer>();
    DiaryAdapter diaryAdapter;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SQLiteHelper(this);
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();

        listView = findViewById(R.id.ListviewId);

        arrayList = new ArrayList<Diary>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // ClickListener for floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryActivity.this, AddDataActivity.class);
                startActivity(intent);
            }
        });

        view();//calling view method

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DiaryActivity.this, UpdateActivity.class);
                intent.putExtra("subject", arrayList.get(i).getSubject());
                intent.putExtra("description", arrayList.get(i).getDescription());
                intent.putExtra("listId", arrayList.get(i).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                diaryAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutIdMainActivity:
                finish();
                Toast.makeText(DiaryActivity.this,"Log Out Successful", Toast.LENGTH_LONG).show();
                return true;
            case R.id.search_view:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() { }

    public void view() {
        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            if (cursor.getInt(4) == MainActivity.USER_ID){
                Diary information = new Diary(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                arrayList.add(information);
            }
        }
        Collections.reverse(arrayList);//reversing arrayList for showing data in a proper way

        diaryAdapter = new DiaryAdapter(this, arrayList);//passing context and arrayList to arrayAdapter
        listView.setAdapter(diaryAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//setting choice mode
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {//method for multiChoice option

            //checking state Item on Click mode or not
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                String id = arrayList.get(i).getId();//for getting database Id
                //if double click Item color will be white
                if (selectList.contains(id) && count > 0) {
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                    selectList.remove(id);
                    count--;
                }
                //else item color will be gray
                else {
                    selectList.add(arrayList.get(i).getId());
                    listView.getChildAt(i).setBackgroundColor(Color.GRAY);
                    unDeleteSelect.add(i);//item position storing on new arrayList
                    count++;
                }
                actionMode.setTitle(count + " item selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();//for connecting menu with main menu here
                inflater.inflate(R.menu.selector_layout, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            //this method for taking action like delete,share
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.deleteContextMenuId) {
                    for (String i : selectList) {
                        db.delete(i);
                        Toast.makeText(getApplicationContext(), count + " item Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DiaryActivity.this, DiaryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                    diaryAdapter.notifyDataSetChanged();
                    actionMode.finish();
                    count = 0;
                }
                return true;
            }

            //this method for destroying actionMode
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                for (int i : unDeleteSelect) {
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);//reset all selected item with gray color
                }
                count = 0;//reset count here
                unDeleteSelect.clear();
                selectList.clear();
            }
        });
    }
}
