package com.example.userservice.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.userservice.vo.RegistUserVO;
import com.example.userservice.vo.ResponseUserVO;

@Mapper
public interface UserDao {

	int insertNewUser(RegistUserVO registUserVO);

	ResponseUserVO selectOneUserByUserId(String userId);

}
