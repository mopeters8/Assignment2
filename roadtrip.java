import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class roadtrip {

    ArrayList<city> roadContents = new ArrayList<>();
    ArrayList<attractions> attractionContents = new ArrayList<attractions>();

//
//        STORING ALL CITIES/CITIES DATA & INPUT FROM USER FOR STARTING/ENDING CITY
//

    public int pickRoads(File roads) throws IOException {
        BufferedReader roadReader = new BufferedReader(new FileReader(roads));
        city addroad = new city();

        String currline;
        int count = 0;
        String currentcity = "";
        while ((currline = roadReader.readLine()) != null) {
            String[] linecontent = currline.split(","); //the content is split into 4s. Name, next city, miles, and minutes to get there.
            city c = addroad.cityadd(linecontent[0], linecontent[1], linecontent[2], linecontent[3], count);  //here I am creating the city and adding it to an arraylist of all the cities.
            System.out.println(count + ": " + linecontent[0]);
            roadContents.add(c);
            count++;


//            if (linecontent[0].equals(currentcity) == false) { //using this simply for the user. It will display the cities without duplicates, so they may select starting city.
//                currentcity = linecontent[0];
//                System.out.println(count + ": " + linecontent[0]);
//                count++;
//            }
            //maybe find a way to remake above^ ?


        }
        return count - 1;
    }

//
//        STORING ALL ATTRACTIONS/ATTRACTION DATA & asking user for what attractions they want.
//

    public int pickEvent(File attractions) throws IOException {
        BufferedReader attractionReader = new BufferedReader(new FileReader(attractions));
        attractions addevent = new attractions();

        String currline;
        int counte = 0;
        String currentattraction = "";
        String junkline = attractionReader.readLine();
        while ((currline = attractionReader.readLine()) != null) {
            String[] linecontent = currline.split(","); //the content is split into 4s. Name, next city, miles, and minutes to get there.
            attractions a = addevent.attractionsadd(linecontent[0], linecontent[1], counte);  //here I am creating the city and adding it to an arraylist of all the cities.
            attractionContents.add(a);

            if (linecontent[0].equals(currentattraction) == false) { //using this simply for the user. It will display the cities without duplicates, so they may select starting city.
                currentattraction = linecontent[0];
                System.out.println(counte + ": " + linecontent[0]+", "+linecontent[1]);
                counte++;
            }
        }
        return counte - 1;
    }

    public static void main (String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        city cityObject = new city();

        File attractions = new File("src/attractions.csv");
        File roads = new File("src/roads.csv");
        roadtrip trip = new roadtrip(); //calls pick road/cities function. returns the count which equals the max number they can choose from.

        int citycount = trip.pickRoads(roads);
        int citychoice = -1;                        //users beginning city
        int cityendchoice = -1;                     //users ending city
        while (citychoice < 0 || citychoice > citycount) {
            System.out.println("\nWelcome! Please Select an Appropriate Starting City: ");
            citychoice = sc.nextInt();
        }
        while (cityendchoice < 0 || cityendchoice > citycount) {
            System.out.println("Please Select an Appropriate Ending City: ");
            cityendchoice = sc.nextInt();
        }


        ArrayList<Integer> attractionlist = new ArrayList<>();  //list of attractions with integers.
        int attractioncount = trip.pickEvent(attractions);

        System.out.println("142: Done.");
        System.out.println("\nPlease add corresponding number for the attraction you wish to visit!\nWhen done, enter 142.");
        int x = -1;
        while (x != 142 || x < 142)
        {
            x = sc.nextInt();
            if (x != 142 || x < 142) { attractionlist.add(x); }

        }

        System.out.println("/////////////////////\n/// YOU SELECTED: ///\n/////////////////////\n");

        System.out.println("Beginning City: "+trip.roadContents.get(citychoice).getName()+", "+trip.roadContents.get(citychoice).getNumber());  //beginning city
        System.out.println("Ending City: "+trip.roadContents.get(cityendchoice).getName()+", "+trip.roadContents.get(cityendchoice).getNumber()+"\n"); //ending city

        for (int i = 0; i < attractionlist.size(); i++) {
            int pick = attractionlist.get(i);
            System.out.print("Attraction "+(i+1)+": "+trip.attractionContents.get(pick).getName()+", "+trip.attractionContents.get(pick).getNumber()+"\n");
        }


    }









}
