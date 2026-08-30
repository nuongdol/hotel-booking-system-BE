package com.example.BookingHotel.security.User;

import com.example.BookingHotel.model.Permission;
import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class HotelUserDetails implements UserDetails{
    private Long id;
    private  String email;
    private String password;
    private Set<String> roles;
    private Set<String> permissions;

    public HotelUserDetails(Long id, String email, String password, Set<String> roles,
                            Set<String> permission){
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.permissions = permission;
    }


    //gán role and permission cho user
    public static HotelUserDetails buildUserDetails(User user){
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission:: getPermissionName)
                .collect(Collectors.toSet());
        return new HotelUserDetails(user.getId(), user.getEmail(), user.getPassword(), roles, permissions);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        //ham tra ve cho Spring Security de kiem tra quyen
        //add roles
        roles.forEach(role->authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        //add permission
        permissions.forEach(permission -> authorities.add(new
                SimpleGrantedAuthority(permission)));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
}
