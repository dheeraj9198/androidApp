package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 20/2/15.
 */
public final class ProfessorExperience {
    private int id;
    private int professorId;
    private int type;
    private String companyName;
    private String position;
    private String startYear;
    private String endYear;
    private int isCurrentPosition;

    public String getDetail(){
        return position+ " ("+startYear+" to "+endYear+") ";
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public int getIsCurrentPosition() {
        return isCurrentPosition;
    }

    public void setIsCurrentPosition(int isCurrentPosition) {
        this.isCurrentPosition = isCurrentPosition;
    }

    @Override
    public String toString() {
        return "ProfessorExperience{" +
                "id=" + id +
                ", professorId=" + professorId +
                ", type=" + type +
                ", companyName='" + companyName + '\'' +
                ", position='" + position + '\'' +
                ", startYear='" + startYear + '\'' +
                ", endYear='" + endYear + '\'' +
                ", isCurrentPosition=" + isCurrentPosition +
                '}';
    }
}
