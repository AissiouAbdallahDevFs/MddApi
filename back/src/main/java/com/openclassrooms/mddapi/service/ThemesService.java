package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.Themes;
import com.openclassrooms.mddapi.repository.ThemesRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ThemesService {

    @Autowired
    private ThemesRepository themesRepository;

    // get all themes
    public Iterable<Themes> getThemes() {
        return themesRepository.findAll();
    }

    // get themes by id
    public Themes getThemesById(Long id) {

        return themesRepository.findById(id).orElse(null);
    }

    // save themes
    public Themes saveThemes(Themes themes) {
      
        return  themesRepository.save(themes);
    }

    // delete themes
    public void deleteThemes(Long id) {
        themesRepository.deleteById(id);
    }

    // update themes
    public Themes updateThemes(Themes themes) {
        return themesRepository.save(themes);
    }

}
