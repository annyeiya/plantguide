package anna.plantguide.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "medicial_collection")
@Data
public class MedicalCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "count_plant")
    private Integer countPlant;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "collection")
    private List<CompositionCollect> compositionCollects;

    @OneToMany(mappedBy = "collection")
    private List<Method> methods;
}

