/*
 * @(#)DeclareDao.java 2018年4月28日下午4:50:27 Test Copyright 2018 Thuisoft, Inc.
 * All rights reserved. THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject to
 * license terms.
 */
package dao;

import java.util.List;

import pojo.Declare;

/*
 * 
 * DeclareDao
 * 
 * @author huayu
 * 
 * @version 1.0
 */
public interface DeclareDao {
    List<Declare> selectAll();

    List<Declare> selectAllByUser(/* @Param("userId") */int usrId);

    void updateDeclare(Declare declare);

    void deleteById(int id);
}
