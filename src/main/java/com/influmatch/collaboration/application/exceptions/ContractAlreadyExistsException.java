package com.influmatch.collaboration.application.exceptions;

public class ContractAlreadyExistsException extends RuntimeException {
    public ContractAlreadyExistsException() {
        super("contract_already_exists");
    }
} 