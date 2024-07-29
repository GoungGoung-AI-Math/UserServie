package Math.AI.my.kafka.infra.kafka.mapper;


import Math.AI.my.kafka.infra.avrobuild.*;
import Math.AI.my.kafka.infra.avrobuild.AnalysisType;
import Math.AI.my.kafka.infra.avrobuild.AttemptAnalysisRequestAvroModel;
import Math.AI.my.kafka.infra.avrobuild.AttemptAnalysisResponseAvroModel;
import Math.AI.my.kafka.infra.avrobuild.MessageType;
import Math.AI.my.kafka.infra.kafka.dtos.attempt.analysis.AttemptAnalysisRequestDto;
import Math.AI.my.kafka.infra.kafka.dtos.attempt.analysis.AttemptAnalysisResponseDto;
import Math.AI.my.kafka.infra.kafka.dtos.attempt.analysis.ContentDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class AttemptAnalysisDataMapper {
    public AttemptAnalysisRequestAvroModel attemptAnalysisRequestToAvroModel(AttemptAnalysisRequestDto attemptAnalysisRequestDto){
        return AttemptAnalysisRequestAvroModel.newBuilder()
                .setAttemptId(attemptAnalysisRequestDto.getAttemptId())
                .setAnalysisType(AnalysisType.valueOf(
                        attemptAnalysisRequestDto.getAnalysisType().name()
                ))
                .setContents(attemptAnalysisRequestDto.getContents().stream().map(c-> User.Math.AI.my.kafka.infra.avrobuild.Content.newBuilder()
                        .setMessageType(MessageType.valueOf(c.getMessageType().name()))
                        .setContent(c.getContent())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public AttemptAnalysisResponseAvroModel attemptAnalysisResponseToAvroModel(AttemptAnalysisResponseDto attemptAnalysisResponseDto){
        return AttemptAnalysisResponseAvroModel.newBuilder()
                .setAttemptId(attemptAnalysisResponseDto.getAttemptId())
                .setAnalysisType(AnalysisType.valueOf(
                        attemptAnalysisResponseDto.getAnalysisType().name()
                ))
                .setMessageType(MessageType.valueOf(
                        attemptAnalysisResponseDto.getMessageType().name()
                ))
                .setContent(attemptAnalysisResponseDto.getContent())
                .build();
    }

    public AttemptAnalysisRequestDto avroModelToAttemptAnalysisRequestDto(AttemptAnalysisRequestAvroModel avroModel){
        return AttemptAnalysisRequestDto.builder()
                .attemptId(avroModel.getAttemptId())
                .analysisType(User.Math.AI.my.kafka.infra.kafka.dtos.AnalysisType.valueOf(avroModel.getAnalysisType().name()))
                .contents(avroModel.getContents().stream().map(c-> ContentDto.builder()
                        .messageType(User.Math.AI.my.kafka.infra.kafka.dtos.MessageType.valueOf(c.getMessageType().name()))
                        .content(c.getContent())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public AttemptAnalysisResponseDto avroModelToAttemptAnalysisResponseDto(AttemptAnalysisResponseAvroModel avroModel){
        return AttemptAnalysisResponseDto.builder()
                .attemptId(avroModel.getAttemptId())
                .analysisType(User.Math.AI.my.kafka.infra.kafka.dtos.AnalysisType.valueOf(avroModel.getAnalysisType().name()))
                .messageType(
                        User.Math.AI.my.kafka.infra.kafka.dtos.MessageType.valueOf(avroModel.getMessageType().name()))
                .content(avroModel.getContent())
                .build();
    }
}
