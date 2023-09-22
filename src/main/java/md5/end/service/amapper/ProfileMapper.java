package md5.end.service.amapper;

import md5.end.model.dto.request.ProfileEditForm;
import md5.end.model.dto.response.ProfileResponse;
import md5.end.model.dto.response.UserResponse;
import md5.end.model.entity.user.User;
import md5.end.security.principal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements IGenericMapper<User, ProfileEditForm, ProfileResponse> {

    @Override
    public User getEntityFromRequest(ProfileEditForm profileEditForm) {

        return User.builder()

                .fullName(profileEditForm.getFullName())
                .birthday(profileEditForm.getBirthday())
                .tel(profileEditForm.getTel())
                .build();
    }

    @Override
    public ProfileResponse getResponseFromEntity(User user) {
        return ProfileResponse.builder()
                .fullName(user.getFullName())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .tel(user.getTel())
                .address(user.getAddress())
                .build();
    }
    public UserResponse getResponseFromEntityForAdmin(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .createdDate(user.getCreatedAt())
                .status(user.isStatus())
                .roles(user.getRoles())
                .build();
    }
}
