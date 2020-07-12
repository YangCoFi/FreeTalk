$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");	//发布之后，将填数据的对话框隐藏

	// //发送ajax请求之前，将csrf令牌设置到请求的消息头中
	// var token = $("meta[name='_csrf']").attr("content");
    // var header = $("meta[name='_csrf_header']").attr("content");
    // $(document).ajaxSend(function (e, xhr, options) {
	// 	xhr.setRequestHeader(header, token);
    // });	//这样发请求时就会携带这个数据
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	console.log("hello");
	//发送异步请求
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		function(data) {
			//返回的是字符串，我需要把它转换成对象
			data = $.parseJSON(data);
			//在提示框中显示返回的消息
			$("#hintBody").text(data.msg);

            //这段代码 表示先显示 2s后自动隐藏
            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                //判断data.code==0？ 是表示发布成功，我们刷新一下页面
				if(data.code == 0){
					window.location.reload();		//重新加载当前页面
				}
            }, 2000);
        }
	);
	console.log("hello again");
//reload 方法，该方法强迫浏览器刷新当前页面。
// 语法：location.reload([bForceGet])参数： bForceGet， 可选参数， 默认为 false，从客户端缓存里取当前页。 true, 则以GET 方式，从服务端取最新的页面, 相当于客户端点击 F5("刷新")
}