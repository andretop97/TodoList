package br.com.andrebrancodev.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(length = 50, nullable = false)
    private String title;
    private String description;
    private String status;
    private String priority;
    private UUID userUuid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public void setTitle(String title)  throws IllegalArgumentException{
        if(title.length() > 50) {
            throw new IllegalArgumentException("Title must be less than 50 characters");
        }
        this.title = title;
    }

}
