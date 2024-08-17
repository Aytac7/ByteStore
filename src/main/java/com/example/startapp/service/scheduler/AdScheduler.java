package com.example.startapp.service.scheduler;

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
    private final AdService adService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void processOldAds(){
        LocalDateTime thirtyDaysAgo= LocalDateTime.now().minusDays(30);
        List<Ad> oldAds=adRepository.findAllByCreatedAtBeforeAndStatus(thirtyDaysAgo, AdStatus.APPROVED);

        for(Ad ad: oldAds){
            ad.setStatus(AdStatus.EXPIRED);
            ad.setStatusChangedAt(LocalDateTime.now());
            adRepository.save(ad);
        }
        if (!oldAds.isEmpty()) {
            notificationService.sendNotification("System", "The automatic deletion process has started", oldAds.size() + " ad status is set to EXPIRED.");
        }

        notificationService.sendNotification("System", "The automatic deletion process has finished", oldAds.size() + "ad status is set to EXPIRED.");
    }

@Scheduled(cron = "0 0 1 * * ?")
    public void deleteExpiredAds() {
        List<Ad> expiredAds = adService.getExpiredAds();

        if (!expiredAds.isEmpty()) {
            notificationService.sendNotification("System", "The automatic deletion process has started", expiredAds.size() + " the ad has been removed from the system.");
            adService.deleteExpired();
            notificationService.sendNotification("System", "The automatic deletion process has finished", expiredAds.size() + " the ad has been removed from the system.");
        } else {
            notificationService.sendNotification("System", "The automatic deletion process has finished", "There are no ads in EXPIRED status to delete.");
        }
    }
}
