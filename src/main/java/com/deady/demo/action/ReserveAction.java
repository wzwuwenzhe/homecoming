package com.deady.demo.action;


import com.deady.demo.entity.Clazz;
import com.deady.demo.entity.FormResponse;
import com.deady.demo.entity.Operator;
import com.deady.demo.entity.Student;
import com.deady.demo.entity.enums.UserTypeEnum;
import com.deady.demo.service.ClassService;
import com.deady.demo.service.SchoolService;
import com.deady.demo.service.StudentService;
import com.deady.demo.utils.OperatorSessionInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ReserveAction {

	@Autowired
	private ClassService classService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private StudentService studentService;

	@RequestMapping(value = "/reserveSearch", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true, createToken = true)
	public String viewReserve(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse();
		Operator op = OperatorSessionInfo.getOperator(req);
		List<String> schoolIdList = new ArrayList<String>();
		if (op.getUserType().equals(UserTypeEnum.ADMIN.getType())) {
			schoolIdList = schoolService.getAllSchoolIds();
		} else {
			schoolIdList.add(op.getSchoolId());
		}
		List<Clazz> classList = classService
				.getClassListBySchoolIds(schoolIdList);
		if(null == classList || classList.size()==0){
            response.setResult(false);
            response.setMsg("无法查询到关联的班级!");
            return response.toString();
        }else{
            response.setData(accessClassList(classList));
            response.setResult(true);
            response.setMsg("查询成功!");
            return response.toString();
        }
	}

	/**
	 * 将classList转换成Json格式
	 * @param classList
	 * @return
	 */
	private String accessClassList(List<Clazz> classList) {
		JsonArray classArr = new JsonArray();
		if(null == classList || classList.size()==0){
			return classArr.toString();
		}
		for (Clazz clazz :classList) {
			JsonObject classObj = new JsonObject();
			classObj.addProperty("schoolId",clazz.getSchoolId());
			classObj.addProperty("id",clazz.getId());
			classObj.addProperty("name",clazz.getName());
			classArr.add(classObj);
		}
		return classArr.toString();
	}

    /**
     * 查询预订信息
     */
	@RequestMapping(value = "/reserveSearch", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true, createToken = true)
	public Object doReserveSearch(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		List<String> schoolIdList = new ArrayList<String>();
		if (op.getUserType().equals(UserTypeEnum.ADMIN.getType())) {
			schoolIdList = schoolService.getAllSchoolIds();
		} else {
			schoolIdList.add(op.getSchoolId());
		}
		List<Clazz> classList = classService
				.getClassListBySchoolIds(schoolIdList);
		String classId = req.getParameter("classId");
		String schoolId = req.getParameter("schoolId");
		List<Student> studentList = new ArrayList<Student>();
		if (!StringUtils.isEmpty(classId)) {
			studentList = classService.getStudentListByClassId(classId);
		}
		JsonObject dataObj = new JsonObject();
        dataObj.addProperty("classList",accessClassList(classList));
        dataObj.addProperty("studentList",accessStudentList(studentList));
        dataObj.addProperty("schoolId",schoolId);
        dataObj.addProperty("classId",classId);
		return new ModelAndView("/reserve/reserve");
	}

    private String accessStudentList(List<Student> studentList) {
        JsonArray studentArr = new JsonArray();
        if(null == studentList || studentList.size()==0){
            return studentArr.toString();
        }
        for (Student student :studentList) {
            JsonObject studentObj = new JsonObject();
            studentObj.addProperty("name",student.getName());
            studentObj.addProperty("phone",student.getPhone());
            studentObj.addProperty("stateDesc",student.getStateDesc());
            studentObj.addProperty("isApply",student.getIsApply());
            studentObj.addProperty("isPay",student.getIsPay());
            studentObj.addProperty("id",student.getId());
            studentArr.add(studentObj);
        }
        return studentArr.toString();
    }

    /**
     * 标记为已支付
     */
    @RequestMapping(value = "/payed", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true, createToken = true, checkToken = true)
	@ResponseBody
	public Object isPayed(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse();
		String studentId = req.getParameter("studentId");
		Student student = studentService.getStudentById(studentId);
		if (null == student) {
			response.setResult(false);
			response.setMsg("参数错误!");
			return response.toString();
		}
		studentService.apply(student.getId(), student.getPhone(), 2);
		response.setResult(true);
		response.setMsg("支付标志修改成功");
		return response.toString();
	}
}
