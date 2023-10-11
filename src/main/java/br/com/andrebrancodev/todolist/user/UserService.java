package br.com.andrebrancodev.todolist.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;
    
    public UserModel create(UserModel user) {
       return userRepository.save(user);
    }

    public UserModel getUsersByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid);
    }

    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }
}
