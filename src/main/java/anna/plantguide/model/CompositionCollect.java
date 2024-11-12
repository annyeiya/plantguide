package anna.plantguide.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Table(name = "composition_collect")
@Data
public class CompositionCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_plant", nullable = false)
    private MedicialPlant plant;

    @ManyToOne
    @JoinColumn(name = "id_collection", nullable = false)
    private MedicalCollection collection;

    @Column(name = "time_gatherng")
    private LocalTime timeGatherng;

    @Column(name = "part_plant")
    private String partPlant;
}

