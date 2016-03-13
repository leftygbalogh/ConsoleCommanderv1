/*
 * Code is prose.
 * 
 * Lefty G Balogh
 */
package consolecommanderv1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lefty G Balogh
 */
public class ConsoleCommanderv1 {

    final static int QUIT = -1;
    final static int GO_BACK = 0;
    final static int DELETE_RANGE = -100;

    public static void printItems(File[] files) {
        int counter = 1;
        String fileName;
//╔══╤══╗
        System.out.print("╔");
        System.out.print(String.format("%0$-4s", "").replace(" ", "═") + "╤");
        System.out.print(String.format("%0$-34s", "").replace(" ", "═") + "╤");
        System.out.print(String.format("%0$-15s", " ").replace(" ", "═") + "╤");
        System.out.print(String.format("%0$-13s", " ").replace(" ", "═"));
        System.out.print("╗");

        System.out.println();
        for (File file : files) {
            Date dateModified = new Date(file.lastModified());

            if (file.getName().length() > 30) {
                fileName = file.getName().substring(0, 30);
            } else {
                fileName = file.getName();
            }

            if (file.isDirectory()) {

                System.out.print(String.format("%0$-4s", (counter < 10) ? "║  " + counter : "║ " + counter));

                System.out.print(String.format("%0$-35s", " │ " + fileName));

                System.out.print(String.format("%0$-16s", " │ " + "<DIR>"));

                System.out.print(String.format("%0$-12s", " │ " + printNormalisedDate(dateModified)));
                System.out.println("  ║");

                counter++;
                
                if (counter%10==0){
                    //╟──┼──╢ 
                    System.out.print("╟");
        System.out.print(String.format("%0$-4s", "").replace(" ", "─") + "┼");
        System.out.print(String.format("%0$-34s", "").replace(" ", "─") + "┼");
        System.out.print(String.format("%0$-15s", " ").replace(" ", "─") + "┼");
        System.out.print(String.format("%0$-13s", " ").replace(" ", "─"));
        System.out.print("╢");
        System.out.println();
                }

            } else {
                System.out.print(String.format("%0$-4s", (counter < 10) ? "║  " + counter : "║ " + counter));
                System.out.print(String.format("%0$-35s", " │ " + fileName));

                System.out.print(String.format("%0$-16s", " │ " + normaliseFileSize(file.length())));

                System.out.print(String.format("%0$-12s", " │ " + printNormalisedDate(dateModified)));
                System.out.println("  ║");
                counter++;
                
                if (counter%10==0){
                    //╟──┼──╢ 
                    System.out.print("╟");
        System.out.print(String.format("%0$-4s", "").replace(" ", "─") + "┼");
        System.out.print(String.format("%0$-34s", "").replace(" ", "─") + "┼");
        System.out.print(String.format("%0$-15s", " ").replace(" ", "─") + "┼");
        System.out.print(String.format("%0$-13s", " ").replace(" ", "─"));
        System.out.print("╢");
        System.out.println();
                }
            }

        }
        //╚══╧══╝
        System.out.print("╚");
        System.out.print(String.format("%0$-4s", "").replace(" ", "═") + "╧");
        System.out.print(String.format("%0$-34s", "").replace(" ", "═") + "╧");
        System.out.print(String.format("%0$-15s", " ").replace(" ", "═") + "╧");
        System.out.print(String.format("%0$-13s", " ").replace(" ", "═"));
        System.out.print("╝");
        System.out.println();
    }

    public static String printNormalisedDate(Date dateModified) {

        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-mm-dd");
        return dt1.format(dateModified);
    }

    public static String normaliseFileSize(long size) {
        double normal = size;
        String unit = "bytes";
        if (normal > 999) {
            normal = size / 1000;
            unit = "Kb";
        }
        if (normal > 999) {
            normal = normal / 1000;
            unit = "MB";
        }
        if (normal > 999) {
            normal = normal / 1000;
            unit = "Gb";
        }

        DecimalFormat df = new DecimalFormat("#.#");

        String number = df.format(normal).toString();
        return (number + " " + unit);

    }

    public static String getActions(Scanner keyboard) {

        System.out.println("Press number to enter directory | b:go back | q:quit | d[FileNumber]:delete");
        System.out.print("Yo, which one? ");
        String action = keyboard.nextLine();
        return action;

    }

    public static int actionConverter(String action) {

        if (action.equalsIgnoreCase("q")) {
            return QUIT;
        }
        if (action.equalsIgnoreCase("b")) {
            return GO_BACK;
        }

        if (action.toLowerCase().startsWith("d")) {
            int ret = Integer.parseInt(action.substring(1));
            ret = ret * -1; //signal deletion by negative number
            ret = ret - 100; //set of by 100 to prevent collision with quit and back
            return ret;
        }

        try {
            int ret = Integer.parseInt(action);
            return ret;
        } catch (Exception e) {
            System.out.println("Error ID 10T. Please try again.");
            Scanner keyboard = new Scanner(System.in);
            return actionConverter(getActions(keyboard));
        }

    }

    public static void previewFile(File[] files, int index) {
        FileReader frFile = null;
        try {

            //it is a file
            int bufferSize = 200;
            String path = files[index].getPath();
            frFile = new FileReader(path);
            BufferedReader brFilePreview = new BufferedReader(frFile, bufferSize);

            String preView;
            int counter = 0;
            System.out.println("\nAre these the droids you are looking for?\n");
            while ((preView = brFilePreview.readLine()) != null && counter < 5) {

                System.out.println(preView);
                counter++;
            }
            System.out.println("\n... and the story goes on\n");
            brFilePreview.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConsoleCommanderv1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConsoleCommanderv1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                frFile.close();
            } catch (IOException ex) {
                Logger.getLogger(ConsoleCommanderv1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void stepOutOfPreview(Scanner keyboard) {
        System.out.println("Press ENTER key to continue.");
        keyboard.nextLine();

    }

    public static void main(String[] args) throws IOException {

        File directory = new File("/home");
        File[] files = directory.listFiles();
        Scanner keyboard = new Scanner(System.in);
        int action;

        printItems(files);

        while ((action = actionConverter(getActions(keyboard))) != QUIT) {
            int index;
            if (action == GO_BACK) {
                if (directory.getParentFile() != null) {
                    directory = directory.getParentFile();
                    files = directory.listFiles();
                    printItems(files);

                }
            }

            if (action < DELETE_RANGE) {
                index = ((action + 100) * -1) - 1;//restore offset, restore delete flag, set action to index
                if (files[index].isFile()) {
                    deleteFile(files[index]);
                } else {
                    System.out.println("Dude, be a dove. Only delete files, will ya.");
                }
            } else {

                if (action > 0) {
                    index = action - 1;

                    if (files[index].isDirectory()) {
                        directory = files[index];
                        files = directory.listFiles();
                        printItems(files);

                    } else {
                        //
                        previewFile(files, index);
                        stepOutOfPreview(keyboard);
                        printItems(files);
                    }

                }
            }

        }
        System.out.println("Thank you for using our services.");
    }

    private static void deleteFile(File file) {
        String name = file.getName();
        String path = file.getAbsolutePath();
        if (file.delete()) {
            System.out.println(file + "has gone to the eternal hunting grounds, where no garbage collectors scavenge the plains.");
        }

    }

}
