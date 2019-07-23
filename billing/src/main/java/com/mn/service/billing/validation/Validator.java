package com.mn.service.billing.validation;

import com.mn.service.billing.models.requests.cart.CartRequest;
import com.mn.service.billing.models.requests.creditcard.CreditCardRequest;
import com.mn.service.billing.models.requests.creditcard.UpdateCreditCardRequest;
import com.mn.service.billing.models.requests.cart.UpdateCartRequest;
import com.mn.service.billing.models.requests.customer.CustomerRequest;
import com.mn.service.billing.models.requests.customer.UpdateCustomerRequest;
import com.mn.service.billing.models.requests.order.OrderRequest;

import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator
{
    // SQLException error code for when trying to insert a row that already exists
    public static int DUPLICATE_ENTRY_ERR_CODE = 1062;

    public static void validateRequest(OrderRequest request)
            throws ValidationException
    {
        validateEmail(request.getEmail());
    }

    public static void validateRequest(CustomerRequest request)
            throws ValidationException
    {
        validateEmail(request.getEmail());
    }

    public static void validateRequest(UpdateCustomerRequest request)
            throws ValidationException
    {
        validateEmail(request.getEmail());
        validateCreditCardID(request.getCcId());
    }

    public static void validateRequest(CreditCardRequest request)
            throws ValidationException
    {
        validateCreditCardID(request.getId());
    }

    public static void validateRequest(UpdateCreditCardRequest request)
            throws ValidationException
    {
        validateCreditCardID(request.getId());
        validateExpirationDate(request.getExpiration());
    }

    public static void validateRequest(CartRequest request)
            throws ValidationException
    {
        validateEmail(request.getEmail());
    }

    public static void validateRequest(UpdateCartRequest request)
            throws ValidationException
    {
        validateEmail(request.getEmail());
        validateQuantity(request.getQuantity());
    }

    private static void validateCreditCardID(String id)
            throws ValidationException
    {
        // Validate id length
        if (id.length() < 16 || id.length() > 20)
            throw new ValidationException(321, "Credit card ID has invalid length.");

        // Validate id format
        Matcher onlyNumbers = Pattern.compile("^[0-9]+$").matcher(id);
        if (!onlyNumbers.matches())
            throw new ValidationException(322, "Credit card ID has invalid value.");
    }

    private static void validateExpirationDate(Date date)
            throws ValidationException
    {
        if (date.getTime() < Calendar.getInstance().getTimeInMillis())
            throw new ValidationException(323, "expiration has invalid value.");
    }

    private static void validateQuantity(Integer quantity)
            throws ValidationException
    {
        if (quantity <= 0)
            throw new ValidationException(33, "Quantity has invalid value.");
    }

    private static void validateEmail(String email)
            throws ValidationException
    {
        // Validate email length
        if (email.length() == 0 || email.length() > 50)
            throw new ValidationException(-10, "Email address has invalid length.");

        // Validate email format
        Pattern emailRegex = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
        if (!emailRegex.matcher(email).matches())
            throw new ValidationException(-11, "Email address has invalid format.");
    }
}
