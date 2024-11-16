package anna.plantguide.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_collection", nullable = false)
    private MedicalCollection collection;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "text_review")
    private String textReview;

    @Min(0)
    @Max(10)
    @Column(name = "estimation", nullable = false)
    private Integer estimation;
}
