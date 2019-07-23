package com.mn.service.movies.validation;

import com.mn.service.movies.models.headers.RequestHeader;

import java.util.regex.Pattern;

public class Validator
{
    public static void validateRequestHeader(RequestHeader requestHeader)
            throws ValidationException
    {
//        validateEmail(requestHeader.getEmail());
//        validateSessionID(requestHeader.getSessionID());
//        validateTransactionID(requestHeader.getTransactionID());
    }

    public static void validateTransactionID(String transactionID)
            throws ValidationException
    {
        if (transactionID == null)
            throw new ValidationException(-17, "TransactionID not provided in request header.");
    }

    public static void validateSessionID(String sessionID)
            throws ValidationException
    {
        if (sessionID == null)
            throw new ValidationException(-17, "SessionID not provided in request header.");
//        if (sessionID.length() != 128)
//            throw new ValidationException("Token has invalid length.", -13);
    }

    public static void validateEmail(String email)
            throws ValidationException
    {
        // Validate email is provided
        if (email == null)
            throw new ValidationException(-16, "Email not provided in request header.");

        // Validate email length
        if (email.length() == 0 || email.length() > 50)
            throw new ValidationException(-10, "Email address has invalid length.");

        // Validate email format
        Pattern emailRegex = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        if (!emailRegex.matcher(email).matches())
            throw new ValidationException(-11, "Email address has invalid format.");
    }
}
