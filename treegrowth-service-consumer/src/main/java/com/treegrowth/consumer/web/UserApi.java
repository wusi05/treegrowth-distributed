package com.treegrowth.consumer.web;

import com.treegrowth.consumer.excelexport.ExcelViewBuilder;
import com.treegrowth.consumer.security.userdetails.TgUserDetails;
import com.treegrowth.consumer.service.UserService;
import com.treegrowth.consumer.vo.PureUser;

import com.treegrowth.model.bo.AmendedUser;
import com.treegrowth.model.bo.UserDetailBasic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("api/users")
public class UserApi {

    @Autowired
    private UserService userService;

    @RequestMapping(method = POST)
    public UserDetailBasic create(@Valid @RequestBody PureUser pureUser) {
        return userService.create(pureUser.convert());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@PathVariable("id") String userId) {
        userService.delete(userId);
    }


    @RequestMapping(value = "/{id}", method = PUT)
    public UserDetailBasic update(@PathVariable("id") String userId, @RequestBody AmendedUser amendedUser) {
        return userService.update(userId, amendedUser);
    }

    //@PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", method = GET)
    public UserDetailBasic get(@AuthenticationPrincipal TgUserDetails tgUserDetails, @PathVariable("id") String userId) {
        return userService.get(userId, userId);
    }

    @RequestMapping(value = "/{id}/excel", method = GET)
    public ModelAndView getExcel(@AuthenticationPrincipal TgUserDetails tgUserDetails, @PathVariable("id") String userId) {
        UserDetailBasic userDetailBasic = userService.get(tgUserDetails.getId(), userId);
        // fill in data to model
        Map<String, Object> model = new HashMap<>();
        model.put("email", userDetailBasic.getEmail());
        ExcelViewBuilder excelViewBuilder = new ExcelViewBuilder();
        return new ModelAndView(excelViewBuilder, model);
    }
}
