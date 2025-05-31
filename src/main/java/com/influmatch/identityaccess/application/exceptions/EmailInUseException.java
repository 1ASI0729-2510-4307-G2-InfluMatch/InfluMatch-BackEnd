// src/main/java/com/influmatch/identityaccess/application/exceptions/EmailInUseException.java
package com.influmatch.identityaccess.application.exceptions;

public class EmailInUseException extends RuntimeException {
    public EmailInUseException() { super("email_in_use"); }
}
