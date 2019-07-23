package com.mn.service.api_gateway.validation;

import javax.ws.rs.core.Response.Status;


public class HTTPStatusCodes {
    public static Status setHTTPStatus(int code) {
        switch (code) {
            case ResultCodes.JSON_MAPPING_EXCEPTION:
            case ResultCodes.JSON_PARSE_EXCEPTION:
            case ResultCodes.EMAIL_INVALID_LENGTH:
            case ResultCodes.EMAIL_INVALID_FORMAT:
            case ResultCodes.PASSWORD_INVALID_LENGTH:
            case ResultCodes.TOKEN_INVALID_LENGTH:
            case ResultCodes.PRIVILEGE_LEVEL_OUT_OF_RANGE:
            case ResultCodes.USER_ID_OUT_OF_RANGE:
                return Status.BAD_REQUEST;
            case ResultCodes.PASSWORD_MISMATCH:
            case ResultCodes.PASSWORD_INSUFFICIENT_LENGTH:
            case ResultCodes.PASSWORD_INSUFFICIENT_CHARS:
            case ResultCodes.USER_NOT_FOUND:
            case ResultCodes.SESSION_NOT_FOUND:
            case ResultCodes.EMAIL_ALREADY_IN_USE:
            case ResultCodes.REGISTRATION_SUCCSSFUL:
            case ResultCodes.LOGIN_SUCCESSFUL:
            case ResultCodes.SESSION_ACTIVE:
            case ResultCodes.SESSION_CLOSED:
            case ResultCodes.SESSION_EXPIRED:
            case ResultCodes.SESSION_REVOKED:
            case ResultCodes.USER_PRIVILEGE_GOOD:
            case ResultCodes.USER_PRIVILEGE_BAD:
            case ResultCodes.PASSWORD_UPDATED:
            case ResultCodes.USER_RETRIEVED:
            case ResultCodes.USER_CREATED:
            case ResultCodes.CANNOT_CREATE_ROOT_USER:
            case ResultCodes.USER_UPDATED:
            case ResultCodes.CANNOT_ELEVATE_USER_TO_ROOT:
                return Status.OK;
            case ResultCodes.INTERNAL_SERVER_ERROR:

            default:
                return Status.INTERNAL_SERVER_ERROR;
        }
    }
}
