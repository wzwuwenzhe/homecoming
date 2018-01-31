package com.deady.demo.service;

import com.deady.demo.entity.Clazz;
import com.deady.demo.entity.Student;

import java.util.List;


public interface ClassService {

	List<Student> getStudentListByClassId(String classId);

	List<Clazz> getClassListBySchoolIds(List<String> schoolIdList);

}
