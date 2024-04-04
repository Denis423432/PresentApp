package com.example.presentapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ClassFullStudentsSign extends AppCompatActivity {

    private TextView userName;
    private TextView studentsCounter;
    private TextView className;
    private LinearLayout studentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
        loadUserData();
        loadClassData();
    }

    private void initializeUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_class_full_students_sign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = findViewById(R.id.userName);
        studentsCounter = findViewById(R.id.StudentsCounter);
        className = findViewById(R.id.textView);
        studentsLayout = findViewById(R.id.Students);
    }

    private void loadUserData() {
        String userID;
        String FILENAME = "ProfesorSign.txt";

        try (FileInputStream fileInputStream = openFileInput(FILENAME);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            userID = stringBuilder.toString();
            fileInputStream.close();
            userName.setText(userID);

            try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {
                outputStreamWriter.write("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadClassData() {
        String FILENAMECLASS = "ClassSign.txt";
        String classData, classNameText, studentsFullNumber;

        try (FileInputStream fileInputStream = openFileInput(FILENAMECLASS);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            classData = stringBuilder.toString();

            String[] parts = classData.split("\\s+");
            classNameText = parts[0];
            studentsFullNumber = parts[1];

            fileInputStream.close();

            try (FileOutputStream fileOutputStream = openFileOutput(FILENAMECLASS, Context.MODE_PRIVATE);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {
                outputStreamWriter.write("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileNameClassStudents = classNameText + "_EleviFull.txt";
        ArrayList<String> studentList = new ArrayList<>();

        try (FileInputStream fileInputStream = openFileInput(fileNameClassStudents);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                if (parts.length >= 2) {
                    studentList.add(parts[0]);
                } else {
                    studentList.add(line);
                }
            }
            fileInputStream.close();

            try (FileOutputStream fileOutputStream = openFileOutput(FILENAMECLASS, Context.MODE_PRIVATE);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)) {
                outputStreamWriter.write("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] students = studentList.toArray(new String[studentList.size()]);

        className.setText("Class name\n" + classNameText);
        studentsCounter.setText(studentsFullNumber + " locuri in clasa");
        loadStudents(students);
    }

    private void loadStudents(String[] studentNames) {
        Context context = studentsLayout.getContext();
        studentsLayout.removeAllViews();

        for (String studentName : studentNames) {
            TextView studentTextView = new TextView(context);
            studentTextView.setText(studentName);
            studentTextView.setGravity(Gravity.CENTER);
            float textSize = 24;
            studentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            studentsLayout.addView(studentTextView);
        }
    }
}
