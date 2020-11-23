public class city {
    roadtrip roadtripObject = new roadtrip();

    private String name;
    private String nextCity = null;
    private String miles = null;
    private String minutes = null;
    private int number;

    public city(String sName, String sNextCity, String sMiles, String sMinutes, int sNumber)
    {
        name  = sName;
        nextCity = sNextCity;
        miles = sMiles;
        minutes = sMinutes;
        number = sNumber;
    }

    public city() {

    }
    public String getName() {
        return name;
    }
    public String getNextCity() {
        return nextCity;
    }
    public String getMiles() {
        return miles;
    }
    public String getMinutes() {
        return minutes;
    }
    public int getNumber() { return number; }


    public city cityadd(String sName, String sNextCity, String sMiles, String sMinutes, int sNumber) {
        city c = new city(sName, sNextCity, sMiles, sMinutes, sNumber);
        //roadtripObject.roadContents.add(c);
        return c;
    }
}
