package org.fastboot.db.ds;


import org.fastboot.db.model.DBConnect;

/**
 * 数据源接口
 * @author laotang
 * @since 1.0
 */
public interface IDataSourceFactory<T> {

    /**
     *  获取数据源
     * @return		DataSource
     */
    T getDataSource(DBConnect connect) throws Exception;

}
