package com.example.userservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.utils.Sha;
import com.example.userservice.vo.AuthenticateUser;
import com.example.userservice.vo.RegistUserVO;
import com.example.userservice.vo.ResponseUserVO;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;

	public ResponseUserVO createNewUser(RegistUserVO registUserVO) {
		String salt = Sha.generateSalt();
		String password = registUserVO.getPwd();
		String encryptedPassword = Sha.getEncrypt(password, salt);
		registUserVO.setPwd(encryptedPassword);
		registUserVO.setSalt(salt);

		int createCount = this.userDao.insertNewUser(registUserVO);
		if (createCount > 0) {
			return this.userDao.selectOneUserByUserId(registUserVO.getUserId());
		}
		return null;
	}

	public List<ResponseUserVO> fetchAllUsers() {
		return this.userDao.selectAllUsers();
	}

	public ResponseUserVO fetchOneUser(String userId) {
		return this.userDao.selectOneUserByUserId(userId);
	}

	public ResponseUserVO fetchOneUserByEmail(String email) {
		return this.userDao.selectOneUserByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String email) 
			throws UsernameNotFoundException {
		ResponseUserVO userData = this.userDao.selectOneUserByEmail(email);
		
		if (userData == null) {
			throw new UsernameNotFoundException("not found user");
		}
		
		return new AuthenticateUser(userData);
	}
}






