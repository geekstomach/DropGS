package com.geekstomach.cloud.client.UI;

import com.geekstomach.cloud.client.Client;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    public JFXButton loginButton;
    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;
    @FXML
    private AnchorPane AnchorPane;

Client client;
    public void handleLoginButtonAction(ActionEvent actionEvent) {
//здесь по кнопке мы только проверяем правильность заполнения полей и
// отправляем запрос на авторизаци
        if (username.getText().isEmpty()||password.getText().isEmpty()){
            showAlertWithoutHeaderText("Заполните поля username и password!");
/*JFXDialog emptyFieldDialog = new JFXDialog(AnchorPane, new Label("Заполните поля username и password"),JFXDialog.DialogTransition.CENTER);
            emptyFieldDialog.show();*/
        } else {
            try {
                client.sendAuthMsg(client.getOut());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (username.getText().equals("admin")&&password.getText().equals("admin")){
                //createMainWindow();
                System.out.println("Пользователь с именем "+ username.getText() + " залогинился!");
                ((Stage)AnchorPane.getScene().getWindow()).hide();//скрывает окно авторизации
            } else {
                showAlertWithoutHeaderText("Вы ввели неверное сочетание login и password");
            }
        }
    }

    public void handleCancelButtonAction(ActionEvent actionEvent) {
        System.out.println("Пользователь с именем "+ username.getText() + " решил выйти(");
        System.exit(0);
    }

    public void showAlertWithoutHeaderText(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);//как украсить это окошко?
        alert.setTitle("be careful,please");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.show();
    }

    public void createMainWindow(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainWindow.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainWindowController mwc = (mainWindowController) loader.getController();
        stage.setTitle("StomachCloud Client : Main Window");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
