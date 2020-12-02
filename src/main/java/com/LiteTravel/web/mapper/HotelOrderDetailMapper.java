package com.LiteTravel.web.mapper;

import com.LiteTravel.web.Model.HotelOrderDetail;
import com.LiteTravel.web.Model.HotelOrderDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HotelOrderDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    long countByExample(HotelOrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int deleteByExample(HotelOrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int deleteByPrimaryKey(Integer orderId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int insert(HotelOrderDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int insertSelective(HotelOrderDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    List<HotelOrderDetail> selectByExample(HotelOrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    HotelOrderDetail selectByPrimaryKey(Integer orderId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int updateByExampleSelective(@Param("record") HotelOrderDetail record, @Param("example") HotelOrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int updateByExample(@Param("record") HotelOrderDetail record, @Param("example") HotelOrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int updateByPrimaryKeySelective(HotelOrderDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table travel_hotel_orders_detail
     *
     * @mbg.generated Wed Dec 02 18:08:17 CST 2020
     */
    int updateByPrimaryKey(HotelOrderDetail record);
}