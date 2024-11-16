package anna.plantguide.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "fio")
    private String fio;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany
    private List<MedicialPlant> medicalPlants;

    @OneToMany
    private List<MedicalCollection> medicalCollections;

    @OneToMany
    private List<Review> reviews;
}
