package com.rubancreation.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    ListView listView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey!! Want to take notes? Try this amazing app now. Link: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            return true;
        }

        else if (item.getItemId() == R.id.rate){
            Toast.makeText(this, "Please wait", Toast.LENGTH_LONG).show();

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));

            return true;
        }

        else if (item.getItemId() == R.id.about_us){
            aboutDialog();

            return true;
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        FloatingActionButton fab = findViewById(R.id.fab);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.rubancreation.notes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null){
            notes.add("This is an Example note");
        }
        else {
            notes = new ArrayList(set);
        }
        

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);
        listView.setDividerHeight(2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.warning_icon)
                        .setTitle("Are You Sure...")
                        .setMessage("Do You want to delete this note...?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Your Note is successfully Deleted.", Toast.LENGTH_LONG).show();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.rubancreation.notes", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();

                            }

                        }
                        )
                        .setNegativeButton("No", null)
                        .show();


                return true;

            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Hey! New Note is Created, Tap the screen to edit.", Toast.LENGTH_LONG).show();
            }
        });

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);
        if (firstStart) {
            showStartDialog();
        }
    }

    //for Exit menu Item
    public void exit(MenuItem item){
        Toast.makeText(this, "Thanks for using NOTES", Toast.LENGTH_LONG).show();
        finish();
    }

    //for Feed Back menu item
    public void checkPermission(MenuItem item) {
        //Ask permission
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Please wait..", Toast.LENGTH_SHORT).show();

               Log.d(" ", "Send Email: ");
               String[] TO_EMAILS = {"app.feedback.rubancreations@gmail.com"};

               Intent intent = new Intent(Intent.ACTION_SENDTO);
               intent.setData(Uri.parse("mailto:"));
               intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAILS);

               intent.putExtra(Intent.EXTRA_SUBJECT, "Reported Problem");
               intent.putExtra(Intent.EXTRA_TEXT, "Hey Ruban Creations I've found a Bug/Problem inside your app. \nProblem Explanation: \n");

               startActivity(Intent.createChooser(intent, "Choose your mail client: "));

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET)
                .check();
    }

    public void privacyPolicy(MenuItem item) {
        //ask permission
        PermissionListener privacyPolicy = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Please wait..", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Check Connection Settings", Toast.LENGTH_LONG).show();
            }
        };
        TedPermission.with(MainActivity.this)
                .setPermissionListener(privacyPolicy)
                .setPermissions(Manifest.permission.INTERNET)
                .check();
    }

    private void showStartDialog(){
        new AlertDialog.Builder(this)
                .setTitle("We are Updated!")
                .setMessage("Hello Buddies, We are updated our app This update includes \nBug Fixes \nFixed add button not working problem \nUpgraded performance \nAdded some menus to know about us \nRate us on google playstore :)")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void aboutDialog(){
        new AlertDialog.Builder(this)
                .setTitle("NOTES - Writing Master")
                .setMessage("Hello users, NOTES - Writing Master." +
                        "One of our first development on PlayStore has surpassed 500+ users around the world" +
                        "Users can write notes in our app" +
                        "An User friendly application. We designed everything, what they expected. " +
                        "We made it more. \n" +
                        "\n" +
                        "Version Name: 1.3")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
}