package leo.bachelorsthesis.backend.authorization;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import leo.bachelorsthesis.backend.error.exceptions.APIAuthenticationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Configurable
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private String secret;

    public AuthorizationFilter(AuthenticationManager authenticationManager, String secret) {
        super(authenticationManager);
        this.secret = secret;
    }
    
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
    {
        String token = request.getHeader("Authorization");
        if(token != null)
        {
            try {
                String user = Jwts.parser().setSigningKey(secret.getBytes())
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody()
                        .getSubject();
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
                return null;
            }
            catch (SignatureException signatureException){
                throw new APIAuthenticationException("The JWT is not signed and thus will not be trusted!");
            }
        }
        return null;
    }
}
