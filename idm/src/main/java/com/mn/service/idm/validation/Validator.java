package com.mn.service.idm.validation;

import com.mn.service.idm.models.*;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Validator
{
    public static void validateFields(CreateUserRequest createUserRequest)
            throws ValidationException
    {
        validateEmail(createUserRequest.getEmail());
        validatePassword(createUserRequest.getPassword());
        validatePlevel(createUserRequest.getPlevel());
    }

    public static void validateUserQueryParams(Integer id, String email, String plevel)
            throws ValidationException
    {
        if (id != null)
            validateUserID(id);
        if (email != null)
            validateEmail(email);
        if (plevel != null)
            validatePlevel(plevel);
    }

    public static void validateFields(UpdatePasswordRequest updatePasswordRequest)
            throws ValidationException
    {
        validateEmail(updatePasswordRequest.getEmail());
        validatePassword(updatePasswordRequest.getOldPword());
        validatePassword(updatePasswordRequest.getNewPword());
    }

    public static void validateFields(LoginRequest loginRequest)
            throws ValidationException
    {
        validateEmail(loginRequest.getEmail());
        validatePassword(loginRequest.getPassword());
    }

    public static void validateFields(SessionStatusRequest sessionStatusRequest)
            throws ValidationException
    {
        validateEmail(sessionStatusRequest.getEmail());
        validateSessionID(sessionStatusRequest.getSessionID());
    }

    public static void validateFields(UserPrivilegeRequest userPrivilegeRequest)
            throws ValidationException
    {
        validateEmail(userPrivilegeRequest.getEmail());
        validatePlevel(userPrivilegeRequest.getPlevel());
    }

    public static void validatePlevel(int plevel)
            throws ValidationException
    {
        if (plevel < 1 || plevel > 5)
            throw new ValidationException("Privilege level out of valid range.", -14);
    }

    public static void validatePlevel(String plevel)
            throws ValidationException
    {
        String[] valid_plevels = {"user", "service", "employee", "admin", "root"};
        if (!Arrays.asList(valid_plevels).contains(plevel.toLowerCase()))
            throw new ValidationException("Privilege level out of valid range.", -14);
    }

    public static void validateUserID(int id)
            throws ValidationException
    {
        if (id < 1)
            throw new ValidationException("User ID number is out of valid range.", -15);
    }

    public static void validateSessionID(String sessionID)
            throws ValidationException
    {
        if (sessionID.length() != 128)
            throw new ValidationException("Token has invalid length.", -13);
    }

    public static void validateEmail(String email)
            throws ValidationException
    {
        // Validate email length
        if (email.length() == 0 || email.length() > 50)
            throw new ValidationException("Email address has invalid length.", -10);

        // Validate email format
        Pattern emailRegex = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        if (!emailRegex.matcher(email).matches())
            throw new ValidationException("Email address has invalid format.", -11);
    }

    public static void validatePassword(char[] pw)
            throws ValidationException
    {
        // Validate password length
        if (pw == null || pw.length == 0)
            throw new ValidationException("Password has invalid length (cannot be empty/null).", -12);
        if (pw.length < 7 || pw.length > 16)
            throw new ValidationException("Password does not meet length requirements.", 12);

        // Validate password char requirements
        boolean containsSpecial = false,
                containsUpper = false,
                containsLower = false,
                containsNum = false;
        Pattern special = Pattern.compile("[ !\"#$%&'()*+,./:;<=>?@\\[\\]^_`{|}~\\\\-]");
        Pattern upper = Pattern.compile("[A-Z]");
        Pattern lower = Pattern.compile("[a-z]");
        Pattern num = Pattern.compile("[0-9]");
        for (char c : pw)
        {
            if (special.matcher(String.valueOf(c)).matches())
                containsSpecial = true;
            else if (upper.matcher(String.valueOf(c)).matches())
                containsUpper = true;
            else if (lower.matcher(String.valueOf(c)).matches())
                    containsLower = true;
                else if (num.matcher(String.valueOf(c)).matches())
                        containsNum = true;
        }
        if (!containsSpecial || !containsUpper || !containsLower || !containsNum)
        {
            throw new ValidationException("Password does not meet character requirements.", 13);
        }
    }
}
