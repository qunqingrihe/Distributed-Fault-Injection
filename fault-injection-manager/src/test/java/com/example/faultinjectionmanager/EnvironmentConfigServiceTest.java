package com.example.faultinjectionmanager;

import com.example.faultinjectionmanager.entity.EnvironmentConfigEntity;
import com.example.faultinjectionmanager.repository.EnvironmentConfigRepository;
import com.example.faultinjectionmanager.service.EnvironmentConfigService;
import config.EnvironmentConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class EnvironmentConfigServiceTest {
    @Mock
    private EnvironmentConfigRepository configRepository;
    @InjectMocks
    private EnvironmentConfigService configService;

    @Test
    void updateDivertPercentage_ValidPercentage_Success(){
        EnvironmentConfigEntity config=new EnvironmentConfigEntity(1L,50);
        when(configRepository.save(any(EnvironmentConfigEntity.class))).thenReturn(config);

        configService.updateDivertPercentage(50);

        verify(configRepository).save(any(EnvironmentConfigEntity.class));
    }

    @Test
    void updateDivertPercentage_InvalidPercentage_ThrowsException(){
        IllegalArgumentException thrown=org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                ()->configService.updateDivertPercentage(-1),
                "Expected updateDivertPercentage to throw, but it didn't"
        );

        org.junit.jupiter.api.Assertions.assertTrue(thrown.getMessage().contains("Divert percentage must be between 0 and 100"));
    }
}
