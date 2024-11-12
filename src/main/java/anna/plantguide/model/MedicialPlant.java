package anna.plantguide.model;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "medicial_plant")
@Data
public class MedicialPlant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "descript")
    private String descript;

    @Column(name = "gatherng_place")
    private String gatherngPlace;

    @Column(name = "contrand", nullable = false)
    private String contrand;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany
    private List<CompositionCollect> compositionCollects;

    @Transient
    private String userLogin;
}
