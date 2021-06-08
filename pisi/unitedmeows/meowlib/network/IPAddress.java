package pisi.unitedmeows.meowlib.network;

public class IPAddress {
    public static IPAddress LOOPBACK    = new IPAddress("127.0.0.1");
    public static IPAddress ANY         = new IPAddress("0.0.0.0");

    private String address;

    IPAddress(String _address) {
        address = _address;
    }


    public String getAddress() {
        return address;
    }

    public static IPAddress parse(String address) {
        return new IPAddress(address);
    }

}
