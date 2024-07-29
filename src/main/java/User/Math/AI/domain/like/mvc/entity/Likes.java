package User.Math.AI.domain.like.mvc.entity;

import User.Math.AI.domain.like.mvc.dto.LikeAddRequest;
import User.Math.AI.domain.userProfile.entity.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public static Likes toEntity(UserProfile userProfile, LikeAddRequest request){
        return Likes.builder()
                .userProfile(userProfile)
                .category(request.getType().getType())
                .relationId(request.getRelationId())
                .build();
    }
}
