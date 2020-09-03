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

        if (item.getItemId() == R.id.add_note) {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "Hey! New Note is Created, Tap the screen to edit.", Toast.LENGTH_LONG).show();

            return true;
        }

        else if (item.getItemId() == R.id.share){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

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

    }

    //for Exit menu Item
    public void exit(MenuItem item) {
        finish();
    }

    //for Feed Back menu item
    public void checkPermission(MenuItem item) {
        //Ask permission
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Please wait..", Toast.LENGTH_SHORT).show();

                Log.d("  ", "send email: ");

                String[] TO_EMAILS = {"rubancreations22@gmail.com"};
                //these CC and BCC
                String[] CC = {"rubancreations22@gmail.com"};
                String[] BCC = {"rubancreations22@gmail.com"};

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, TO_EMAILS);
                intent.putExtra(Intent.EXTRA_CC, CC);
                intent.putExtra(Intent.EXTRA_BCC, BCC);

                intent.putExtra(Intent.EXTRA_SUBJECT, "This is a Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "This is the body of the Email");

                startActivity(Intent.createChooser(intent, "Choose your mail client.."));

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Click allow to access Permission.", Toast.LENGTH_SHORT).show();
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
}