package com.example.orderservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.orderservice.exceptions.OrderException;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrderVO;
import com.example.orderservice.vo.ResponseItemVO;
import com.example.orderservice.vo.ResponseOrderVO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/order-service-api")
public class OrderApiController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrderVO> makeNewOrder(@PathVariable String userId,
			@RequestBody @Valid RequestOrderVO requestOrderVO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new OrderException(bindingResult.getFieldErrors());
		}

		requestOrderVO.setUserId(userId);
		
		// 주문한 아이템의 수량을 체크.
		String itemUrl = "http://ITEM-SERVICE-API/item-service-api/items/%s";
		ResponseEntity<ResponseItemVO> item = 
				this.restTemplate.exchange(
					itemUrl.formatted(requestOrderVO.getItemId()),
					HttpMethod.GET,
					null, // HttpHeader
					new ParameterizedTypeReference<>() {});
		
		ResponseItemVO orderItem = item.getBody();
		if (orderItem.getStock() < requestOrderVO.getItemOrderCount()) {
			throw new OrderException("주문 수량이 잘못되었습니다.");
		}
		
		
		ResponseOrderVO newOrder = this.orderService.createNewOrder(requestOrderVO);
		
		// 주문한 아이템의 수량을 주문 수량만큼 감소.
		
		return new ResponseEntity<ResponseOrderVO>(newOrder, HttpStatusCode.valueOf(201));
	}

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrderVO>> fetchAllOrders(@PathVariable String userId) {
		List<ResponseOrderVO> usersOrderList = this.orderService.fetchAllOrdersByUserId(userId);
		return new ResponseEntity<>(usersOrderList, HttpStatus.OK);
	}

}