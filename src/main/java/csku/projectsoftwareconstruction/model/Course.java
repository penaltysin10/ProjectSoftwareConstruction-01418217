package csku.projectsoftwareconstruction.model;

public class Course {
    private String courseId;
    private String title;
    private int credit;
    private int year;
    private String difficultLevel;
    private String prereqId;
    private String completeClass;

    public Course(String courseId, String title, int credit, int year,
                  String difficultLevel, String completeClass, String prereqId){
        this.courseId = courseId;
        this.title = title;
        this.credit = credit;
        this.year = year;
        this.difficultLevel = difficultLevel;
        this.prereqId = prereqId;
        this.completeClass = completeClass;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public int getCredit() {
        return credit;
    }

    public int getYear() {
        return year;
    }

    public String getDifficultLevel() {
        return difficultLevel;
    }

    public String getPrereqId() {
        return prereqId;
    }

    public String getCompleteClass() {
        return completeClass;
    }

    public void setCompleteClass(String completeClass) {
        this.completeClass = completeClass;
    }
}
