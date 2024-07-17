package User.Math.AI.domain.school.entity;

import User.Math.AI.domain.userProfile.entity.UserProfile;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long personnel;

    private Long totalSolved;

    private Double AnswerRate;

    @OneToMany(mappedBy = "school")
    private List<UserProfile> userProfiles;
}
