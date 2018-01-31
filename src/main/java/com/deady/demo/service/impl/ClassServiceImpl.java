package com.deady.demo.service.impl;


import com.deady.demo.dao.ClassDAO;
import com.deady.demo.entity.Clazz;
import com.deady.demo.entity.Student;
import com.deady.demo.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

	@Autowired
	private ClassDAO classDAO;

	@Override
	public List<Student> getStudentListByClassId(String classId) {
		return classDAO.findStudentListByClassId(classId);
	}

	@Override
	public List<Clazz> getClassListBySchoolIds(List<String> schoolIdList) {
		return classDAO.findClassListBySchoolIds(schoolIdList);
	}

}
