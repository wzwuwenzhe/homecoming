package com.deady.demo.service.impl;

import com.deady.demo.dao.StudentDAO;
import com.deady.demo.entity.Student;
import com.deady.demo.service.StudentService;
import com.deady.demo.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wzwuw on 2018/1/30.
 */
@Service
public class StudentServiceImpl implements StudentService {


    @Autowired
    private StudentDAO studentDAO;

    @Override
    public void addStudent(List<Student> studentList) {
        studentDAO.insertStudents(studentList);
    }

    @Override
    public Student getStudentByClassIdAndName(String classId, String name) {
        return studentDAO.findStudentByClassIdAndName(classId, name);
    }

    /**
     *
     * @param studentId
     * @param type
     *            1:报名 2:付款
     */
    @Override
    public void apply(String studentId, String phone, int type) {
        String now = DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");
        if (type == 1) {
            studentDAO.apply(studentId, phone, now, "1");
        } else if (type == 2) {
            studentDAO.pay(studentId, now);
        } else if (type == 3) {
            studentDAO.apply(studentId, phone, now, "2");
        }
    }

    @Override
    public Student getStudentById(String studentId) {
        return studentDAO.findStudentById(studentId);
    }


}
