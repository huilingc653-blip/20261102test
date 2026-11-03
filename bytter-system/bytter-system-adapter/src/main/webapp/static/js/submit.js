/**
 * 表单提交组件
 * 本组件依赖jquery form
 * liuyj
 * 2017年10月24日
 * 0.0.1 基本功能

 */
layui.define(['jquery','form'], function(exports){
	var $ = layui.jquery,form = layui.form();
	var Sumbit =function(){
		this.config = {
			filter:'',//提交按钮的lay-filter属性
			url:'',	//提交数据的url
			type:'POST',
			data:'',
			// transfer:false,//页面是否要跳转
			success:undefined,//请求成功的回调
		}
	}
	/**
* 参数设置
* @param {Object} options
*/
Sumbit.prototype.set = function(options){
	var that = this;
	$.extend(true, that.config, options);
	return that
}

Sumbit.prototype.on = function(){
	var that = this;
	var _config =  that.config;
	var success = _config.success;
	success = form.makeCallBack(success);//验证是否为此方法
	form.on('submit('+that.config.filter+')',function(data){
		$.ajax({
            type: _config.type,
            url: _config.url,
            data:data.field,
            dataType: 'json',
            success: function(result, status, xhr) {
                if(result){
                	if(success){
                		success(result);
                	}
                }
            },
        });
		return false;
	})
	
}

var obj = {
	on:function(ops){
    var submit = new Sumbit();
        submit.set(ops);
        submit.on();
	}
}
   	//输入口
	exports('submit', obj);
})