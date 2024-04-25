package services;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DataValidation {

    public static boolean dataLength(TextField inputTextField, Label inputLabel, String validationText, String requiredLength) {
        boolean isDataLength = true;
        String validationString = null;

        if (!inputTextField.getText().matches("\\b\\w{" + requiredLength + "}")) {
            isDataLength = false;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isDataLength;
    }


    public static boolean textAlphabet(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isAlphabet = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[a-zA-Z]+")) {
            isAlphabet = false;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isAlphabet;
    }

    public static boolean textNumeric(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isNumeric = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[0-9]+")) {
            isNumeric = false;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isNumeric;
    }
    public static boolean textNumericPhone(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isNumeric = true;
        String validationString = null;

        // Ensure exactly 8 digits from 0 to 9
        if (!inputTextField.getText().matches("[0-9]{8}")) {
            isNumeric = false;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isNumeric;
    }

    public static boolean emailFormat(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isEmail = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com")) {
            isEmail = false;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isEmail;
    }

    public static boolean zID(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isZID = true;
        String validationString = null;

        if (!inputTextField.getText().matches("z[0-9]{7}")) {
            isZID = false;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isZID;
    }

    public static boolean textFieldIsNull(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isNull = false;
        String validationString = null;

        if (inputTextField.getText().isEmpty()) {
            isNull = true;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isNull;
    }

    private static void displayValidationMessage(Label inputLabel, String validationString) {
        if (validationString != null) {
            inputLabel.setTextFill(Color.RED);
            inputLabel.setText(validationString);
        }
    }

    public static boolean textAlphabetspace(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isAlphabet = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[a-zA-Z ]+")) {
            isAlphabet = false;
            validationString = validationText;
        }

        displayValidationMessage(inputLabel, validationString);

        return isAlphabet;
    }

}