package com.example.medicinedatabase;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button insert, about;
    private EditText medName;
    private EditText date;
    private Spinner spinner;
    private DataSource dataSource;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private Intent intent;
    private Pattern pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Creating a notification channel
        createNotificationChannel();

        // Initializing Data Source and other layout objects
        dataSource = new DataSource(this);
        insert =  findViewById(R.id.insertBtn);
        about =  findViewById(R.id.aboutBtn);
        medName = findViewById(R.id.med);
        date = findViewById(R.id.date);
        spinner = findViewById(R.id.list);

        // Creating a pattern for date
        pattern = Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{2}$");

        // Filling dropDown menu
        String[] items = new String[]{"Morning", "Afternoon", "Evening", "Night"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown, items);
        spinner.setAdapter(adapter);

        // a focus change listener object for hiding keyboard if user clicks somewhere on screen
        View.OnFocusChangeListener focusChangeListener = (view, hasFocus) -> {
            if(!hasFocus) {
                hideKeyboard(view);
            }
        };

        // setting focus change property of all the fields
        medName.setOnFocusChangeListener(focusChangeListener);
        date.setOnFocusChangeListener(focusChangeListener);
        spinner.setOnFocusChangeListener(focusChangeListener);

        // Initializing alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // insert button on click listener
        insert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // storing name,date and time field value
                String name = medName.getText().toString().toLowerCase(Locale.ROOT);
                if(name.equals("")) {
                    Toast.makeText(getBaseContext(), "Enter The Data In Correct Format", Toast.LENGTH_LONG).show();
                    return;
                }
                String dates = date.getText().toString();
                // matching date pattern
                if(!pattern.matcher(dates).matches()) {
                    Toast.makeText(getBaseContext(), "Please Enter The Date in DD/MM/YY Format", Toast.LENGTH_LONG).show();
                    date.setText(null);
                    return;
                }
                String time = spinner.getSelectedItem().toString().toLowerCase(Locale.ROOT);

                // Separating day, month and year from date string
                String[] data = dates.split("/");
                int day = Integer.parseInt(data[0]);
                int month = Integer.parseInt(data[1]);
                int year = Integer.parseInt("20" + data[2]);

                // Checking if the date entered is valid or not
                if(!isValid(day,month,year)) {
                    return;
                }

                // inserting values in database
                boolean insert = dataSource.insertValues(name,dates,time);

                // Setting hour based on selected value of time
                int hour;
                switch (time) {
                    case "morning":
                        hour = 8;
                        break;
                    case "afternoon":
                        hour = 13;
                        break;
                    case "evening":
                        hour = 18;
                        break;
                    default:
                        hour = 21;
                        break;
                }

                // setting up alarm if alarm data was successfully inserted in database
                if(insert) {
                    // setting the hour of alarm
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 17);
                    calendar.set(Calendar.MINUTE, 5);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(year, month-1, day);  // month value ranges from 0-11

                    // creating an intent to receiver class
                    intent = new Intent(MainActivity.this, AlarmReceiver.class);

                    // Creating a bundle adding medicine name to it and adding bundle to intent so that we can transfer data from one activity to another
                    Bundle bundle = new Bundle();
                    bundle.putString("med", name);
                    intent.putExtras(bundle);

                    // setting pending intent to register broadcast class and setting flags depending on SDK version
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    } else {
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    // setting alarm. RTC_WAKEUP will wakes the alarm instantly, alarm time is specified in milliseconds
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Toast.makeText(getBaseContext(), "Alarm Created for " + name + " Reminder: " + dates, Toast.LENGTH_LONG).show();

                    medName.setText(null);
                    date.setText(null);
                    spinner.setSelection(0);
                } else {
                    Toast.makeText(getBaseContext(), "Couldn't insert Data! Database insertion error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "sidChannel";
            String desc = "Channel for alarm manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("sid", name,importance);
            notificationChannel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private boolean isValid(int day, int month, int year) {

        if(!(day <= 31 && day >= 1 && month >= 1 && month <= 12 && year >= 22)) {
            Toast.makeText(getBaseContext(), "Please Enter The Date Before The Current Day In Proper Manner", Toast.LENGTH_LONG).show();
            date.setText(null);
            return false;
        }

        // checking if alarm is set for future, not past!
        Calendar cal1 = Calendar.getInstance(); // returns today's date
        Calendar cal2 = Calendar.getInstance();
        cal2.set(year, month-1, day); // setting alarm's date
        if(cal2.before(cal1)) {
            Toast.makeText(getBaseContext(), "You Cannot Set An Alarm For The Time That Is Already Gone!", Toast.LENGTH_LONG).show();
            date.setText(null);
            return false;
        }
        return true;
    }

}
