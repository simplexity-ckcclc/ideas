/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-15
 */

package com.ckcclc.anything;

import java.net.InetAddress;

public class Test {

    public static void main(String[] args) {
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
}
