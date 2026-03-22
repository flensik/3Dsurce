package com.tool.surce;

import android.os.Bundle;
import android.os.Environment;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    LinearLayout filePanel, rightPanel;
    GLView glView;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        filePanel = findViewById(R.id.filePanel);
        rightPanel = findViewById(R.id.rightPanel);

        glView = new GLView(this);
        ((FrameLayout)findViewById(R.id.glContainer)).addView(glView);

        loadFiles(Environment.getExternalStorageDirectory());
    }

    void loadFiles(File dir) {
        filePanel.removeAllViews();

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            Button btn = new Button(this);
            btn.setText(f.getName());

            btn.setOnClickListener(v -> {
                if (f.isDirectory()) {
                    loadFiles(f);
                } else if (f.getName().endsWith(".obj")) {
                    glView.loadObj(f.getAbsolutePath());
                } else if (f.getName().endsWith(".png") || f.getName().endsWith(".jpg")) {
                    glView.loadTexture(f.getAbsolutePath());
                }
            });

            filePanel.addView(btn);
        }
    }
}