package md5.end.controller;

import md5.end.exception.AccessDeniedException;
import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProfileEditForm;
import md5.end.model.dto.request.RePasswordRequest;
import md5.end.model.entity.user.User;
import md5.end.security.principal.UserDetailService;
import md5.end.service.IUserService;
import md5.end.service.amapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private ProfileMapper profileMapper;
//    @GetMapping("")
//    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
//    public ResponseEntity<List<?>> findAll() {
//
//        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
//    }
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<Page<?>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        return new ResponseEntity<>(userService.findAll(page,size), HttpStatus.OK);
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<?> searchByName(
            @RequestParam(defaultValue = "") String fullName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) throws NotFoundException {
        return new ResponseEntity<>(userService.findByFullNameContainingIgnoreCase(fullName,page,size), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        User user = userDetailService.getCurrentUser();
        if(id.equals(user.getId())){
            return new ResponseEntity<>(profileMapper.getResponseFromEntity(user), HttpStatus.OK);
        } else {
            Optional<User> userOptional = userService.findById(id);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>("User's id not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(profileMapper.getResponseFromEntity(userOptional.get()), HttpStatus.OK);

        }
    }


    @PutMapping("/{id}/edit-profile")
    public ResponseEntity<?> editProfile(
            @PathVariable Long id,
            @Valid @ModelAttribute ProfileEditForm profileEditForm){
        User user = userDetailService.getCurrentUser();
        if(id.equals(user.getId())) {
            return new ResponseEntity<>(userService.editProfile(user.getId(), profileEditForm), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You don't have permission to access this page.",HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody RePasswordRequest rePasswordRequest) throws BadRequestException {
        User user = userDetailService.getCurrentUser();
        if(id.equals(user.getId())){
            userService.changePassword(user.getId(), rePasswordRequest.getOldPass(),rePasswordRequest.getNewPass(),rePasswordRequest.getReNewPass());
            return new ResponseEntity<>("Change password successfully!",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You don't have permission to access this page.",HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping("/{id}/change-status")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>("User's id not found", HttpStatus.NOT_FOUND);
        }
        if (id==1) {
            return new ResponseEntity<>("You can't change status of admin!", HttpStatus.BAD_REQUEST);
        }
        userService.changeStatus(userOptional.get());
        return new ResponseEntity<>("Change status successfully.", HttpStatus.OK);
    }
}
