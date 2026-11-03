/**
 * 表单验证
 * 本组件依赖jquery,form
 * liuyj
 * 2017年6月19日
 * 0.0.1 失去焦点时验证
 * 0.0.2 新增在容器元素，实现在容器内的元素就可以验证
 * 0.0.3 为了配合外面直接可以调用form.verifyFun(),将此调用的参数改成了dom容器标签，不能传dom对象


 */
bt.define(['jquery','form'], function(exports){
	var $ = layui.jquery,form = layui.form();
	var VerifyFun =function(){
	}


    VerifyFun.prototype.init = function(ops){
        var that = this;
        var _config ={};
        _config.elem = ops.elem;
        if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
          throwError('Verify error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        var $container;
        if(typeof(_config.elem) === 'string') {
          $container = $('' + _config.elem + '');
        }
        if(typeof(_config.elem) === 'object') {
          $container = _config.elem;
        }
        if($container.length === 0) {
            throwError('Verify error:找不到elem参数配置的容器，请检查.');
        }
        var verifyElem = $container.find('*[lay-verify]');
        bt.each(verifyElem,function(){
            $(this).on("blur",function(){
                form.verifyFun($(this).parent())
            })
        })
    }
    function throwError(msg) {
        throw new Error(msg);
    };
    
    var obj = {
    	init:function(ops){
        var v = new VerifyFun();

    		v.init(ops);
    	}
    }
   	//输入口
	exports('verify', obj);	  
})