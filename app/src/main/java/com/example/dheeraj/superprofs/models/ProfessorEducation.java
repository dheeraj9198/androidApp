package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 19/2/15.
 */
public final class ProfessorEducation {

    private int id;
    private int professor_id;
    private String college;
    private String graduation_year;
    private String degree;
    private String field;

    @Override
    public String toString() {
        return "ProfessorEducation{" +
                "id=" + id +
                ", professor_id=" + professor_id +
                ", college='" + college + '\'' +
                ", graduation_year='" + graduation_year + '\'' +
                ", degree='" + degree + '\'' +
                ", field='" + field + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfessor_id() {
        return professor_id;
    }

    public void setProfessor_id(int professor_id) {
        this.professor_id = professor_id;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGraduation_year() {
        return graduation_year;
    }

    public void setGraduation_year(String graduation_year) {
        this.graduation_year = graduation_year;
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
}
