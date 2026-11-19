package com.example.studentmanagement;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Student {

    @Id
    private int studentId;
    private String studentName;
    private String email;
    private String courseName;
    private double programmingScore;
    private double mathematicsScore;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> technicalSkills = new ArrayList<>();

    public Student() {
    }

    public Student(int studentId, String studentName, String email, String courseName,
                   double programmingScore, double mathematicsScore,
                   List<String> technicalSkills) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courseName = courseName;
        setProgrammingScore(programmingScore);
        setMathematicsScore(mathematicsScore);
        setTechnicalSkills(technicalSkills);
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(double programmingScore) {
        if (programmingScore < 0 || programmingScore > 100) {
            throw new IllegalArgumentException("Programming score must be between 0 and 100");
        }
        this.programmingScore = programmingScore;
    }

    public double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(double mathematicsScore) {
        if (mathematicsScore < 0 || mathematicsScore > 100) {
            throw new IllegalArgumentException("Mathematics score must be between 0 and 100");
        }
        this.mathematicsScore = mathematicsScore;
    }

    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> technicalSkills) {
        this.technicalSkills = (technicalSkills != null)
                ? new ArrayList<>(technicalSkills)
                : new ArrayList<>();
    }

    public void addSkill(String skill) {
        if (skill != null && !technicalSkills.contains(skill)) {
            technicalSkills.add(skill);
        }
    }

    public double getAverageScore() {
        return (programmingScore + mathematicsScore) / 2.0;
    }

    @Override
    public String toString() {
        return "Student{id=" + studentId + ", name='" + studentName + "', email='" + email
                + "', course='" + courseName + "', programming=" + programmingScore
                + ", maths=" + mathematicsScore + ", skills=" + technicalSkills + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return studentId == ((Student) o).studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}