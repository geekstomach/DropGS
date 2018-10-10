package com.geekstomach.cloud.server.DB;

import org.sqlite.JDBC;

import java.sql.*;

public class DBService {
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
    public void addUser(String nick, String login, String password) {

        try {
        connect();
        statement = connection.prepareStatement("INSERT INTO Users (nick,login,password) VALUES ('"+nick+"','"+login+"','"+password+"');");
((PreparedStatement) statement).executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }

    }
    public String getNickByLoginAndPass(String login, String pass){
        try {
            connect();
            ResultSet rs = statement.executeQuery("SELECT nick FROM Users WHERE login = '" + login + "' AND password = '" + pass + "';");
            while (rs.next()){
                return rs.getString("nick");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
        return null;
    }

    public int getUserIDByLoginAndPass(String login, String pass){
        try {
            connect();
            ResultSet rs = statement.executeQuery("SELECT ID FROM Users WHERE login = '" + login + "' AND password = '" + pass + "';");
            while (rs.next()){
                return rs.getInt("ID");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
        return -1;
    }
    public boolean isUserExists (String login){
        try {
            connect();
            ResultSet rs = statement.executeQuery("SELECT login FROM Users WHERE login = '" + login + "';");
            if (rs == null || rs.isClosed() || !rs.next()) return false;
            else return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
        return false;}
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
}
