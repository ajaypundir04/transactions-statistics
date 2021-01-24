package com.n26.rest;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import com.n26.service.TransactionServiceImpl;
import com.n26.util.TestUtil;
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
@WebMvcTest(value = TransactionController.class)
@ContextConfiguration(classes = {TransactionController.class})
public class TransactionControllerTest {

    private static final String TRANSACTIONS_URL = "/transactions";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private TransactionServiceImpl transactionService;

    @Before
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testPost() throws Exception {
        Mockito.doNothing().when(transactionService).create(Mockito.any(Transaction.class));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(TRANSACTIONS_URL).content(objectMapper.writeValueAsString(TestUtil.transaction(LocalDateTime.now())))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assert.assertNotNull(mvcResult);
    }

    @Test
    public void testPostThrowsOlderTransactionException() throws Exception {
        Mockito.doThrow(OlderTransactionException.class).when(transactionService).create(Mockito.any(Transaction.class));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(TRANSACTIONS_URL).content(objectMapper.writeValueAsString(TestUtil.transaction(LocalDateTime.now())))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        Assert.assertNotNull(mvcResult);
    }

    @Test
    public void testPostThrowsUnParsableTransactionException() throws Exception {
        Mockito.doThrow(UnParsableTransactionException.class).when(transactionService).create(Mockito.any(Transaction.class));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(TRANSACTIONS_URL).content(objectMapper.writeValueAsString(TestUtil.transaction(LocalDateTime.now())))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andReturn();

        Assert.assertNotNull(mvcResult);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(TRANSACTIONS_URL).content(objectMapper.writeValueAsString(TestUtil.transaction(null)))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andReturn();

        Assert.assertNotNull(mvcResult);
    }

    @Test
    public void testPostThrowsBadRequest() throws Exception {
        Mockito.doThrow(UnParsableTransactionException.class).when(transactionService).create(Mockito.any(Transaction.class));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(TRANSACTIONS_URL).content(objectMapper.writeValueAsString("-in-va-lid"))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Assert.assertNotNull(mvcResult);

    }

    @Test
    public void testDelete() throws Exception {
        Mockito.doNothing().when(transactionService).delete();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete(TRANSACTIONS_URL).content(objectMapper.writeValueAsString("-in-va-lid"))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        Assert.assertNotNull(mvcResult);

    }

    @Profile("embedded")
    @SpringBootApplication
    public static class Config {

    }

}

