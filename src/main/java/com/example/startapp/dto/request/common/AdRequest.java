package com.example.startapp.dto.request.common;

import com.example.startapp.entity.Brand;
import com.example.startapp.entity.Category;
import com.example.startapp.entity.Image;
import com.example.startapp.entity.Model;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.enums.PhonePrefix;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdRequest {

    @NotNull
    private Long price;

    @NotNull
    private String header;

    @Size(max = 700)
    @NotNull
    private String additionalInfo;

    @NotNull
    private Boolean isNew;

    @NotNull
    private Integer userId;

    @NotNull
    private Category category_id;

    @NotNull
    private Brand brand_id;

    @NotNull
    private Model model_id;

    @NotNull
    private List<Image> images;

    @NotNull
    private PhonePrefix phonePrefix;

    @NotNull
    private String phoneNumber;
}
