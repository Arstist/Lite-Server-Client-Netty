package Server.Handler;

import Lite.*;
import Server.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.util.Date;

public class LiteServerChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel is registered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel is un    registered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel is active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel is inactive");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if (msg instanceof TextMessage) {
            Command command = new Command();
            var message = (TextMessage) msg;
            System.out.println("incoming text message: " + message.getText());

            if(command.checker(message.getText())) {
                ((TextMessage) msg).setText(command.checkCommand(message.getText()));
            }
            ctx.writeAndFlush(msg);

        }
        if (msg instanceof DateMessage) {
            var message = (DateMessage) msg;
            System.out.println("incoming date message: " + message.getDate());
            ctx.writeAndFlush(msg);
        }

        if(msg instanceof DownloadFileRequestMessage) {
            var message = (DownloadFileRequestMessage) msg;
            try(RandomAccessFile accessFile = new RandomAccessFile(message.getPath(), "r")) {
                final FileMessage fileMessage = new FileMessage();
                accessFile.length();
                byte[] content = new byte[(int)accessFile.length()];
                accessFile.read(content);
                fileMessage.setContent(content);
                ctx.writeAndFlush(fileMessage);
            }
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Catch cause " + cause.getMessage());
        ctx.close();
    }

    public String getDate() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s/%s/%s | %s:%s", now.getDayOfMonth(), now.getMonthValue(), now.getYear(), now.getHour(), now.getMinute());
    }
}
