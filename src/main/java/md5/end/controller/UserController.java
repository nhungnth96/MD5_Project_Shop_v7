package md5.end.controller;

import md5.end.exception.BadRequestException;
import md5.end.model.dto.request.RePasswordRequest;
import md5.end.model.entity.user.User;
import md5.end.security.principal.UserDetailService;
import md5.end.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<User>> findAll() {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>("User's id not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

      @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<?> searchByName(@RequestParam(name = "q") String fullName) {
        if(userService.searchAllByFullNameContainingIgnoreCase(fullName).isEmpty()){
            return new ResponseEntity<>("No result found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.searchAllByFullNameContainingIgnoreCase(fullName), HttpStatus.OK);
    }


    @PutMapping("/{id}/status")
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

//    @PutMapping("/change-password")
//    public ResponseEntity<String> changePassword(@Valid @RequestBody RePasswordRequest rePasswordRequest) throws BadRequestException {
//        User user = userDetailService.getCurrentUser();
//        userService.changePassword(user.getId(), rePasswordRequest.getOldPass(),rePasswordRequest.getNewPass(),rePasswordRequest.getRePass());
//        return new ResponseEntity<>("Change password successfully",HttpStatus.OK);
//    }
}
