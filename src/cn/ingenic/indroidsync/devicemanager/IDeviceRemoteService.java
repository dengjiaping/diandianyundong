package cn.ingenic.indroidsync.devicemanager;

public interface IDeviceRemoteService {
    String DESPRITOR = "DeviceRemoteService";

    String request(int cmd, String url);
}
