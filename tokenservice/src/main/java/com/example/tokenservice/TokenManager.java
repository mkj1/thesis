/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.tokenservice;

import java.util.ArrayList;

/**
 *
 * @author gxv
 */
public class TokenManager {
    private ArrayList revokedTokens = new ArrayList();

    public String getToken() {
        return "NEWTOKEN";
    }

    public boolean checkToken(String token) {
        if (!isRevoked(token)) {
            revokeToken(token);
            return true;
        } else {
            return false;
        }
    }

    public boolean isRevoked(String token) {
        return revokedTokens.contains(token);
    }

    public boolean revokeToken(String token) {
        return revokedTokens.add(token);
    }

}
