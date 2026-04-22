package lt.linas_puplauskas.service;

import lt.linas_puplauskas.constants.AppClock;
import lt.linas_puplauskas.model.restaurant.Dish;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class PricingService {

    private static final double HAPPY_HOUR_DISCOUNT = 0.30;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public double getPrice(Dish dish, Restaurant restaurant) {
        if (isPeakHour(restaurant)) {
            return Math.round(dish.getPrice() * (1 - HAPPY_HOUR_DISCOUNT) * 100.0) / 100.0;
        }
        return dish.getPrice();
    }

    public boolean isPeakHour(Restaurant restaurant) {
        if (restaurant == null
                || restaurant.getHappyHoursStart() == null
                || restaurant.getHappyHoursEnd() == null) return false;

        try {
            LocalTime start = LocalTime.parse(restaurant.getHappyHoursStart(), TIME_FORMATTER);
            LocalTime end = LocalTime.parse(restaurant.getHappyHoursEnd(), TIME_FORMATTER);
            return !AppClock.NOW.isBefore(start) && !AppClock.NOW.isAfter(end);
        } catch (Exception e) {
            return false;
        }
    }
}