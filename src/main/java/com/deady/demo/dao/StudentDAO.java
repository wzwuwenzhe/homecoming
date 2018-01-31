package com.deady.demo.dao;


import com.deady.demo.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wzwuw on 2018/1/30.
 */
public interface StudentDAO {


    void insertStudents(List<Student> studentList);

    Student findStudentByClassIdAndName(@Param("classId") String classId,
                                        @Param("name") String name);

    void apply(@Param("studentId") String studentId,
               @Param("phone") String phone, @Param("now") String now,
               @Param("applyType") String applyType);

    void pay(@Param("studentId") String studentId, @Param("now") String now);

    Student findStudentById(String studentId);


}
