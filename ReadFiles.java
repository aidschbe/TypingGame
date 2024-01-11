package bbrz.Frameworks;

/*
  Author: Moritz Wenger
  Date: 22.12.2021
  Version: 0.2
  Description:
                Reads the contents of a given file and saves them in an ArrayList of Type String
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFiles extends TypingGame {
    /**
     * getsFileInformation of the two given files.
     * true if everything worked as intended.
     *
     * @return true/false
     */
    public boolean getFileInfo(File currentFile) {
        if (currentFile.exists()) {
            /*
            System.out.println("--------------------------------------------------");
            System.out.println("File name: " + firstnamesFile.getName());
            System.out.println("Absolute path: " + firstnamesFile.getAbsolutePath());
            System.out.println("Writeable: " + firstnamesFile.canWrite());
            System.out.println("Readable " + firstnamesFile.canRead());
            System.out.println("File size in bytes " + firstnamesFile.length());
            System.out.println("--------------------------------------------------");
            System.out.println("File name: " + lastnamesFile.getName());
            System.out.println("Absolute path: " + lastnamesFile.getAbsolutePath());
            System.out.println("Writeable: " + lastnamesFile.canWrite());
            System.out.println("Readable " + lastnamesFile.canRead());
            System.out.println("File size in bytes " + lastnamesFile.length());
            System.out.println("--------------------------------------------------");
            */
            return true;
        }
        System.out.println("The file does not exist.");
        return false;
    }

    /**
     * Saves the contents of a given textfile to a given ArrayList.
     * param given filename
     * param given ArrayList
     */
    public void saveToContainer(File currentFile, ArrayList<String> currentList) {
        try {
            Scanner scanner = new Scanner(currentFile);
            while (scanner.hasNextLine()) {
                // Read Data String
                String data = scanner.nextLine();
                // Edit Data String
                data = data.replace("\\", "");
                data = data.replace("}", "");
                data = data.replace("\n", "");
                // Add cleaned Data String to ArrayList
                currentList.add(data);
                // Add 0.0 to SECONDS and PROGRESS lists
                // WORDS_SECONDS.add(0.0);
                // WORDS_PROGRESS.add(0.0);

            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }
}

