package com.project.contap.controller;

import com.project.contap.model.user.User;
import com.project.contap.security.WebSecurityConfig;
import com.project.contap.service.MainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MypageController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
class MypageControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MainService mainService;

    User testUser;

    @BeforeEach
    public void setup() {
        testUser = User.builder()
                    .email("testUse@gmail.com")
                    .pw("1234qwer")
                    .userName("testUser").build();
    }

    @Nested
    @DisplayName("")
    class Post {
        @Nested
        @DisplayName("")
        class PostSuccess {
        }

        @Nested
        @DisplayName("")
        class PostFail {
        }
    }

    @Nested
    @DisplayName("")
    class Get {
        @Nested
        @DisplayName("")
        class GetSuccess {
        }

        @Nested
        @DisplayName("")
        class GetFail {
        }
    }

    @Nested
    @DisplayName("")
    class Delete {
        @Nested
        @DisplayName("")
        class DeleteSuccess {
        }

        @Nested
        @DisplayName("")
        class DeleteFail {
        }
    }
}