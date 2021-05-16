package com.tango.services;

import com.tango.models.films.genre.Genre;
import com.tango.models.films.genre.GenreRepository;
import com.tango.models.films.tag.Tag;
import com.tango.models.films.tag.TagRepository;
import com.tango.models.roles.Role;
import com.tango.models.roles.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService {
    private final TagRepository tagRepository;
    private final GenreRepository genreRepository;
    private final RoleRepository roleRepository;

    public CommonService(@Autowired TagRepository tagRepository,
                         @Autowired GenreRepository genreRepository,
                         @Autowired RoleRepository roleRepository) {
        this.tagRepository = tagRepository;
        this.genreRepository = genreRepository;
        this.roleRepository = roleRepository;
    }

    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }
}
