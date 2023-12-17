package com.thinktank.api.clients;

import com.thinktank.api.fallbackfactories.BlockClientFallBackFactory;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.BlockClassifyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉16⽇ 13:48
 * @Description: block的Feign客户端
 * @Version: 1.0
 */
@FeignClient(value = "block", fallbackFactory = BlockClientFallBackFactory.class)
@RequestMapping("/block")
public interface BlockClient {
    @GetMapping("getBlockClassify")
    R<List<BlockClassifyDto>> getBlockClassify();
}
