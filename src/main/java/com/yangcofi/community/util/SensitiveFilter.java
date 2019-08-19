package com.yangcofi.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SensitiveFilter
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/19 13:11
 **/

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换敏感词为一个常量
    public static final String REPLACEMENT = "***";

    //初始化根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct              //表示这是一个初始化方法 在容器实例化这个Bean 在调用它的构造器之后 这个方法就会被自动调用
    public void init(){
        //类加载器是从类路径下去加载资源 就是targrt的classes目录之下 程序一编译 所有的文件都会在classes目录之下

        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //把字节流转换成字符流
                //把字符流转换成缓冲流 效率高
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String ketword;         //读一行一个敏感词
            while ((ketword = reader.readLine()) != null){
                //将敏感词添加到前缀树
                this.addKeyWord(ketword);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败" + e.getMessage());
        }
    }


    //将一个敏感词添加到前缀树中去
    private void addKeyWord(String ketword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < ketword.length(); i ++){
            char c = ketword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null){
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            //让指针指向子节点
            tempNode = subNode;

            //设置结束标识
            if (i == ketword.length() - 1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 检索敏感词 返回替换掉的字符串
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }

        //指针1 指向前缀树
        TrieNode temp1 = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //记录最终结果
        StringBuilder sb = new StringBuilder();

        //因为指针3会很可能先到最后 利用指针3做循环效率高
        while (position < text.length()){
            char c = text.charAt(position);
            if (isSymbol(c)){
                //指针1是不是指向根 是，将此符号记录结果，让指针2向下走一步
                if (temp1 == rootNode){
                    sb.append(c);
                    begin ++;
                }
                //指针3无论如何都走
                position ++;
                continue;
            }
            //检查下级节点
            temp1 = temp1.getSubNode(c);
            if (temp1 == null){
                //以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++ begin;
                //重新到根节点
                temp1 = rootNode;
            }else if (temp1.isKeyWordEnd()){
                //发现了敏感词 begin开头，position结尾 将这一段字符串换掉
                sb.append(REPLACEMENT);
                //指针进入
                begin = ++ position;
                //重新到根节点
                temp1 = rootNode;
            }else {
                //检查下一个字符
                position ++;
            }
        }
        //将最后一批字符记录结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否为符号
    public boolean isSymbol(Character c){
        //2E80 9FFF是东亚的文字范围 我们不认为他是符号
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);    //isAsciiAlphanumeric特殊字符，返回flase，我们加一个反 !
    }

    //定义前缀树
    private class TrieNode{
        //定义关键词结束标志
        private boolean isKeyWordEnd = false;

        //当前节点的子节点 key是下级节点字符 value是下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        //获取子节点方法
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }

}
