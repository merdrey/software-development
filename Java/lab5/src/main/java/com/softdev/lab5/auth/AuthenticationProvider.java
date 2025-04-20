package com.softdev.lab5.auth;

import com.softdev.lab5.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserRepo userRepo;

    @Value("${private.session-timeout}")
    private int sessionTimeout;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String userName,
                                       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        Object token = usernamePasswordAuthenticationToken.getCredentials();
        Optional<com.softdev.lab5.models.User> uu = userRepo.findByToken(String.valueOf(token));
        if (uu.isEmpty())
            throw new UsernameNotFoundException("user is not found");
        com.softdev.lab5.models.User u = uu.get();

        boolean timeout = true;
        LocalDateTime dt  = LocalDateTime.now();
        if (u.activity != null) {
            LocalDateTime nt = u.activity.plusMinutes(sessionTimeout);
            if (dt.isBefore(nt))
                timeout = false;
        }
        if (timeout) {
            u.token = null;
            userRepo.save(u);
            throw new NonceExpiredException("session is expired");
        }
        else {
            u.activity = dt;
            userRepo.save(u);
        }

        return new User(u.login, u.password,
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList("USER"));
    }
}
