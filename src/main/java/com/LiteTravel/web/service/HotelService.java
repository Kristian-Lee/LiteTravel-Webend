package com.LiteTravel.web.service;

import com.LiteTravel.web.DTO.*;
import com.LiteTravel.web.Model.*;
import com.LiteTravel.web.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    public HotelMapper hotelMapper;

    @Autowired
    public RoomMapper roomMapper;

    @Autowired
    public RoomBedMapMapper roomBedMapper;

    @Autowired
    public BedMapper bedMapper;

    @Autowired
    public RegionMapper regionMapper;
    // 默认酒店列表
//    @Cacheable(cacheNames = {"hotels"}, key = "#page")
    public ResultVO<HotelDTO> getHotels(Integer page, Integer pageSize){
        return selectByExample(page, pageSize, new HotelExample());
    }
    // 推荐酒店
//    @Cacheable(cacheNames = {"relateHotels"}, key = "#hotelId")
    public ResultVO<HotelDTO> getHotels(Integer hotelId, Integer page, Integer pageSize)
    {
        HotelExample hotelExample = new HotelExample();
//      todo 推荐算法尚未写好
        hotelExample.createCriteria()
                .andHotelIdNotEqualTo(hotelId);
        return selectByExample(page, pageSize, hotelExample);
    }

    private ResultVO<HotelDTO> selectByExample(Integer page, Integer pageSize, HotelExample hotelExample){
        /* 分页：
         * 参数1: 第几页
         * 参数2: 每页展示几个数据 */
        PageHelper.startPage(page, pageSize);
        List<Hotel> hotels = hotelMapper.selectByExample(hotelExample);
        PageInfo<Hotel> info = new PageInfo<>(hotels, 5);
        List<Integer> addressIds = hotels.stream().map(Hotel::getHotelAddress).distinct().collect(Collectors.toList());
        RegionExample regionExample = new RegionExample();
        regionExample.createCriteria()
                .andIdIn(addressIds);
        List<Region> regions = regionMapper.selectByExample(regionExample);
        Map<Integer, String> addressMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getMername));
        List<HotelDTO> data = hotels.stream().map(hotel -> {
            HotelDTO hotelDTO = new HotelDTO();
            BeanUtils.copyProperties(hotel, hotelDTO);
            // 写入地址数据 hotelTotalAddress;
            String addressString = addressMap.get(hotel.getHotelAddress());
            hotelDTO.setHotelAddressString(addressString.substring(addressString.indexOf("省,") + 2));
            return hotelDTO;
        }).collect(Collectors.toList());
        return new ResultVO(data, info);
    }

    // 展现酒店单页
//    @Cacheable(cacheNames = {"hotel"}, key = "#hotelId + '[' + #roomFlag + ']'")
    public HotelDTO selectHotelById(Integer hotelId, boolean roomFlag){
        Hotel hotel = hotelMapper.selectByPrimaryKey(hotelId);
        HotelDTO hotelDTO = new HotelDTO();
        // 获得基本数据
        BeanUtils.copyProperties(hotel, hotelDTO);
        // 写入地址信息
        RegionExample regionExample = new RegionExample();
        regionExample.createCriteria()
                .andIdEqualTo(hotel.getHotelAddress());
        List<Region> regions = regionMapper.selectByExample(regionExample);
        if(regions.size() > 0){
            hotelDTO.setHotelAddressString(regions.get(0).getMername());
        }
        // 判断是否需要room数据, 借此获得Room数据
        if(roomFlag){
            RoomExample roomExample = new RoomExample();
            roomExample.createCriteria()
                    .andHotelIdEqualTo(hotelId);
            List<Room> rooms = roomMapper.selectByExample(roomExample);
            List<RoomDTO> roomDTOs = rooms.stream().map(this::getRoomDTO).collect(Collectors.toList());
            hotelDTO.setRooms(roomDTOs);
        }
        return hotelDTO;
    }

    public RoomDTO getRoomDTO(Integer roomId){
        return getRoomDTO(roomMapper.selectByPrimaryKey(roomId));
    }

    public List<HotelOrderDetailDTO> getHotelOrderDetailByRoomIds(List<Integer> roomIds) {
        List<RoomDTO> roomDTOs = getRoomDTObyIds(roomIds);
        return roomDTOs.stream().map(roomDTO -> {
            HotelOrderDetailDTO hotelOrderDetailDTO = new HotelOrderDetailDTO();
            BeanUtils.copyProperties(roomDTO, hotelOrderDetailDTO);
            return hotelOrderDetailDTO;
        }).collect(Collectors.toList());
    }

    public List<RoomDTO> getRoomDTObyIds(List<Integer> roomIds) {
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria()
                .andRoomIdIn(roomIds);
        return getRoomDTOs(roomMapper.selectByExample(roomExample));
    }
    private RoomDTO getRoomDTO(Room room){
        RoomDTO roomDTO = new RoomDTO();
        BeanUtils.copyProperties(room, roomDTO);

        // 获取roomBedMap
        RoomBedMapExample roomBedMapExample = new RoomBedMapExample();
        roomBedMapExample.createCriteria()
                .andRoomIdEqualTo(room.getRoomId());
        List<RoomBedMap> roomBedMaps = roomBedMapper.selectByExample(roomBedMapExample);
        List<Integer> bedIds = roomBedMaps.stream().map(RoomBedMapKey::getBedId).distinct().collect(Collectors.toList());
        // 获取bed
        BedExample bedExample = new BedExample();
        bedExample.createCriteria()
                .andBedIdIn(bedIds);
        List<Bed> bedList = bedMapper.selectByExample(bedExample);
        // 获取bedCount
        Map<Integer, Integer> bedCountMap = roomBedMaps.stream().collect(Collectors.toMap(RoomBedMapKey::getBedId, RoomBedMap::getBedCount));
        // bed和bedCount写入bedDTO
        List<BedDTO> bedDTOs = bedList.stream().map(bed -> {
            BedDTO bedDTO = new BedDTO();
            BeanUtils.copyProperties(bed, bedDTO);
            bedDTO.setBedCount(bedCountMap.get(bed.getBedId()));
            return bedDTO;
        }).collect(Collectors.toList());
        roomDTO.setBeds(bedDTOs);
        return roomDTO;
    }
    private List<RoomDTO> getRoomDTOs(List<Room> rooms){
        return rooms.stream().map(this::getRoomDTO).collect(Collectors.toList());
    }
}
