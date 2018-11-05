package com.geekstomach.cloud.server.TheHat2;

import com.geekstomach.cloud.server.DB.DBService;

public class User {
    int userID;
    String nick;
    String login;
    String password;
    String userDir;
    DBService table = new DBService();

    public User( String nick, String login, String password) {
        table.addUser(nick,login,password);
        this.userID =   table.getUserIDByLoginAndPass(login, password);
        System.out.println(userID);
        this.nick = nick;
        this.login = login;
        this.password = password;
        userDir = "server\\repository\\"+userID;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.nick = table.getNickByLoginAndPass(login, password);
        this.userID = table.getUserIDByLoginAndPass(login, password);


    }



    public static void main(String[] args) {

        User user = new User("Austin","Powers","Dangerous" );
        System.out.println(user.table.getNickByLoginAndPass("login", "password"));
        System.out.println(user.table.getNickByLoginAndPass("admin", "admin"));
        System.out.println(user.table.isUserExists("admin"));
        System.out.println(user.table.isUserExists("Жора"));
    }
}
