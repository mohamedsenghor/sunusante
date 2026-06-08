package dev.sunusante.gateway.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import dev.sunusante.gateway.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class MfaService {

    private final UserAccountRepository userAccountRepository;
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public MfaService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public Mono<String> setupMfa(String login) {
        return userAccountRepository.findByInternalUserLogin(login)
            .map(userAccount -> {
                final GoogleAuthenticatorKey key = gAuth.createCredentials();
                userAccount.setMfaSecret(key.getKey());
                userAccountRepository.save(userAccount).subscribe();
                return key.getKey();
            });
    }

    public Mono<Boolean> validateMfa(String login, int code) {
        return userAccountRepository.findByInternalUserLogin(login)
            .map(userAccount -> {
                if (userAccount.getMfaSecret() == null) {
                    return false;
                }
                return gAuth.authorize(userAccount.getMfaSecret(), code);
            })
            .defaultIfEmpty(false);
    }
}
