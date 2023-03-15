package br.pucpr.musicserver.rest.users;

import br.pucpr.musicserver.lib.security.JWT;
import br.pucpr.musicserver.rest.users.requests.LoginRequest;
import br.pucpr.musicserver.rest.users.resonses.UserLoginResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger();

    private JWT jwt;
    private AuthServerClient authServer;

    public UserService(JWT jwt, AuthServerClient authServer) {
        this.jwt = jwt;
        this.authServer = authServer;
    }

    public UserLoginResponse login(LoginRequest credentials){
        return authServer.login(credentials)
                .exceptionally(error-> {
                    logger.error("Login failed - ", error.getCause());
                    return null;
                })
                .join();
    }

    public UserLoginResponse createTestUser(String token){
        return jwt.createTestUser(token);
    }



}
