package cs.healthCare.network;

public class Resource {
    private static final String BaseUrl = "http://61.84.24.138:3000/";
    public static String getUrl(String subUrl)
    {
        return BaseUrl+subUrl;
    }
}
