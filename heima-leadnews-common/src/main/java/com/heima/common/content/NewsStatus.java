package com.heima.common.content;

public interface NewsStatus {
    /**
     * 草稿
     */
    public static final Short DRAFT = 0;
    /**
     * 提交待审核
     */
    public static final Short SUBMIT = 1;
    /**
     * 审核失败
     */
    public static final Short JUDGE_FAILURE = 2;
    /**
     * 人工审核
     */
    public static final Short MANUAL_CHECK = 3;
    /**
     * 人工审核通过
     */
    public static final Short MANUAL_CHECK_PASSED = 4;
    /**
     * 待发布(审核通过)
     */
    public static final Short PASSED_TO_BE_RELEASED = 8;
    /**
     * 已发布
     */
    public static final Short RELEASED = 9;


}
