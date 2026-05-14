package org.lbc.shortlink.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.lbc.shortlink.admin.dao.entity.GroupDO;
import org.lbc.shortlink.admin.dto.req.GroupSortReqDTO;

import java.util.List;

/**
 * 短链接分组持久层
 */
public interface GroupMapper extends BaseMapper<GroupDO> {
    int batchUpdateSortOrder(@Param("list") List<GroupSortReqDTO> list,
                              @Param("username") String username);
}
