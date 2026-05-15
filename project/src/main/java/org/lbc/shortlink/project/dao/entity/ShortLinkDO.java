package org.lbc.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.lbc.shortlink.project.common.database.BaseDO;

@Data
@TableName("t_link")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkDO extends BaseDO {
    private Long id;
    //分组 id
    private String gid;
    //域名
    private String domain;
    //短链接
    private String shortUri;
    //完整短链接
    private String fullShortUrl;
    //原始链接
    private String originUrl;
    //点击量
    private Integer clickNum;
    //启用标识： 0 未启用，1 启用
    private Integer statusEnable;
    //创建类型：0 接口创建，1 控制台创建
    private Integer createdType;
    //有效期类型：0 永久有效 1 自定义
    private Integer validDateType;
    //有效期时间戳(秒)
    private Long validDate;
    //描述
    private String remark;
}
