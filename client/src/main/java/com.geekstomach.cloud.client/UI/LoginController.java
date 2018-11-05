package com.geekstomach.cloud.client.UI;


import com.geekstomach.cloud.client.utils.Client;
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

    public JFXTextField getUsername() {
        return username;
    }

    public JFXPasswordField getPassword() {
        return password;
    }

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    public javafx.scene.layout.AnchorPane getAnchorPane() {
        return AnchorPane;
    }

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
            System.out.println("Пробуем инициализировать пользоателя");
            System.out.println(username.getText());
            System.out.println(password.getText());
            //client = new Client(username.getText(),password.getText());
            Client.getInstance().setLogin(username.getText());
            Client.getInstance().setPassword(password.getText());
            username.clear();
            password.clear();
            Client.getInstance().initialize();

            if (Client.getInstance().isAuthorized())
                createMainWindow();
        }
    }

    public void handleCancelButtonAction(ActionEvent actionEvent) {
        System.out.println("Пользователь решил выйти(");
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
        System.out.println("создаем основное окно");
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
        ((Stage)this.getAnchorPane().getScene().getWindow()).hide();//скрывает окно авторизации
        stage.show();
        //здесь же надо заполнить поля файлов
    }
}
