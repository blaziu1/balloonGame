package pl.blazej.balloons;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Util {

    int getMapWidth(String mapFile) throws IOException {
        InputStream in = getClass().getResourceAsStream(mapFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        while (line != null) {
            System.out.println(line);
            if (line.contains("properties")) {
                String[] parameters = line.split("\\s+");
                return Integer.parseInt(parameters[1]);
                }
            line = br.readLine();
            }
        return 0;
    }

    int getMapHeight(String mapFile) throws IOException {
        InputStream in = getClass().getResourceAsStream(mapFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        while (line != null) {
            if (line.contains("properties")) {
                String[] parameters = line.split("\\s+");
                return Integer.parseInt(parameters[2]);
            }
            line = br.readLine();
        }
        return 0;
    }

    int getMapMode(String mapFile) {
        InputStream in = getClass().getResourceAsStream(mapFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            String line = br.readLine();
            while (line != null) {
                if (line.contains("properties")) {
                    String[] parameters = line.split("\\s+");
                    return Integer.parseInt(parameters[3]);
                }
                line = br.readLine();
            }
            return 0;
        } catch(IOException e){
            System.out.println("IOException");
            return -1;
        }
    }

    Vector<Balloon> getBalloons(String mapFile) throws IOException {
        InputStream in = getClass().getResourceAsStream(mapFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        String[] balonString;
        Vector<Balloon> balloons = new Vector<>();
        while(br.ready()){
            line = br.readLine();
            if(!line.contains("properties") && !line.contains("#")){
                balonString = line.split("\\s+");
                int x = Integer.parseInt(balonString[0]);
                int y = Integer.parseInt(balonString[1]);
                int colourCode = Integer.parseInt(balonString[2]);
                Colour colour = getColour(colourCode);
                balloons.add(new Balloon(colour, x, y));
            }
        }
        return balloons;
    }

    private int getNumberOfColours() {
        try {
            Scanner in = new Scanner(new File("files/difficulty.txt"));
            String numOfColours = in.nextLine();
            return Integer.parseInt(numOfColours);
        } catch (FileNotFoundException e){
            System.out.println("difficulty.txt file not found");
            return -1;
        }
    }

    int getDESCENDFAB() {
        try {
            Scanner in = new Scanner(new File("files/difficulty.txt"));
            in.nextLine();
            String descendFab = in.nextLine();
            return Integer.parseInt(descendFab);
        } catch (FileNotFoundException e){
            System.out.println("difficulty.txt file not found");
            return -1;
        }
    }

    int getDESCENDARC() {
        try {
            Scanner in = new Scanner(new File("files/difficulty.txt"));
            in.nextLine();
            in.nextLine();
            String descendArc = in.nextLine();
            return Integer.parseInt(descendArc);
        } catch (FileNotFoundException e){
            System.out.println("difficulty.txt file not found");
            return -1;
        }
    }

    Colour getColour(int ColourCode) {
        Colour colour;
        int numOfColours = getNumberOfColours();
        if (ColourCode == 6) {
            Random rand = new Random();
            ColourCode = rand.nextInt(numOfColours) + 1;
        }
        switch (ColourCode) {
            case 1:
                colour = Colour.GREEN;
                break;
            case 2:
                colour = Colour.RED;
                break;
            case 3:
                colour = Colour.BLUE;
                break;
            case 4:
                colour = Colour.YELLOW;
                break;
            default:
                colour = Colour.EMPTY;
                break;
        }
        if (ColourCode == 10)
            colour = Colour.GREEN;
        if (ColourCode == 100)
            colour = Colour.RED;
        if (ColourCode == 1000)
            colour = Colour.BLUE;
        if (ColourCode == 10000)
            colour = Colour.YELLOW;
        if (ColourCode == 110) {
            Random rand = new Random();
            ColourCode = rand.nextInt(2);
            if (ColourCode == 0)
                colour = Colour.GREEN;
            if (ColourCode == 1)
                colour = Colour.RED;
        }
        if (ColourCode == 1010) {
            Random rand = new Random();
            ColourCode = rand.nextInt(2);
            if (ColourCode == 0)
                colour = Colour.GREEN;
            if (ColourCode == 1)
                colour = Colour.BLUE;
        }
        if (ColourCode == 10010) {
            Random rand = new Random();
            ColourCode = rand.nextInt(2);
            if (ColourCode == 0)
                colour = Colour.GREEN;
            if (ColourCode == 1)
                colour = Colour.YELLOW;
        }
        if (ColourCode == 1100) {
            Random rand = new Random();
            ColourCode = rand.nextInt(2);
            if (ColourCode == 0)
                colour = Colour.RED;
            if (ColourCode == 1)
                colour = Colour.BLUE;
        }
        if (ColourCode == 10100) {
            Random rand = new Random();
            ColourCode = rand.nextInt(2);
            if (ColourCode == 0)
                colour = Colour.RED;
            if (ColourCode == 1)
                colour = Colour.YELLOW;
        }
        if (ColourCode == 11000) {
            Random rand = new Random();
            ColourCode = rand.nextInt(2);
            if (ColourCode == 0)
                colour = Colour.BLUE;
            if (ColourCode == 1)
                colour = Colour.YELLOW;
        }
        if (ColourCode == 1110) {
            Random rand = new Random();
            ColourCode = rand.nextInt(3);
            if (ColourCode == 0)
                colour = Colour.GREEN;
            if (ColourCode == 1)
                colour = Colour.RED;
            if (ColourCode == 2)
                colour = Colour.BLUE;
        }
        if (ColourCode == 10110) {
            Random rand = new Random();
            ColourCode = rand.nextInt(3);
            if (ColourCode == 0)
                colour = Colour.GREEN;
            if (ColourCode == 1)
                colour = Colour.RED;
            if (ColourCode == 2)
                colour = Colour.YELLOW;
        }
        if (ColourCode == 11100) {
            Random rand = new Random();
            ColourCode = rand.nextInt(3);
            if (ColourCode == 0)
                colour = Colour.YELLOW;
            if (ColourCode == 1)
                colour = Colour.RED;
            if (ColourCode == 2)
                colour = Colour.BLUE;
        }
        if (ColourCode == 11010) {
            Random rand = new Random();
            ColourCode = rand.nextInt(3);
            if (ColourCode == 0)
                colour = Colour.GREEN;
            if (ColourCode == 1)
                colour = Colour.YELLOW;
            if (ColourCode == 2)
                colour = Colour.BLUE;
        }
        if (ColourCode == 11110) {
            Random rand = new Random();
            ColourCode = rand.nextInt(4);
            if (ColourCode == 0)
                colour = Colour.GREEN;
            if (ColourCode == 1)
                colour = Colour.YELLOW;
            if (ColourCode == 2)
                colour = Colour.BLUE;
            if (ColourCode == 3)
                colour = Colour.RED;
        }
        return colour;
    }
}