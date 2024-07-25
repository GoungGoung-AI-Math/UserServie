package User.Math.AI.domain.userProfile.entity;

import User.Math.AI.domain.like.entity.Likes;
import User.Math.AI.domain.school.entity.School;
import User.Math.AI.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nickName;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private Long solvedCount;

    @ElementCollection
    private List<Long> solvedProblems;

    @OneToMany(mappedBy = "userProfile")
    private List<Likes> likes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public static UserProfile createUserProfile(Users user, School school, String nickName) {
        return UserProfile.builder()
                .nickName(nickName)
                .school(school)
                .solvedCount(0L)
                .users(user)
                .solvedProblems(new ArrayList<>())
                .build();
    }

    public void addSolvedProblem(Long problemId) {
        this.solvedProblems.add(problemId);
        this.solvedCount = (long) this.solvedProblems.size();
    }
}
