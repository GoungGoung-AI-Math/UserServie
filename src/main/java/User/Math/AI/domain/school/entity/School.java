package User.Math.AI.domain.school.entity;

import User.Math.AI.domain.userProfile.entity.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    public void addTotalSolved() {
        this.totalSolved += 1;
    }
}
