package Client;

import Lite.*;
import Server.Command;
import Server.Handler.JsonDecoder;
import Server.Handler.JsonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.io.RandomAccessFile;
import java.util.Scanner;

public class Client {
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        new Client().run();
    }

    public void run() throws InterruptedException {
        Command command = new Command();
        NioEventLoopGroup worker = new NioEventLoopGroup(1);
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {

                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(512 * 512, 0, 2, 0, 2),
                                    new LengthFieldPrepender(2),

                                    new JsonEncoder(),
                                    new JsonDecoder(),

                                    new SimpleChannelInboundHandler<Message>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
                                             if (msg instanceof FileMessage) {
                                                var message = (FileMessage) msg;
                                                try (final RandomAccessFile randomAccessFile = new RandomAccessFile("1", "rw")) {
                                                    randomAccessFile.write(message.getContent());
                                                }
                                                ctx.close();
                                            }
                                    //        System.out.println("\nIncoming message: " + msg.getText());

                                        }
                                    }


                            );
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();

    //            while (true) {
                    final DownloadFileRequestMessage message = new DownloadFileRequestMessage();
                    message.setPath("D:\\JavaProject\\Echo-Server-Netty\\src\\main\\java\\Server\\123.txt");
                    channel.writeAndFlush(message);


//                    final TextMessage message = new TextMessage();
//                    System.out.print("Write msg: ");
//                    String msg = sc.nextLine();
//                    message.setText(msg);
//                    channel.writeAndFlush(message);
//                }
        }
    }
