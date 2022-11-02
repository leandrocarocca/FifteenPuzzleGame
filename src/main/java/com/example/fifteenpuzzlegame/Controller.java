package com.example.fifteenpuzzlegame;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    public int numberOfMoves = 0;
    @FXML
    private  GridPane puzzleGridPane;
    @FXML
    private Button restartButton;
    @FXML
    private Label numberOfMovesLabel;
    @FXML
    private VBox vBox;
    @FXML
    private MenuBar menuBar;

    private int N;
    private ArrayList<Integer> winningCoordinates;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Skriv in N: ");
        N = scan.nextInt();
        for (int i = 0; i <= N; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setPercentWidth(100d / N);
                puzzleGridPane.getColumnConstraints().add(column);
                RowConstraints row = new RowConstraints();
                row.setPercentHeight(100d / N);
                puzzleGridPane.getRowConstraints().add(row);
        }
        Button[] arr = createArrayOfButtons(N, N);
        allocateButtons(N, N, arr);
        winningCoordinates = getCoordinates();
    }

    public void restartGame()
    {
        puzzleGridPane.getChildren().clear();
        numberOfMoves = 0;
        numberOfMovesLabel.setText("Number of moves: " + (numberOfMoves));
        Button[] arr = shuffleButtons(createArrayOfButtons(N, N));
        allocateButtons(N, N, arr);
        if(!isSolvable()){
            System.out.println("Ej lösbart");
            //restartGame();
        }
        else{
            System.out.println("Lösbar!");
        }

    }

    private ArrayList<Integer> getCoordinates()
    {
        ArrayList<Integer> coordinates = new ArrayList<>();
        for(Node node :puzzleGridPane.getChildren()){
            Button button = (Button) node;
            coordinates.add(GridPane.getRowIndex(button));
            coordinates.add(GridPane.getColumnIndex(button));
        }
        return coordinates;
    }

    public Button[] createArrayOfButtons(int numberOfCols, int numberOfRows)
    {
        String buttonNumber;
        int numberOfButtons = numberOfCols * numberOfRows;
        Button[] arrayOfButtons = new Button[numberOfButtons];
        for (int i = 0; i < numberOfButtons - 1; i++)
        {
            buttonNumber = String.valueOf(i + 1);
            Button button = new Button();
            button.setText(buttonNumber);
            button.setId("button" + buttonNumber);
            button.setOnAction(actionEvent ->
            {
                if(moveIfPossible((Button) actionEvent.getTarget())){
                    numberOfMoves++;
                    numberOfMovesLabel.setText("Number of moves: " + (numberOfMoves));
                }
                checkWin();
            });
            GridPane.setHgrow(button, Priority.ALWAYS);
            button.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
            arrayOfButtons[i] = button;
        }
        Button emptySlot = new Button();
        emptySlot.setId("emptySlot");
        GridPane.setHgrow(emptySlot, Priority.ALWAYS);
        emptySlot.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        arrayOfButtons[numberOfButtons - 1] = emptySlot;

        return arrayOfButtons;
    }

    public Button[] shuffleButtons(Button[] arrayOfButtons)
    {
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < arrayOfButtons.length; i++)
        {
            positions.add(i);
        }
        Collections.shuffle(positions);
        Button[] newArrayOfButtons = new Button[arrayOfButtons.length];
        int index = 0;
        for (int pos : positions)
        {
            newArrayOfButtons[pos] = arrayOfButtons[index];
            index++;
        }
        return newArrayOfButtons;
    }

    public boolean moveIfPossible(Button sourceButton) {
        int currentRow = GridPane.getRowIndex(sourceButton);
        int currentColumn = GridPane.getColumnIndex(sourceButton);
        int neighborRow;
        int neighborColumn;
        Button button;
        for (Node node : puzzleGridPane.getChildren())
        {
            neighborColumn = GridPane.getColumnIndex(node);
            neighborRow = GridPane.getRowIndex(node);
            if (neighborRow == currentRow && (neighborColumn == currentColumn - 1 || neighborColumn == currentColumn + 1))
            {
                button = (Button) node;
                if (button.getId().equals("emptySlot"))
                {
                    swap(button, sourceButton);
                    return true;
                }

            } else if (neighborColumn == currentColumn && (neighborRow == currentRow - 1 || neighborRow == currentRow + 1))
            {
                button = (Button) node;
                if (button.getId().equals("emptySlot"))
                {
                    swap(button, sourceButton);
                    return true;
                }
            }
        }
        return false;
    }

    public void swap(Button button1, Button button2){
        Integer temp = GridPane.getRowIndex(button1);
        GridPane.setRowIndex(button1, GridPane.getRowIndex(button2));
        GridPane.setRowIndex(button2, temp);

        temp = GridPane.getColumnIndex(button1);
        GridPane.setColumnIndex(button1, GridPane.getColumnIndex(button2));
        GridPane.setColumnIndex(button2, temp);
    }
    public void allocateButtons(int numberOfCols, int numberOfRows, Button[] buttonArray){
        Button[] temp = new Button[numberOfRows];
        int counter = 0;
        for(int i = 0; i < numberOfRows; i++){
            for(int j = 0; j < numberOfCols; j++){
                temp[j] = buttonArray[counter];
                counter++;
            }
            puzzleGridPane.addRow(i, temp);
        }
    }

    public void checkWin(){
        ArrayList<Integer> coordinates = getCoordinates();
        int index = 0;
        boolean win = true;
        for(int i: coordinates){
            if(i != winningCoordinates.get(index)){
                win = false;
            }
            index++;
        }
        if (win){
            System.out.println("Du vann!");
        }
        else {
            System.out.println("Ingen vinst");
        }
    }

    public boolean isSolvable(){
        /*
        from https://www.geeksforgeeks.org/check-instance-15-puzzle-solvable/
        If N is odd, then puzzle instance is solvable if number of inversions is even in the input state.
        If N is even, puzzle instance is solvable if
        the blank is on an even row counting from the bottom (second-last, fourth-last, etc.) and number of inversions is odd.
        the blank is on an odd row counting from the bottom (last, third-last, fifth-last, etc.) and number of inversions is even.
    For all other cases, the puzzle instance is not solvable.
         */
        int row;
        int col;
        ArrayList<Button> buttonsInOrder = new ArrayList<>();
        ArrayList<Integer> coordinates = getCoordinates();
        for (int i = 0; i < coordinates.size() - 1; i++){
            row = coordinates.get(i);
            i++;
            col = coordinates.get(i);
            Button button = getButtonByCoordinates(row, col);
            buttonsInOrder.add(button);
        }
        int numberOfInversions = getNumberOfInversions(buttonsInOrder);
        int rowOfEmptySlot = -1;
        for(Node node :puzzleGridPane.getChildren()){
            Button button = (Button) node;
            if(button.getId().equals("emptySlot")){
                rowOfEmptySlot = GridPane.getRowIndex(button);
            }
        }
        int rowOfEmptySlotFromBottom = Math.abs(rowOfEmptySlot - (N - 1));
        if(N % 2 == 0){
            if(numberOfInversions % 2 != 0){
                return rowOfEmptySlotFromBottom % 2 == 0;
            }
            else return rowOfEmptySlotFromBottom % 2 != 0;
        }
        else{
            return numberOfInversions % 2 == 0;
        }
    }

    public Button getButtonByCoordinates(int row, int col){
        Button tempButton;
        for(Node node: puzzleGridPane.getChildren()){
            tempButton = (Button) node;
            if(GridPane.getColumnIndex(tempButton) == col && GridPane.getRowIndex(tempButton) == row){
                return tempButton;
            }
        }
        return null;
    }

    public int getNumberOfInversions(ArrayList<Button> arrayOfButtons){
        Button b1;
        Button b2;
        int val1;
        int val2;
        int j;
        int inversions = 0;
        for (int i = 0; i < arrayOfButtons.size() - 1; i++){
            b1 = arrayOfButtons.get(i);
            j = i + 1;
            b2 = arrayOfButtons.get(j);
            try
            {
                val1 = Integer.parseInt(b1.getText());
                val2 = Integer.parseInt(b2.getText());
                val2++;
                if(val1 == val2){
                    inversions++;
                }
            }
            catch (NumberFormatException e){
                System.out.println("Cannot parse a string that does not represent a number.");
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        return inversions;
    }





}