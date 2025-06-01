package com.influmatch.collaboration.application.exceptions;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException() {
        super("contract_not_found");
    }
} 