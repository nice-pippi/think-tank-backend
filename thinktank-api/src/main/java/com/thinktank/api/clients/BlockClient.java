package com.thinktank.api.clients;

import com.thinktank.api.config.FeignInterceptor;
import com.thinktank.api.fallbackfactories.BlockClientFallBackFactory;
import com.thinktank.common.utils.R;
import com.thinktank.generator.vo.BlockMasterListVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 弘
 * @CreateTime: 2024年02⽉02⽇ 17:24
 * @Description: block的Feign客户端
 * @Version: 1.0
 */
@FeignClient(value = "block", configuration = FeignInterceptor.class, fallbackFactory = BlockClientFallBackFactory.class)
@RequestMapping("/block")
public interface BlockClient {
    @GetMapping("/master/{id}")
    R<BlockMasterListVo> getAllBlockMasterById(@PathVariable("id") Long id);
}
