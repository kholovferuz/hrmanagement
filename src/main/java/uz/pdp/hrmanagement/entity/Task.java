package uz.pdp.hrmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.hrmanagement.entity.enums.TaskStatus;

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

    // manager or director
    @ManyToOne
    private User fromUser;


    @ManyToOne
    private User toUser;
}
