package com.tie.yennichi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;

/**
* 登録日、更新日に関係するentity
*/
@MappedSuperclass
@Data
public class AbstractEntity {
	
	// 作成日
    @Column(name = "created_at")
    private Date createdAt;

    // 更新日
    @Column(name = "updated_at")
    private Date updatedAt;

    // 登録日をセット
    @PrePersist
    public void onPrePersist() {
        Date date = new Date();
        setCreatedAt(date);
        setUpdatedAt(date);
    }

    // 更新日をセット
    @PreUpdate
    public void onPreUpdate() {
        setUpdatedAt(new Date());
    }
}