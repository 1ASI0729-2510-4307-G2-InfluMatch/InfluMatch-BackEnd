package com.influmatch.collaboration.application.exceptions;

public class InvalidContractStateException extends RuntimeException {
    public InvalidContractStateException() {
        super("invalid_contract_state");
    }
} 