package com.interview.esphere.service;

import com.interview.esphere.domain.model.User;

public interface UserService {
	User findUserByEmail(String email);
	void saveUser(User user);
}
