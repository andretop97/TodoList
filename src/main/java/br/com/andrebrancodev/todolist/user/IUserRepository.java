package br.com.andrebrancodev.todolist.user;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID>{
    UserModel findByUuid(UUID uuid);

    UserModel findByUsername(String username);

    Boolean existsByUsernameOrEmail(String username, String email);
}
