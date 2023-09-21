package md5.end.repository;

import md5.end.model.entity.user.User;
import org.hibernate.type.StringNVarcharType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByTel(String tel);
    Optional<User> findByUsernameContainingIgnoreCase (String username);
    Optional<User> findByEmail (String email);
    Optional<User> findByTel (String tel);

    List<User> searchAllByFullNameContainingIgnoreCase(String name);


}
