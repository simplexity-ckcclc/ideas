/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-8
 */

package com.ckcclc.anything.checker;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.function.Predicate;

/**
 * {@link <code>com.google.common.base.Predicates</code>}
 */
public class Checkers {

    private PredicateHolder holderHead;
    private PredicateHolder holderTail;

    private Checkers() {
        this.holderHead = new PredicateHolder();
        this.holderTail = this.holderHead;
    }

    public static Checkers newInstance() {
        return new Checkers();
    }

    public static <T> Predicate<T> and(Predicate<? super T>... components) {
        if (components.length == 0) return null;
        Predicate predicate = components[0];
        for (int i = 1; i < components.length; i++) {
            predicate = predicate.and(components[i]);
        }
        return predicate;
    }

    public static <T> Predicate<T> or(Predicate<? super T>... components) {
        if (components.length == 0) return null;
        Predicate predicate = components[0];
        for (int i = 1; i < components.length; i++) {
            predicate = predicate.or(components[i]);
        }
        return predicate;
    }

    static class StringChecker {
        public static Predicate<String> isNotBlank() {
            return StringUtils::isNotBlank;
        }

        public static Predicate<String> in(List<String> list) {
            return input -> list.contains(input);
        }
    }

    static class FloatChecker {
        public static Predicate<Float> between(float lowerBound, float upperBound) {
            return input -> input != null && input >= lowerBound && input <= upperBound;
        }
    }

    static class ListChecker {
        public static Predicate<List> isNotEmpty() {
                return input -> !input.isEmpty();
        }
    }

    public <T> Checkers add(T object, Predicate<T> predicate) {
        return this.addHolder(object, predicate);
    }

    public boolean check() {
        PredicateHolder holder = holderHead;
        while (holder != null) {
            if (holder.predicate != null && !holder.predicate.test(holder.object)) {
                return false;
            }
            holder = holder.next;
        }
        return true;
    }

    private PredicateHolder addHolder() {
        PredicateHolder holder = new PredicateHolder();
        this.holderTail = this.holderTail.next = holder;
        return holder;
    }

    private <T> Checkers addHolder(T object, Predicate predicate) {
        PredicateHolder holder = this.addHolder();
        holder.object = object;
        holder.predicate = predicate;
        return this;
    }

    private final class PredicateHolder<T> {
        T object;
        Predicate predicate;
        PredicateHolder next;

        private PredicateHolder() {
        }
    }
}
