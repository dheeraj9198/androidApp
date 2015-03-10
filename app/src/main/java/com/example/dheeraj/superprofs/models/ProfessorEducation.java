package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 19/2/15.
 */
public final class ProfessorEducation {

    private int id;
    private int professorId;
    private String college;
    private String graduationYear;
    private String degree;
    private String field;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "ProfessorEducation{" +
                "id=" + id +
                ", professorId=" + professorId +
                ", college='" + college + '\'' +
                ", graduationYear='" + graduationYear + '\'' +
                ", degree='" + degree + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
