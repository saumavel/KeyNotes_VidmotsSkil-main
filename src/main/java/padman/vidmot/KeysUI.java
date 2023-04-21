package padman.vidmot;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import padman.vinnsla.SampleHolder;
import java.net.URL;
import java.util.*;

/**
 The KeysUI class is the controller class for the graphical user interface for the keyboard keys and its functionalities.
 It implements the Initializable interface.
 */
public class KeysUI implements Initializable {

    @FXML
    private Button fxTransUpSemi, fxTransDownSemi, fxTransReset;
    @FXML
    private Label fxRootNote;
    @FXML
    private Button fxZ, fxX, fxC, fxV, fxB, fxN, fxM, fxComma, fxDot, fxÞ, fxA, fxS, fxD, fxF, fxG, fxH, fxJ, fxK, fxL, fxÆ, fxQ, fxW, fxE, fxR, fxT, fxY, fxU, fxI, fxO, fxP, fx1, fx2, fx3, fx4, fx5, fx6, fx7, fx8, fx9, fx0;
    private Button[] buttons;
    private HashMap<KeyCode, Button> keycode_button_map;
    private int[] keyIndicesMajor = { 0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 12, 14, 16, 17, 19, 21, 23, 24, 26, 28, 24, 26, 28, 29, 31, 33, 35, 36, 38, 40, 36, 38, 40,41, 43, 45, 47, 48, 50, 52 };
    private Set<Integer> minor = new HashSet<>(Arrays.asList(4, 9, 11, 16, 21, 23, 28, 33, 35, 40, 45, 47, 52));
    private HashMap<KeyCode, Integer> keycode_int_map;
    private HashMap<Integer, String> int_noteNames_map;
    private ObservableList<KeyCode> pressedKeys = FXCollections.observableArrayList();
    @FXML
    private Button fxShowNotes, fxMinorMajor;
    private String[] keyboardKeys;
    @FXML
    private Button fxQuit, fxMinimize;
    @FXML
    private HBox topBar;
    @FXML
    private Slider fxVolSlide;
    private final String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private int transposition = 0;
    private boolean isMajor = true;
    private boolean showNotes = false;

    /**
     Initializes the GUI components and calls other necessary set-up methods.
     @param url URL location of the FXML file used to build the interface
     @param resourceBundle ResourceBundle used to localize the GUI components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtons();
        SampleHolder.importSamplesFromFolder("src/main/resources/padman/vidmot/Audio/PianoC2-C7");
        setStyleClasses();
        setRootNoteLabel();
        setUpFocus();
        setupListeners();
        addVolumeSlider(fxVolSlide);
    }
    /**
     * Sets up the focus traversable for certain GUI components.
     */
    private void setUpFocus() {
        fxShowNotes.setFocusTraversable(false);
        fxTransUpSemi.setFocusTraversable(false);
        fxTransDownSemi.setFocusTraversable(false);
    }

    private void setupListeners() {
        pressedKeys.addListener((ListChangeListener<KeyCode>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    List<? extends KeyCode> removed = change.getRemoved();
                    for (KeyCode keyCode : removed) {
                        SampleHolder.setFade(keycode_int_map.get(keyCode));
                    }
                } else if (pressedKeys.isEmpty()) {
                    SampleHolder.setFade();
                }
            }
        });
    }
    /**
     Initializes the Button[] buttons array and HashMap<KeyCode, Button> buttonMap used to map the KeyCodes to the buttons.
     */
    private void setButtons(){
        buttons = new Button[]{fxZ, fxX, fxC, fxV, fxB, fxN, fxM, fxComma, fxDot, fxÞ, fxA, fxS, fxD, fxF, fxG, fxH, fxJ, fxK, fxL, fxÆ, fxQ, fxW, fxE, fxR, fxT, fxY, fxU, fxI, fxO, fxP, fx1, fx2, fx3, fx4, fx5, fx6, fx7, fx8, fx9, fx0};
        String[] buttonIds = {"Z", "X", "C", "V", "B", "N", "M", "COMMA", "PERIOD", "SLASH", "A", "S", "D", "F", "G", "H", "J", "K", "L", "SEMICOLON", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "DIGIT1", "DIGIT2", "DIGIT3", "DIGIT4", "DIGIT5", "DIGIT6", "DIGIT7", "DIGIT8", "DIGIT9", "DIGIT0"};
        for (int i = 0; i < buttons.length && i < buttonIds.length; i++) {
            buttons[i].setId(buttonIds[i]);
        }
        int cnt = 0;
        keyboardKeys = new String[buttons.length];
        for(Button b : buttons){
            keyboardKeys[cnt] = b.getText();
            cnt++;
        }
        keycode_button_map = new HashMap<>();
        keycode_int_map = new HashMap<>();
        KeyCode[] keyCodes = { KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N, KeyCode.M, KeyCode.COMMA, KeyCode.PERIOD, KeyCode.SLASH, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L, KeyCode.SEMICOLON, KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T, KeyCode.Y, KeyCode.U, KeyCode.I, KeyCode.O, KeyCode.P, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3, KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6, KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9, KeyCode.DIGIT0 };
        for (int i = 0; i < keyCodes.length; i++) {
            keycode_button_map.put(keyCodes[i], buttons[i]);
            keycode_int_map.put(keyCodes[i], keyIndicesMajor[i]);
        }
        int_noteNames_map = new HashMap<>();

        int octave = 2;
        for (int i = 0; i < 64; i++) {
            String noteName = noteNames[i % noteNames.length];
            noteName += octave;
            int_noteNames_map.put(i, noteName);
            if ((i+1) % noteNames.length == 0) { octave++; }
        }
    }
    /**
     Sets the style classes for the buttons.
     */
    private void setStyleClasses() {
        for (int i = 0; i < 10; i++) {
            buttons[i].getStyleClass().add("oct1");
        }
        for (int i = 10; i < 20; i++) {
            buttons[i].getStyleClass().add("oct2");
        }
        for (int i = 20; i < 30; i++) {
            buttons[i].getStyleClass().add("oct3");
        }
        for (int i = 30; i < 40; i++) {
            buttons[i].getStyleClass().add("oct4");
        }
    }
    /**
     Called when a key is released.
     Removes the "buttonPressed" CSS style class, and removes the released key code from the pressedKeys set.
     @param e KeyEvent object
     */
    @FXML
    protected void onKeyReleased(KeyEvent e) {
        pressedKeys.remove(e.getCode());
        Button button = keycode_button_map.get(e.getCode());
        button.getStyleClass().remove("buttonPressed");
        }
    /**
     * Called when a key is pressed in the GUI. Determines the corresponding index of the media to be played
     * and plays the corresponding note. Updates the GUI to indicate that the key has beenpressed.
     *
     * @param e KeyEvent corresponding to the key that was pressed
     */
    @FXML
    protected void onKeyPressed(KeyEvent e) {
        int keyIndex = -1;
        if (keycode_int_map.containsKey(e.getCode())) {
            keyIndex = keycode_int_map.get(e.getCode()) + transposition;
        }
        if (keyIndex != -1) {
            if (pressedKeys.contains(e.getCode())) { return; }
            pressedKeys.add(e.getCode());
            if (!isMajor) {
                if (minor.contains(keyIndex - transposition)) {
                    keyIndex -= 1;
                }
            }
            SampleHolder.playMedia(keyIndex);
            Button button = keycode_button_map.get(e.getCode());
            button.getStyleClass().add("buttonPressed");
        }
    }

    @FXML
    private void mousePressedSample(MouseEvent e) {
        Button button = (Button) e.getSource();
        int keyIndex = -1;
        if (button != null) {
            keyIndex = keycode_int_map.get(KeyCode.valueOf(button.getId())) + transposition;
        }
        if (keyIndex != -1) {
            if (pressedKeys.contains(KeyCode.valueOf(button.getId()))) { return; }
            pressedKeys.add(KeyCode.valueOf(button.getId()));
            if (!isMajor) {
                if (minor.contains(keyIndex - transposition)) {
                    keyIndex -= 1;
                }
            }
            SampleHolder.playMedia(keyIndex);
        }
    }


    @FXML
    public void noteNamesHandler(ActionEvent e) {
        setButtonTxt(e);
    }

    /**
     * Sets the text of the Root Note label to the current root note
     */
    private void setRootNoteLabel() {
        fxRootNote.setText("Root Note: " + noteNames[transposition]);
    }

    /**
     * Handles the transposition of the keyboard
     * @param e button press event from one of the transposition buttons
     */
    @FXML
    public void transpose(ActionEvent e) {
        if (e.getSource().equals(fxTransUpSemi)) {
            transposition = (transposition + 1) % 12;
        } else if (e.getSource().equals(fxTransDownSemi)) {
            transposition = (transposition - 1 + 12) % 12;
        } else if (e.getSource().equals(fxTransReset)) {
            transposition = 0;
            if (!isMajor) { minorMajorButton(e); }
        }
        setRootNoteLabel();
        if(showNotes) { setButtonTxt(e); }
    }

    /**
     Sets the text of each button in the buttons array based on whether "Show Note Names" or "Show Keyboard" has been clicked.
     Also based on the current scale and transposition.
     */
    private void setButtonTxt(ActionEvent e) {
        int key = 0;
        if (fxShowNotes.getText().equals("Show Keyboard")) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setText(keyboardKeys[i]); }
            showNotes = false;
            fxShowNotes.setText("Show Notes");
            return;
        }
        for (int i = 0; i < buttons.length; i++) {
            key = keyIndicesMajor[i];
            if (!isMajor && minor.contains(key - transposition)) {
                key -= 1;
            }
            key += transposition;
            buttons[i].setText(int_noteNames_map.get(key));
        }
        fxShowNotes.setText("Show Keyboard");
    }

    /**
     * Handles the switching between major and minor scales
     * @param e event from the minor/major button
     */
    @FXML
    private void minorMajorButton(ActionEvent e) {
        if (isMajor) {
            fxMinorMajor.setText("Switch to Major");
            isMajor = false;
        } else {
            fxMinorMajor.setText("Switch to Minor");
            isMajor = true;
        }
        if (showNotes) { setButtonTxt(e); }
        setRootNoteLabel();
    }

    /**
     * Quits the application when x in the top left corner is clicked
     */
    @FXML
    private void quitApp() {
        Platform.exit();
    }
    /**
     * Minimizes the application when - in the top left corner is clicked
     */
    @FXML
    private void minimizeClick() {
        Stage stage = (Stage) fxMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
    Sets up a Volume slider to control the main volume of all media-players
     */
    public void addVolumeSlider(Slider fxVolSlide) {
        fxVolSlide.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100.0;
            SampleHolder.setVolume(volume);
        });
    }

    /**
     * Allows the window to be dragged by clicking and holding the top bar
     * @param event event from the top bar
     */
    @FXML
    private void dragWindow(MouseEvent event) {
        Stage stage = (Stage) topBar.getScene().getWindow();
        double offsetX = event.getSceneX();
        double offsetY = event.getSceneY();

        topBar.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - offsetX);
            stage.setY(dragEvent.getScreenY() - offsetY);
        });
    }
}
