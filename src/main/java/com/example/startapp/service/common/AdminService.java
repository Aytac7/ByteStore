package com.example.startapp.service.common;

import com.example.startapp.dto.response.common.AdDTO;
import com.example.startapp.entity.common.Feedbacks;
import com.example.startapp.entity.common.Ad;
import com.example.startapp.entity.auth.User;
import com.example.startapp.entity.common.Image;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdRepository adRepository;
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

    public List<AdDTO> getAdsByStatus(AdStatus status) {
        List<Ad> ads = adRepository.findAdsByStatus(status);
        return ads.stream()
                .map(this::convertToAdDTO)
                .collect(Collectors.toList());
    }

    private AdDTO convertToAdDTO(Ad ad) {
        return AdDTO.builder()
                .id(ad.getId())
                .price(ad.getPrice())
                .header(ad.getHeader())
                .additionalInfo(ad.getAdditionalInfo())
                .isNew(ad.getIsNew())
                .userId(ad.getUser().getUserId())
                .categoryId(ad.getCategory().getId())
                .brandId(ad.getBrand().getId())
                .modelId(ad.getModel().getId())
                .city(ad.getCity())
                .imageUrls(ad.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .phonePrefix(ad.getPhonePrefix())
                .phoneNumber(ad.getPhoneNumber())
                .status(ad.getStatus().toString())
                .build();
    }

    public void deleteAd(Long adId) {
        adRepository.deleteById(adId);
    }

    public List<Feedbacks> getFeedbacks(){
        return feedbackRepository.findAll();
    }

}
