package com.deady.demo.action;

import com.deady.demo.entity.FormResponse;
import com.deady.demo.entity.Student;
import com.deady.demo.service.StudentService;
import com.deady.demo.utils.DateUtils;
import com.deady.demo.utils.MsgSendUtils;
import com.deady.demo.utils.OperatorSessionInfo;
import com.deady.demo.utils.RedisUtils;
import net.shunpay.util.ConfigUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by wzwuw on 2018/1/30.
 */
@Controller
public class ApplyAction {

    private static Logger logger = LoggerFactory.getLogger(ApplyAction.class);
    private static final PropertiesConfiguration config = ConfigUtil
            .getProperties("deady");
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    @DeadyAction(checkLogin = false, createToken = true)
    public Object viewApply(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        String classId = req.getParameter("classId");
        req.setAttribute("classId", classId);
        return new ModelAndView("/apply/apply");
    }

    @RequestMapping(value = "/getStudentByNameAndClassId", method = RequestMethod.POST)
    @DeadyAction(checkLogin = false, createToken = true)
    @ResponseBody
    public String getStudent(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        FormResponse response = new FormResponse();
        String classId = req.getParameter("classId");
        String name = req.getParameter("name");
        Student student = null;
        if (!StringUtils.isEmpty(classId)) {
            student = studentService.getStudentByClassIdAndName(classId, name);
        }
        if (null == student) {
            response.setResult(false);
            response.setMsg("抱歉,该班级下没有找到您,请输入正确的姓名后重试!");
            return response.toString();
        }
        response.setResult(true);
        response.setMsg("找到该学生信息!");
        return response.toString();
    }

    /**
     * 发送验证码
     */
    @RequestMapping(value = "/sendCodeMsg", method = RequestMethod.POST)
    @DeadyAction(checkLogin = false, createToken = true)
    @ResponseBody
    public Object sendCodeMsg(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        FormResponse response = new FormResponse();
        String classId = req.getParameter("classId");
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String pattern = config.getString("phone.expression");
        boolean isMatch = Pattern.matches(pattern, phone);
        if (!isMatch) {
            response.setResult(false);
            response.setMsg("手机号码不正确!");
            return response.toString();
        }
        Student student = null;
        if (!StringUtils.isEmpty(classId)) {
            student = studentService.getStudentByClassIdAndName(classId, name);
        }
        if (null == student) {
            response.setResult(false);
            response.setMsg("抱歉,该班级下没有找到您,请输入正确的姓名后重试!");
            response.setData("-1");
            return response.toString();
        }
        if (student.getIsApply().equals("1") && student.getIsPay().equals("0")) {
            response.setResult(true);
            response.setMsg("您已成功报名!");
            response.setData("0");
            return response.toString();
        }
        if (student.getIsApply().equals("1") && student.getIsPay().equals("1")) {
            response.setResult(true);
            response.setData("1");
            response.setMsg("您已成功报名!并缴费成功!");
            return response.toString();
        }
        if (student.getIsApply().equals("2")) {
            response.setResult(true);
            response.setData("2");
            response.setMsg("您之前已登记为去不了,如果改变主意了,请联系管理员");
            return response.toString();
        }
        // 校验当天发送的验证码的次数
        Jedis jedis = RedisUtils.getJedis();
        String dateStr = DateUtils.getCurrentDate("yyyyMMdd");
        String key = dateStr + "_" + phone;
        String value = jedis.get(dateStr + "_" + phone);
        logger.info("dateStr:" + dateStr);
        logger.info("key:" + key);
        logger.info("value:" + value);
        if (null != value) {
            int times = Integer.parseInt(value);
            if (times >= 3) {
                response.setResult(false);
                response.setMsg("抱歉,一天只能发送三次验证码,请明天再试");
                response.setData("3");
                return response.toString();
            } else {
                jedis.set(key, (++times) + "");
            }
        }

        String code = MsgSendUtils.generateCheckCode();
        logger.info("------发送短信开始------");
        Map<String, Object> resultMap = MsgSendUtils.sendMsg(phone,
                student.getName(), code);
        logger.info("------发送短信结束------");
        logger.info(student.getName() + ":code:" + code);
        if (null != resultMap && (Boolean) resultMap.get("result")) {
            // 验证码 存到session中
            Student stu = OperatorSessionInfo.getStudent(req);
            if (null == stu) {
                stu = student;
            }
            stu.setMsgCode(code);
            stu.setPhone(phone);
            OperatorSessionInfo.save(req,
                    OperatorSessionInfo.STUDENT_SESSION_ID, stu, 10);

            // 发成功了一次 记录下来
            if (null == value) {
                jedis.set(key, "1");
            }
            response.setResult(true);
            response.setMsg("验证码发送成功!");
            response.setData("4");
            return response.toString();
        } else {
            logger.info("message:" + resultMap.get("message"));
            response.setResult(false);
            response.setMsg("验证码发送失败!");
            response.setData("5");
            return response.toString();
        }
    }

    /**
     * 申请参加
     */
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @DeadyAction(checkLogin = false, createToken = true, checkToken = true)
    @ResponseBody
    public Object doApply(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        FormResponse response = new FormResponse();
        String vcode = req.getParameter("vcode");
        if (StringUtils.isEmpty(vcode)) {
            response.setResult(false);
            response.setMsg("请输入验证码!");
            return response.toString();
        }
        Student stu = OperatorSessionInfo.getStudent(req);
        if (null == stu) {
            response.setResult(false);
            response.setMsg("亲,有效期过了,请重试!");
            return response.toString();
        }
        if (!stu.getMsgCode().equals(vcode)) {
            response.setResult(false);
            response.setMsg("验证码错误!");
            return response.toString();
        }
        // 验证通过 添加报名信息
        studentService.apply(stu.getId(), stu.getPhone(), 1);
        // 显示付款页面(由于没有注册公司,所以只能显示个人账号二维码)
        response.setResult(true);
        response.setMsg("报名成功!");
        return response.toString();
    }

    /**
     * 不参加
     */

    @RequestMapping(value = "/canNotGo", method = RequestMethod.POST)
    @DeadyAction(checkLogin = false, createToken = true, checkToken = true)
    @ResponseBody
    public Object doCanNotGo(HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        FormResponse response = new FormResponse();
        String vcode = req.getParameter("vcode");
        if (StringUtils.isEmpty(vcode)) {
            response.setResult(false);
            response.setMsg("请输入验证码!");
            return response.toString();
        }
        Student stu = OperatorSessionInfo.getStudent(req);
        if (null == stu) {
            response.setResult(false);
            response.setMsg("亲,有效期过了,请重试!");
            return response.toString();
        }
        if (!stu.getMsgCode().equals(vcode)) {
            response.setResult(false);
            response.setMsg("验证码错误!");
            return response.toString();
        }
        studentService.apply(stu.getId(), stu.getPhone(), 3);
        response.setResult(true);
        response.setMsg("信息录入成功!");
        return response.toString();
    }

}
