package leo.bachelorsthesis.backend.rest;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getHealth(){
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        HashMap<String, String> response = new HashMap<>();
        response.put("alive", "yes");
        response.put("user", loggedInUser.getName());
        return new ResponseEntity<>(new JSONObject(response).toJSONString(), HttpStatus.OK);
    }
}
