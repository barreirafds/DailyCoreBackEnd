package be.dailycorebackend.dal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    // Empty constructor required by JPA
    public Task() {
    }

    public Task(String title, boolean completed, Routine routine) {
        this.title = title;
        this.completed = completed;
        this.routine = routine;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }
}