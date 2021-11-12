package com.project.contap.controller;


import com.project.contap.model.user.User;
import com.project.contap.security.WebSecurityConfig;
import com.project.contap.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MypageController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
class MypageControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    private MypageService mypageService;

    User testUser;


}