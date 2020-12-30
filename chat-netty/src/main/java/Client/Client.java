package Client;

import io.atomix.cluster.messaging.MessagingConfig;
import io.atomix.cluster.messaging.impl.NettyMessagingService;
import io.atomix.utils.net.Address;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.AsynchronousFileChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.nio.*;

public class Client {

    static int my_port;
    static int server_port = 12345;

    ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
    NettyMessagingService ms = new NettyMessagingService("nome", Address.from(my_port), new MessagingConfig());

    public static void main(String[] args) {

        my_port = Integer.parseInt(args[0]);

        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        NettyMessagingService ms = new NettyMessagingService("nome", Address.from(my_port), new MessagingConfig());

        ms.registerHandler("joined_room", (a,m)->{

            System.out.println("> Joined Room");

        }, es);

        ms.registerHandler("already_in_room", (a,m)->{

            System.out.println("> You're already in a Room!");

        }, es);

        ms.registerHandler("left_room", (a,m)->{

            System.out.println("> Left Room");

        }, es);

        ms.registerHandler("not_in_room", (a,m)->{

            System.out.println("> Join a room first!");

        }, es);

        ms.registerHandler("message_sent", (a,m)->{

            System.out.println("> Your Message was Sent!");

        }, es);

        ms.registerHandler("message", (a,m)->{

            System.out.println("New message received: " + new String(m));

        }, es);

        ms.start();

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String r;

        while(true) {
            try {
                r = bf.readLine();

                if(r.startsWith("/room")) {
                    String[] elem = r.split(" ", 2);
                    ms.sendAsync(Address.from("localhost", server_port), "join_room", elem[1].getBytes());
                }
                else if(r.startsWith("/leave")) {
                    ms.sendAsync(Address.from("localhost", server_port), "leave_room", "".getBytes());
                }
                else {
                    ms.sendAsync(Address.from("localhost", server_port), "message", r.getBytes());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
