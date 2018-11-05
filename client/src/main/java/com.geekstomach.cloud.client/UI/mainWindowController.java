package com.geekstomach.cloud.client.UI;

import com.geekstomach.cloud.client.utils.Client;
import com.geekstomach.cloud.common.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class mainWindowController implements Initializable{
    @FXML
    Button sendFile;
    @FXML
    Button deleteLocalFile;
    @FXML
    Button loadFile;
    @FXML
    Button deleteServerFile;

    @FXML
    TableView<FileInfo> localFilesTable, cloudFilesTable;
    @FXML
    TableColumn<FileInfo,String> nameColumnClient;
    @FXML
    TableColumn<FileInfo,String> sizeColumnClient;
    @FXML
    TableColumn<FileInfo,String> nameColumnServer;
    @FXML
    TableColumn<FileInfo,String> sizeColumnServer;

    private ObservableList<FileInfo> cloudFilesList;
    private ObservableList<FileInfo> localFilesList;

    public void sendFile(ActionEvent actionEvent) {
        try {
            Client.getInstance().sendFile(Paths.get(localFilesTable.getSelectionModel().getSelectedItem().getFile().getAbsolutePath()),Client.getInstance().getOut());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Отправить файл...");
        System.out.println(Paths.get(localFilesTable.getSelectionModel().getSelectedItem().getFile().getAbsolutePath()).toString());

    }



    public void deleteLocalFile(ActionEvent actionEvent) {
        System.out.println("Удалить локальный файл...");
    }

    public void loadFile(ActionEvent actionEvent) {
        Client.getInstance().loadFile(Paths.get(cloudFilesTable.getSelectionModel().getSelectedItem().getFile().getAbsolutePath()));
        System.out.println("Загрузить файл...");

    }

    public void deleteServerFile(ActionEvent actionEvent) {
        System.out.println("Удалить облачный файл...");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Интерфейс ObservableList необходим для updating'а данных в TableView
              cloudFilesList = FXCollections.observableArrayList();
              localFilesList = FXCollections.observableArrayList();

          //для ячеек колонк необходимо установить PropertyValueFactory
             nameColumnClient.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("file"));
             sizeColumnClient.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("size"));
            nameColumnServer.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("file"));
            sizeColumnServer.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("size"));

             //привязываем колонки.
             //localFilesTable.getColumns().addAll(nameColumnClient,sizeColumnClient);
                  //привязываем лист.
             localFilesTable.setItems(localFilesList);
             cloudFilesTable.setItems(cloudFilesList);

        try {
            localFilesList.clear();
            cloudFilesList.clear();
            localFilesList.addAll(Files.list(Paths.get("client\\local_storage")).map(Path::toFile).map(FileInfo::new).collect(Collectors.toList()));
            cloudFilesList.addAll(getCloudFileList());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
private ObservableList<FileInfo> getCloudFileList(){

        return Client.getInstance().getCloudFileList();
}
    //метод возвращающий в окно авторизации
}
