package User.Math.AI.domain.like;

public class LikeException extends RuntimeException{
    public LikeException(String msg, Throwable cause){
        super(msg, cause);
    }
    public LikeException(String msg){
        super(msg);
    }
}
