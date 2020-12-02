package com.LiteTravel.web.controller;

import com.LiteTravel.web.DTO.*;
import com.LiteTravel.web.service.HotelService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HotelController {

    @Autowired
    public HotelService hotelService;

    /* todo 酒店列表实际上用了两个接口来接受两个不同的请求, 冗余了, 试试可不可以更加尽可能的重用 */
    /* 默认第一页 */
    @GetMapping("/hotels")
    public String HotelList(ModelMap model){
        setPageHotel(1, model);
        return "hotels";
    }
    /* 点击页面数切换 分页显示酒店列表 */
    @GetMapping("/hotels/{page}")
    public String HotelPage(@PathVariable("page") Integer page, ModelMap model){
        setPageHotel(page, model);
        return "hotels";
    }
    /* 使用PageHelper获得并设置 分页数据 */
    private void setPageHotel(Integer page, ModelMap model){
        /* 向service层分发请求处理 */
        List<HotelDTO> hotels = hotelService.getHotels(page, 6);
        /* 分页信息类
        * 参数1：数据集合
        * 参数2：需要展示的最大导航页数*/
        PageInfo<HotelDTO> info = new PageInfo<>(hotels, 5);
        /* 设置筛选页面的筛选项目为Hotel */
//        model.addAttribute("category", "hotel");
        /* 放入数据 */
        /* 放入hotel列表数据 */
        model.addAttribute("hotels", hotels);
        /* 放入页面信息数据 */
        model.addAttribute("pageInfo", info);
    }

    @GetMapping("/hotel/{hotelId}")
    public String Hotel(@PathVariable("hotelId") Integer hotelId, ModelMap model){
        /* 获取酒店基本信息 */
        HotelDTO hotel = hotelService.selectHotelById(hotelId, true);

        /* todo 获取酒店具体介绍数据 */
        /* done 获取房间块展示数据 */
        /* todo 获取房间可折叠展示块信息 */

        List<HotelDTO> relatedHotels = hotelService.getHotels(hotelId, 1, 3);
        /* 设置酒店基本信息数据 */
        model.addAttribute("hotel", hotel);
        /* todo 设置酒店具体介绍数据 */
        /* done 设置房间块展示数据 */
        /* todo 设置房间可折叠展示块信息 */

        /* 设置推荐酒店基本信息数据 */
        /* todo 设计推荐算法 */
        model.addAttribute("hotels", relatedHotels);

        return "hotel-single";
    }

    @PostMapping("/hotel/book")
    @Transactional
    public String bookHotel(@RequestParam("hotelId") Integer hotelId,
                            @RequestParam("roomId") Integer roomId,
                            @RequestParam("userId") Integer userId,
                            @RequestParam("checkIn") String checkIn,
                            @RequestParam("checkOut") String checkOut,
                            @RequestParam("roomCount") Integer roomCount,
                            @RequestParam("price") float price, ModelMap model) throws ParseException {
        HotelOrderSubmitDTO hotelOrderSubmitDTO = new HotelOrderSubmitDTO();
        hotelOrderSubmitDTO.setHotelId(hotelId);
        hotelOrderSubmitDTO.setUserId(userId);
        hotelOrderSubmitDTO.setHotel(hotelService.selectHotelById(hotelId, false));
        hotelOrderSubmitDTO.setRooms(hotelService.getRoomDTObyIds(Collections.singletonList(roomId)));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        Date checkInDate = dateFormat.parse(checkIn);
        Date checkOutDate = dateFormat.parse(checkOut);
        hotelOrderSubmitDTO.setCheckIn(checkInDate);
        hotelOrderSubmitDTO.setCheckOut(checkOutDate);
        hotelOrderSubmitDTO.setPrice(price);
        Integer days = (int)((checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24));//计算时间
        hotelOrderSubmitDTO.setDays(days);
        hotelOrderSubmitDTO.setPrice(price);
        hotelOrderSubmitDTO.setRoomCount(roomCount);
        hotelOrderSubmitDTO.setTotal(price * days * roomCount);
//        System.out.println(hotelOrderSubmitDTO.toString());
        model.addAttribute("hotelOrder", hotelOrderSubmitDTO);
        return "hotel-order";
    }
}
