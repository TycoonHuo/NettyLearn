package com.huo.NIO;

import java.io.IOException;
import java.net.Socket;

/**
 * @author huoyun
 * @date 2019/6/8-13:23
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        System.out.println(socket);
        socket.close();
    }
}
