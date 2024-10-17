package com.example.startapp.service.common;

import com.example.startapp.entity.common.Feedbacks;
import com.example.startapp.entity.common.Ad;
import com.example.startapp.entity.auth.User;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.enums.UserRole;
import com.example.startapp.exception.AdNotFoundException;
import com.example.startapp.exception.UnauthorizedException;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.repository.common.AdRepository;
import com.example.startapp.exception.EmptyRejectionException;
import com.example.startapp.repository.common.FeedbacksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final FeedbacksRepository feedbackRepository;


    public void approveAd(Long adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Invalid ad ID"));

        ad.setStatus(AdStatus.APPROVED);
        ad.setStatusChangedAt(LocalDateTime.now());
        adRepository.save(ad);
    }

    public void rejectAd(Long adId, String rejectionReason) throws EmptyRejectionException {
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new EmptyRejectionException("Rejection reason must not be empty");
        }

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Invalid ad ID"));


        ad.setStatus(AdStatus.REJECTED);
        ad.setRejectionReason(rejectionReason);
        ad.setStatusChangedAt(LocalDateTime.now());

        adRepository.save(ad);
    }

    public List<Ad> getPendingAds() {
        return adRepository.findAllByStatus(AdStatus.PENDING);
    }

    public List<Ad> getRejectedAds() {
        return adRepository.findAllByStatus(AdStatus.REJECTED);
    }

    public List<Ad> getApprovedAds() {
        return adRepository.findAllByStatus(AdStatus.APPROVED);
    }
    public List<Feedbacks> getFeedbacks(){
        return feedbackRepository.findAll();
    }

}
