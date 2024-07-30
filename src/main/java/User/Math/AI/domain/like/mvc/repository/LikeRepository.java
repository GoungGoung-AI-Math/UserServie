package User.Math.AI.domain.like.mvc.repository;

import User.Math.AI.domain.like.mvc.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {

}
