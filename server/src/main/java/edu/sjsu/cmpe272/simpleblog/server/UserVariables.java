package edu.sjsu.cmpe272.simpleblog.server;


public class UserVariables {
    private String user;
    private String publicKey;

    public UserVariables() {}

    public UserVariables(String user, String publicKey) {
        this.user = user;
        this.publicKey = publicKey;
    }

    // Getters
    public String getUser() {
        return user;
    }

    public String getPublicKey() {
        return publicKey;
    }

    // Setters
    public void setUser(String user) {
        this.user = user;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}

