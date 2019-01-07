package com.imooc.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import retrofit2.http.Field;

import javax.persistence.*;
import java.util.Date;

/**
 * 类目
 * Created by SqMax on 2018/3/17.
 */
@Entity
@DynamicUpdate
@Data
@Table(name = "biz_route")
public class Callplan {
    /**类目id.**/
    @Id
    @GeneratedValue
    private Integer id;
    /**类目名字**/

    private String fromStation;
    /** 类目编号**/

    private String toStation;

    public Callplan(){

    }
    public Callplan(String fromStation, String toStation){
        this.fromStation=fromStation;
        this.toStation=toStation;
    }


}
