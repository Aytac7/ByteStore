package com.example.startapp.mapper;

import com.example.startapp.dto.request.common.AdRequest;
import com.example.startapp.dto.response.common.AdResponse;
import com.example.startapp.entity.Ad;
import com.example.startapp.entity.Image;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
//
//@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface AdMapper {
//    Ad mapToEntity(AdRequest adRequest);
//    AdResponse mapToResponse(Ad ad);
//
//    default List<Image> mapMultipartFilesToImages(List<MultipartFile> files, Ad ad) {
//        if (files == null || files.isEmpty()) {
//            return null;
//        }
//        return files.stream()
//                .map(file -> {
//                    try {
//                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//                        String filePath = "/uploads/" + fileName; // Adjust based on your file storage logic
//
//                        Image image = new Image();
//                       image.setFileName(fileName);
//                        image.setFileType(file.getContentType());
//                        image.setFilePath(filePath);
//                        image.setUrl(filePath);     image.setAd(ad); // Set the Ad reference
//                        return image;
//                    } catch (Exception e) {
//                        throw new RuntimeException("Failed to process image file", e);
//                    }
//                })
//                .collect(Collectors.toList());
//    }
//
//    default List<String> mapImagesToUrls(List<Image> images) {
//        if (images == null || images.isEmpty()) {
//            return null;
//        }
//        return images.stream()
//                .map(Image::getUrl)
//                .collect(Collectors.toList());
//    }
//}
