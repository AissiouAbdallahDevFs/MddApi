package com.openclassrooms.mddapi.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.service.ThemesService;
import com.openclassrooms.mddapi.model.Themes;
import java.util.List;
import java.util.HashMap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;




@RestController
@CrossOrigin
@RequestMapping("/api")
@Api(tags = "Themes", description = "Operations related to Themes")
public class ThemesController {

    @Autowired
    private ThemesService themesService;


    // get all themes
    @GetMapping("/themes")
    @ApiOperation(value = "Get all themes", notes = "Returns a list of all themes.")
    public HashMap<String, List<Themes>> getThemes() {
        List<Themes> themesList = (List<Themes>) themesService.getThemes();
        HashMap<String, List<Themes>> response = new HashMap<String, List<Themes>>();
        response.put("themes", themesList);
        return response;
    }
    
    // get theme by id
    @GetMapping("/theme/{id}")
    @ApiOperation(value = "Get theme by ID", notes = "Returns a theme by its ID.")
    public Themes getThemeById(Long id) {
        return themesService.getThemesById(id);
    }
    

    // save theme
    @PostMapping("/themes")
    @ApiOperation(value = "Create a new theme", notes = "Creates a new theme.")
    public Themes saveThemes(Themes themes) {
        Themes savedThemes = themesService.saveThemes(themes);
        return savedThemes;
    }
}
