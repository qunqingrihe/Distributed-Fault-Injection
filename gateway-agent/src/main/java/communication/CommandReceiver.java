package communication;

public interface CommandReceiver {
    /**
     * 接收并处理来自Web管理系统的指令。
     * @param command 接收到的指令字节
     */
    void receiveCommand(String command);
}
