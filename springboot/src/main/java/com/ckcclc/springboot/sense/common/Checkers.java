/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-8
 */

package com.ckcclc.springboot.sense.common;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * This class is to check parameters
 * @see com.google.common.base.Predicates
 */
public class Checkers {

    private static final Logger logger = LoggerFactory.getLogger(Checkers.class);

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

    public static <T> Predicate<T> notNull() {
        return Objects::nonNull;
    }

    public static <T> Predicate<T> in(Collection<T> collection) {
        return input -> collection != null && collection.contains(input);
    }

    public static class StringChecker {
        public static Predicate<String> isNotBlank() {
            return StringUtils::isNotBlank;
        }
    }

    public static class FloatChecker {
        public static Predicate<Float> between(float lowerBound, float upperBound) {
            return input -> input != null && input >= lowerBound && input <= upperBound;
        }
    }

    public static class ListChecker {
        public static Predicate<List> isNotEmpty() {
                return input -> input != null && !input.isEmpty();
        }
    }

    public static class DateChecker {
        public static Predicate<String> isValid(String format) {
            return input -> {
                DateFormat df = new SimpleDateFormat(format);
                try {
                    df.parse(input);
                } catch (Exception e) {
                    return false;
                }
                return true;
            };
        }

        public static Predicate<Date> before(Date other) {
            return input -> input != null && other != null && input.before(other);
        }
    }

    public static class CronChecker {
        public static Predicate<String> isValidQuartz() {
            return input -> {
                CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
                try {
                    Cron quartzCron = parser.parse(input);
                    quartzCron.validate();
                } catch (Exception e) {
                    return false;
                }
                return true;
            };
        }
    }

    public <T> Checkers add(T object, Predicate<T> predicate) {
        return this.add(object, predicate, null);
    }

    public <T> Checkers add(T object, Predicate<T> predicate, String errorMsg) {
        return this.addHolder(object, predicate, errorMsg);
    }

    /**
     * check parameters
     * @return true if check parameters pass
     */
    public Result check() {
        PredicateHolder holder = holderHead;
        while (holder != null) {
            if (holder.predicate != null && !holder.predicate.test(holder.object)) {
                return new Result(holder.errorMsg);
            }
            holder = holder.next;
        }
        return new Result();
    }

    private PredicateHolder addHolder() {
        PredicateHolder holder = new PredicateHolder();
        this.holderTail = this.holderTail.next = holder;
        return holder;
    }

    private <T> Checkers addHolder(T object, Predicate predicate, String errorMsg) {
        PredicateHolder holder = this.addHolder();
        holder.object = object;
        holder.errorMsg = errorMsg;
        holder.predicate = predicate;
        return this;
    }

    private final class PredicateHolder<T> {
        T object;
        String errorMsg;
        Predicate predicate;
        PredicateHolder next;

        private PredicateHolder() {
        }
    }

    public class Result {
        boolean valid;
        String errorMsg;

        Result() {
            this.valid = true;
        }

        Result(String errorMsg) {
            this.valid = false;
            this.errorMsg = errorMsg;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

}

