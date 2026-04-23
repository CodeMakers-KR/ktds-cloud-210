package com.example.itemservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.itemservice.dao.ItemDao;
import com.example.itemservice.vo.ResponseItemVO;

@Service
public class ItemService {

	@Autowired
	private ItemDao itemDao;
	
	public List<ResponseItemVO> fetchAllItems() {
		return this.itemDao.selectAllItems();
	}

	public ResponseItemVO fetchItem(String itemId) {
		return this.itemDao.selectItem(itemId);
	}

}






