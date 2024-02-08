package communication;
import core.ProjectEnvironmentProxyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCommandReceiver implements CommandReceiver {

    private final ProjectEnvironmentProxyHandler proxyHandler;

    @Autowired
    public DefaultCommandReceiver(ProjectEnvironmentProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    @Override
    public void receiveCommand(String command) {
        // 命令格式: "serviceType=ratio"
        String[] parts = command.split("=");
        if (parts.length == 2) {
            String serviceType = parts[0];
            double ratio;
            try {
                ratio = Double.parseDouble(parts[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid command ratio value", e);
            }
            proxyHandler.updateTrafficRatio(serviceType, ratio);
        } else {
            throw new IllegalArgumentException("Invalid command format");
        }
    }
}

