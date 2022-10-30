package com.example.fifteenpuzzlegame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Controller
{
    int numberOfMoves;
    @FXML
    private Label gameLabel;
    @FXML
    private GridPane puzzleGridPane;
    @FXML
    private Button quitButton;

    public void startGame(){
        gameLabel.setText("Number of moves: " + numberOfMoves);
    }

    public void quitGame(){
        System.out.println("Game ended!");
    }
}