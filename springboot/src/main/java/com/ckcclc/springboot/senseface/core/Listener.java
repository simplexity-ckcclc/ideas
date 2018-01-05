/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.senseface.core;

public interface Listener {

    default void onExecute(){
    }

    default void onUnExecutable(){
    }
}
