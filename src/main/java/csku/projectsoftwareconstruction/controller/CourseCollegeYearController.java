package csku.projectsoftwareconstruction.controller;

import csku.projectsoftwareconstruction.model.Course;
import csku.projectsoftwareconstruction.model.DBConnection;
import csku.projectsoftwareconstruction.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CourseCollegeYearController {

    private Student student;
    private ArrayList<Course> allCollegeYearCourse;
    private ObservableList<Course> collegeYearsCourse;


    @FXML protected TextField studentID;
    @FXML protected TextField studentName;
    @FXML protected TableView collegeYearCourseTableView;
    @FXML protected TableColumn courseIDColumn;
    @FXML protected TableColumn titleColumn;
    @FXML protected TableColumn creditColumn;
    @FXML protected TableColumn yearColumn;
    @FXML protected TableColumn difficultLevelColumn;
    @FXML protected TableColumn passColumn;
    @FXML protected TableColumn prereqColumn;
    @FXML protected Button passBtn;
    @FXML protected Button viewCourseDetailBtn;
    @FXML protected TextField courseIdSearch;
    @FXML protected TextArea courseDetailTextArea;
    @FXML protected Button logoutBtn;
    @FXML protected Button backPageBtn;

    @FXML
    public void setUp(String id, String name, int collegeYear){
        student = new Student(id, name);
        allCollegeYearCourse = new ArrayList<>();
        collegeYearsCourse = FXCollections.observableArrayList();
        studentID.setText(student.getId());
        studentName.setText(student.getName());
        getAllCourseCollegeYear(collegeYear);
        getPassCourse();
        collegeYearsCourse.addAll(allCollegeYearCourse);
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("courseId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("credit"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("year"));
        difficultLevelColumn.setCellValueFactory(new PropertyValueFactory<Course,String>("difficultLevel"));
        passColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("completeClass"));
        prereqColumn.setCellValueFactory(new PropertyValueFactory<Course,String>("prereqId"));
        setColorDifficultLevel();
        collegeYearCourseTableView.setItems(collegeYearsCourse);
    }

    @FXML
    public void selectCourseToPassHandlerAction(ActionEvent actionEvent){
        courseDetailTextArea.clear();
        String courseId = courseIdSearch.getText();
        if (courseId.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CourseID is empty!");
            alert.setContentText("Please fill in courseID search field.");
            alert.showAndWait();
        }
        else {
            boolean found = false;
            for (Course course : allCollegeYearCourse) {
                if (course.getCourseId().equals(courseId)) {
                    found = true;
                    if (!student.getPassCourse().contains(course)) {
                        course.setCompleteClass("PASS");
                        student.addCompleteCourse(course);
                        DBConnection dbConnection = new DBConnection();
                        Connection connection = dbConnection.getConnectionDB();
                        PreparedStatement statement = null;
                        String query = "INSERT INTO takes VALUES(?,?)";
                        try {
                            statement = connection.prepareStatement(query);
                            statement.setString(1, student.getId());
                            statement.setString(2, courseId);
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            dbConnection.closeConnectionDB(connection, statement, null);
                        }
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information ERROR");
                        alert.setHeaderText(null);
                        alert.setContentText("You not have to check this course is pass because your passed!");
                        alert.showAndWait();
                    }
                    break;
                }
            }
            if (found){
                collegeYearsCourse.clear();
                collegeYearsCourse.addAll(allCollegeYearCourse);
                collegeYearCourseTableView.setItems(collegeYearsCourse);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not Found!");
                alert.setContentText("Not found courseID!");
                alert.showAndWait();
            }
        }
        courseIdSearch.clear();
    }

    @FXML
    public void viewCourseDetailBtnHandlerAction(ActionEvent actionEvent){
        if (courseIdSearch.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CourseID is empty!");
            alert.setContentText("Please fill in courseID search field.");
            alert.showAndWait();
        }
        else {
            boolean found = false;
            Course courseDetail = null;
            for (Course course : allCollegeYearCourse) {
                if (course.getCourseId().equals(courseIdSearch.getText())){
                    courseDetail = course;
                    found = true;
                    break;
                }
            }
            if (!found){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not Found!");
                alert.setContentText("Not found courseID!");
                alert.showAndWait();
            }
            else {
                courseDetailTextArea.setText("ID: " + courseDetail.getCourseId() +
                        "\nTitle: " + courseDetail.getTitle() + "\nCredit: " + courseDetail.getCredit() +
                        "\nPrereq: " + courseDetail.getPrereqId());
            }
        }
        courseIdSearch.clear();
    }

    private void getAllCourseCollegeYear(int collegeYear){
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM Course WHERE year = '" + collegeYear + "'";
            connection = dbConnection.getConnectionDB();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String courseId = resultSet.getString(1);
                String title = resultSet.getString(2);
                int credit = resultSet.getInt(3);
                int year = resultSet.getInt(4);
                String difficultLevel = resultSet.getString(5);
                String pass = resultSet.getString(6);
                String prereq = resultSet.getString(7);
                allCollegeYearCourse.add(new Course(courseId, title, credit, year, difficultLevel, pass, prereq));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnectionDB(connection, statement, resultSet);
        }
    }

    private void getPassCourse(){
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM takes WHERE student_id = '" + student.getId() + "'";
            connection = dbConnection.getConnectionDB();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String courseID = resultSet.getString(2);
                for (Course course : allCollegeYearCourse){
                    if (course.getCourseId().equals(courseID)){
                        course.setCompleteClass("PASS");
                        student.addCompleteCourse(course);
                        break;
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            dbConnection.closeConnectionDB(connection, statement, resultSet);
        }
    }

    private void setColorDifficultLevel(){
        difficultLevelColumn.setCellFactory(column->{
            return new TableCell<Course, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty){
                        setText("");
                        setStyle("");
                    }
                    else {
                        setItem(item);
                        Course course = getTableView().getItems().get(getIndex());
                        if (course.getDifficultLevel().equals("easy")){
                            setTextFill(Color.valueOf("00FA9A"));
                            setStyle("-fx-background-color: #00FA9A");
                        }
                        else if (course.getDifficultLevel().equals("medium")){
                            setTextFill(Color.valueOf("87CEFA"));
                            setStyle("-fx-background-color: #87CEFA");
                        }
                        else if (course.getDifficultLevel().equals("hard")){
                            setTextFill(Color.valueOf("EC884B"));
                            setStyle("-fx-background-color: #EC884B");
                        }
                        else if (course.getDifficultLevel().equals("very hard")){
                            setTextFill(Color.valueOf("F33B35"));
                            setStyle("-fx-background-color: #F33B35");
                        }
                    }
                }
            };
        });
    }

    @FXML
    public void logoutBtnHandlerAction(ActionEvent actionEvent) {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_page.fxml"));
            stage.setTitle("Main Page");
            stage.setScene(new Scene(loader.load(), 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backPageBtnHandlerAction(ActionEvent actionEvent) {
        Stage stage = (Stage) backPageBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/show_allcourse.fxml"));
            stage.setTitle("ALL Course Information");
            stage.setScene(new Scene(loader.load(), 1050, 700));
            ShowAllCourseController showAllCourseController = (ShowAllCourseController) loader.getController();
            showAllCourseController.setUpStudent(student.getId(), student.getName());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
