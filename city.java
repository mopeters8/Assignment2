public class city {
    roadtrip roadtripObject = new roadtrip();

    private String name;
    private String nextCity = null;
    private String miles = null;
    private String minutes;
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
    public int getMiles() {
        return Integer.parseInt(miles);
    }
    public int getMinutes() {
        int thisminutes = Integer.parseInt(minutes);
        return thisminutes;
    }
    public int getNumber() { return number; }


    public city cityadd(String sName, String sNextCity, String sMiles, String sMinutes, int sNumber) {
        city c = new city(sName, sNextCity, sMiles, sMinutes, sNumber);
        //roadtripObject.roadContents.add(c);
        return c;
    }
}
