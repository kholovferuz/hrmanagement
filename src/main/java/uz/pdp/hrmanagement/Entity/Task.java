package uz.pdp.hrmanagement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.hrmanagement.Entity.enums.TaskStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Timestamp deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    // topshiriq bergan user
    @ManyToOne
    private User fromUser;

    // topshiriq olgan user
    @ManyToOne
    private User toUser;
}
