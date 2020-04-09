package br.com.ithappens.ithappensbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionMessage {

    private String userMessage;
    private String developerMessage;
}
