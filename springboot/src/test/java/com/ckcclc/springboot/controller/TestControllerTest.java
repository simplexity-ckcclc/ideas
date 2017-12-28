package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebMvcTest
//public class TestControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private CacheService cacheService;
//
//    @MockBean
//    private PersonMapper personMapper;
//
//    @MockBean
//    private RetryService retryService;
//
//    @Test
//    public void cacheGet() throws Exception {
//        given(this.cacheService.get(anyString())).willReturn("bar");
//
//        this.mvc.perform(get("/test/cache/get/foo").accept(MediaType.ALL))
//                .andExpect(status().isOk())
//                .andExpect(content().string("bar"));
//    }
//}


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TestController testController;

    private String namespace = "/test";
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();

    }

    @Test
    public void queryUsers() throws Exception {
        StringBuilder url = new StringBuilder(namespace).append("/cache/get/foo");

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(url.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("query users :" + mvcResult.getResponse().getContentAsString());
    }
}
