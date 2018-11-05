package com.geekstomach.cloud.server.TheHat2;

import com.geekstomach.cloud.common.Auth;
import com.geekstomach.cloud.server.DB.DBService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    DBService db = new DBService();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;

            if (msg instanceof Auth) {
                Auth auth = (Auth) msg;
//Оставим регистрацию нового пользователя на потом
/*                if (auth.getStatus().equals("/create")) {
                    db.connect();

                    if (db.isLoginNameNotBusy(auth.getLogin())) {
                        db.addNewUser(auth.getLogin(), auth.getPassword());
                        //серверу сказать, чтобы запомнил логин и отправил клиенту сообщение, чтобы юзера впустили в облако
                        ctx.fireChannelRead(msg);
                        ctx.channel().pipeline().remove(this);
                        db.disconnect();
                    } else {
                        CloudCommand busyLogin = new CloudCommand("/busyLogin");
                        ctx.fireChannelRead(busyLogin);
                        db.disconnect();
                    }
                }*/
                if (auth.getStatus().equals("/enter")) {
                    db.connect();
if (db.isUserExists(auth.getLogin())){
                    if(db.getNickByLoginAndPass(auth.getLogin(), auth.getPassword())!=0) {
                        ctx.fireChannelRead(msg);
                        User user = new User(auth.getLogin(), auth.getPassword());
                        ctx.pipeline().addLast(new ServerHandler(user));
                        ctx.channel().pipeline().remove(this);//В случае успешной авторизации удаляем этот Handler из pipeline
                        db.disconnect();
                    }else{
                        CloudCommand wrongLoginOrPassword = new CloudCommand("/wrongLoginOrPassword");
                        ctx.fireChannelRead(wrongLoginOrPassword);
                        db.disconnect();
                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
