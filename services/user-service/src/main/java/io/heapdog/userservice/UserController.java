package io.heapdog.userservice;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = List.of(
            User.builder().id(1).name("User1").build(),
            User.builder().id(2).name("User2").build()
    );

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }


    @GetMapping("/hostname")
    public String getHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }

}
