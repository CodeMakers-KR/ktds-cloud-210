package com.example.orderservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orderservice.dao.OrderDao;
import com.example.orderservice.vo.RequestOrderVO;
import com.example.orderservice.vo.ResponseOrderVO;

@Service
public class OrderService {
	
	@Autowired
	private OrderDao orderDao;

	public ResponseOrderVO createNewOrder(RequestOrderVO requestOrderVO) {
		this.orderDao.insertNewOrder(requestOrderVO);
		return this.orderDao.selectOneOrderByOrderId(requestOrderVO.getOrderId());
	}

	public List<ResponseOrderVO> fetchAllOrdersByUserId(String userId) {
		return this.orderDao.selectAllOrdersByUserId(userId);
	}

}