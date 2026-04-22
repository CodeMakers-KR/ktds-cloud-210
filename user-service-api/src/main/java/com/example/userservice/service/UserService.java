package com.example.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userservice.dao.UserDao;
import com.example.userservice.vo.RegistUserVO;
import com.example.userservice.vo.ResponseUserVO;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Transactional
	public ResponseUserVO createNewUser(RegistUserVO registUserVO) {
		
		registUserVO.setSalt("test-salt");
		
		int insertCount = this.userDao.insertNewUser(registUserVO);
		if (insertCount == 1) {
			return this.userDao.selectOneUserByUserId(registUserVO.getUserId());
		}
		
		return null;
	}

}
