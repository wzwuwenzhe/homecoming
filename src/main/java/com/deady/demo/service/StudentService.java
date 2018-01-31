package com.deady.demo.service;


import com.deady.demo.entity.Student;

import java.util.List;

/**
 * Created by wzwuw on 2018/1/30.
 */
public interface StudentService {

    void addStudent(List<Student> studentList);

    Student getStudentByClassIdAndName(String classId, String name);

    /**
     *
     * @param studentId
     * @param type
     *            1:报名 2:付款3:不报名
     */
    void apply(String studentId, String phone, int type);

    Student getStudentById(String studentId);




}
