function like(btn, entityType, entityId, entityUserId, postId) {            //超链接 当成按钮来用了btn
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0){
                //成功需要改变html里面赞的数量
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus == 1 ? '已赞' : '赞');     //status==1表示赞成功了
            }else {
                alert(data.msg);
            }
        }
    )
}