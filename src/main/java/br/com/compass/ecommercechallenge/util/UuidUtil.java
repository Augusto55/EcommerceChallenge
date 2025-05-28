package br.com.compass.ecommercechallenge.util;

import br.com.compass.ecommercechallenge.exception.InvalidUuidFormatException;

import java.util.UUID;

public class UuidUtil {

    public static UUID validateStringToUUID(String string){
        UUID uuid;
        try{
            uuid = UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            throw new InvalidUuidFormatException();
        }

        return uuid;
    }
}
