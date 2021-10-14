package com.indexation.cv.service;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.repository.CVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CVService {
    @Autowired
    private CVRepository cvRepository;

    public List<CVModel> searchCV(String keyword) {
        return cvRepository.search(keyword);
    }

}
