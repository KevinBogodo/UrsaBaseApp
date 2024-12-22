package com.urssa.urssaAppPressing.v2.permission;

import com.urssa.urssaAppPressing.v2.appConfig.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "code")
    private String code;

    public Permission(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
