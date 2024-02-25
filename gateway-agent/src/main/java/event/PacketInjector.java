package event;

import java.io.IOException;

public class PacketInjector {

    public static void injectFaultyPacket() {
        try {
            // 构建并执行 Python 脚本
            String script = "import scapy.all as scapy\n" +
                    "packet = scapy.IP(dst=\"192.168.0.1\")/scapy.ICMP()/'Hello'\n" +
                    "scapy.send(packet, verbose=0)";
            Process process = Runtime.getRuntime().exec("python -c \"" + script + "\"");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
