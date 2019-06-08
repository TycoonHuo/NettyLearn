package com.huo.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO server
 * NIO中通道都是 可选择的 selectable channel  注册到一个selector上 来管理
 *
 * @author huoyun
 * @date 2019/6/8-13:15
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // selector可以管理所有的selectable channel  nio的核心
        Selector selector = Selector.open();

        // 负责建立tcp连接
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.bind(new InetSocketAddress(8888));

        ssc.configureBlocking(false);

        // 第二个参数代表 让selector监听它身上的什么事情
        SelectionKey register = ssc.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(register);

        while (true) {
            // 选择 它感兴趣的 key  这个方法阻塞的
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                handle(key);
            }
        }
    }

    private static void handle(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            // key可以拿到 它所代表的 channel 和selector
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel accept = channel.accept();

            Selector selector = key.selector();
            accept.configureBlocking(false);

            SelectionKey register = accept.register(selector, SelectionKey.OP_READ);
            System.out.println(register);
        } else if (key.isReadable()) {
            SocketChannel channel = (SocketChannel) key.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(512);
            byteBuffer.clear();
            int len = channel.read(byteBuffer);
            if (-1 != len) {
                System.out.println("client发来消息了");

            } else {
                System.out.println("client退出了");
                key.cancel();
            }

        }
    }
}
