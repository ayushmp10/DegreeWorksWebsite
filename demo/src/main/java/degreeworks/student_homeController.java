package degreeworks;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Advisor;
import model.CourseList;
import model.DataLoader;
import model.Student;
import model.UserList;

public class student_homeController implements Initializable {
    private CourseList courseList;
    private UserList userList;
    private Student currStudent;
    @FXML private GridPane grid_studentInfo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userList = UserList.getInstance();
        // if the program has reached this far the current student
        currStudent = (Student) userList.getCurrUser();
        courseList = CourseList.getInstance();

        ArrayList<Advisor> allAdvisors = DataLoader.getAdvisors();
        
        Label studentTitle = new Label(currStudent.getFirstName() + " " + currStudent.getLastName());
        studentTitle.setFont(new Font(20));
        studentTitle.setMaxWidth(1000);
        studentTitle.setMaxHeight(grid_studentInfo.getMaxHeight());
        studentTitle.setMinHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(studentTitle, HPos.CENTER);
        studentTitle.setAlignment(Pos.CENTER);

        Label studentID = new Label(currStudent.getUSCID());
        studentID.setFont(new Font(20));
        studentID.setMaxWidth(1000);
        studentID.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        studentID.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(studentID, HPos.CENTER);
        studentID.setAlignment(Pos.CENTER);

        Label studentClassification = new Label(currStudent.getYear());
        studentClassification.setFont(new Font(20));
        studentClassification.setMaxWidth(1000);
        studentClassification.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        studentClassification.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(studentClassification, HPos.CENTER);
        studentClassification.setAlignment(Pos.CENTER);


        Label studentMajor = new Label(currStudent.getDegree().getSubject());
        studentMajor.setFont(new Font(20));
        studentMajor.setMaxWidth(1000);
        studentMajor.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        studentMajor.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(studentMajor, HPos.CENTER);
        studentMajor.setAlignment(Pos.CENTER);


        Label studentApplicationArea = new Label(currStudent.getApplicationArea());
        studentApplicationArea.setFont(new Font(20));
        studentApplicationArea.setMaxWidth(1000);
        studentApplicationArea.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        studentApplicationArea.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(studentApplicationArea, HPos.CENTER);
        studentApplicationArea.setAlignment(Pos.CENTER);


        String studentAdvisorName = "none";
        for (Advisor advisor : allAdvisors) {
            if (advisor.getUUID().equals(currStudent.getAdvisor())) {
                studentAdvisorName = advisor.getFirstName() + " " + advisor.getLastName();
            }
            
        }
        Label studentAdvisor = new Label(studentAdvisorName);
        studentAdvisor.setFont(new Font(20));
        studentAdvisor.setMaxWidth(1000);
        studentAdvisor.setMaxHeight(grid_studentInfo.getMaxHeight());
        studentAdvisor.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setHalignment(studentAdvisor, HPos.CENTER);
        studentAdvisor.setAlignment(Pos.CENTER);


        Label studentGPA = new Label(Double.toString(currStudent.getGPA()));
        studentGPA.setFont(new Font(20));
        studentGPA.setMaxWidth(1000);
        studentGPA.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        studentGPA.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(studentGPA, HPos.CENTER);
        studentGPA.setAlignment(Pos.CENTER);


        Label studentPhoneNumber = new Label(currStudent.getPhoneNumber());
        studentPhoneNumber.setFont(new Font(20));
        studentPhoneNumber.setMaxWidth(1000);
        studentPhoneNumber.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        studentPhoneNumber.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(studentPhoneNumber, HPos.CENTER);
        studentPhoneNumber.setAlignment(Pos.CENTER);


        /* 
        Label name = new Label("Student Name");
        name.setFont(new Font(20));
        name.setMaxWidth(1000);
        name.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        name.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(name, HPos.CENTER);

        Label id = new Label("USC ID");
        id.setFont(new Font(20));
        id.setMaxWidth(1000);
        id.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        id.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(id, HPos.CENTER);

        Label classification = new Label("Classification");
        classification.setFont(new Font(20));
        classification.setMaxWidth(1000);
        classification.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        classification.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(classification, HPos.CENTER);

        Label major = new Label("Major");
        major.setFont(new Font(20));
        major.setMaxWidth(1000);
        major.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        major.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(major, HPos.CENTER);

        Label applicationArea = new Label("Application Area");
        applicationArea.setFont(new Font(20));
        applicationArea.setMaxWidth(1000);
        applicationArea.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        applicationArea.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(applicationArea, HPos.CENTER);

        Label advisor = new Label("Advisor");
        advisor.setFont(new Font(20));
        advisor.setMaxWidth(1000);
        advisor.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        advisor.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(advisor, HPos.CENTER);

        Label gpa = new Label("GPA");
        gpa.setFont(new Font(20));
        gpa.setMaxWidth(1000);
        gpa.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        gpa.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(gpa, HPos.CENTER);

        Label phoneNumber = new Label("Phone Number");
        phoneNumber.setFont(new Font(20));
        phoneNumber.setMaxWidth(1000);
        phoneNumber.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        phoneNumber.setMaxHeight(grid_studentInfo.getMaxHeight());
        GridPane.setHalignment(phoneNumber, HPos.CENTER);
        */

        //grid_studentInfo.add(name, 0,0);
        //grid_studentInfo.add(id,0,1);
        //grid_studentInfo.add(classification,0,2);
        //grid_studentInfo.add(major,0,3);
        grid_studentInfo.add(studentTitle, 1, 0);
        grid_studentInfo.add(studentID, 1, 1);
        grid_studentInfo.add(studentClassification, 1, 2);
        grid_studentInfo.add(studentMajor, 1, 3);
        //grid_studentInfo.add(applicationArea, 2, 0);
        //grid_studentInfo.add(advisor, 2, 1);
        //grid_studentInfo.add(gpa, 2, 2);
        //grid_studentInfo.add(phoneNumber,2,3);
        grid_studentInfo.add(studentApplicationArea,3,0);
        grid_studentInfo.add(studentAdvisor,3,1);
        grid_studentInfo.add(studentGPA,3,2);
        grid_studentInfo.add(studentPhoneNumber,3,3);
        
    }

    private Advisor findAdvisorByID(ArrayList<Advisor> allAdvisors, UUID advisorID) {
        for (Advisor advisor : allAdvisors) {
            if (advisor.getID().equals(advisorID)) {
                return advisor;
            }
        }
        return null; // If no advisor with the given ID is found
    }

    @FXML
    void availableCoursesClicked() throws IOException{
        App.setRoot("student_availableCourses");
    }

    @FXML
    void changeMajorClicked() throws IOException{
        App.setRoot("student_changeMajor");
    }

    @FXML
    void commentsClicked() throws IOException{
        App.setRoot("student_comments");
    }

    @FXML
    void completedCoursesClicked() throws IOException{
        App.setRoot("student_completedCourses");
    }

    @FXML
    void homeClicked() throws IOException{
        App.setRoot("student_home");
    }

    @FXML
    void majorMapClicked() throws IOException{
        App.setRoot("student_majorMap");
    }

    @FXML
    void onLogOutClicked() throws IOException{
        App.setRoot("home");
    }

    @FXML
    void planGeneratorClicked() throws IOException{
        App.setRoot("student_planGenerator");
    }
}
