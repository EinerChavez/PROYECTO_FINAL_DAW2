package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.entity.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UsuarioDetailImpl implements UserDetails {
    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return usuario.getContrasenia();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    public String getUser() {
        return usuario.getNombre();
    }

    public Usuario getUsuario() {
        return this.usuario;
    }
}
