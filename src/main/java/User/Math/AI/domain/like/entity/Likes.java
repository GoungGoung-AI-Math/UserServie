package User.Math.AI.domain.like.entity;

import User.Math.AI.domain.userProfile.entity.UserProfile;
import jakarta.persistence.*;

@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;

    // ex 질문 아이디, 답변 아이디, 풀이 아이디 ...
    private long relationId;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
