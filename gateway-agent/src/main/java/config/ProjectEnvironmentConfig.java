package config;

import core.EnvironmentBehavior;
import core.ProjectEnvironmentBehavior;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("project")
public class ProjectEnvironmentConfig {

    @Bean
    public EnvironmentBehavior projectEnvironmentBehavior(EnvironmentConfig config, @Qualifier("testEnvironmentChannel") Channel testEnvironmentChannel, @Qualifier("originalTargetChannel") Channel originalTargetChannel) {
        return new ProjectEnvironmentBehavior(config, testEnvironmentChannel, originalTargetChannel);
    }
}
