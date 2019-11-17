package com.yangcofi.community.dao.elasticsearch;

import com.yangcofi.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @InterfaceName DiscussPostRepository
 * @Description TODO
 * @Author YangC
 * @Date 2019/9/17 16:46
 **/

@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
