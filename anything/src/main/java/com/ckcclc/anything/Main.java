/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-14
 */

package com.ckcclc.anything;

import com.ckcclc.anything.json.PeopleRemark;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SocketException {
//        System.out.println(getLocalIPv4Address().getHostAddress());
//        System.out.println(Main.class.getResource("/").getFile());
//        System.out.println(Instant.now().toString());
//        System.out.println(new Date());
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        streamNull();
    }

    private static void utc() {
//        DateTime now = DateTime.now( DateTimeZone.UTC );
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

    private static void stream() {
        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);
    }

    private static void reflect() throws NoSuchMethodException {
        Class clz = PeopleRemark.class;
        Method method = clz.getMethod("toJSonString");
        System.out.println(method.getDeclaringClass());
        System.out.println(Object.class.equals(method.getDeclaringClass()));
    }

    private static void ipAddr() {
        // TODO Auto-generated method stub
        InetAddress ia = null;
        try {
            ia = ia.getLocalHost();

            String localname = ia.getHostName();
            String localip = ia.getHostAddress();
            System.out.println("local name" + localname);
            System.out.println("local ip" + localip);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void streamNull() {
        List<String> list = Arrays.asList("foo", "");
        System.out.println("foo and 'blank': " + list.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(",")));

        List<String> list2 = Arrays.asList("", "");
        System.out.println("'blank' and 'blank': " + list2.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(",")));

        List<String> list3 = Arrays.asList("foo", null);
        System.out.println("foo and 'null': " + list3.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(",")));

        List<String> list4 = Arrays.asList(null, null);
        System.out.println("'null' and 'null': " + list4.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(",")));

        String string = Stream.of(null, null, null, null).
                filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining(","));
        System.out.println(string);
        System.out.println(StringUtils.isNotBlank(string));
    }
}
