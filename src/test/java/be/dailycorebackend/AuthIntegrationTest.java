package be.dailycorebackend;

import be.dailycorebackend.api.dto.CreateUserRequest;
import be.dailycorebackend.api.dto.LoginRequest;
import be.dailycorebackend.api.dto.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIntegrationTest extends IntegrationTestSupport {

    @Test
    void register_returnsTokenAndUser() throws Exception {
        String email = uniqueEmail();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Diego",
                                  "email": "%s",
                                  "password": "secret123",
                                  "phoneNumber": "+351900000000"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.user.name", is("Diego")))
                .andExpect(jsonPath("$.user.email", is(email)));
    }

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        String email = uniqueEmail();
        registerUser("Diego", email, "secret123");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("secret123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.email", is(email)));
    }

    @Test
    void login_withInvalidCredentials_returns401() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("missing@example.com");
        loginRequest.setPassword("wrong-password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Invalid email or password")));
    }

    @Test
    void register_duplicateEmail_returns409() throws Exception {
        String email = uniqueEmail();
        registerUser("Diego", email, "secret123");

        CreateUserRequest duplicate = new CreateUserRequest();
        duplicate.setName("Other");
        duplicate.setEmail(email);
        duplicate.setPassword("secret123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Email already registered: " + email)));
    }

    @Test
    void getMe_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getMe_withToken_returnsCurrentUser() throws Exception {
        String email = uniqueEmail();
        AuthTokens auth = registerUser("Diego", email, "secret123");

        mockMvc.perform(authorized(auth.token(), get("/api/auth/me")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(auth.userId().intValue())))
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.name", is("Diego")));
    }

    @Test
    void updateProfile_updatesUserData() throws Exception {
        String email = uniqueEmail();
        AuthTokens auth = registerUser("Diego", email, "secret123");

        UpdateUserRequest update = new UpdateUserRequest();
        update.setName("Diego Updated");
        update.setEmail(email);
        update.setPhoneNumber("+351911111111");

        mockMvc.perform(authorized(auth.token(), put("/api/auth/me"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Diego Updated")))
                .andExpect(jsonPath("$.phoneNumber", is("+351911111111")));
    }
}
