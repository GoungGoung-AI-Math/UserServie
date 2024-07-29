package User.Math.AI.domain.userProfile.entity;

import lombok.Getter;

@Getter
public enum UserActionScore {
    ANSWER(50),
    LIKE(20),
    PEER_REVIEW(50),
    TEACHER_REVIEW(200);

    private final int points;

    UserActionScore(int points) {
        this.points = points;
    }

}