package controllers;

import database.LocalDataBase;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.GameDataPVP;
import models.SceneNavigator;
import java.util.Calendar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class GamePVPController implements Initializable {

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;
    @FXML
    private Label Player1Score;
    @FXML
    private Label Player2Score;
    @FXML
    private Label TieScore;
    @FXML
    private Button playAgainButton;

    private int playerTurn = 0;
    private int p1score = 0;
    private int p2score = 0;
    private int tiescore = 0;
    private String line = "";
    private int squareCount = 0;
    private int flagWin = 0;
    private ArrayList<Button> buttons;
    private byte[] b;
    private String bString = "-";   //added this at first cause printing reading the file didnt show first letter..
    @FXML
    private ImageView backImg;
    @FXML
    private ImageView saveImg;
    @FXML
    private ImageView loadImg;
    @FXML
    private Label Player1Name;
    @FXML
    private Label Player2Name;
    LocalDataBase ldb;

    private Parent root;
    private Scene scene;
    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));

        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });
    }

    @FXML
    void playAgain(ActionEvent event) {
        playerTurn = 0;
        squareCount = 0;
        flagWin = 0;
        b = "".getBytes(); // do this better
        bString = "-";
        buttons.forEach(this::resetButton);
    }

    public void resetButton(Button button) {
        button.setDisable(false);
        button.setText("");
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            try {
                setPlayerSymbol(button);
                button.setDisable(true);
                String check = checkIfGameIsOver();
                if (winLoseChecker(check)==true) {
                    try {
                        
                        SceneNavigator sceneNavigator = new SceneNavigator();
                        sceneNavigator.navigateImg(mouseEvent, "/views/Winner.fxml");
                        
                    } catch (IOException ex) {
                        Logger.getLogger(GamePVPController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(GamePVPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void setPlayerSymbol(Button button) {
        if (playerTurn % 2 == 0) {
            button.setText("X");
            playerTurn = 1;
        } else {
            button.setText("O");
            playerTurn = 0;
        }

        bString += button.getId() + button.getText() + "-";
    }

    public String checkIfGameIsOver() throws IOException {

        squareCount++;
        String d = "";
        line = button1.getText() + button2.getText() + button3.getText();
        winLoseChecker(line);
        line = button4.getText() + button5.getText() + button6.getText();
        winLoseChecker(line);
        line = button7.getText() + button8.getText() + button9.getText();
        winLoseChecker(line);
        line = button1.getText() + button5.getText() + button9.getText();
        winLoseChecker(line);
        line = button3.getText() + button5.getText() + button7.getText();
        winLoseChecker(line);
        line = button1.getText() + button4.getText() + button7.getText();
        winLoseChecker(line);
        line = button2.getText() + button5.getText() + button8.getText();
        winLoseChecker(line);
        line = button3.getText() + button6.getText() + button9.getText();
        winLoseChecker(line);
        if (squareCount == 9 && flagWin == 0) {
            getData("Tied");
            tiescore++;
            TieScore.setText(Integer.toString(tiescore));
        }
        d=line;
        return d;
    }

    boolean winLoseChecker(String line) throws IOException {
        switch (line) {
            case "XXX":
                flagWin = 1;
                p1score++;
                Player1Score.setText(Integer.toString(p1score));
                 {
                    try {
                        getData(Player1Name.getText());
                    } catch (IOException ex) {
                        Logger.getLogger(GamePVPController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                buttons.forEach(button -> {
                    button.setDisable(true);
                });
                return true;
            case "OOO":
                flagWin = 1;
                p2score++;
                Player2Score.setText(Integer.toString(p2score));
                getData(Player2Name.getText());
                buttons.forEach(button -> {
                    button.setDisable(true);
                });
                return true;
            default:

                return false;
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        SceneNavigator sceneNavigator = new SceneNavigator();
        sceneNavigator.navigateImg(event, "/views/Menu.fxml");
    }

    @FXML
    private void goToHistoryPVP(ActionEvent event) throws IOException {
        SceneNavigator sceneNavigator = new SceneNavigator();
        sceneNavigator.navigateBtn(event, "/views/HistoryPVP.fxml");

    }

    public void DisplayNames(String p1Name, String p2Name) {
        Player1Name.setText(p1Name);
        Player2Name.setText(p2Name);
    }

    public void getData(String winner) throws IOException {
        String date = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss").format(Calendar.getInstance().getTime());
        ldb = new LocalDataBase();

        GameDataPVP gm = new GameDataPVP(
                date, Player1Name.getText(), Player1Score.getText(),
                Player2Name.getText(), Player2Score.getText(), winner);
        ldb.writeData(gm);
    }

    @FXML
    private void saveGame(MouseEvent event) {

        // should create an alert here.
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();

        File file = fc.showSaveDialog(stage);
        FileOutputStream fos;
        try {
            if (file != null) {
                fos = new FileOutputStream(file);
                b = bString.getBytes();
                fos.write(b);
                // 0X-1O-3X
                fos.close();
            }
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
        }

    }

    @FXML
    private void loadGame(MouseEvent event) {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(stage);
        FileInputStream fis;

        try {
            if (file != null) {
                fis = new FileInputStream(file);
                while (fis.read() != -1) {
                    int size = fis.available();
                    b = new byte[size];
                    fis.read(b);
                    bString = new String(b);
                    String[] arrString = bString.split("-");

                    for (int i = 0; i < arrString.length; i++) {
                        if ("X".equals(Character.toString(arrString[i].charAt(1))) | "O".equals(Character.toString(arrString[i].charAt(1)))) {
                            buttons.get(Integer.parseInt(Character.toString(arrString[i].charAt(0))) - 1).setText(Character.toString(arrString[i].charAt(1)));
                            buttons.get(Integer.parseInt(Character.toString(arrString[i].charAt(0))) - 1).setDisable(true);
                        }

                        if (arrString.length % 2 == 0) {
                            playerTurn = 0;
                        } else {
                            playerTurn = 1;
                        }

                    }

                    fis.close();
                }
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    public void Data(String p1Name, String p2Name, int p1Score, int p2Score, int tScore) {
        Player1Name.setText(p1Name);
        Player2Name.setText(p2Name);
        Player1Score.setText(Integer.toString(p1Score));
        Player2Score.setText(Integer.toString(p2Score));
        TieScore.setText(Integer.toString(tScore));
    }

}