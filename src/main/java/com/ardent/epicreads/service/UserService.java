package com.ardent.epicreads.service;

import com.ardent.epicreads.entity.User;
import com.ardent.epicreads.dto.WebUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	User findById(long id);

	User findByUserName(String userName);

	List<User> findAllCustomers();

	void save(WebUser webUser);

	void update(User user);

	void delete(long userName);

	void banUser(Long userId);

	void unbanUser(Long userId);
}
