package com.kpn.userauthentication.repository;

import com.kpn.userauthentication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    User findByEmailAndPassword(String email,String password);
}
