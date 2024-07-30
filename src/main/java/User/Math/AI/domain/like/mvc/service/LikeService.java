package User.Math.AI.domain.like.mvc.service;

import User.Math.AI.domain.like.LikeException;
import User.Math.AI.domain.like.mvc.dto.LikeAddRequest;
import User.Math.AI.domain.like.mvc.entity.Likes;
import User.Math.AI.domain.like.mvc.repository.LikeRepository;
import User.Math.AI.domain.userProfile.entity.UserActionScore;
import User.Math.AI.domain.userProfile.entity.UserProfile;
import User.Math.AI.domain.userProfile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserProfileRepository userProfileRepository;
    public void likeAddForUser(LikeAddRequest request){
        UserProfile userProfile = userProfileRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new LikeException(request.getReceiverId()+"는 존재하지 않는 유저입니다."));

        Likes likes = Likes.toEntity(userProfile, request);
        likeRepository.save(likes);

        // userProfile 에 rating 더하는 로직 추가
        userProfile.addActionScore(UserActionScore.LIKE);
    }
}
