package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {
    private String login;
    private String pass;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationRequestDTO that = (AuthenticationRequestDTO) o;
        return login.equals(that.login) && pass.equals(that.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, pass);
    }
}
