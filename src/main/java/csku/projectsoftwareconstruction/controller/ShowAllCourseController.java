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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ShowAllCourseController {

    private Student student;
    private ArrayList<Course> allCourse;
    private ObservableList<Course> displayAllCourse;
    private static final int FIRST_YEAR = 1;
    private static final int SECOND_YEAR = 2;
    private static final int THIRD_YEAR = 3;
    private static final int FOURTH_YEAR = 4;

    @FXML
    protected Button logoutBtn;
    @FXML
    public Button firstYearBtn;
    @FXML
    public Button secondYearBtn;
    @FXML
    public Button thirdYearBtn;
    @FXML
    public Button fourthYearBtn;
    @FXML
    protected TextField studentID;
    @FXML
    protected TextField studentName;
    @FXML
    protected TableView<Course> allCourseTableView;
    @FXML
    protected TableColumn<Course, String> courseIdColumn;
    @FXML
    protected TableColumn<Course, String> titleColumn;
    @FXML
    protected TableColumn<Course, Integer> creditColumn;
    @FXML
    protected TableColumn<Course, Integer> yearColumn;
    @FXML
    protected TableColumn<Course, String> difficultColumn;
    @FXML
    protected TableColumn<Course, String> passColumn;
    @FXML
    protected TableColumn<Course, String> prereqColumn;

    @FXML
    public void initialize(){
        allCourse = new ArrayList<>();
        displayAllCourse = FXCollections.observableArrayList();
        showAllCourseFromDBToDisplay();
        displayAllCourse.addAll(allCourse);
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        difficultColumn.setCellValueFactory(new PropertyValueFactory<>("difficultLevel"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("completeClass"));
        prereqColumn.setCellValueFactory(new PropertyValueFactory<>("prereqId"));
        setColorDifficultLevel();
        allCourseTableView.setItems(displayAllCourse);
    }

    @FXML
    public void goToAllCourseEachCollegeYear(ActionEvent actionEvent){
        if (actionEvent.getSource().equals(firstYearBtn)){
            Stage stage = (Stage) firstYearBtn.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/course_college_year.fxml"));
                stage.setTitle("1st Year Course Information");
                stage.setScene(new Scene(loader.load(), 1050, 700));
                CourseCollegeYearController firstYear = (CourseCollegeYearController) loader.getController();
                firstYear.setUp(student.getId(), student.getName(), FIRST_YEAR);
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        if (actionEvent.getSource().equals(secondYearBtn)){
            Stage stage = (Stage) secondYearBtn.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/course_college_year.fxml"));
                stage.setTitle("2nd Year Course Information");
                stage.setScene(new Scene(loader.load(), 1050, 700));
                CourseCollegeYearController secondYear = (CourseCollegeYearController) loader.getController();
                secondYear.setUp(student.getId(), student.getName(), SECOND_YEAR);
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        if (actionEvent.getSource().equals(thirdYearBtn)){
            Stage stage = (Stage) thirdYearBtn.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/course_college_year.fxml"));
                stage.setTitle("3th Year Course Information");
                stage.setScene(new Scene(loader.load(), 1050, 700));
                CourseCollegeYearController thirdYear = (CourseCollegeYearController) loader.getController();
                thirdYear.setUp(student.getId(), student.getName(), THIRD_YEAR);
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        if (actionEvent.getSource().equals(fourthYearBtn)){
            Stage stage = (Stage) firstYearBtn.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/course_college_year.fxml"));
                stage.setTitle("4th Year Course Information");
                stage.setScene(new Scene(loader.load(), 1050, 700));
                CourseCollegeYearController fourthYear = (CourseCollegeYearController) loader.getController();
                fourthYear.setUp(student.getId(), student.getName(), FOURTH_YEAR);
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void setUpStudent(String id, String name){
        student = new Student(id, name);
        studentID.setText(student.getId());
        studentName.setText(student.getName());
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

    private void showAllCourseFromDBToDisplay(){
        DBConnection dbConnection = new DBConnection();
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM Course";
        try {
            connect = dbConnection.getConnectionDB();
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String courseId = resultSet.getString(1);
                String title = resultSet.getString(2);
                int credit = resultSet.getInt(3);
                int year = resultSet.getInt(4);
                String difficultLevel = resultSet.getString(5);
                String pass = resultSet.getString(6);
                String prereq = resultSet.getString(7);
                allCourse.add(new Course(courseId, title, credit, year, difficultLevel, pass, prereq));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.closeConnectionDB(connect, statement, resultSet);
        }
    }

    private void setColorDifficultLevel(){
        difficultColumn.setCellFactory(column->{
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
}
