/**
 * 组件：数字格式化
 * 刘勇军
 * 2017年5月27日
 * 0.0.1 实现千分位、有效数字、正数、负数；
 * 0.0.2 实现最大值、最小值、位数自动补全;
 * 0.0.3 增加靠左显示;
 * 0.0.4 添加失去焦点时回调;
 * 0.0.5 添加percent参数，支持百分比输入;
 * 0.0.6 新增加设置value的方法;
 * 0.0.7 新增直接init初始化页面bt-money;
 * 0.0.8 此组件已废弃，不建议使用;改用moneyType业务件;
 */


layui.define(['jquery', 'formatnumber'], function(exports) {
   var $ = layui.jquery;
   var MoneyNum = function () {
        this.config ={};
    }

    MoneyNum.prototype.v = '0.0.6';

    MoneyNum.prototype.setting = function(ops){
        var that = this;
        $.extend(true, that.config, ops);
        var _config = that.config;
        var $container;
        if(ops && typeof ops ==='object'){
            if(typeof(ops.elem) !== 'string' && typeof(ops.elem) !== 'object') {
                throwError('moneynum error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
            }
        
            if(typeof(ops.elem) === 'string') {
                $container = $('' + _config.elem + '');
            }
            if(typeof(ops.elem) === 'object') {
                $container = _config.elem;
            }
            if($container.length === 0) {
                throwError('moneynum error:找不到elem参数配置的容器，请检查.');
            }
        }else{
            $container = $(".bt-money");
        }
        _config.elem = $container;
        if($container.length ==1){
            that.init($container)
        }else{
            bt.each($container,function(i,item){
                that.init($(item))
            })
        }
    }

    /**
     * 方法入口，初始化
     */

    MoneyNum.prototype.init = function($elem){
        var settings = {},$container = $elem || this.config.elem, _config = this.config;
        var decimal = $container.data('decimal'),
        minus = $container.data('minus'),
        max = $container.data('max'),
        min = $container.data('min'),
        name = $container.data('name'),
        vname = $container.attr('name'),
        percent = $container.data("type") =="percent",
        callback = $container.data("callback");
        callback = this.makeCallBack(callback);

        if(vname && !name){
            $container.attr("data-name",vname);
        }

        if(!vname && !name){
            throwError('moneynum error:请在'+_config.elem+'容器设置name或者data-name属性');
        }
        $container.removeAttr("name");
        $container.css(
            {
                "text-align":"right",
                "padding-right":"5px"
            }
        );
        settings = {
            elem:$container,
            decimal:_config.decimal == 0 ? 0 :!_config.decimal ? decimal:_config.decimal,
            minus:_config.minus ?_config.minus:minus,
            max:_config.max ? _config.max:max,
            min:_config.min ? _config.min:min,
            callback:_config.callback ? _config.callback:callback,
            percent: (percent || _config.percent)? true:false,
        }
        var num = new FormatNumber();
       	num.init(settings);
    }

    MoneyNum.prototype.setValue = function(value){
        this.config.elem.val(value);
        this.config.elem.siblings("input").remove()
        this.init()
    }

    MoneyNum.prototype.makeCallBack = function(callBack){
        //校验下拉树的回调方法是否存在
        if(callBack && typeof(callBack) != "function"){
            try {
                callBack = eval(callBack);
            } catch(e) {
                alert(callBack + 'not exist！');
            }
                
        }
        return callBack;
    }

     function throwError(msg) {
        throw new Error(msg);
    };
    var obj = {
    	init:function(ops){
    		var format = new MoneyNum();
            format.setting(ops);
            // console.error("此组件已废弃，不建议使用;改用moneyType业务件")
    		// format.init();
            return format
    	}
    }
   exports('moneynum',obj)
})