package math.ai.my.kafka.infra.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnalysisType {
    ATTEMPT("attempt"),
    QUESTION("question");
    private final String type;
    AnalysisType(String type){
        this.type = type;
    }

    @JsonValue
    public String getType(){
        return type;
    }
}
