package com.next.up.code.myskripsi.core.data.api.request;

public class LoginRequest {
    private String nim;
    private String password;
    private String role;

    public LoginRequest(String nim, String password, String role) {
        this.nim = nim;
        this.password = password;
        this.role = role;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNim() {
        return nim;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }
}
