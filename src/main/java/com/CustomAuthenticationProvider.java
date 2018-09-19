package com;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String name = auth.getName();
		String password = auth.getCredentials().toString();

		if ("jeveson".equals(name) && "123456".equals(password)) {
			List<Role> authorities = new ArrayList<>();
			authorities.add(new Role());
			return new UsernamePasswordAuthenticationToken(auth.getPrincipal(), null,authorities);
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> auth) {

		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

	class Role implements GrantedAuthority {

		private static final long serialVersionUID = -5384405492405907243L;

		@Override
		public String getAuthority() {
			return "ACTUATOR";
		}

	}
}
