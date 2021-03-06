package com.LiteTravel.web.service;

import com.LiteTravel.web.DTO.*;
import com.LiteTravel.web.Model.*;
import com.LiteTravel.web.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/*要将HotelOrderService整合进HotelService, 高强度耦合*/
@Service
public class HotelOrderService {

    @Autowired
    public HotelMapper hotelMapper;
    @Autowired
    public RoomMapper roomMapper;
    @Autowired
    public RoomBedMapMapper roomBedMapMapper;
    @Autowired
    public BedMapper bedMapper;
    @Autowired
    public HotelOrderMapper hotelOrderMapper;
    @Autowired
    public HotelOrderDetailMapper hotelOrderDetailMapper;
    @Autowired
    public RegionMapper regionMapper;

    public ResultVO<HotelOrderBlockDTO> getOrders(Integer page, Integer pageSize){
        return selectByExample(page, pageSize, new HotelOrderExample());
    }

    private ResultVO selectByExample(Integer page, Integer pageSize, HotelOrderExample hotelOrderExample) {
        /* 分页：
         * 参数1: 第几页
         * 参数2: 每页展示几个数据 */
        PageHelper.startPage(page, pageSize);
        List<HotelOrder> hotelOrders = hotelOrderMapper.selectByExample(hotelOrderExample);
        PageInfo<HotelOrder> info = new PageInfo<>(hotelOrders, 5);
        List<Integer> hotelIds = hotelOrders.stream().map(HotelOrder::getHotelId).distinct().collect(Collectors.toList());
        Map<Integer, String> hotelNameMap;
        Map<Integer, String> hotelUriMap;
        if (hotelIds.size() > 0){
            HotelExample hotelExample = new HotelExample();
            hotelExample.createCriteria()
                    .andHotelIdIn(hotelIds);
            List<Hotel> hotels = hotelMapper.selectByExample(hotelExample);
            hotelNameMap = hotels.stream().collect(Collectors.toMap(Hotel::getHotelId, Hotel::getHotelName));
            hotelUriMap = hotels.stream().collect(Collectors.toMap(Hotel::getHotelId, Hotel::getHotelImgUri));
        }else {
            hotelNameMap = new HashMap<>();
            hotelUriMap = new HashMap<>();
        }
        List<HotelOrderBlockDTO> data = hotelOrders.stream().map(hotelOrder -> {
            HotelOrderBlockDTO hotelOrderDTO = new HotelOrderBlockDTO();
            BeanUtils.copyProperties(hotelOrder, hotelOrderDTO);
            String hotelName = hotelNameMap.get(hotelOrder.getHotelId());;
            String hotelUri = hotelUriMap.get(hotelOrder.getHotelId());
            hotelOrderDTO.setHotelName(hotelName);
            hotelOrderDTO.setHotelImgUri(hotelUri);
            return hotelOrderDTO;
        }).collect(Collectors.toList());
        return new ResultVO(data, info);
    }


    public List<HotelOrderBlockDTO> selectAll(){
        return hotelOrderMapper.selectByExample(new HotelOrderExample()).stream().map(hotelOrder ->{
            HotelOrderBlockDTO hotelOrderDTO = new HotelOrderBlockDTO();
            BeanUtils.copyProperties(hotelOrder, hotelOrderDTO);
            return hotelOrderDTO;
        }).collect(Collectors.toList());
    }

    public HotelOrderInfoDTO getHotelOrderInfoById(Integer orderId){
        //最终返回的数据类型
        HotelOrderInfoDTO hotelOrderDTO = new HotelOrderInfoDTO();

        HotelOrder hotelOrder = hotelOrderMapper.selectByPrimaryKey(orderId);
        HotelOrderDetailExample hotelOrderDetailExample = new HotelOrderDetailExample();
        hotelOrderDetailExample.createCriteria()
                .andOrderIdEqualTo(orderId);
        List<HotelOrderDetail> hotelOrderDetails = hotelOrderDetailMapper.selectByExample(hotelOrderDetailExample);

        BeanUtils.copyProperties(hotelOrder, hotelOrderDTO);
        Hotel hotel = hotelMapper.selectByPrimaryKey(hotelOrder.getHotelId());
        HotelDTO hotelDTO = new HotelDTO();
        BeanUtils.copyProperties(hotel, hotelDTO);
        RegionExample regionExample = new RegionExample();
        regionExample.createCriteria()
                .andIdEqualTo(hotel.getHotelAddress());
        List<Region> regions = regionMapper.selectByExample(regionExample);
        if(regions.size() > 0){
            hotelDTO.setHotelAddressString(regions.get(0).getMername());
        }
        hotelOrderDTO.setHotel(hotelDTO);
        List<HotelOrderDetailDTO> hotelOrderDetailDTOs = hotelOrderDetails.stream().map(hotelOrderDetail -> {
            HotelOrderDetailDTO hotelOrderDetailDTO = new HotelOrderDetailDTO();
            BeanUtils.copyProperties(hotelOrderDetail, hotelOrderDetailDTO);
            /*获取房间信息和对应床型数据*/
            return hotelOrderDetailDTO;
        }).collect(Collectors.toList());
        hotelOrderDTO.setDetails(hotelOrderDetailDTOs);
        return hotelOrderDTO;
    }
    /*
    * 插入新的订单
    * */
    public int insertHotelOrder(HotelOrder hotelOrder, List<HotelOrderDetail> hotelOrderDetails){
        hotelOrderMapper.insert(hotelOrder);
        int insertId = hotelOrder.getOrderId();
        for (HotelOrderDetail hotelOrderDetail : hotelOrderDetails){
            hotelOrderDetail.setOrderId(insertId);
            hotelOrderDetailMapper.insert(hotelOrderDetail);
        }
        return insertId;
    }
    /*
    * 更新订单
    * */
    public int updateHotelOrder(HotelOrder hotelOrder){
        //支付日期
        Date pay_date = new Date();
        HotelOrderExample hotelOrderExample = new HotelOrderExample();
        hotelOrderExample.createCriteria()
                .andOrderIdEqualTo(hotelOrder.getOrderId())
                .andPayDateIsNull()
                .andStatusEqualTo("0");
        hotelOrder.setPayDate(pay_date);
        hotelOrder.setStatus("1");
        return hotelOrderMapper.updateByExampleSelective(hotelOrder, hotelOrderExample);
    }

    public int deleteOrder(Integer orderId) {
        HotelOrderDetailExample hotelOrderDetailExample = new HotelOrderDetailExample();
        hotelOrderDetailExample.createCriteria()
                .andOrderIdEqualTo(orderId);
        int i = hotelOrderDetailMapper.deleteByExample(hotelOrderDetailExample);
        return i & hotelOrderMapper.deleteByPrimaryKey(orderId);
    }

    public int confirmOrder(Integer orderId) {
        HotelOrder hotelOrder = hotelOrderMapper.selectByPrimaryKey(orderId);
        String status = hotelOrder.getStatus();
        HotelOrder modified = new HotelOrder();
        modified.setOrderId(orderId);
        switch (status){
            case "1":
                Date checkIn = new Date();
                modified.setConfirmCheckIn(checkIn);
                modified.setStatus("2");
                break;
            case "2":
                Date checkOut = new Date();
                if (hotelOrder.getConfirmCheckIn() == null){
                    modified.setConfirmCheckIn(checkOut);
                }
                modified.setConfirmCheckOut(checkOut);
                modified.setStatus("3");
                break;
            default:
                break;
        }
        return hotelOrderMapper.updateByPrimaryKeySelective(modified);
    }
}
