package kr.toolcat.tcp2tcp.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Starter {

    public static void main(String[] args) throws IOException {
        InputStream input = createSocket(System.getProperty("input")).getInputStream();
        List<OutputStream> outputList = new ArrayList<OutputStream>();
        for (String output : System.getProperty("output").split(",")) {
            outputList.add(createSocket(output).getOutputStream());
        }
        byte[] buffer = new byte[64];
        while (true) {
            input.read(buffer);
            for (OutputStream output : outputList) {
                output.write(buffer);
                output.flush();
            }
        }
    }

    private static Socket createSocket(String host) throws IOException {
        String[] _host = host.split(":");
        if (_host.length != 2) {
            throw new IllegalArgumentException("The host must bee specified like: 'hostName:port'");
        } else {
            String hostName = _host[0];
            int port = Integer.valueOf(_host[1]);
            return new Socket(hostName, port);
        }
    }
}
