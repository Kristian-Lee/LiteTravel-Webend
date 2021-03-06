package com.LiteTravel.web.controller;

import com.LiteTravel.web.DTO.BlogDTO;
import com.LiteTravel.web.DTO.HotelDTO;
import com.LiteTravel.web.DTO.ResultVO;
import com.LiteTravel.web.service.BlogService;
import com.LiteTravel.web.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    HotelService hotelService;
    @Autowired
    BlogService blogService;

    @GetMapping(path = {"/index", "/index.html"})
    public String indexPage(ModelMap model){
        /*  获取推荐酒店信息 */
        ResultVO<HotelDTO> resultVO = hotelService.getHotels(1, 5);
        List<HotelDTO> hotelDTOs = resultVO.resultList;
        model.addAttribute("hotels", hotelDTOs);
        List<BlogDTO> blogDTOS = blogService.selectAll();
        model.addAttribute("blogs", blogDTOS);
        return "index";
    }
}
