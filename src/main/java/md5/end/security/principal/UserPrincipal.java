package md5.end.security.principal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import md5.end.model.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String birthday;
    private String tel;
    private String address;
    private String createdDate;
    private String updatedDate;
    private boolean status;
    // user principal không lưu trữ dưới dạng role mà lưu dưới dạng GrantedAuthority
    // private List<Role> roles;
    // bảng Role cột role phải đặt tên là ROLE_name để nhận dạng các quyền khi đọc
    // và tự động xóa tiền tố ROLE_
    private Collection<? extends GrantedAuthority> authorities;
    // chuyển đổi user sang principal
    public static  UserPrincipal build(User user){
        List<GrantedAuthority> authorityList = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .birthday(user.getBirthday())
                .tel(user.getTel())
                .address(user.getAddress())
                .createdDate(user.getCreatedAt())
                .status(user.isStatus())
                .authorities(authorityList)
                .build();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
