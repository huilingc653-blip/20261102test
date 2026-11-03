/**
 * 组件:比例组件
 * 刘勇军
 * 2017年12月11日
 * 0.0.1 支持基本功能；
 */

layui.define(['jquery', 'formatnumber'], function(exports) {
    var $ = layui.jquery;

    var RatioType = function () {
        this.config ={
            elem:""
        };
        this.bizName = "ratioType";
        this.currentObject = null;
        this.ratio ='';
    }

    RatioType.fn = RatioType.prototype;

    RatioType.fn.v = '0.0.1';

    /**
     * 方法入口，初始化
     */
    RatioType.fn.init = function(options){
        var that = this;
        that.render()
    }

    /*
        对控件进行渲染
        @function
        @param {Jquery DOM} elem jq dom对象
        @param {String} reset 控件值重置标识
    */

    RatioType.fn.render = function(elem,reset){
        var that = this;
        that.initConfig(elem,reset)
       
    };
   
    //对参数进行初始化
    RatioType.fn.initConfig = function(elem,reset,ratioType){ //reset主要用于标识是否是重置值，form reset中用得到
        var that = this;
        var ratioType = ratioType;
        var settings = {};
        var domObj = elem || $('.bt-biz-ratioType:not(.init)');
        bt.each(domObj,function(){
            var $container = $(this);
            if($container.hasClass('init')){
                return
            }
            var decimal = $container.data("decimal"),
                max = $container.data('max'),
                min = $container.data('min'),
                name = $container.data('name'),
                vname = $container.attr('name'),
                bizName = $container.data("biz-name"),
                type = $container.data("type".toLowerCase()),//自定义汇率类型，暂时支持百分位/千分位
                callback = $container.data("callback");
            callback = that.makeCallBack(callback);

            if(vname && !name){
                $container.attr("data-name",vname);
            };
            $container.removeAttr("name"); //新增加删除name属性方法
            //重置ratioType值，如果输入了type，就以type为准
            type = type === undefined ? 'percent':type;
            var symbol = type =='percent'?'%':(type =='thousands'?'‰':'%');
            max = max === undefined ?100:(max > 100 ?100:max);
            min = min === undefined ? 0:( min < 0 ? 0:min);





            settings = {
                elem:$container,
                decimal:decimal?decimal:2,
                max:max,
                min:min,
                callback:callback,
                rateType:type,
                ratiotype:true // 标识是比例控件
            }
            if(!$container.parent().hasClass("pl")){ //添加symbol
                var paramWidth = $container.data('width');
                var width;
                if( paramWidth ){
                    paramWidth = paramWidth.replace('px','');
                    width = paramWidth;
                }else {
                    width = $('.layui-input:visible')[0] && $('.layui-input:visible')[0].clientWidth || 214;
                }

                var wrapWidth = width + 'px';
                var rateWidth = width - 18;
                $container[0].clientWidth = rateWidth;
            	$container.css({
                    "border":"none",
                    "width":rateWidth+"px"
                });
                $container.wrap('<div class="pl rateWrap" style="width:'+wrapWidth+'"></div>').after('<span name="percentsymbol" class="inputMark">'+symbol+'</span>')
            }
            $container.addClass("init textRight input-inline ");

            var v = $container.val();
            if(!reset && v !=''){ //存贮默认值
                $container.data('defaultvalue',v);
            }
            var num = new FormatNumber();
            num.init(settings);
        })
    }

    RatioType.fn.makeCallBack = function(callBack){
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

    RatioType.fn.val = function(bizName){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var id = that.currentObject.data('name');
        if(bizName === undefined){
            return $('#'+id).val();
        }else{
            that.currentObject.val(bizName).removeClass("init");
            $('#'+id).remove();
            that.render(that.currentObject,'reset')
        }

    };

    RatioType.fn.attr= function(attr,value){
        var that = this;
        if(!that.currentObject){
            return "";
        }

        if(attr && arguments.length === 1 ){
            return that.currentObject.attr(attr);
        }

        if(arguments.length === 2 ){
            if(attr.indexOf("data-") !== -1){
                var attrName = attr.replace("data-",'');
                that.currentObject.data(attrName,value);
            }else{
                that.currentObject.attr(attr,value);
            }
            that.currentObject.removeClass("init");
            that.currentObject.siblings("input").remove();
            that.render(that.currentObject)
        }
    };
    

    function throwError(msg) {
        throw new Error("[bt-biz-RateType:" + msg);
    };

    var ratioType = new RatioType();
    var obj = {
        init:function(ops){
            ratioType.init(ops);
            return this;
        },
        render:function(){
            ratioType.render();
        },
        get:function(bizName){
            var obj = $('input[data-biz-name="'+bizName+'"]');
            if(obj.length ===0){
                throw new Error("[bt-biz-RatioType:元素容器找不到，具体设置格式请参考文档API");
            }
            ratioType.currentObject = obj;
            return ratioType;
        },
    }
    exports('ratioType',obj);
})