package com.tool.surce;

import java.io.*;
import java.util.*;

public class ObjLoader {

    public static float[] load(String path) {
        List<Float> list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] p = line.split(" ");
                    list.add(Float.parseFloat(p[1]));
                    list.add(Float.parseFloat(p[2]));
                    list.add(Float.parseFloat(p[3]));
                }
            }

            br.close();
        } catch (Exception e) {}

        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);

        return arr;
    }
}