package com.ckcclc.anything.trial;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by ckcclc on 18/08/2017.
 */
public class Trial {

    public static void main(String[] args) {
//        Solution solution = new Solution();
//
//        int[] A = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
//        System.out.println(solution.maxSubArray2(A));
//
//        String B = "abcdcb";
//        System.out.println(solution.longestPalindrome(B));

        int a = 1;
        long b = 2L;
        String c = "3";
        System.out.println(String.valueOf(a));
        System.out.println(String.valueOf(b));
        System.out.println(String.valueOf(c));
        System.out.println(String.valueOf(null));

    }


}

class Solution {
    public int maxSubArray(int[] A) {
        int max = A[0];
        int[] sum = new int[A.length];
        sum[0] = A[0];

        for (int i = 1; i < A.length; i++) {
            sum[i] = Math.max(A[i], sum[i - 1] + A[i]);
            max = Math.max(max, sum[i]);
        }

        return max;
    }

    public int maxSubArray2(int[] A) {
        int newsum=A[0];
        int max=A[0];
        for(int i=1;i<A.length;i++){
            newsum=Math.max(newsum+A[i],A[i]);
            max= Math.max(max, newsum);
        }
        return max;
    }

    public String longestPalindrome(String s) {
        if (s.isEmpty()) {
            return null;
        }

        if (s.length() == 1) {
            return s;
        }

        String longest = s.substring(0, 1);
        for (int i = 0; i < s.length(); i++) {
            // get longest palindrome with center of i
            String tmp = helper(s, i, i);
            if (tmp.length() > longest.length()) {
                longest = tmp;
            }

            // get longest palindrome with center of i, i+1
            tmp = helper(s, i, i + 1);
            if (tmp.length() > longest.length()) {
                longest = tmp;
            }
        }

        return longest;
    }

    // Given a center, either one letter or two letter, Find longest palindrome
    public String helper(String s, int begin, int end) {
        while (begin >= 0 && end <= s.length() - 1 && s.charAt(begin) == s.charAt(end)) {
            begin--;
            end++;
        }
        return s.substring(begin + 1, end);
    }



    public void set(String key, String value) {
        excute(jedis -> jedis.set(key, value));
    }



    public void excute(Action action) {
        Jedis jedis = new JedisPool().getResource();
        try {
            action.act(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
    }

    public <T> T excuteReturn(ActionReturn<T> actionReturn) {
        Jedis jedis = new JedisPool().getResource();
        try {
            return actionReturn.act(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

}

interface Action {
    void act(Jedis jedis);
}


interface ActionReturn<T> {
    T act(Jedis jedis);
}