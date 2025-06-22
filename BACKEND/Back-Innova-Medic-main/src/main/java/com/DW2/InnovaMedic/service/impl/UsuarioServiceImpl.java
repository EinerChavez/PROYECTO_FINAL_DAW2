package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.entity.Usuario;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UsuarioServiceImpl implements UserDetailsService{

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findOneByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("El usuario no se ha encontrado con el email: " + email) );

        return new UsuarioDetailImpl(usuario);
    }
}
