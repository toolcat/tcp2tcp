package kr.toolcat.tcp2tcp.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Starter {

    public static final Executor EXECUTOR = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        Socket socket1 = createSocket(System.getProperty("host1"));
        Socket socket2 = createSocket(System.getProperty("host2"));
        createForwarder(socket1.getInputStream(), socket2.getOutputStream());
        createForwarder(socket2.getInputStream(), socket1.getOutputStream());
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

    private static void createForwarder(final InputStream inputStream, final OutputStream outputStream) {
        EXECUTOR.execute(() -> {
            byte[] buffer = new byte[64];
            try {
                while (true) {
                    inputStream.read(buffer);
                    outputStream.write(buffer);
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
