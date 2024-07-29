package math.ai.my.kafka.infra.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    TEXT("text"),
    IMAGE_URL("image_url");
    private String type;
    MessageType(String type){
        this.type = type;
    }

    @JsonValue // for 직렬화
    public String getType() {
        return type;
    }

    @JsonCreator // for 역직렬화
    public static MessageType from(String val){
        for(MessageType messageType : MessageType.values()){
            if(messageType.getType().equals(val)){
                return messageType;
            }
        }
        throw new RuntimeException("부정확한 메세지 타입입니다.");
    }

    @Override
    public String toString(){
        return type;
    }
}