package com.example.startapp.service.common;

import com.example.startapp.entity.Ad;
import com.example.startapp.entity.User;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.enums.UserRole;
import com.example.startapp.repository.UserRepository;
import com.example.startapp.repository.common.AdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public void approveAd(Long adId,Long adminId){
        Ad ad=adRepository.findById(adId).orElseThrow(() -> new IllegalArgumentException("Invalid ad ID"));

        User admin=userRepository.findById(adminId).orElseThrow(() -> new IllegalArgumentException("Invalid admin ID"));

        if(!admin.getRole().equals(UserRole.ADMIN)){
            throw new SecurityException("Only admins can approve ads");
        }

        ad.setStatus(AdStatus.APPROVED);
        ad.setStatusChangedAt(LocalDateTime.now());

        adRepository.save(ad);


    }

    public void rejectAd(Long adId, Long adminId, String rejectionReason) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ad ID"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin ID"));

        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new SecurityException("Only admins can reject ads");
        }

        ad.setStatus(AdStatus.REJECTED);
        ad.setRejectionReason(rejectionReason);
        ad.setStatusChangedAt(LocalDateTime.now());

        adRepository.save(ad);
    }
    public List<Ad> getPendingAds(){
       return adRepository.findAllByStatus(AdStatus.PENDING);
    }

    public List<Ad> getRejectedAds(){
        return adRepository.findAllByStatus(AdStatus.REJECTED);
    }

    public List<Ad> getApprovedAds(){
        return adRepository.findAllByStatus(AdStatus.APPROVED);
    }
}
