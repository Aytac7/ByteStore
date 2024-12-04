package com.example.startapp.controller.common;

import com.example.startapp.dto.response.common.AdDTO;
import com.example.startapp.entity.common.Ad;
import com.example.startapp.entity.auth.User;
import com.example.startapp.entity.common.Feedbacks;

import com.example.startapp.enums.AdStatus;
import com.example.startapp.exception.EmptyRejectionException;
import com.example.startapp.service.common.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/{adId}/approve")
    public ResponseEntity<String> approveAd(@PathVariable Long adId) {

        try {
            adminService.approveAd(adId);
            return ResponseEntity.ok("Ad approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error approving ad: " + e.getMessage());

        }
    }

    @PutMapping("/{adId}/reject")
    public ResponseEntity<String> rejectAd(@PathVariable Long adId,
                                           @RequestParam String rejectionReason) throws EmptyRejectionException {
        try {
            adminService.rejectAd(adId, rejectionReason);
            return ResponseEntity.ok("Ad rejected successfully with reason: " + rejectionReason);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error rejecting ad: " + e.getMessage());
        }
    }

    @GetMapping("/ads")
    public Page<AdDTO> getAdsByStatus(@RequestParam("status") AdStatus status, Pageable pageable) {
        return adminService.getAdsByStatus(status, pageable);
    }



    @GetMapping("/feedbacks")
    public ResponseEntity<List<Feedbacks>> getFeedbacks() {
        List<Feedbacks> feedbacks = adminService.getFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @DeleteMapping("/delete/{adId}")
    public ResponseEntity<String> deleteAd(@PathVariable Long adId) {
        adminService.deleteAd(adId);
        return ResponseEntity.ok("Ad deleted successfully");
    }

}
