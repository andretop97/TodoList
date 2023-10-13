package br.com.andrebrancodev.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.andrebrancodev.todolist.user.IUserRepository;
import br.com.andrebrancodev.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();
                if (!servletPath.contains("/tasks")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                
                String authorization = request.getHeader("Authorization");
                
                if (authorization == null || authorization.isEmpty()) {
                    response.sendError(401, "Token not found");
                    return;
                }

                var authEncoded = authorization.substring("Basic".length()).trim();
            
                byte[] authDecoded= Base64.getDecoder().decode(authEncoded);

                String authString = new String(authDecoded);

                String[] authArray = authString.split(":");

                if (authArray.length != 2) {
                    response.sendError(401, "Token invalid");
                    return;
                }

                String username = authArray[0];
                String password = authArray[1];

                UserModel user = userRepository.findByUsername(username);

                if (user == null) {
                    response.sendError(401, "User not found");
                    return;
                }

                Boolean passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().toCharArray()).verified;

                if (Boolean.FALSE.equals(passwordVerify)) {
                    response.sendError(401, "Password incorrect");
                    return;
                }

                request.setAttribute("user", user);

                filterChain.doFilter(request, response);
    }

    
}
