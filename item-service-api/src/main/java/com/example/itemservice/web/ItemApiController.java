package com.example.itemservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itemservice.service.ItemService;
import com.example.itemservice.vo.ResponseItemVO;

@RestController
@RequestMapping("/item-service-api")
public class ItemApiController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping("/items")
	public List<ResponseItemVO> getAllItems() {
		return this.itemService.fetchAllItems();
	}
}







