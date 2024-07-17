package com.fof.server.service;

import com.fof.server.repository.ListingImagesRepository;
import com.fof.server.model.normal.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    public final ListingImagesRepository listingImagesRepository;

    public ResponseDTO updateListingImages() {
        return ResponseDTO.builder()
                .status("success")
                .message("Listing images updated successfully")
                .build();
    }
}
