/**
 * tab切换控件
 * 本组件依赖layui
 * liuyj
 * 2017年4月10日
 * 0.0.1 tab切换功能
 */


layui.define(['element','jquery','form'], function (exports) {
    "use strict";
      var  $ = layui.jquery,
        element = layui.element(),
        form = layui.form(),
        Btab = function () {
            this.config = {
                elem: undefined,//容器
                url:undefined,
                data:undefined,
              
            };
        };

	/**
	 * 参数设置
	 * @param {Object} options
	 */
    Btab.prototype.set = function (options) {
        var that = this;
        $.extend(true, that.config, options);
        return that;
    };
	
    Btab.prototype.render = function(){
        var _that = this,
        _config = _that.config;
        if (typeof (_config.elem) !== 'string' && typeof (_config.elem) !== 'object') {
            common.throwError('Btab error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        var $container;
        if (typeof (_config.elem) === 'string') {
            $container = $('' + _config.elem + '');
        }
        if (typeof (_config.elem) === 'object') {
            $container = _config.elem;
        }
        if ($container.length === 0) {
            common.throwError('Btab error:找不到elem参数配置的容器，请检查.');
        }
        _config.elem = $container;
        if(_config.data === undefined && _config.url === undefined) {
            common.throwError('Btab error:请为Navbar配置数据源.')
        }
        if(_config.data !== undefined && typeof(_config.data) === 'object') {
            var html = getHtml(_config);
            $container.html(html);
            _config.elem = $container;
        } else {
            $.ajax({
                type: _config.type,
                url: _config.url,
                // async: false, //_config.async,
                dataType: 'json',
                success: function(result, status, xhr) {
                    _config.data = result;
                    var html = getHtml(_config);
                    $container.html(html);
                },
                error: function(xhr, status, error) {
                    common.msgError('btab error:' + error);
                },
                complete: function(xhr, status) {
                    _that.config.elem = $container;
                }
            });
    }
}
	function getHtml(ops){
        var data = ops.data;
        var tabtitle = '<ul class="layui-tab-title">',
        tabcontent='<div class="layui-tab-content">';
        for(var i=0;i<data.length;i++){
            if(i==0){
                tabtitle +='<li class="layui-this" lay-id="'+data[i].tabId+'">'+data[i].tabname+'</li>';
                tabcontent +='<div class="layui-tab-item layui-show" lay-id="'+data[i].tabId+'">'+data[i].detail+'</div>'  
            }else{
                tabtitle +='<li lay-id="'+data[i].tabId+'">'+data[i].tabname+'</li>';
                tabcontent +='<div class="layui-tab-item" lay-id="'+data[i].tabId+'">'+data[i].detail+'</div>'  
            }
        }
        tabtitle +='</ul>';
        tabcontent +='</div>';
        var temp = '<div class="layui-tab" >';
        temp +=tabtitle + tabcontent +'</div>';
        return temp;
    }
    var obj ={
        init:function(ops){
            var b = new Btab();
            b.set(ops);
            b.render();
            return b;
        }
    }
    exports("btab", obj)
});