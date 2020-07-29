package org.fastboot.db.model;

import java.io.Serializable;

/**
 * 所有Entity类的基类
 *
 * @author Laotang
 * @version 1.0
 */
public class IdEntity implements Serializable {

    public final static String ID_FIELD = "id";

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
