package User.Math.AI.domain.userProfile.dto.response;

import User.Math.AI.domain.userProfile.entity.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
    // 학교이름
    // 닉네임
    // 프로필사진
    // 가입일
    // 티어
    // 레이팅
    // 제출 수
    // 푼 문제 수 (userProfile, solvedCount.count) (맞은 문제)
    // 질문 개수   questions.count
    // 답변 개수   answers.count
    // 받은 좋아요 수 likes.count
    private String schoolName;
    private String nickName;
    private String profileImageUrl;
    private LocalDateTime createAt;
    private Tier tier;
    private Long rating;
    private Long solvedCount;
    private Long questionCount;
    private Long answerCount;
    private Long likeCount;
}
