package com.mn.service.movies.validation;

import java.io.IOException;

public class ValidationException extends IOException
{
    private int caseNumber;
    public ValidationException(int caseNumber, String message)
    {
        super(message);
        this.caseNumber = caseNumber;
    }

    public int getCaseNumber()
    {
        return caseNumber;
    }
}
