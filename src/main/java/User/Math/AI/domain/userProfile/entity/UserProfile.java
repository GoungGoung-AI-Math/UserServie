package User.Math.AI.domain.userProfile.entity;

import User.Math.AI.domain.like.mvc.entity.Likes;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    private String profileImageUrl;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    private Long solvedCount;

    @ElementCollection
    private List<Long> solvedProblems;

    @ElementCollection
    private List<Long> questions;

    @ElementCollection
    private List<Long> answers;

    @OneToMany(mappedBy = "userProfile")
    private List<Likes> likes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    private Long rating;

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

    public void addQuestion(Long questionId) {
        this.questions.add(questionId);
    }

    public void addActionScore(UserActionScore actionScore){
        rating += actionScore.getPoints();
    }
}
