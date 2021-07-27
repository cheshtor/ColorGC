package io.buyan.colorgc.common.utils;

import java.lang.management.ManagementFactory;
import java.net.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * 操作系统相关信息获取工具
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/20
 */
public class OSUtils {

    private static volatile String OS_NAME;
    private static volatile String HOST_NAME;
    private static volatile List<String> IPV4_LIST;
    private static volatile int PROCESS_NO = 0;

    /**
     * 获取操作系统名称
     * @return 操作系统名称
     */
    public static String getOsName() {
        if (OS_NAME == null) {
            OS_NAME = System.getProperty("os.name");
        }
        return OS_NAME;
    }

    /**
     * 获取主机名
     * @return 主机名
     */
    public static String getHostName() {
        if (HOST_NAME == null) {
            try {
                InetAddress host = InetAddress.getLocalHost();
                HOST_NAME = host.getHostName();
            } catch (UnknownHostException e) {
                HOST_NAME = "unknown";
            }
        }
        return HOST_NAME;
    }

    /**
     * 获取所有的 IPV4 地址
     * @return 所有的 IPV4 地址
     */
    public static List<String> getAllIPV4() {
        if (IPV4_LIST == null) {
            IPV4_LIST = new LinkedList<>();
            try {
                Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
                while (interfs.hasMoreElements()) {
                    NetworkInterface networkInterface = interfs.nextElement();
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress address = inetAddresses.nextElement();
                        if (address instanceof Inet4Address) {
                            String addressStr = address.getHostAddress();
                            if ("127.0.0.1".equals(addressStr)) {
                                continue;
                            } else if ("localhost".equals(addressStr)) {
                                continue;
                            }
                            IPV4_LIST.add(addressStr);
                        }
                    }
                }
            } catch (SocketException e) {

            }
        }
        return IPV4_LIST;
    }

    /**
     * 获取 IPV4 地址
     * @return IPV4 地址
     */
    public static String getIPV4() {
        final List<String> allIPV4 = getAllIPV4();
        if (allIPV4.size() > 0) {
            return allIPV4.get(0);
        } else {
            return "no-hostname";
        }
    }

    /**
     * 获取应用系统进程号
     * @return 应用系统进程号
     */
    public static int getProcessNo() {
        if (PROCESS_NO == 0) {
            try {
                PROCESS_NO = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            } catch (Exception e) {
                PROCESS_NO = -1;
            }
        }
        return PROCESS_NO;
    }

}
