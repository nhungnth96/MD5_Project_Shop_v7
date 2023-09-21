package md5.end.service.impl;


import md5.end.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import md5.end.model.entity.user.Role;
import md5.end.model.entity.user.RoleName;
import md5.end.service.IRoleService;

import javax.security.auth.login.LoginException;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;
    @Override
    public Role findByRoleName(RoleName roleName) throws LoginException {
        Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);
        if(roleOptional.isPresent()){
            return roleOptional.get();
        } else {
            throw new LoginException(roleName+" not found.");
        }


    }
}
