package com.thinktank.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thinktank.admin.service.ManagePostService;
import com.thinktank.api.clients.SearchClient;
import com.thinktank.common.exception.ThinkTankException;
import com.thinktank.common.utils.R;
import com.thinktank.generator.dto.PostInfoDto;
import com.thinktank.generator.entity.PostInfo;
import com.thinktank.generator.mapper.PostInfoMapper;
import com.thinktank.generator.vo.PostInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 弘
 * @CreateTime: 2023年12⽉23⽇ 15:51
 * @Description: 帖子管理业务接口实现类
 * @Version: 1.0
 */
@Slf4j
@Service
public class ManagePostServiceImpl implements ManagePostService {
    @Autowired
    private PostInfoMapper postInfoMapper;

    @Autowired
    private SearchClient searchClient;

    @Override
    public IPage<PostInfoVo> page(PostInfoDto postInfoDto) {
        if (postInfoDto.getCurrentPage() == null || postInfoDto.getCurrentPage() < 0) {
            log.error("当前页码非法");
            throw new ThinkTankException("当前页码非法");
        }
        if (postInfoDto.getSize() == null || postInfoDto.getSize() < 0) {
            log.error("每页显示数量非法");
            throw new ThinkTankException("每页显示数量非法");
        }
        Page<PostInfo> page = new Page<>(postInfoDto.getCurrentPage(), postInfoDto.getSize());
        return postInfoMapper.page(page, postInfoDto);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (postInfoMapper.deleteById(id) == 0) {
            log.error("删除帖子失败");
            throw new ThinkTankException("删除帖子失败");
        }

        R<String> result = searchClient.deletePostInfoDoc(id);
        if (result.getStatus() != 200) {
            log.error("删除帖子文档失败");
            throw new ThinkTankException("删除帖子文档失败");
        }
    }
}
