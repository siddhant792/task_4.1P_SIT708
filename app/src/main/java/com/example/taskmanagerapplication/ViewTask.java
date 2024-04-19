package com.example.taskmanagerapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewTask extends AppCompatActivity {

    private TextView tv_title, tv_description, tv_due_date;
    private ImageView edit, delete;

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    String[] d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_task);
        Intent intent = getIntent();
        d = intent.getStringArrayExtra("data");
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);
        tv_due_date = findViewById(R.id.tv_due_date);
        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);

        tv_title.setText("Title: " + d[1]);
        tv_description.setText("Description: " + d[2]);
        tv_due_date.setText("Due on: " + d[3]);

        edit.setOnClickListener(v -> {
            Intent i = new Intent(this, CreateEditTask.class);
            i.putExtra("data", d);
            startActivity(i);
        });

        delete.setOnClickListener(v -> {
            database.delete("my_table", "id = ?", new String[]{String.valueOf(d[0])});
            Toast.makeText(this, "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}