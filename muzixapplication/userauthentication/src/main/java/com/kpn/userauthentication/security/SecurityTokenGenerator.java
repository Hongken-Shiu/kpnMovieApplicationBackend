package com.kpn.userauthentication.security;

import com.kpn.userauthentication.domain.User;

public interface SecurityTokenGenerator {
	String createToken(User user);
}

