package com.rggt.edutectno.bootcamp.demojwt.userdto;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;

    @Basic
    private String username;


    private String lastname;

    private String firstname;
    private String country;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Constructor privado
    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.lastname = builder.lastname;
        this.firstname = builder.firstname;
        this.country = builder.country;
        this.password = builder.password;
        this.role = builder.role;
    }

    public User() {

    }

    // Métodos getter
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getCountry() {
        return country;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Clase Builder estática
    public static class Builder {
        private Integer id;
        private String username;
        private String lastname;
        private String firstname;
        private String country;
        private String password;
        private Role role;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
