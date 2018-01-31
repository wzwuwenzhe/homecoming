package com.deady.demo.service.impl;


import com.deady.demo.dao.OperatorDAO;
import com.deady.demo.entity.Operator;
import com.deady.demo.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorServiceImpl implements OperatorService {

	@Autowired
	private OperatorDAO operatorDAO;

	@Override
	public Operator getOperatorByLoginName(String userName) {
		return operatorDAO.getOperatorByLoginName(userName);
	}

}
