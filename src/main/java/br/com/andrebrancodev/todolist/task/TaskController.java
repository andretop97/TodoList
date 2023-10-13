package br.com.andrebrancodev.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.andrebrancodev.todolist.user.UserModel;
import br.com.andrebrancodev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel task, HttpServletRequest request) {
        UserModel user = (UserModel) request.getAttribute("user");
        task.setUserUuid(user.getUuid());

        LocalDateTime now = LocalDateTime.now();

        if(task.getStartDate() != null && now.isAfter(task.getStartDate())) {
            return ResponseEntity.status(400).body("Start date must be after now");
        }

        if(task.getStartDate() != null && task.getStartDate().isAfter(task.getEndDate())) {
            return ResponseEntity.status(400).body("Start date must be before EndDate date");
        }

        TaskModel savedTask = taskRepository.save(task);
        return ResponseEntity.status(201).body(savedTask);
    }

    @GetMapping
    public ResponseEntity list(HttpServletRequest request) {
        UserModel user = (UserModel) request.getAttribute("user");
        return ResponseEntity.ok(taskRepository.findByUserUuid(user.getUuid()));
    }
    

    @PutMapping("/{uuid}")
    public ResponseEntity update(@RequestBody TaskModel task, HttpServletRequest request, @PathVariable("uuid") UUID uuid) {
        UserModel user = (UserModel) request.getAttribute("user");
        TaskModel taskFound = taskRepository.findByUuidAndUserUuid(uuid, user.getUuid());

        if (taskFound == null) {
            return ResponseEntity.status(404).build();
        }

        Utils.updateObject(task, taskFound);

        TaskModel savedTask = taskRepository.save(taskFound);

        return ResponseEntity.status(200).body(savedTask);
    }
}
