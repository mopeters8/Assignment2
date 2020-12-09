import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class roadtrip {

    private ArrayList<city> roadContents = new ArrayList<>();      //general road/cities list
    private ArrayList<attractions> attractionContents = new ArrayList<attractions>(); //general attractions list
    private int[][] myTable;

    public void buildtable() {
        myTable = new int[roadContents.size()][5];
        for (int i = 0; i < roadContents.size(); i++) {
            myTable[i][0] = i; //vertex
            myTable[i][1] = 0; //known
            myTable[i][2] = -1; //path
            myTable[i][3] = -1; //cost
            myTable[i][4] = 0; // is attraction
        }
    }

    public void known(int vertex) {
        if (vertex != -1) {
            myTable[vertex][1] = 1;
        }
    }

    public int least_cost_uknown() {
        int min = 0;
        for (int i = 0; i < myTable.length; i++) {
            if (myTable[i][3] > min){
                min = myTable[i][3];
            }
        }
        //above is to just find a large number.

        int position = -1;
        for (int i = 0; i < myTable.length; i++) {
            //case for if its starting vertex
            if (myTable[i][3] == 0 && myTable[i][1] == 0) {
//                System.out.println("<in first loop>");
                min = myTable[i][3];
                position = myTable[i][0];
            }
            //case for if its smaller and not known
            else if (myTable[i][3] < min && myTable[i][3] != -1 && myTable[i][1] == 0 && myTable[i][2] > -1) {
//                System.out.println("<in second loop>");
                min = myTable[i][3];
                position = myTable[i][0];
            }
        }
//        System.out.println("least miles: "+min);
//        System.out.println("position: "+position);
//        System.out.println("_________________________");

        return position;
    }

    public int cost(int vertex) {
        return myTable[vertex][3];
    }

    public int edge_weight (int vertex, int adj) {
        int miles = roadContents.get(adj).getMiles();
        return miles;
    }

    public void update_distance(int adj, int vertex) {
        int new_cost = edge_weight(vertex, adj);
        myTable[adj][3] = new_cost;
    }

    public void update_path(int adj, int vertex) {
        myTable[adj][2] = vertex;
    }

    public int findcity (String city) {
        int vertex = -1;
        int count = 0;
        while (vertex == -1 && count != roadContents.size()) {
            if (roadContents.get(count).getName().equals(city)) {
                vertex = count;
            }
            count++;
        }
        count = 0;
        while (vertex == -1) {
            if(roadContents.get(count).getNextCity().equals(city))
            {
                vertex = count;
            }
            count++;
        }
        return vertex;
    }

    public List<Integer> adjacent (int start) {
        List<Integer> adjacencylist = new ArrayList<>();
        //System.out.println("Getting used!");
        if (start != -1) {
            String firstName = roadContents.get(start).getName();
            String secondName = roadContents.get(start).getNextCity();

            int count = 0;
            while (count != roadContents.size()) {
                String currName = roadContents.get(count).getName();
                String nextName = roadContents.get(count).getNextCity();
                if (currName.equals(firstName) || nextName.equals(firstName) || currName.equals(secondName) || nextName.equals(secondName)) {
                    adjacencylist.add(roadContents.get(count).getNumber());
                    //System.out.println((count+1)+" Added: "+roadContents.get(count).getNextCity()+" from "+roadContents.get(count).getName());
                }
                count++;
            }
        }
        return adjacencylist;
    }

    public List<Integer> isAttraction(List<Integer> atrcns) {
        List<Integer> vertexes = new ArrayList<>();
        for (int i = 0; i < atrcns.size(); i++) {
            for (int n = 0; n < attractionContents.size(); n++) {
                if (atrcns.get(i) == attractionContents.get(n).getNumber()) {
                    int aVertex = findcity(attractionContents.get(n).getLocation());
                    myTable[aVertex][4] = 1;
                    vertexes.add(aVertex);
                }
            }
        }
        return vertexes;
    }

    public List<String> addBestRoutes(int vertex) {
        List<String> collection = new ArrayList<>();
        int currPath = myTable[vertex][2];
        while (currPath != -1) {
            int nextPath = myTable[currPath][2];
            int pathvertex = myTable[currPath][0];

            if (!collection.isEmpty() && collection.get(collection.size()-1).equals(roadContents.get(pathvertex).getName())){
                if (nextPath == -1) {

                } else {
                    collection.add(roadContents.get(pathvertex).getNextCity());
                }
            } else {
                collection.add(roadContents.get(pathvertex).getName());
            }
            currPath = nextPath;
        }
        return collection;
    }

    public List<String> route (String starting_city, String ending_city, List<Integer> routeAttractions) {
        List<String> bestpath = new ArrayList<>();
        List<Integer> routeAtt = isAttraction(routeAttractions);
        int firstV = findcity(starting_city);
        myTable[firstV][3] = 0; //changing starting city to known AND 0 cost.

        /////////////////////////////////////////
        //        DJIKSTRAS ALGORITHM
        /////////////////////////////////////////

        int v = 0;
        while (v != -1) {
            v = least_cost_uknown();
            known(v);
            if (routeAtt.contains(v)) {
                bestpath.addAll(0,addBestRoutes(v));
                buildtable();
                myTable[v][3] = 0;
                routeAtt.remove(routeAtt.indexOf(v));
            }
            List<Integer> adjacent1 = adjacent(v);
            for (int n = 0; n < adjacent1.size() ; n++) {
                if (cost(adjacent1.get(n)) > cost(v) + edge_weight(v, adjacent1.get(n)) || cost(adjacent1.get(n)) == -1) {
                    update_distance(adjacent1.get(n), v);
                    update_path(adjacent1.get(n), v);
                }
            }
        }

        //////////////////////////////////////////
        // Final Combination/Creation of Best Route
        /////////////////////////////////////////

        int currPath = findcity(ending_city);
//        bestpath.add("Starting City: "+starting_city);
        bestpath.addAll(0, addBestRoutes(currPath));
        bestpath.add(0,"Ending at: "+ending_city);

        return bestpath;
    }

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
            //System.out.println(count + ": " + linecontent[0]);
            roadContents.add(c);
            //count++;

            if (linecontent[0].equals(currentcity) == false) { //using this simply for the user. It will display the cities without duplicates, so they may select starting city.
                currentcity = linecontent[0];
                System.out.println(count + ": " + linecontent[0]);
                //count++;
            }
            //maybe find a way to remake above^ ?
            count++;


        }
        return count - 1;
    }

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

//        THIS IS MY MAIN
//
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
            System.out.println("\nWelcome! Please Select an Appropriate Starting City by entering the city's code: ");
            citychoice = sc.nextInt();
        }
        while (cityendchoice < 0 || cityendchoice > citycount) {
            System.out.println("Please Select an Appropriate Ending City by entering the city's code: ");
            cityendchoice = sc.nextInt();
        }



        ArrayList<Integer> attractionpicks = new ArrayList<>();  //list of attractions with integers.
        int attractioncount = trip.pickEvent(attractions);

        System.out.println("142: Done.");
        System.out.println("\nPlease add corresponding number for the attraction you wish to visit!\nWhen done, enter 142.");
        int x = -1;
        while (x != 142 && x < 142) //while loop for picking all the wanted attractions.
        {
            x = sc.nextInt();
            if (x != 142 && x < 142) { attractionpicks.add(x); }

        }

        System.out.println("/////////////////////\n/// YOU SELECTED: ///");
        System.out.println("Beginning City: "+trip.roadContents.get(citychoice).getName()+", "+trip.roadContents.get(citychoice).getNumber());  //beginning city
        System.out.println("Ending City: "+trip.roadContents.get(cityendchoice).getName()+", "+trip.roadContents.get(cityendchoice).getNumber()); //ending city
        for (int i = 0; i < attractionpicks.size(); i++) {
            int pick = attractionpicks.get(i);
            System.out.print("Attraction "+(i+1)+": "+trip.attractionContents.get(pick).getName()+", "+trip.attractionContents.get(pick).getNumber()+"\n");
        }
        System.out.println("////////////////////\n");;
        trip.buildtable();
        List<String> bestroute = trip.route(trip.roadContents.get(citychoice).getName(), trip.roadContents.get(cityendchoice).getName(), attractionpicks);

        for (int i = 0; i < bestroute.size(); i++) {
            System.out.println((bestroute.get(i)));
        }

    }
}
