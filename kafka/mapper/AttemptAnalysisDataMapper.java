package com.example.demo.my.kafka.infra.kafka.mapper;


import com.example.demo.my.kafka.infra.avrobuild.AnalysisType;
import com.example.demo.my.kafka.infra.avrobuild.AttemptAnalysisRequestAvroModel;
import com.example.demo.my.kafka.infra.avrobuild.AttemptAnalysisResponseAvroModel;
import com.example.demo.my.kafka.infra.avrobuild.MessageType;
import com.example.demo.my.kafka.infra.kafka.dtos.attempt.analysis.AttemptAnalysisRequestDto;
import com.example.demo.my.kafka.infra.kafka.dtos.attempt.analysis.AttemptAnalysisResponseDto;
import org.springframework.stereotype.Component;


@Component
public class AttemptAnalysisDataMapper {
    public AttemptAnalysisRequestAvroModel attemptAnalysisRequestToAvroModel(AttemptAnalysisRequestDto attemptAnalysisRequestDto){
        return AttemptAnalysisRequestAvroModel.newBuilder()
                .setAttemptId(attemptAnalysisRequestDto.getAttemptId())
                .setAnalysisType(AnalysisType.valueOf(
                        attemptAnalysisRequestDto.getAnalysisType().name()
                ))
                .setMessageType(MessageType.valueOf(
                        attemptAnalysisRequestDto.getMessageType().name()
                ))
                .setContent(attemptAnalysisRequestDto.getContent())
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
                .analysisType(com.example.demo.my.kafka.infra.kafka.dtos.AnalysisType.valueOf(avroModel.getAnalysisType().name()))
                .messageType(
                        com.example.demo.my.kafka.infra.kafka.dtos.MessageType.valueOf(avroModel.getMessageType().name()))
                .content(avroModel.getContent())
                .build();
    }

    public AttemptAnalysisResponseDto avroModelToAttemptAnalysisResponseDto(AttemptAnalysisResponseAvroModel avroModel){
        return AttemptAnalysisResponseDto.builder()
                .attemptId(avroModel.getAttemptId())
                .analysisType(com.example.demo.my.kafka.infra.kafka.dtos.AnalysisType.valueOf(avroModel.getAnalysisType().name()))
                .messageType(
                        com.example.demo.my.kafka.infra.kafka.dtos.MessageType.valueOf(avroModel.getMessageType().name()))
                .content(avroModel.getContent())
                .build();
    }
}
