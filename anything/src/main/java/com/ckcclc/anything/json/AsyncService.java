/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-11
 */

package com.ckcclc.anything.json;

import java.util.concurrent.TimeUnit;

public class AsyncService {

    public void async() {
        new Thread(() -> {
            System.out.println("start thread");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("finish thread");
        }).start();
    }
}
