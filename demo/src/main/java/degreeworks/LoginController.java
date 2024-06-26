package degreeworks;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.*;

public class LoginController {
    private DegreeWorks degreeWorks;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void initialize() {
        degreeWorks = DegreeWorks.getInstance();
    }

    @FXML
    private void handleLoginButtonAction() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserList userList = UserList.getInstance();
        if (username != null && password != null) {

            // Log the user in
            if (degreeWorks.login(username, password)) {
                // this needs to be able to determine what type of user is logging in and move
                // them to that page
                User currUser = userList.getUser(username, password);
                boolean setCurrUser = userList.setCurrUser(currUser);
                System.out.println(setCurrUser);
                if (currUser instanceof Student) {
                    // move to student page
                    App.setRoot("student_home");
                } else if (currUser instanceof Advisor) {
                    // move to advisor page
                    App.setRoot("advisor_home");// this is an empty unopenable page currently
                } else if (currUser instanceof Guardian) {
                    // move to guardian page
                    App.setRoot("guardian_home");
                }
            } else {
                Utility.showAlert("ERROR", "Invalid Credentials", "Incorrect Username or Password.");
            }
        } else {
            // Throw an error message: "Incorrect password or username"
            Utility.showAlert("ERROR", "Invalid Credentials", "Incorrect Username or Password.");
        }
    }

    @FXML
    void backButtonClicked(ActionEvent event) {
        try {
            App.setRoot("home");
        } catch (IOException ioe) {
            Utility.showAlert("ERROR", "Exception loading home page", ioe.getLocalizedMessage());
        }
    }
}
