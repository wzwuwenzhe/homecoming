package com.deady.demo.dao;


import com.deady.demo.entity.Operator;

public interface OperatorDAO {

	Operator getOperatorByLoginName(String userName);

}
