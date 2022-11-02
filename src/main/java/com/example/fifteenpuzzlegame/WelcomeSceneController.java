package com.example.fifteenpuzzlegame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeSceneController
{
    @FXML
    public Label welcomeLabel;
    @FXML
    public Button startGameButton;
    @FXML
    public TextField nTextField;
    @FXML
    public Label textFieldLabel;
    Scene scene;
    Stage stage;
    public void switchToGameScene(ActionEvent event) throws IOException
    {
        int N = 0;
        try
        {
            N = Integer.parseInt(nTextField.getText());
        }
        catch (NumberFormatException e){
            welcomeLabel.setText("Please enter an integer!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("gameScene.fxml"));
        Parent parent = loader.load();

        scene = new Scene(parent);

        // Get gameController
        gameController controller = loader.getController();
        controller.startGame(N);
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setN(ActionEvent actionEvent)
    {
    }
}
