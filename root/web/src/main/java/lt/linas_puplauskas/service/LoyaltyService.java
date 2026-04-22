package lt.linas_puplauskas.service;

import lt.linas_puplauskas.constants.AppClock;
import lt.linas_puplauskas.model.client.Client;
import lt.linas_puplauskas.model.order.Order;
import lt.linas_puplauskas.model.restaurant.Restaurant;
import lt.linas_puplauskas.service.user.WebClientService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class LoyaltyService {

    private static final double POINTS_PER_EURO = 1.0;
    private static final double PEAK_MULTIPLIER = 2.0;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final WebClientService webClientService;

    public LoyaltyService(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    public void awardPoints(Client client, Order order, Restaurant restaurant) {
        if (client == null || order == null) return;

        int points = calculatePoints(order.getTotalPrice(), restaurant);
        client.setBonusPoints(client.getBonusPoints() + points);
        webClientService.save(client);
    }

    public void redeemPoints(Client client, int pointsToRedeem) {
        if (client == null) return;
        if (pointsToRedeem > client.getBonusPoints()) return;

        client.setBonusPoints(client.getBonusPoints() - pointsToRedeem);
        webClientService.save(client);
    }

    public int calculatePoints(double orderTotal, Restaurant restaurant) {
        double multiplier = isPeakHour(restaurant) ? PEAK_MULTIPLIER : POINTS_PER_EURO;
        return (int) Math.floor(orderTotal * multiplier);
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