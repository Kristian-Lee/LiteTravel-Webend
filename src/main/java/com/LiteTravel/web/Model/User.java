package com.LiteTravel.web.Model;

public class User {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column travel_user_account.user_id
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column travel_user_account.user_code
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    private String userCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column travel_user_account.user_password
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    private String userPassword;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column travel_user_account.user_state
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    private String userState;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column travel_user_account.user_id
     *
     * @return the value of travel_user_account.user_id
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column travel_user_account.user_id
     *
     * @param userId the value for travel_user_account.user_id
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column travel_user_account.user_code
     *
     * @return the value of travel_user_account.user_code
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column travel_user_account.user_code
     *
     * @param userCode the value for travel_user_account.user_code
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column travel_user_account.user_password
     *
     * @return the value of travel_user_account.user_password
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column travel_user_account.user_password
     *
     * @param userPassword the value for travel_user_account.user_password
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column travel_user_account.user_state
     *
     * @return the value of travel_user_account.user_state
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public String getUserState() {
        return userState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column travel_user_account.user_state
     *
     * @param userState the value for travel_user_account.user_state
     *
     * @mbg.generated Mon Feb 17 11:45:59 CST 2020
     */
    public void setUserState(String userState) {
        this.userState = userState == null ? null : userState.trim();
    }
}