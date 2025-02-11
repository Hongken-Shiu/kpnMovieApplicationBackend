package com.kpn.movieservice.proxy;


import com.kpn.movieservice.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "userauthentication", url = "http://localhost:8080")
public interface UserProxy {
    @PostMapping ("/api/v3/register")
    public ResponseEntity<?> saveUser(@RequestBody User user);


}

