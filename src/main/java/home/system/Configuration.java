package home.system;

public class Configuration {
    // System info
    private static String systemIPAddress;
    private static int systemPort;
    private static String systemName;

    // Bootstrap server info
    private static String bsIpAddress;
    private static int bsPort;


    public static String getSystemIPAddress() {
        return systemIPAddress;
    }

    public static void setSystemIPAddress(String systemIPAddress) {
        Configuration.systemIPAddress = systemIPAddress;
    }

    public static int getSystemPort() {
        return systemPort;
    }

    public static void setSystemPort(int systemPort) {
        Configuration.systemPort = systemPort;
    }

    public static String getSystemName() {
        return systemName;
    }

    public static void setSystemName(String systemName) {
        Configuration.systemName = systemName;
    }

    public static String getBsIpAddress() {
        return bsIpAddress;
    }

    public static void setBsIpAddress(String bsIpAddress) {
        Configuration.bsIpAddress = bsIpAddress;
    }

    public static int getBsPort() {
        return bsPort;
    }

    public static void setBsPort(int bsPort) {
        Configuration.bsPort = bsPort;
    }
}
