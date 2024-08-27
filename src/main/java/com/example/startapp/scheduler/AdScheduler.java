package com.example.startapp.scheduler;

import com.example.startapp.entity.Ad;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.repository.common.AdRepository;
import com.example.startapp.service.common.AdService;
import com.example.startapp.service.common.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class AdScheduler {
    private final AdRepository adRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void processOldAds() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Ad> oldAds = adRepository.findAllByCreatedAtBefore(thirtyDaysAgo);

        if (!oldAds.isEmpty()) {
            adRepository.deleteAll(oldAds);
            notificationService.sendNotification("System", "The automatic deletion process has finished", oldAds.size() + " ads have been deleted.");
        }
    }
}

