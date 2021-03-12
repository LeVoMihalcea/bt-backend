package leo.bachelorsthesis.backend.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import leo.bachelorsthesis.backend.domain.dtos.LoginDto;
import leo.bachelorsthesis.backend.error.exceptions.APIAuthenticationException;
import leo.bachelorsthesis.backend.service.UserService.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final int hours;
    private final String keyForJwtTokens;
    private final UserService userService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, String keyForJwtTokens, int hours,
                                UserService userService) {
        this.authenticationManager = authenticationManager;
        this.keyForJwtTokens = keyForJwtTokens;
        this.hours = hours;
        this.userService = userService;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDto credentials = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(), credentials.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new APIAuthenticationException("The input could not be decoded!");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        String token = Jwts.builder()
                .setSubject(((User) authResult.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + hours * 3600000))
                .signWith(SignatureAlgorithm.HS512, keyForJwtTokens.getBytes())
                .compact();

        HashMap<String, String> responseToClient = new HashMap<>();
        responseToClient.put("token", token);
        responseToClient.put("user", jsonEncodeUser(((User) authResult.getPrincipal()).getUsername()));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.valueOf(new JSONObject(responseToClient)));
        response.getWriter().flush();
        logger.info("Log in successful");
    }

    private String jsonEncodeUser(String email) {
        leo.bachelorsthesis.backend.domain.User user = userService.findByEmail(email);
        JSONObject jsonEncodedUser = new JSONObject();
        jsonEncodedUser.put("id", user.getId());
        jsonEncodedUser.put("email", user.getEmail());
        jsonEncodedUser.put("firstName", user.getFirstName());
        jsonEncodedUser.put("lastName", user.getLastName());
        return jsonEncodedUser.toJSONString();
    }
}
