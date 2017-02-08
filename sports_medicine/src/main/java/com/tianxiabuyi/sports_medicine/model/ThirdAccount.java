package com.tianxiabuyi.sports_medicine.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/9/26.
 */
@Table(name = "ThirdAccount")
public class ThirdAccount {

    /**
     * source : qq
     * union_id : 4D9E1CEF635DDB9FA340566C615CC74E
     */
    @Column(name = "source")
    private String source;
    @Column(name = "union_id",isId = true)
    private String union_id;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUnion_id() {
        return union_id;
    }

    public void setUnion_id(String union_id) {
        this.union_id = union_id;
    }
}
