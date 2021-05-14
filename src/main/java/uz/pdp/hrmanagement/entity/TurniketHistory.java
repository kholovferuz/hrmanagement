package uz.pdp.hrmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TurniketHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean enterOrExit;

    private LocalDate date;

    private LocalTime time;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Turniket turniket;
}
