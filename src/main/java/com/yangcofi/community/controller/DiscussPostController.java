package com.yangcofi.community.controller;

import com.yangcofi.community.entity.DiscussPost;
import com.yangcofi.community.entity.User;
import com.yangcofi.community.service.DiscussPostService;
import com.yangcofi.community.util.CommunityUtil;
import com.yangcofi.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @ClassName DiscussPostController
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/19 15:35
 **/
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody           //返回的是字符串 不是网页
    public String addDiscussPost(String title, String content){

        //异步的 给页面返回json字符串
        User user = hostHolder.getUser();
        if (user == null){
            return CommunityUtil.getJSONString(403, "您还未登录");           //403没有权限
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        //type status不设置也可以 因为默认就是零 普通帖子
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        //报错的情况将来统一处理
        return CommunityUtil.getJSONString(0, "发布成功!");
    }
}
