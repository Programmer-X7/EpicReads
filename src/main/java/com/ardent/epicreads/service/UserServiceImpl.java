package com.ardent.epicreads.service;

import com.ardent.epicreads.dao.RoleDao;
import com.ardent.epicreads.dao.UserDao;
import com.ardent.epicreads.entity.Role;
import com.ardent.epicreads.entity.User;
import com.ardent.epicreads.repository.OrderRepository;
import com.ardent.epicreads.repository.UserRepository;
import com.ardent.epicreads.security.CustomUserDetails;
import com.ardent.epicreads.dto.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private final UserDao userDao;
	private final UserRepository userRepository;
	private final RoleDao roleDao;
	private final BCryptPasswordEncoder passwordEncoder;
	private final OrderRepository orderRepository;

	@Autowired
	public UserServiceImpl(UserDao userDao, UserRepository userRepository, RoleDao roleDao, BCryptPasswordEncoder passwordEncoder, OrderRepository orderRepository) {
		this.userDao = userDao;
        this.userRepository = userRepository;
        this.roleDao = roleDao;
		this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
    }

	@Override
	public User findById(long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User findByUserName(String userName) {
		// check the database if the user already exists
		return userDao.findByUserName(userName);
	}

	@Override
	public List<User> findAllCustomers() {
		return userRepository.findAllByRoleName("ROLE_CUSTOMER");
	}

	@Override
	public void save(WebUser webUser) {
		User user = new User();

		// assign user details to the user object
		user.setUserName(webUser.getUserName());
		user.setPassword(passwordEncoder.encode(webUser.getPassword()));
		user.setFirstName(webUser.getFirstName());
		user.setLastName(webUser.getLastName());
		user.setPhoneNumber(webUser.getPhoneNumber());
		user.setEnabled(true);

		// Todo: How to register for ROLE_ADMIN
		// give default role of "CUSTOMER" on Registration
		// user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_ADMIN")));
		user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_CUSTOMER")));

		// save user in the database
		userDao.save(user);
	}

	@Override
	public void update(User user) {
		User existingUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found!"));

		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setPhoneNumber(user.getPhoneNumber());
		existingUser.setProfileImage(user.getProfileImage());
		existingUser.setUserName(user.getUserName());

		userRepository.save(existingUser);
	}

	@Override
	@Transactional
	public void delete(long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Delete Order details for the user
		orderRepository.deleteByUserId(userId);

		// Delete from user table
		userRepository.delete(user);
	}

	@Override
	public void banUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
		user.setEnabled(false);
		userRepository.save(user);
	}

	@Override
	public void unbanUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
		user.setEnabled(true);
		userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUserName(userName);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());

		return new CustomUserDetails(user.getUserName(), user.getPassword(), authorities);
	}

	private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role tempRole : roles) {
			SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
			authorities.add(tempAuthority);
		}

		return authorities;
	}
}
