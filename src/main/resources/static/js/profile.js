$(function(){
	$(".follow-btn").click(follow);			//关注按钮的单击事件
});

function follow() {
	var btn = this;			//方法里首先获取当前的按钮
	if($(btn).hasClass("btn-info")) {		//如果按钮的样式是btn-info（蓝色的样式）
		// 关注TA
		$.post(
			CONTEXT_PATH + "/follow",
			{"entityType":3, "entityId":$(btn).prev().val()},							//因为是关注人		//entityId  这个我们从html中获取 而页面上没有显示id，没地方取，这里我们在button之前加一个隐藏框
			function (data) {
				data = $.parseJSON(data);			//先转成js对象 JQuery.parseJSON用于将格式完好的JSON字符串转为与之对应的JavaScript对象
				if (data.code == 0){
					window.location.reload();			//让页面重新刷新一下
				}else {
					alert(data.msg);
				}
            }
		);
		// $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");		//这里只是关注之后文字变化了 并且样式做了改动
	} else {
		// 取消关注
        $.post(
            CONTEXT_PATH + "/unfollow",
            {"entityType":3, "entityId":$(btn).prev().val()},							//因为是关注人		//entityId  这个我们从html中获取 而页面上没有显示id，没地方取，这里我们在button之前加一个隐藏框
            function (data) {
                data = $.parseJSON(data);			//先转成js对象 JQuery.parseJSON用于将格式完好的JSON字符串转为与之对应的JavaScript对象
                if (data.code == 0){
                    window.location.reload();			//让页面重新刷新一下
                }else {
                    alert(data.msg);
                }
            }
        );
		// $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
	}
}