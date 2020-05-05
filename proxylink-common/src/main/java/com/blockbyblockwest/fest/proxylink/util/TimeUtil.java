package com.blockbyblockwest.fest.proxylink.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

  public static boolean hasPassed(long past, TimeUnit unit, long amount) {
    return hasPassed(past, System.currentTimeMillis(), unit, amount);
  }

  public static boolean hasPassed(long past, long current, TimeUnit unit, long amount) {
    return current - past >= unit.toMillis(amount);
  }

  public static boolean hasPassed(long past, Duration duration) {
    return hasPassed(past, System.currentTimeMillis(), duration);
  }

  public static boolean hasPassed(long past, long current, Duration duration) {
    return current - past >= duration.toMillis();
  }

  public static long diff(long first, long second) {
    return Math.abs(first - second);
  }

  public static long diffToCurrent(long input) {
    return System.currentTimeMillis() - input;
  }

  private static final ChronoUnit[] DURATION_ACCURACIES = {ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS,
      ChronoUnit.MILLIS};

  public static Duration stripAccuracy(Duration duration, int targetAccuracy, ChronoUnit smallestUnit) {
    int accuracy = 0;
    Duration durationLeft = duration;

    for (ChronoUnit unit : DURATION_ACCURACIES) {
      long current;
      switch (unit) {
        case DAYS:
          current = durationLeft.toDays();
          break;
        case HOURS:
          current = durationLeft.toHours();
          break;
        case MINUTES:
          current = durationLeft.toMinutes();
          break;
        case SECONDS:
          current = durationLeft.getSeconds();
          break;
        case MILLIS:
          current = durationLeft.toMillis();
          break;
        default:
          current = 0;
          break;
      }

      if (current > 0 && (++accuracy > targetAccuracy || unit.getDuration().minus(smallestUnit.getDuration()).isNegative())) {
        duration = duration.minus(current, unit);
      }
      durationLeft = durationLeft.minus(current, unit);
    }

    return duration;
  }

}