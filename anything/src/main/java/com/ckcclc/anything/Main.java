/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-14
 */

package com.ckcclc.anything;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SocketException {
//        System.out.println(getLocalIPv4Address().getHostAddress());
        System.out.println(Main.class.getResource("/").getFile());
    }

    public static InetAddress getLocalIPv4Address() throws SocketException {
        Enumeration<?> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress ip = null;
        if (interfaces != null) {
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
                logger.debug("NI: " + ni.getName() + ", " + ni.getDisplayName());
                if (ni.isLoopback() || !ni.isUp())
                    continue;
                Enumeration<?> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && (ip instanceof Inet4Address)) {
                        logger.debug("IP addr: " + ip.getHostAddress());
                        break;
                    }
                }
                if (ip != null) break;
            }
        }
        return ip;
    }
}
