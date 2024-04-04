package com.example.presentapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ElevAccount extends AppCompatActivity {

    TextView userName;
    TextView className;
    Button present;
    Button absent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_elev_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName= findViewById(R.id.className);
        className= findViewById(R.id.ClassName);

        present = findViewById(R.id.present);
        absent = findViewById(R.id.absent);

        String userID;
        String FILENAME = "ElevSign.txt";

        try {
            FileInputStream fileInputStream = openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            userID = stringBuilder.toString();

            fileInputStream.close();
            userName.setText(userID);

            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write("");
            outputStreamWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String userId = userID.trim();
        String FILENAMECLASSSAVE = userId+".txt";
        String classID;

        try {
            FileInputStream fileInputStream = openFileInput(FILENAMECLASSSAVE);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                if (bufferedReader.ready()) {
                    stringBuilder.append("\n");
                }
            }

            classID = stringBuilder.toString();

            fileInputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        className.setText("Your class is " + classID);
        String ClassID = classID + ".txt";
        Log.w(TAG,ClassID);

        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileInputStream fileInputStream = openFileInput(ClassID);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    String line;
                    boolean alreadyPresent = false;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 1 && parts[0].equals(userId)) {
                            alreadyPresent = true;
                            break;
                        }
                    }

                    if (!alreadyPresent) {
                        try (FileOutputStream fileOutputStream = openFileOutput(ClassID, MODE_APPEND);
                             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                            bufferedWriter.write(userId + " " + 1);
                            bufferedWriter.newLine();

                            Toast.makeText(ElevAccount.this,"Tocmai ati fost trecut prezent !", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(ElevAccount.this, "Sunteți deja trecut în !", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileInputStream fileInputStream = openFileInput(ClassID);
                     InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    String line;
                    boolean alreadyPresent = false;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length >= 1 && parts[0].equals(userId)) {
                            alreadyPresent = true;
                            break;
                        }
                    }

                    if (!alreadyPresent) {
                        try (FileOutputStream fileOutputStream = openFileOutput(ClassID, MODE_APPEND);
                             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                            bufferedWriter.write(userId + " " + 0);
                            bufferedWriter.newLine();

                            Toast.makeText(ElevAccount.this,"Tocmai ati fost trecut absent !", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(ElevAccount.this, "Sunteți deja trecut !", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

