package com.guesslive.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.guesslive.admin.model.SeccoAccount;
import com.guesslive.admin.model.StatBucks;


public interface SeccoMapper {
	
	public SeccoAccount getSeccoAccount(@Param("mobile") String mobile);

    public List<SeccoAccount> getUndealSeccoAccountList(@Param("today")String today,@Param("start")int start,@Param("total")int total);

    public void addSeccoAccount(SeccoAccount seccoAccount);

    public void updateSeccoAccount(SeccoAccount seccoAccount);

    public void addStarBucks(StatBucks statBucks);
	
}
