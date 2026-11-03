/**
 * 数字转大写组件
 * 本组件依赖layui的jquery
 * liuyj
 * 2017年5月24日
 * 0.0.1 实现数字转大写
 * 0.0.2 设置无外边框
 * 0.0.3 调用money中的方法
 * 0.0.4 代码优化
 */
bt.define(['jquery','money'], function(exports){
    "use strict";
    var $ = bt.jquery;
    var CapNum = function(){
    	this.config = {
    		elem:"",
    		linkElemDiv:"", //关联的div即输入大写字母的元素
    	}
    }

    CapNum.prototype.set = function(options){
    	var that = this;
    	$.extend(true, that.config, options);
    	return that
   	};
    CapNum.prototype.init = function(){
		var that = this,$container,_config = that.config,elem = _config.elem;
    	//判定elem的合法性
    	if(typeof( elem ) !== 'string' && typeof(elem) !== 'object') {
    	  	throwError('capNumber error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
    	}
    	if(typeof( elem ) === 'string') {
    	  	$container = $('' + elem + '');
    	}
        if(typeof( elem) === 'object') {
            $container = elem;
        }
    	//判定linkElem的合法性
    	if(typeof(_config.linkElemDiv) !== 'string' && typeof(_config.linkElemDiv) !== 'object') {
    	  	throwError('capNumber error: linkElemDiv参数未定义或设置出错，具体设置格式请参考文档API.');
    	}
    	var $linkElemDiv;
    	if( typeof(_config.linkElemDiv) === 'string') {
    	  	$linkElemDiv = $('' + _config.linkElemDiv + '');
    	}


    	if( $linkElemDiv.length === 0) {
    	    throwError('capNumber error:找不到linkElemDiv参数配置的容器，请检查.');
    	}
		$linkElemDiv.closest('td').addClass('bread-text'); //增加表格内换行，当数字为11位时可能会超出表格边界

    	$container.addClass('textRight paddingRight5');//添加输入框的样式
        $linkElemDiv.addClass('capNubmer');//添加大写字母的样式
    	$container.on('input propertychange',function(){
    		var txt = $(this).val();
            txt = txt.replace(/\,/g,'');
            txt = txt.toString();
            var tempValueArray = txt.split(".");
            txt = tempValueArray.length > 1 ?
                (tempValueArray[0].length > 14 ? tempValueArray[0].substr(0, 14) : tempValueArray[0]) + "." + tempValueArray[1] :
                (txt.length > 14 ? txt.substr(0, 14) : txt);
            $linkElemDiv.html(bt.money.toChinese(txt))
    	})
        if($container.val()){
            $linkElemDiv.html(bt.money.toChinese($container.val()))
        }
	};
    function throwError(msg) {
        throw new Error(msg);
    }
   	var obj = {
   		init:function(ops){
            var capNumber = new CapNum();
   			capNumber.set(ops);
   			capNumber.init();
   		}
   	};
   	//输入口
	exports('capNumber', obj);	  
});