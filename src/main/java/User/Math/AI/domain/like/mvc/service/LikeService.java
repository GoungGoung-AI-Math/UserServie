package User.Math.AI.domain.like.mvc.service;

import User.Math.AI.domain.like.mvc.dto.LikeAddRequest;
import User.Math.AI.domain.like.mvc.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeRepository likeRepository;

    public void likeAddForUser(LikeAddRequest request){

    }
}
