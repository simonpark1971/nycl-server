package com.redspark.nycl;

import com.redspark.nycl.domain.Club;
import com.redspark.nycl.service.ClubService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SecurityController {

  @Autowired
  private ClubService clubService;

  @CrossOrigin(origins = "*")
  @PostMapping(value = "/security/login", consumes = "application/json")
  public ResponseEntity login(@RequestBody Map<String,String> loginMap) {

    Subject currentUser = SecurityUtils.getSubject();
    String clubName = null;
    if(!currentUser.isAuthenticated()) {
      String username = loginMap.get("username");
      Club usersClub = clubService.getMainContactsClub(username);
      if(usersClub != null) {
        username = "club.user";
        clubName = usersClub.getClubName();
      }
      UsernamePasswordToken token = new UsernamePasswordToken(username, loginMap.get("password"));
      token.setRememberMe(true);
      try {
        currentUser.login(token);
      } catch(Exception loginException) {
        // return an error code
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
      }
    }
    Map<String, String> userMap = new HashMap<>();
    if (currentUser.hasRole("leagueAdmin")) {
      userMap.put("role", "leagueAdmin");
    } else if (currentUser.hasRole("clubAdmin")) {
      if(null == clubName) {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
      }
      userMap.put("role", "clubAdmin");
      userMap.put("clubName", clubName);
    }
    return new ResponseEntity(userMap, HttpStatus.OK);
  }

  @RequestMapping(value = "/security/getrole", method = RequestMethod.GET)
  public String getRole(@RequestParam(value = "username") String username) {
    return "allowed";
  }

  @GetMapping(value = "/security/logout")
  @CrossOrigin(origins = "*")
  public void logout() {
    Subject currentUser = SecurityUtils.getSubject();
    currentUser.logout();
  }
}
