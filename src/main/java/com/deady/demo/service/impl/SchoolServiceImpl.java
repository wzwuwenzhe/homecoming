package com.deady.demo.service.impl;


import com.deady.demo.dao.SchoolDAO;
import com.deady.demo.service.SchoolService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wzwuw on 2018/1/29.
 */
@Service
public class SchoolServiceImpl implements SchoolService {

    private SchoolDAO schoolDAO;

    public List<String> getAllSchoolIds(){
       return schoolDAO.findAllSchoolIds();
    };


}
