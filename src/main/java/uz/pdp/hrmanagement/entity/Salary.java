package uz.pdp.hrmanagement.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double monthlyAmount;

    private Integer monthNumber;

    private boolean isPaid;

    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp paidTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;
}
