package md5.end.service.impl;


import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProfileEditForm;
import md5.end.model.dto.request.RegisterForm;
import md5.end.model.dto.response.ProductResponse;
import md5.end.model.dto.response.ProfileResponse;
import md5.end.model.dto.response.UserResponse;
import md5.end.model.entity.product.Product;
import md5.end.model.entity.product.ProductImage;
import md5.end.model.entity.user.Role;
import md5.end.model.entity.user.RoleName;
import md5.end.model.entity.user.User;
import md5.end.repository.IUserRepository;
import md5.end.security.principal.UserDetailService;
import md5.end.service.IRoleService;
import md5.end.service.IUserService;
import md5.end.service.amapper.ProfileMapper;
import md5.end.service.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.security.auth.login.LoginException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    FileService fileService;
    @Autowired
    private ProfileMapper profileMapper;
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<UserResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> userPage = userRepository.findAll(pageable);
        Page<UserResponse> userResponsePage = userPage.map(user -> profileMapper.getResponseFromEntityForAdmin(user));
        return userResponsePage;
    }

    @Override
    public Page<UserResponse> findByFullNameContainingIgnoreCase(String name, int page, int size) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> userPage = userRepository.findByFullNameContainingIgnoreCase(name,pageable);
        Page<UserResponse> userResponsePage = userPage.map(user -> profileMapper.getResponseFromEntityForAdmin(user));
        if(userResponsePage.isEmpty()){
            throw new NotFoundException("No result found.");
        }
        return userResponsePage;
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
    public User editProfile(Long userId, ProfileEditForm profileEditForm) {

        User OldDataUser = userRepository.findById(userId).get();

        User NewDataUser = User.builder()
                .id(userId)
                .username(OldDataUser.getUsername())
                .password(OldDataUser.getPassword())
                .email(OldDataUser.getEmail())
                .createdAt(OldDataUser.getCreatedAt())
                .updatedAt(LocalDateTime.now().toString())
                .status(OldDataUser.isStatus())
                .roles(OldDataUser.getRoles())
                .cartItems(OldDataUser.getCartItems())
                .orders(OldDataUser.getOrders())
                .fullName(profileEditForm.getFullName() == null ? OldDataUser.getFullName() : profileEditForm.getFullName())
                .birthday(profileEditForm.getBirthday() == null ? OldDataUser.getBirthday() : profileEditForm.getBirthday())
                .tel(profileEditForm.getTel() == null ? OldDataUser.getTel() : profileEditForm.getTel())
                .address(profileEditForm.getAddress() == null ? OldDataUser.getAddress() : profileEditForm.getAddress())
                .avatar(profileEditForm.getAvatar() == null ? OldDataUser.getAvatar() : fileService.uploadFile(profileEditForm.getAvatar()))
                .build();
        return userRepository.save(NewDataUser);
    }

    @Override
    public void changeStatus(User user) {
        user.setStatus(!user.isStatus());
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long userId,String oldPass,String newPass,String reNewPassword) throws BadRequestException {
        User user = userRepository.findById(userId).get();
        String userPassword = user.getPassword();

        if(!(encoder.matches(oldPass,userPassword))){
            throw new BadRequestException("Old password is incorrect");
        } else if(newPass.equals(oldPass)){
            throw new BadRequestException("New password is the same old password.");
        } else if(!newPass.equals(reNewPassword)) {
            throw new BadRequestException("Re-password is not the same new password");
        }
        user.setPassword(encoder.encode(newPass));

            userRepository.save(user);
        }

}
