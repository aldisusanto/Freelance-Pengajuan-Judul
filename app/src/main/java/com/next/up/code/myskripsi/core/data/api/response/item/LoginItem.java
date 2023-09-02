package com.next.up.code.myskripsi.core.data.api.response.item;

public class LoginItem {
    private String message;
    private UserData data;

    public String getMessage() {
        return message;
    }

    public UserData getData() {
        return data;
    }

    public static class UserData {
        private Integer id;
        private String nim;
        private String nama;
        private String role;

        public Integer getId() {
            return id;
        }

        public String getNim() {
            return nim;
        }

        public String getNamaMahasiswa() {
            return nama;
        }

        public String getRole() {
            return role;
        }
    }
}
