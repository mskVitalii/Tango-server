package com.tango.controllers;

import com.tango.models.films.genre.Genre;
import com.tango.models.films.tag.Tag;
import com.tango.models.roles.Role;
import com.tango.services.CommentService;
import com.tango.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/common")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CommonController {
    private final CommonService commonService;

    @Autowired
    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("tags")
    public List<Tag> getTags() {
        return commonService.getTags();
    }

    @GetMapping("genres")
    public List<Genre> getGenres() {
        return commonService.getGenres();
    }

    @GetMapping("roles")
    public List<Role> getRoles() {
        return commonService.getRoles();
    }
}
