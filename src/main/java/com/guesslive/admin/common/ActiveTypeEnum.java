/**
 * @Title: ActiveTypeEnum.java
 * @Package com.guesslive.vendor.common
 * @Description: TODO
 * Copyright: Copyright (c) 2015-2016
 * Company:嗨购科技技术有限公司
 * 
 * @author Haigou-abao
 * @date 2016年7月27日 上午11:35:26
 * @version V1.0
 */

package com.guesslive.admin.common;

/**
  * @ClassName: ActiveTypeEnum
  * @Description: TODO
  * @author Haigou-abao
  * @date 2016年7月27日 上午11:35:26
  *
  */
public enum ActiveTypeEnum {
	// 0：无活动1:今日秒杀,2:限时抢购, 3：分享折扣, 4: 卡券专区
	NONE(0),
	TODAY(1),
	LIMIT(2),
	SHARE(3),
	CARDAREA(4) ;
	
	private int type;
	
	/**
	 * getter method
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	private ActiveTypeEnum(int type) {
		this.type = type;
	}

}
