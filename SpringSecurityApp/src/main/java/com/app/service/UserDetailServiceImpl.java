package com.app.service;

import com.app.persistence.entity.UserEntity;
import com.app.persistence.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    // Control para inyectar dependencias
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository
                .findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));

        // SpringSecurity maneja los permisos con GrantedAuthority
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Convierte los permisos y roles en objetos que entienda spring security
        userEntity.getRolesEntities().forEach(role -> authorityList
                .add(new SimpleGrantedAuthority("ROLE_"
                        .concat(role.getRoleEnum().name()))));

        // stream recorrre cada uno de los roles
        userEntity.getRolesEntities().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permissionEntity -> authorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(), 
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialsNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }

}
