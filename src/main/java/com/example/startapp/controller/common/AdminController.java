package com.example.startapp.controller.common;

import com.example.startapp.entity.Ad;
import com.example.startapp.entity.User;
import com.example.startapp.exception.EmptyRejectionException;
import com.example.startapp.service.common.AdService;
import com.example.startapp.service.common.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    //    @PutMapping("/ads/{adId}/approve")
//    public ResponseEntity<String> approveAd(@PathVariable Long adId, @AuthenticationPrincipal User admin) {
//        adminService.approveAd(adId, admin.getUserId());
//        return ResponseEntity.ok("Ad approved successfully");
//    }\
    @PutMapping("/ads/{adId}/approve")
    public ResponseEntity<String> approveAd(@PathVariable Long adId,
                                            @AuthenticationPrincipal User admin) {
        try {
            adminService.approveAd(adId, admin.getUserId());
            return ResponseEntity.ok("Ad approved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error approving ad: " + e.getMessage());
        }
    }

    @PutMapping("/ads/{adId}/reject")
    public ResponseEntity<String> rejectAd(@PathVariable Long adId,
                                           @RequestParam String rejectionReason,
                                           @AuthenticationPrincipal User admin) throws EmptyRejectionException {
        try {
            adminService.rejectAd(adId, admin.getUserId(), rejectionReason);
            return ResponseEntity.ok("Ad rejected successfully with reason: " + rejectionReason);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error rejecting ad: " + e.getMessage());
        }
    }

    @GetMapping("/pendingAds")
    public ResponseEntity<List<Ad>> getPendingAds() {
        List<Ad> pendingAds = adminService.getPendingAds();
        return ResponseEntity.ok(pendingAds);
    }


    @GetMapping("/rejectedAds")
    public ResponseEntity<List<Ad>> getRejectedAds() {
        List<Ad> rejectedAds = adminService.getRejectedAds();
        return ResponseEntity.ok(rejectedAds);
    }

    @GetMapping("/approvedAds")
    public ResponseEntity<List<Ad>> getApprovedAds() {
        List<Ad> approvedAds = adminService.getApprovedAds();
        return ResponseEntity.ok(approvedAds);
    }
}
