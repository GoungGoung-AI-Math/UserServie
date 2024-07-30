package math.ai.my.kafka.infra.kafka.mapper;



import math.ai.my.kafka.infra.avrobuild.AttemptAnalysisRequestAvroModel;
import math.ai.my.kafka.infra.avrobuild.AttemptAnalysisResponseAvroModel;
import math.ai.my.kafka.infra.avrobuild.Content;
import math.ai.my.kafka.infra.kafka.dtos.AnalysisType;
import math.ai.my.kafka.infra.kafka.dtos.MessageType;
import math.ai.my.kafka.infra.kafka.dtos.attempt.analysis.AttemptAnalysisRequestDto;
import math.ai.my.kafka.infra.kafka.dtos.attempt.analysis.AttemptAnalysisResponseDto;
import math.ai.my.kafka.infra.kafka.dtos.attempt.analysis.ContentDto;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class AttemptAnalysisDataMapper {
    public AttemptAnalysisRequestAvroModel attemptAnalysisRequestToAvroModel(AttemptAnalysisRequestDto attemptAnalysisRequestDto){
        return AttemptAnalysisRequestAvroModel.newBuilder()
                .setAttemptId(attemptAnalysisRequestDto.getAttemptId())
                .setAnalysisType(math.ai.my.kafka.infra.avrobuild.AnalysisType.valueOf(
                        attemptAnalysisRequestDto.getAnalysisType().name()
                ))
                .setContents(attemptAnalysisRequestDto.getContents().stream().map(c-> Content.newBuilder()
                        .setMessageType(math.ai.my.kafka.infra.avrobuild.MessageType.valueOf(c.getMessageType().name()))
                        .setContent(c.getContent())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public AttemptAnalysisResponseAvroModel attemptAnalysisResponseToAvroModel(AttemptAnalysisResponseDto attemptAnalysisResponseDto){
        return AttemptAnalysisResponseAvroModel.newBuilder()
                .setAttemptId(attemptAnalysisResponseDto.getAttemptId())
                .setAnalysisType(math.ai.my.kafka.infra.avrobuild.AnalysisType.valueOf(
                        attemptAnalysisResponseDto.getAnalysisType().name()
                ))
                .setMessageType(math.ai.my.kafka.infra.avrobuild.MessageType.valueOf(
                        attemptAnalysisResponseDto.getMessageType().name()
                ))
                .setContent(attemptAnalysisResponseDto.getContent())
                .build();
    }

    public AttemptAnalysisRequestDto avroModelToAttemptAnalysisRequestDto(AttemptAnalysisRequestAvroModel avroModel){
        return AttemptAnalysisRequestDto.builder()
                .attemptId(avroModel.getAttemptId())
                .analysisType(AnalysisType.valueOf(avroModel.getAnalysisType().name()))
                .contents(avroModel.getContents().stream().map(c-> ContentDto.builder()
                        .messageType(MessageType.valueOf(c.getMessageType().name()))
                        .content(c.getContent())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public AttemptAnalysisResponseDto avroModelToAttemptAnalysisResponseDto(AttemptAnalysisResponseAvroModel avroModel){
        return AttemptAnalysisResponseDto.builder()
                .attemptId(avroModel.getAttemptId())
                .analysisType(AnalysisType.valueOf(avroModel.getAnalysisType().name()))
                .messageType(
                        MessageType.valueOf(avroModel.getMessageType().name()))
                .content(avroModel.getContent())
                .build();
    }
}
