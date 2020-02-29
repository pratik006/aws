package com.prapps.aws.email;

import java.util.Objects;

public class EmailAccount {
    private String username;
    private String password;
    private String host;
    private int port;

    public EmailAccount() {}

    public EmailAccount(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return "EmailAccount{" +
                "username='" + username + '\'' +
                ", password='" + (password==null?"null":"<not null>") + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAccount that = (EmailAccount) o;
        return port == that.port &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, host, port);
    }
}
