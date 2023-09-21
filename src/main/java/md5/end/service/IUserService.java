package md5.end.service;

import md5.end.exception.BadRequestException;
import md5.end.model.dto.request.ProfileEditForm;
import md5.end.model.dto.request.RegisterForm;
import md5.end.model.dto.response.ProfileResponse;
import md5.end.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();

    Optional<User> findById(Long id);
    Optional<User> findByTel (String tel);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    User save(RegisterForm registerDto) throws LoginException;
    void changeStatus(User user);
    void changePassword(Long userId,String oldPass,String newPass,String rePassword) throws BadRequestException;

    List<User> searchAllByFullNameContainingIgnoreCase(String fullName);
}
