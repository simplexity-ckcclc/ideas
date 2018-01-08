/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.sense.core;

public interface Listener {

    default void onExecute(){
    }

    default void onUnExecutable(){
    }
}
