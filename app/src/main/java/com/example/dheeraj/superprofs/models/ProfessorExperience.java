package com.example.dheeraj.superprofs.models;

import org.boon.Str;

/**
 * Created by dheeraj on 20/2/15.
 */
public final class ProfessorExperience {
    private int id;
    private int professor_id;
    private int type;
    private String company_name;
    private String position;
    private String start_year;
    private String end_year;
    private int is_current_position;

    public String getDetail(){
        return position+ " ("+start_year+" to "+end_year+") ";
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStart_year() {
        return start_year;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public String getEnd_year() {
        return end_year;
    }

    public void setEnd_year(String end_year) {
        this.end_year = end_year;
    }

    public int getIs_current_position() {
        return is_current_position;
    }

    public void setIs_current_position(int is_current_position) {
        this.is_current_position = is_current_position;
    }

    @Override
    public String toString() {
        return "ProfessorExperience{" +
                "id=" + id +
                ", professor_id=" + professor_id +
                ", type=" + type +
                ", company_name='" + company_name + '\'' +
                ", position='" + position + '\'' +
                ", start_year='" + start_year + '\'' +
                ", end_year='" + end_year + '\'' +
                ", is_current_position=" + is_current_position +
                '}';
    }
}
