/*
 * @(#)ShopMarketDao.java 2018年4月28日下午4:26:20 Test Copyright 2018 Thuisoft, Inc.
 * All rights reserved. THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject to
 * license terms.
 */
package dao;

import java.util.List;

import pojo.ShopMarket;

/*
 * import java.util.List;
 * 
 * ShopMarketDao
 * 
 * @author huayu
 * 
 * @version 1.0
 */
public interface ShopMarketDao {
    List<ShopMarket> selectAll();

    void update(ShopMarket market);

    void delete(int id);

    void add(ShopMarket market);
}
