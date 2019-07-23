package com.mn.service.api_gateway.utility;

import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;

public class TransactionIDGenerator {
    private static final int ID_SIZE = 64;

    public static String generateTransactionID() {
        SecureRandom rngesus = new SecureRandom();
        byte[] id = new byte[ID_SIZE];
        rngesus.nextBytes(id);
        String idString = Hex.encodeHexString(id);
        return idString;
    }
}
