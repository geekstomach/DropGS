package com.geekstomach.cloud.server;

import org.sqlite.JDBC;

import java.sql.*;

public class AuthService {
    private Connection connection;
    private Statement statement;
    String path = "jdbc:sqlite:server\\src\\main\\resources\\UsersDB.db";

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection(path);//тут какаято ошибка

        statement = connection.createStatement();
        System.out.println("База Подключена!");
    }
    public String getNickByLoginAndPass(String login, String pass){
        try {
            ResultSet rs = statement.executeQuery("SELECT nick FROM Users WHERE login = '" + login + "' AND password = '" + pass + "';");
            while (rs.next()){
                return rs.getString("nick");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --------Закрытие--------
    public void disconnect(){
        try {
            statement.close();
            connection.close();
            System.out.println("Соединения закрыты");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AuthService authService = new AuthService();
        authService.connect();
        System.out.println(authService.getNickByLoginAndPass("login", "password"));
        System.out.println(authService.getNickByLoginAndPass("admin", "admin"));
        authService.disconnect();
    }*/
}
