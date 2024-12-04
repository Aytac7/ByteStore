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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<AdDTO> getAdsByStatus(AdStatus status, Pageable pageable) {
        Page<Ad> ads = adRepository.findAdsByStatus(pageable, status);

        if (ads.isEmpty()) {
            System.out.println("No ads found for status: " + status);
            return Page.empty();
        }


        return ads.map(ad -> {

            return AdDTO.builder()
                    .id(ad.getId())
                    .userId(ad.getUser().getUserId())
                    .price(ad.getPrice())
                    .header(ad.getHeader())
                    .additionalInfo(ad.getAdditionalInfo())
                    .isNew(ad.getIsNew())
                    .categoryId(ad.getCategory().getId())
                    .brandId(ad.getBrand().getId())
                    .modelId(ad.getModel().getId())
                    .modelName(ad.getModel().getName())
                    .categoryName(ad.getCategory().getName())
                    .brandName(ad.getBrand().getName())
                    .imageUrls(ad.getImages().stream()
                            .map(Image::getImageUrl)
                            .collect(Collectors.toList()))
                    .phonePrefix(ad.getPhonePrefix())
                    .phoneNumber(ad.getPhoneNumber())
                    .status(ad.getStatus().toString())
                    .city(ad.getCity())
                    .build();
        });
    }

    public void deleteAd(Long adId) {
        adRepository.deleteById(adId);
    }

    public List<Feedbacks> getFeedbacks() {
        return feedbackRepository.findAll();
    }

}
