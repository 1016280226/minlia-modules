package com.minlia.cloud.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.minlia.cloud.tenant.entity.AbstractTenantEntity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Created by will on 6/22/17.
 */

//JPA
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.DEFAULT, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)


//Batis
//@org.springframework.data.mybatis.annotations.MappedSuperclass
public abstract class AbstractEntity extends AbstractTenantEntity {


    @Override
    @Transient
    @org.springframework.data.annotation.Transient
    public int hashCode() {
        return 17 + (isEmpty() ? 0 : getId().hashCode() * 31);
    }

    /**
     * 判断是否相等
     *
     * @param obj 对象
     * @return 是否相等
     */
    @Override
    @Transient
    @org.springframework.data.annotation.Transient
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (isEmpty() || obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        AbstractEntity entity = (AbstractEntity) obj;
        if (entity.isEmpty()) {
            return false;
        }
        return getId().equals(entity.getId());
    }


    public String toString() {
        return "AbstractEntity (id=" + this.getId() + ")";
    }

}