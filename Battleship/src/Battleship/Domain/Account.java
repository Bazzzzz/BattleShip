/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import java.io.Serializable;

/**
 *
 * @author sebas
 */
public class Account implements Serializable {

    private String loginName;
    private String password;

    /**
     * Constructs an account.
     *
     * @param loginName not null
     * @param password not null, larger than 4
     */
    public Account(String loginName, String password) {
        if (loginName == null || loginName.equals("")) {
            throw new IllegalArgumentException("Login name was null.");
        }
        if (password == null || password.equals("")) {
            throw new IllegalArgumentException("Password was null.");
        }
        if (password.length() < 4) {
            throw new IllegalArgumentException("Password has to be larger than 4");
        }
        this.loginName = loginName;
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Changes the password of the user if the entered original password is
     * correct.
     *
     * @param originalPassword not null
     * @param newPassword not null, larger than 5
     * @return True if changed, False if not.
     */
    public boolean changePassword(String originalPassword, String newPassword) {
        if (originalPassword != null && newPassword != null) {
            if (newPassword.length() < 4) {
                throw new IllegalArgumentException("Password has to be larger than 4");
            }
            if (originalPassword.equals(this.password)) {
                this.password = newPassword;
                return true;
            }
        } else {
            throw new IllegalArgumentException("Original password and original password can't be null.");
        }

        return false;
    }

    @Override
    public String toString() {
        return "Account login name: " + this.loginName;
    }
}
