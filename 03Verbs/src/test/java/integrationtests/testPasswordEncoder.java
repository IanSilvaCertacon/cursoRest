package integrationtests;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class testPasswordEncoder {
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new Pbkdf2PasswordEncoder("", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        String rawPassword = "admin234";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);

        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

}
