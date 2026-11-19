package com.example.studentmanagement;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    // ---------------- CRUD (used by Q3 APIs) ----------------

    // Q2.8: duplicate ID prevent + score validation
    public Student addStudent(Student s) {
        if (repo.existsById(s.getStudentId())) {
            throw new IllegalArgumentException("Student ID " + s.getStudentId() + " already exists");
        }
        validateScores(s.getProgrammingScore(), s.getMathematicsScore());
        return repo.save(s);
    }

    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    public Student getStudentById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID " + id));
    }

    public Student updateScoresAndSkills(int id, Student updated) {
        Student existing = getStudentById(id);
        validateScores(updated.getProgrammingScore(), updated.getMathematicsScore());
        existing.setProgrammingScore(updated.getProgrammingScore());
        existing.setMathematicsScore(updated.getMathematicsScore());
        existing.setTechnicalSkills(updated.getTechnicalSkills());
        return repo.save(existing);
    }

    public void deleteStudent(int id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Student not found with ID " + id);
        }
        repo.deleteById(id);
    }

    // ---------------- Q2.4 Search ----------------

    public Optional<Student> searchByEmail(String email) {
        return repo.findByEmail(email);
    }

    public List<Student> searchByCourse(String courseName) {
        return repo.findByCourseName(courseName);
    }

    // ---------------- Q2.1, 2.2, 2.3 ----------------

    public double calculateAverage(Student s) {
        return s.getAverageScore();
    }

    public String getCategory(double average) {
        if (average >= 85) {
            return "Excellent";
        } else if (average >= 70) {
            return "Good";
        } else if (average >= 50) {
            return "Satisfactory";
        } else {
            return "Needs Improvement";
        }
    }

    public String getStrongerSubject(Student s) {
        if (s.getProgrammingScore() > s.getMathematicsScore()) {
            return "Programming";
        } else if (s.getMathematicsScore() > s.getProgrammingScore()) {
            return "Mathematics";
        } else {
            return "Both are equal";
        }
    }

    public Map<String, Object> getReport(Student s) {
        double avg = calculateAverage(s);
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("studentId", s.getStudentId());
        report.put("studentName", s.getStudentName());
        report.put("average", avg);
        report.put("category", getCategory(avg));
        report.put("strongerSubject", getStrongerSubject(s));
        return report;
    }

    public List<Map<String, Object>> getAllReports() {
        return repo.findAll().stream()
                .map(this::getReport)
                .collect(Collectors.toList());
    }

    // ---------------- Q2.5 Merit list ----------------

    public List<Student> getMeritList() {
        List<Student> list = new ArrayList<>(repo.findAll());
        list.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        return list;
    }

    // ---------------- Q2.6 Same technical skills ----------------

    public Map<String, List<String>> findStudentsWithSameSkills() {
        Map<String, List<String>> skillMap = new TreeMap<>();
        for (Student s : repo.findAll()) {
            for (String skill : s.getTechnicalSkills()) {
                skillMap.computeIfAbsent(skill.trim().toLowerCase(), k -> new ArrayList<>())
                        .add(s.getStudentName());
            }
        }
        skillMap.entrySet().removeIf(entry -> entry.getValue().size() < 2);
        return skillMap;
    }

    // ---------------- Q2.7 Weak programming but strong maths ----------------

    public List<Student> getWeakProgrammingStrongMaths() {
        return repo.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50 && s.getMathematicsScore() >= 75)
                .collect(Collectors.toList());
    }

    // ---------------- Validation ----------------

    private void validateScores(double programming, double maths) {
        if (programming < 0 || programming > 100 || maths < 0 || maths > 100) {
            throw new IllegalArgumentException("Scores must be between 0 and 100");
        }
    }
}