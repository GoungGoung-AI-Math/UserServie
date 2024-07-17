package User.Math.AI.domain.userProfile.entity;

import User.Math.AI.domain.like.entity.Like;
import User.Math.AI.domain.school.entity.School;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long personnel;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private Long solvedCount;

    @OneToMany(mappedBy = "userProfile")
    private List<Like> likes;


}
