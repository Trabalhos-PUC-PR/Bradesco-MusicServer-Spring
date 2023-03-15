package br.pucpr.musicserver.rest.users;

import br.pucpr.musicserver.rest.users.requests.LoginRequest;
import br.pucpr.musicserver.rest.users.resonses.UserLoginResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.concurrent.CompletableFuture;

@Service // ou @Component
public class AuthServerClient {

    private static final Logger logger = LogManager.getLogger();
    @Async
    public CompletableFuture<UserLoginResponse> login(LoginRequest credentials){
        var api = new RestTemplate();
        var uri = new DefaultUriBuilderFactory()
                .builder()
                .scheme("http")
                .host("localhost")
                .port(3001)
                .path("/api/users/login")
                .build();
        var headers = new HttpHeaders();

//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        var request = new HttpEntity<>(credentials/*, headers*/);

        var response = api.exchange(
                uri,
                HttpMethod.POST,
                request,
                UserLoginResponse.class
        );

        if(response.getStatusCode().is2xxSuccessful()){
            var user = response.getBody();
            logger.info("Logged user: '%s' (%s)", user.getEmail(), user.getId());
            return CompletableFuture.completedFuture(user);
        }
        return CompletableFuture.failedFuture(
                new Exception("Invalid response: %d" + response.getStatusCode().value())
        );
    }

}
