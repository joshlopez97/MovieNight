package com.mn.service.idm.validation;

import java.io.IOException;

public class ValidationException extends IOException
{
    private int caseNumber;
    public ValidationException(String message, int caseNumber)
    {
        super(message);
        this.caseNumber = caseNumber;
    }

    public int getCaseNumber()
    {
        return caseNumber;
    }
}
