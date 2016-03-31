package com.app1.security.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import com.app1.model.User;
import com.app1.service.RoleManager;
import com.app1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceImpl  implements
		ICustomUserDetailsService {
	@Autowired
	private UserService sysUserService;
	@Autowired
	private RoleManager roleManager;

	@Override
	public UserDetails loadUserByUsername(String loginUserName)
			throws UsernameNotFoundException {

		//当前用户
		User user = sysUserService.getUserByUsername(loginUserName);

		return user;
	}




}
