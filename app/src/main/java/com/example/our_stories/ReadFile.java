package com.example.our_stories;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
    public static String readChapter(String filePath) {
        StringBuilder res = new StringBuilder();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                res.append(myReader.nextLine());
            }
            myReader.close();
            myObj.deleteOnExit();
        } catch (FileNotFoundException e) {

        }
        return res.toString();
    }
}