package com.example.taskmanagerapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

public class CreateEditTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText et_title, et_description;
    private TextView title;
    private Button btn_pick, btnAddTaskToDb;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private String pickedDate = "", pickedTime = "";
    private String[] d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_edit_task);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);
        btn_pick = findViewById(R.id.buttonPickDateTime);
        btnAddTaskToDb = findViewById(R.id.btnAddTaskToDb);
        title = findViewById(R.id.test);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        Intent intent = getIntent();
        d = intent.getStringArrayExtra("data");
        if(d != null) {
            title.setText("Editing Task: " + d[0]);
            et_title.setText(d[1]);
            et_description.setText(d[2]);
            btnAddTaskToDb.setText("Update");
        } else {
            d = null;
        }
        btn_pick.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    CreateEditTask.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            TimePickerDialog tpd = TimePickerDialog.newInstance(CreateEditTask.this, true);
            dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            tpd.show(getSupportFragmentManager(), "Timepickerdialog");
        });
        btnAddTaskToDb.setOnClickListener(v -> {
            addDataToDB(d);
        });
    }

    private void addDataToDB(String[] d) {
        if(et_title.getText().toString().isEmpty() || et_description.getText().toString().isEmpty()) {
            if(d == null && (pickedDate.isEmpty() || pickedTime.isEmpty())) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        ContentValues values = new ContentValues();
        if(d == null) {
            values.put("title", et_title.getText().toString());
            values.put("description", et_description.getText().toString());
            values.put("dueDate", pickedDate + " - " + pickedTime);
            database.insert("my_table", null, values);
            Toast.makeText(this, "Task has been added successfully", Toast.LENGTH_SHORT).show();
        }else {
            values.put("title", et_title.getText().toString());
            values.put("description", et_description.getText().toString());
            values.put("dueDate", d[3]);
            database.update("my_table", values, "id = ?", new String[]{String.valueOf(d[0])});
            Toast.makeText(this, "Task has been updated successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        pickedDate = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        pickedTime = hourOfDay+"h"+minute+"m"+second;
    }
}