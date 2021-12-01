package Server;

public class SettingConnection {
    private  final String host = "localhost";
    private  final String url = "jdbc:mysql://localhost:3306/users";
    private  final int port = 8080;
    private  final String name = "root";
    private  final String pass = "porter";
    private  final int bufferSize = 1024 * 64;


    public String getHost() {
        return host;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
