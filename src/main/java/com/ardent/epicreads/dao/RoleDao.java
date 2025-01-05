package com.ardent.epicreads.dao;

import com.ardent.epicreads.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
