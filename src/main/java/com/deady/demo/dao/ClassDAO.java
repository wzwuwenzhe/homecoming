package com.deady.demo.dao;


import com.deady.demo.entity.Clazz;
import com.deady.demo.entity.Student;

import java.util.List;

public interface ClassDAO {

	List<Student> findStudentListByClassId(String classId);

	List<Clazz> findClassListBySchoolIds(List<String> schoolIdList);

}
