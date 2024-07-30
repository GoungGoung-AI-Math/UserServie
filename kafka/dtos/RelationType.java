package math.ai.my.kafka.infra.kafka.dtos;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RelationType {
    PROBLEM("problem"),
    SOLUTION("solution"),
    ATTEMPT("attempt"),
    REVIEW("review"),
    QUESTION("question"),
    ANSWER("answer");

    private final String type;
    RelationType(String type){
        this.type = type;
    }

    @JsonValue
    public String getType(){
        return type;
    }

}
