package com.example.itemservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.itemservice.service.ItemService;
import com.example.itemservice.vo.ReqeustUpdateStockVO;
import com.example.itemservice.vo.ResponseItemVO;

@RestController
@RequestMapping("/item-service-api")
public class ItemApiController {

	@Autowired
	private ItemService itemService;

	@GetMapping("/items")
	public ResponseEntity<List<ResponseItemVO>> getAllItems() {
		List<ResponseItemVO> itemList = this.itemService.fetchAllItems();
		return new ResponseEntity<List<ResponseItemVO>>(itemList, HttpStatusCode.valueOf(200));
	}
	
	@GetMapping("/items/{itemId}")
	public ResponseEntity<ResponseItemVO> getItem(
				@PathVariable String itemId
			) {
		ResponseItemVO itemList = this.itemService.fetchItem(itemId);
		return new ResponseEntity<>(itemList, HttpStatusCode.valueOf(200));
	}
	
	@PutMapping("/items/{itemId}")
	public ResponseEntity<ResponseItemVO> updateStock(
			@PathVariable String itemId,
			@RequestBody ReqeustUpdateStockVO updateStockVO) {
		updateStockVO.setItemId(itemId);
		
		ResponseItemVO updateResult = this.itemService.updateStock(updateStockVO);
		
		return new ResponseEntity<>(updateResult, HttpStatusCode.valueOf(200));
		
	}
	
}








