package com.n26.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.service.StatisticsServiceImpl;
import com.n26.service.TransactionServiceImpl;
import com.n26.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@WebMvcTest(value = StatisticsController.class)
@ContextConfiguration(classes = {StatisticsController.class})
public class StatisticsControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private StatisticsServiceImpl statisticsService;

    @Before
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        Mockito.reset(statisticsService);

    }

    @Test
    public void testGet() throws Exception {

        Statistics statistics = TestUtil.statistics();
        Mockito.when(statisticsService.getStatistics()).thenReturn(statistics);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/statistics").content(objectMapper.writeValueAsString(TestUtil.transaction(LocalDateTime.now())))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Assert.assertNotNull(mvcResult);
        Assert.assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Statistics.class),
                Matchers.equalTo(statistics));
    }



    @Profile("embedded")
    @SpringBootApplication
    public static class Config {

    }

}

