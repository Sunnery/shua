/**
 * @Title: GoodsStatusEnum.java
 * @Package com.guesslive.vendor.common
 * Copyright: Copyright (c) 2015-2016
 * Company:嗨购科技技术有限公司
 * 
 * @author Haigou-abao
 * @date 2016年7月27日 上午11:20:49
 * @version V1.0
 */

package com.guesslive.admin.common;

/**
  * @ClassName: GoodsStatusEnum
  * @author Haigou-abao
  * @date 2016年7月27日 上午11:20:49
  *
  */
public enum GoodsStatusEnum {
	// 状态 (0:删除, 1: 待提交, 2:待审核, 3:审核通过, 4：待上架，5：已上架)
	
	DELETE(0),
	PENDING_SUBMIT(1),
	PENDING_CHECK(2),
	CHECK_PASS(3),
	ACTIVE_PENDING_CHECK(4),
	ACTIVED(5);
	
	/**
	 * 商品状态
	 */
	private int status;
	
	/**
	 * getter method
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	private GoodsStatusEnum(int status){
		this.status = status;
	}
}
