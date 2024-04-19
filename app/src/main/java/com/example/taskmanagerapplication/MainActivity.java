package com.example.taskmanagerapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TaskAdapter adapter;
    private List<String[]> dataList;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        swipeRefreshLayout = findViewById(R.id.main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_main) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
                if (id == R.id.action_back) {
                    finish();
                    return true;
                }
                return false;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getAllDataSortedByNameDesc();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        findViewById(R.id.btn_add_task).setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateEditTask.class);
            startActivity(intent);
        });
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();

        adapter = new TaskAdapter(this, dataList);
        getAllDataSortedByNameDesc();
    }

    public void getAllDataSortedByNameDesc() {
        dataList.clear();
        Cursor cursor = database.query(
                "my_table",
                null,
                null,
                null,
                null,
                null,
                "dueDate DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getLong(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String dueDate = cursor.getString(cursor.getColumnIndex("dueDate"));
                dataList.add(new String[]{id, title, description, dueDate});
            } while (cursor.moveToNext());
            cursor.close();
        }
        rv.setAdapter(adapter);
    }
}