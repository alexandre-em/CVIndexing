package com.indexation.cv.service;

import com.indexation.cv.Exception.TagException;
import com.indexation.cv.data.Tags;
import com.indexation.cv.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagsService {
    @Autowired
    TagsRepository tagsRepository;

    public Tags getTagsById(String id) throws TagException {
        Optional<Tags> tag = tagsRepository.findById(id);
        if (tag.isPresent()) return tag.get();
        throw new TagException("Tag not found");
    }

    public Tags getTagsByCvId(String cvId) throws TagException {
        List<Tags> tag = tagsRepository.findTagsByCvId(cvId);
        if(tag.size()<=0) throw new TagException("This CV is not tagged");
        return tag.get(0);
    }

    public List<Tags> getTagsByKeywords(String keyword, boolean matchAll) throws TagException {
        List<Tags> result;
        if (!matchAll)
            result = tagsRepository.findTagsByKeyword(keyword);
        else {
            List<String> tags = Arrays.asList(keyword.split(","));
            result = tagsRepository.findTagsByTags(tags);
        }
        if(result.size()<=0) throw new TagException("CV matching the tags not found");
        return result;
    }

    public Tags createTags(String cvId, List<String> tags) throws TagException {
        List<Tags> cvTags = tagsRepository.findTagsByCvId(cvId);
        if(cvTags.size()>=1) throw new TagException("This CV is already tagged");
        if (tags == null) return tagsRepository.save(new Tags(cvId, new ArrayList<>()));
        return tagsRepository.save(new Tags(cvId, tags));
    }

    public Tags updateTagsByCvId(String cvId, List<String> tags) throws TagException {
        Tags cvTags = getTagsByCvId(cvId);
        cvTags.getTags().addAll(
                tags.stream().filter(val -> !cvTags.getTags().contains(val))
                        .collect(Collectors.toList()));
        return tagsRepository.save(cvTags);
    }
}
