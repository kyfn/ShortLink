package org.lbc.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkCreateRespDTO {
    private String gid;
    private String originUrl;
    private String fullShortUrl;
}
