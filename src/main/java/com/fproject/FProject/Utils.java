package com.fproject.FProject;

import java.security.SecureRandom;

/**
 *
 * @author javier
 */
public class Utils {
    private static final String charactersSC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    public static final String generatorSC(int maxNumber) {

        SecureRandom random = new SecureRandom();
        StringBuilder codigo = new StringBuilder(maxNumber);
        for (int i = 0; i < maxNumber; i++) {
            int indice = random.nextInt(charactersSC.length());
            codigo.append(charactersSC.charAt(indice));
        }
        return codigo.toString();
    }
    
    public static final String generatorSC(int maxNumber, String charactersSC) {

        SecureRandom random = new SecureRandom();
        StringBuilder codigo = new StringBuilder(maxNumber);
        for (int i = 0; i < maxNumber; i++) {
            int indice = random.nextInt(charactersSC.length());
            codigo.append(charactersSC.charAt(indice));
        }
        return codigo.toString();
    }
}
