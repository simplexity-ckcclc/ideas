package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.dao.PersonMapper;
import com.ckcclc.springboot.service.CacheService;
import com.ckcclc.springboot.service.RetryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TestController.class)
public class TestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CacheService cacheService;

    @MockBean
    private PersonMapper personMapper;

    @MockBean
    private RetryService retryService;

    @Test
    public void cacheGet() throws Exception {
        given(this.cacheService.get(anyString())).willReturn("bar");

        this.mvc.perform(get("/test/cache/get/foo").accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string("bar"));
    }
}


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
//public class TestControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TestController testController;
//
//    private String namespace = "/test";
//    @Before
//    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
//
//    }
//
//    @Test
//    public void queryUsers() throws Exception {
//        StringBuilder url = new StringBuilder(namespace).append("/cache/get/foo");
//
//        MvcResult mvcResult = mockMvc.perform(
//                MockMvcRequestBuilders.get(url.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        System.out.println("query users :" + mvcResult.getResponse().getContentAsString());
//    }
//}
