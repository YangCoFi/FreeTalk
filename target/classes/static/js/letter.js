$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");

	var toName = $("#recipient-name").val();		//文本框的值	.val $("#message-text") id选择器
	var content = $("#message-text").val();
	//异步地发一个post请求
	$.post(
		CONTEXT_PATH + "/letter/send",
		{"toName":toName,"content":content},
		function (data) {		//处理服务端返回的数据	data是一个普通地字符串。当然他满足Json的格式
			data = $.parseJSON(data);					//利用JQuery的parseJSON将其转为JSON对象
        	if (data.code == 0){
        		//发送成功了	提示框
				$("#hintBody").text("发送成功!");				//普通的元素 不是表单元素 取内容全用.text()
        	}else {
                $("#hintBody").text(data.msg);
            }
            //把页面刷新
            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                //过两秒隐藏起来，当前页面刷新一下
				location.reload();		//reload()重载当前页面
            }, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}