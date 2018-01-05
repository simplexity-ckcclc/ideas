/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.anything.cron;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.Weekdays;
import com.cronutils.parser.CronParser;

import static com.cronutils.model.field.expression.FieldExpressionFactory.*;

public class Main {

    public static void main(String[] args) {
        String cron = create();
        validate(cron);

    }

    private static String create() {

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
//                .withYear(always())
//                .withDoM(between(SpecialChar.L, 3))
                .withDoW(on(Weekdays.SUNDAY.getWeekday()).and(on(Weekdays.FRIDAY.getWeekday())))
                .withMonth(always())
//                .withDoM(between(SpecialChar.L, 3))
                .withDoM(questionMark())
                .withHour(always())
                .withMinute(every(2))
                .withSecond(on(0))
                .instance();

        // Obtain the string expression
        String cronAsString = cron.asString(); // 0 * * L-3 * ? *
        System.out.println(cronAsString);
        return cronAsString;
    }

    private static void validate(String cron) {
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
        Cron quartzCron = parser.parse(cron);
        quartzCron.validate();
    }
}

