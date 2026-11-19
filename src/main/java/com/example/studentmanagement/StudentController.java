package com.example.studentmanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // ---------------- Q3: the 6 required APIs ----------------

    // 1. Add a student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStudent(student));
    }

    // 2. Retrieve all students
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // 3. Search a student by ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable("id") int id) {
        return service.getStudentById(id);
    }

    // 4. Update student scores and skills
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable("id") int id, @RequestBody Student student) {
        return service.updateScoresAndSkills(id, student);
    }

    // 5. Delete a student record
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable("id") int id) {
        service.deleteStudent(id);
        return "Student deleted with ID " + id;
    }

    // 6. Display the merit list
    @GetMapping("/merit-list")
    public List<Student> getMeritList() {
        return service.getMeritList();
    }

    // ---------------- Extra APIs to show Q2 logic in viva ----------------

    // average + category + stronger subject for every student
    @GetMapping("/reports")
    public List<Map<String, Object>> getAllReports() {
        return service.getAllReports();
    }

    @GetMapping("/search/email")
    public ResponseEntity<Student> searchByEmail(@RequestParam("email") String email) {
        return service.searchByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/course")
    public List<Student> searchByCourse(@RequestParam("course") String course) {
        return service.searchByCourse(course);
    }

    @GetMapping("/same-skills")
    public Map<String, List<String>> getSameSkills() {
        return service.findStudentsWithSameSkills();
    }

    @GetMapping("/needs-improvement")
    public List<Student> getWeakProgrammingStrongMaths() {
        return service.getWeakProgrammingStrongMaths();
    }

    // ---------------- Error handling ----------------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleError(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}