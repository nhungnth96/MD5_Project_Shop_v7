package md5.end.service.impl;


import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProfileEditForm;
import md5.end.model.dto.request.RegisterForm;
import md5.end.model.dto.response.ProfileResponse;
import md5.end.model.entity.user.Role;
import md5.end.model.entity.user.RoleName;
import md5.end.model.entity.user.User;
import md5.end.repository.IUserRepository;
import md5.end.security.principal.UserDetailService;
import md5.end.service.IRoleService;
import md5.end.service.IUserService;
import md5.end.service.amapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.security.auth.login.LoginException;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProfileMapper profileMapper;
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchAllByFullNameContainingIgnoreCase(String name) {
        return userRepository.searchAllByFullNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByTel(String tel) {
        return userRepository.findByTel(tel);
    }


    @Override
    public User save(RegisterForm registerDto) throws LoginException {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new LoginException("Username is existed");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new LoginException("Email is existed");
        }
        Set<Role> roles = new HashSet<>();
        if(registerDto.getRoles()==null||registerDto.getRoles().isEmpty()){
            roles.add(roleService.findByRoleName(RoleName.ROLE_BUYER));
        } else {
            for (String role : registerDto.getRoles()) {
                if(!(role.equalsIgnoreCase("admin")|| role.equalsIgnoreCase("seller")||role.equalsIgnoreCase("buyer"))){
                    throw new LoginException("Case role name is wrong");
                }
                switch (role) {
                    case "admin":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_ADMIN));
                    case "seller":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_SELLER));
                    case "buyer":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_BUYER));
                }
            }

        }
        return userRepository.save( User.builder()
                .fullName(registerDto.getFullName())
                .username(registerDto.getUsername())
                .password(encoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .createdAt(LocalDate.now().toString())
                .status(true)
                .roles(roles)
                .build());
    }


    @Override
    public void changeStatus(User user) {
        user.setStatus(!user.isStatus());
        userRepository.save(user);
    }

//    @Override
//    public void changePassword(Long userId,String oldPass,String newPass,String rePassword) throws BadRequestException {
//        User user = userRepository.findById(userId).get();
//        String userPassword = user.getPassword();
//        if(!oldPass.equals(userPassword)){
//            throw new BadRequestException("Old password is incorrect");
//        } else if(newPass.equals(oldPass)){
//            throw new BadRequestException("New password is the same old password.");
//        } else if(!newPass.equals(rePassword)) {
//            throw new BadRequestException("Re-password is not the same new password");
//        }
//        user.setPassword(newPass);
//            userRepository.save(user);
//        }
}
