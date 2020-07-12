//通常我们是在页面加载完以后，html标签都加载完了，这个时候用js给标签动态绑定一个事件
//下面这是是页面加载完之后调用 和js里面window.onload
$(function () {
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);
});


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

//置顶
function setTop() {
    $.post(
        CONTEXT_PATH + "/discuss/top",
        {"id":$("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0){
                //因为是点了置顶，所以如果成功的话，就将按钮设置为disable
                $("#topBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    );
}

//加精
function setWonderful() {
    $.post(
        CONTEXT_PATH + "/discuss/wonderful",
        {"id":$("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0){
                $("#wonderfulBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    );
}

//删除
function setDelete() {
    $.post(
        CONTEXT_PATH + "/discuss/delete",
        {"id":$("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0){
                //帖子删除以后，就直接跳转到首页
                location.href = CONTEXT_PATH + "/index";
            } else {
                alert(data.msg);
            }
        }
    );
}
