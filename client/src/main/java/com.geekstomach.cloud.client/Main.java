package com.geekstomach.cloud.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml")); //загружаем интерфейс из файла
        primaryStage.initStyle(StageStyle.TRANSPARENT);//Что это?
        primaryStage.setTitle("StomachCloud Client : Login Window"); //создаем окно

        primaryStage.setScene(new Scene(root)); //
        primaryStage.setResizable(false);//запрещаем изменение размеров данного окна
        primaryStage.show(); //показать окно


    }

    @Override
    public void stop() throws Exception {
        super.stop();

    }


    public static void main(String[] args) {
        launch(args);

    }

}
