package com.tianxiabuyi.sports_medicine.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 搜索社区历史纪录实体类
 */
@Table(name = "community_search_history")
public class CommunityHistory {
    @Column(name = "id",isId = true)
    private long id;
    @Column(name = "content")
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
