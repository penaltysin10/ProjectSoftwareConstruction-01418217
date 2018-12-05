package csku.projectsoftwareconstruction.model;

import java.util.ArrayList;

public class Student {
    private String id;
    private String name;
    private ArrayList<Course> passCourse;

    public Student(String id, String name){
        this.id = id;
        this.name = name;
        passCourse = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addCompleteCourse(Course course){
        passCourse.add(course);
    }

    public ArrayList<Course> getPassCourse() {
        return passCourse;
    }
}
