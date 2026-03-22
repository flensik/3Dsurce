package com.tool.surce;

import android.content.Context;
import android.widget.*;

import java.io.File;

public class TXDTool {

    public static void load(String path, LinearLayout panel, Context ctx) {
        panel.removeAllViews();

        File f = new File(path);

        TextView t = new TextView(ctx);
        t.setText("TXD: " + f.getName());
        t.setTextColor(0xFF00AAFF);

        Button extract = new Button(ctx);
        extract.setText("Extract");

        Button replace = new Button(ctx);
        replace.setText("Replace");

        panel.addView(t);
        panel.addView(extract);
        panel.addView(replace);
    }
}