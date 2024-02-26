package com.example.faultinjectionmanager;

import com.example.faultinjectionmanager.controller.ConfigController;
import com.example.faultinjectionmanager.service.EnvironmentConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ConfigController.class)
public class ConfigControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvironmentConfigService environmentConfigService;

    @Test
    void updateDivertPercentage_ReturnsSuccessMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/config/divertPercentage")
                .param("divertPercentage","50"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Divert percentage update successfully"));
    }
}
