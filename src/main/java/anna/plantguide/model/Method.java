package anna.plantguide.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "method")
@Data
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_desease", nullable = false)
    private Disease desease;

    @ManyToOne
    @JoinColumn(name = "id_collection", nullable = false)
    private MedicalCollection collection;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "metod_applic")
    private String metodApplic;

    @Column(name = "realise_form")
    private String realiseForm;
}
