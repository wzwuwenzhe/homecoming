package com.deady.demo.action;

import com.deady.demo.entity.Student;
import com.deady.demo.service.StudentService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wzwuw on 2018/1/30.
 */
@Controller
public class ApplyAction {

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/getStudentByNameAndClassId", method = RequestMethod.POST)
    @DeadyAction(checkLogin = false, createToken = true)
    @ResponseBody
    public String getStudent(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        JsonObject json = new JsonObject();
        String classId = req.getParameter("classId");
        String name = req.getParameter("name");
        Student student = null;
        if (!StringUtils.isEmpty(classId)) {
            student = studentService.getStudentByClassIdAndName(classId, name);
        }
        if (null == student) {
            json.addProperty("result",false);
            json.addProperty("msg","抱歉,该班级下没有找到您,请输入正确的姓名后重试!");
            return json.toString();
        }
        json.addProperty("result",true);
        json.addProperty("msg","找到该学生信息!");
        return json.toString();
    }


}
