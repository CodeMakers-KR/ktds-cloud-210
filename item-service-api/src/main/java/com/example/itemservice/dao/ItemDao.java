package com.example.itemservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.itemservice.vo.ResponseItemVO;

@Mapper
public interface ItemDao {

	List<ResponseItemVO> selectAllItems();

	ResponseItemVO selectItem(String itemId);

}
