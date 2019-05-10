package com;

import javax.swing.text.MaskFormatter;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Searching {

    private String[] args;
    private String directory;
    private String fileName;
    private File outFile;

    public Searching(String[] args) {
        this.args = args;
        getDirectory();
        getNameFile();
        getOutFile();
    }

    public void getDirectory() {
        for (int index = 0; index < args.length; index++) {
            if (args[index].equals("-d"))
                this.directory = args[++index];
        }
    }

    public void getNameFile() {
        for (int index = 0; index < args.length; index++) {
            if (args[index].equals("-n"))
                this.fileName = args[++index];
        }
    }


    public boolean isFile(String file) {
        for (int index = 0; index < args.length; index++) {
            switch (args[index]) {
                case "-m":
                    return matchMask(file);
                case "-f":
                    return file.equals(fileName);
                case "-r":
                    return Pattern.compile(fileName).matcher(file).matches();
                default:
                    break;
            }
        }
        return false;
    }

    public void getOutFile() {
        for (int index = 0; index < args.length; index++) {
            if (args[index].equals("-o"))
                outFile = new File(args[++index]);
        }
    }

    public void writetFiles() throws ParseException {
        File Directory = new File(directory);
        Queue<File> files = new LinkedList<>(Arrays.asList(Directory.listFiles()));
        while (!files.isEmpty()) {
            File file = files.poll();
            if (file.isDirectory()) {
                for (File myFile : file.listFiles()) {
                    files.offer(myFile);
                }
            } else if (isFile(file.getName())) {
                writeFile(file);
            }
        }
    }

    public boolean matchMask(String file) {
        char[] fileArray = file.toCharArray();
        char[] fileNameArray = fileName.toCharArray();
        for (int index = 0; index < fileArray.length; index++) {
            if (!(fileArray[index] == fileNameArray[index] || fileNameArray[index] == '*')) {
                return false;
            }
        }
        return true;
    }

    public void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, true));
             BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String str = "";
            String text = "";
            do {
                text += str;
            } while ((str = reader.readLine()) != null);
            writer.write(text);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new Searching(args).writetFiles();
        } catch (ParseException ex) {
            System.out.println("wrong format");
        }
    }
}
