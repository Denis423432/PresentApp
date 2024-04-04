package com.example.presentapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ProfesorClassesLook extends AppCompatActivity {

    TextView userName;

    private final String FILENAMECLASS = "ClassSign.txt";
    private final String FILENAME = "ProfesorSign.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profesor_classes_look);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName= findViewById(R.id.userName);

        String userID;

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
        LinearLayout classesLayout = findViewById(R.id.Classes);
        TextView noClass = findViewById(R.id.noClass);

        ArrayList<String> numeButoaneList = new ArrayList<>();
        ArrayList<String> numarELEviClasaList = new ArrayList<>();
        String FILECLASSDATA = "ClasesData.txt";

        File file = new File(getFilesDir(),FILECLASSDATA);

        if (file.exists()) {
            noClass.setText("");
            try (FileInputStream fileInputStream = openFileInput(FILECLASSDATA);
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split("\\s+");
                    if (parts.length > 0) {
                        numeButoaneList.add(parts[0]);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (FileInputStream fileInputStream = openFileInput(FILECLASSDATA);
                 InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split("\\s+");
                    if (parts.length > 0) {
                        numarELEviClasaList.add(parts[1]);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] numeButoane = numeButoaneList.toArray(new String[0]);
            String[] numarELEviClasa = numarELEviClasaList.toArray(new String[1]);

            generateClassesButtons(numeButoane.length, numeButoane, classesLayout, userID, numarELEviClasa);
        }
        else {
            noClass.setText("Momentan nu ai creat nici o clasa");
        }
    }
    public void generateClassesButtons(int numarDeButoane, String[] numeleButoanelor, LinearLayout layout, String userID, String[] NumberClassStudents) {
        // Inițializarea variabilor pentru dimensiunile butoanelor și spațiile dintre ele
        float buttonWidthPercent = 0.25f;
        float buttonHeightPercent = 0.05f;
        int marginBetweenButtons = 20;
        int marginTop = 20;

        int id = 0; // ID-ul butonului

        // Parcurge fiecare rând
        for (int i = 0; i < numarDeButoane; i += 3) {
            // Creează un nou rând pentru a adăuga butoanele
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_HORIZONTAL);

            // Adaugă un padding pentru toate rândurile, cu excepția primului
            if (i > 0) {
                row.setPadding(0, marginTop, 0, 0);
            }

            // Calculează numărul de butoane pe rând pentru rândul curent
            int butoanelePeRand = Math.min(3, numarDeButoane - i);

            // Parcurge fiecare buton din rând
            for (int j = 0; j < butoanelePeRand; j++) {
                // Creează un nou buton și îi setează ID-ul și textul
                Button button = new Button(this);
                id++;
                button.setId(id);
                int finalId = id;
                int finalI = i;
                int finalJ = j;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try (FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_APPEND);
                             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                            bufferedWriter.write(userID);
                            bufferedWriter.newLine();

                            Intent intent = new Intent(ProfesorClassesLook.this, ClassView.class);
                            startActivity(intent);
                            Log.w(TAG,"AI APASAT BUTONUL: "+ finalId);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try (FileOutputStream fileOutputStream = openFileOutput(FILENAMECLASS, MODE_APPEND);
                             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {

                            bufferedWriter.write(numeleButoanelor[finalI + finalJ] +" "+ NumberClassStudents[finalI + finalJ]);
                            bufferedWriter.newLine();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                // Setează textul butonului folosind array-ul de nume de butoane
                button.setText(numeleButoanelor[i + j]);

                // Obține dimensiunile ecranului pentru a calcula dimensiunile butonului
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = displayMetrics.heightPixels;

                // Setează parametrii pentru dimensiunile butonului
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        (int) (screenWidth * buttonWidthPercent),
                        (int) (screenHeight * buttonHeightPercent)
                );

                // Adaugă marginea între butoane, cu excepția primului buton din rând
                if (j > 0) {
                    params.setMargins(marginBetweenButtons, 0, 0, 0);
                }

                // Setează parametrii butonului și stilul său
                button.setLayoutParams(params);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(50);
                drawable.setColor(Color.rgb(93, 230, 255));
                button.setBackground(drawable);

                // Adaugă butonul la rândul curent
                row.addView(button);
            }

            // Setează parametrii pentru rând și adaugă rândul în layout
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(marginBetweenButtons, 0, 0, 0);
            row.setLayoutParams(rowParams);
            layout.addView(row);
        }
    }
}