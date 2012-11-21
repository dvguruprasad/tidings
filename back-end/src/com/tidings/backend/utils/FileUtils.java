package com.tidings.backend.utils;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtils {
    public static void foreachLineInFile(String file, IActionOnLine actionOnLine) {
        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                actionOnLine.act(strLine);
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
