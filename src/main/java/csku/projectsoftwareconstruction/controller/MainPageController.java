package csku.projectsoftwareconstruction.controller;

import csku.projectsoftwareconstruction.model.DBConnection;
import csku.projectsoftwareconstruction.model.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class MainPageController {

    private Student presentStudent;

    @FXML protected Button loginBtn;
    @FXML protected TextField studentID;


    @FXML
    public void loginBtnHandlerAction(ActionEvent actionEvent) {
       if (studentID.getText().isEmpty()){
           Alert alertContext = new Alert(Alert.AlertType.INFORMATION);
           alertContext.setTitle("Information Nisit");
           alertContext.setContentText("Please fill up this form!");
           alertContext.showAndWait();
       }
       else {
           String id = studentID.getText();
           presentStudent = readStudentFromDB(id);
           if (presentStudent == null){
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Not Found!");
               alert.setHeaderText(null);
               alert.setContentText("Not found Student ID");
               alert.showAndWait();
           }
           else {
               try {
                   Stage stage = (Stage) loginBtn.getScene().getWindow();
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/show_allcourse.fxml"));
                   stage.setTitle("All Course Information");
                   stage.setScene(new Scene(loader.load(), 1050, 700));
                   ShowAllCourseController showAll = (ShowAllCourseController) loader.getController();
                   showAll.setUpStudent(presentStudent.getId(), presentStudent.getName());
                   stage.show();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
       studentID.clear();
    }

    public Student readStudentFromDB(String id){
        Student student = null;
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM Student WHERE student_id='" + id
                    + "'";
            connection = dbConnection.getConnectionDB();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String studentId = resultSet.getString("student_id");
                String studentName = resultSet.getString("student_name");
                student = new Student(studentId, studentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnectionDB(connection, statement, resultSet);
        }
        return student;
    }
}
