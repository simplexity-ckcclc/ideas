/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-8
 */

package com.ckcclc.anything.checker;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        boolean check = Checkers.newInstance()
                .add("hello", Checkers.and(Checkers.StringChecker.isNotBlank(),
                        Checkers.StringChecker.in(Arrays.asList("hello", "world"))))
                .add(0.01f, Checkers.FloatChecker.between(0.0f, 0.5f))
                .add(Arrays.asList(1, 2), Checkers.ListChecker.isNotEmpty())
                .check();

        System.out.println(check);
    }
}
