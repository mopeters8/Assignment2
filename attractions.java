public class attractions {
    roadtrip roadtripObject = new roadtrip();

    private String attraction;
    private String location;
    private int number;

    public attractions(String sAttraction, String sLocation, int sNumber)
    {
        attraction  = sAttraction;
        location = sLocation;
        number = sNumber;
    }

    public attractions() {

    }
    public String getName() {
        return attraction;
    }
    public String getLocation() {
        return location;
    }
    public int getNumber() { return number;}

    public attractions attractionsadd(String sAttraction, String sLocation, int sNumber) {
        attractions a = new attractions(sAttraction, sLocation, sNumber);
        return a;
    }
}
