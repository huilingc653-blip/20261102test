/**
 * 组件：货币类型业务组件
 * 刘勇军
 * 2017年6月27日
 * 0.0.1 实现千分位、有效数字、正数、负数；
 * 0.0.2 将capNum  money方法，全部合并在一起；
 * 0.0.3 给input添加一个相对定位；
 * 0.0.4 添加重置事件
 * 0.0.5 decimal默认值改成4
 * 0.0.6 新增加defaultDecimal参数，用于设置有效数字
 * 0.0.7 删除重置方法，统一有form来处理
 * 0.0.8 第一次初始化时，把值存贮起来
 * 0.0.9 新增加format方法，用于转成百分数或者千分数
 * 0.0.10 解决format方法的精度问题，
 * 0.0.11 解决添加获取利率的方法，
 * 0.0.12 rate值存在本地缓存中
 * 0.0.13 保存输入框与layui-input长度一致
 * 0.0.14 当表格是可编辑的汇率输入框为0时，需要重置它的宽度
 * 0.0.15 新增宽度可以自定义参数 data-width=""
 * 0.0.16 解决输入框输入了值之后，再调用attr方法时，值不对的问题
 * 0.0.17 新增data-supermax参数，用于设置超最大值，此参数优先级更高，支持大于1
 * 0.0.18 新增data-supermin参数，用于设置超最小值，此参数优先级更高，支持小于等于0
 */

layui.define(['jquery', 'formatnumber','money','date'], function(exports) {
    var $ = layui.jquery;

    var RateType = function () {
        this.config ={
            elem:""
        };
        this.bizName = "rateType";
        this.currentObject = null;
        this.rate ='';
    }

    RateType.fn = RateType.prototype;

    RateType.fn.v = '0.0.5';

    /**
     * 方法入口，初始化
     */
    RateType.fn.init = function(options){
        var that = this;
        that.render()
    }

    RateType.fn.render = function(ops,reset){
        var that = this;
        var rateType = "";
        rateType = that.getLocalStorageRate();
        if(rateType){
            that.rate = rateType;
            that.initConfig(ops,reset,rateType);
        }else{
            var url = basePath + '/sys/config/type/sys/name/rate';
            $.get(url,function(data){
                var rate = data.value; //0是percent 1是thousands
                if(rate == 1){
                    rateType = "thousands";
                }else if(rate == 0){
                    rateType = 'percent';
                }
                var obj = {
                    rate:rateType,
                    time:Date.parse(new Date())
                }
                localStorage.setItem('rateType',JSON.stringify(obj));//将rateType存储到rateType字段
                that.rate = rateType;
                that.initConfig(ops,reset,rateType);
            },"json")
        }
    };

    //获取本地缓存rate
    RateType.fn.getLocalStorageRate = function () {
        var obj = localStorage.getItem("rateType");
        if(obj){
            obj = JSON.parse(obj)
            var currentTime = Date.parse(new Date());
            var gapTime = currentTime - obj.time;
            if(gapTime < 300000){
                return obj.rate;
            }
        }
        return false;
    }

    //对参数进行初始化
    RateType.fn.initConfig = function(ops,reset,rateType){
        var that = this;
        var rateType = rateType;
        var settings = {};
        var domObj = ops || $('.bt-biz-rateType:not(.init)');
        bt.each(domObj,function(){
            var $container = $(this);
            if($container.hasClass('init')){
                return
            }
            var decimal = $container.data("decimal");
            if($container[0].tagName !=="INPUT"){
                var text = $container.text();
                var decimal = $container.data("decimal")
                var num = btMoney.format(text,{
                    decimal:decimal?decimal:4
                });
                $container.text(num);
            }else{
                var minus = $container.data('minus'),
                    max = $container.data('max'),
                    superMax =  $container.data('supermax'),
                    min = $container.data('min'),
                    superMin =  $container.data('supermin'),
                    name = $container.data('name'),
                    vname = $container.attr('name'),
                    bizName = $container.data("biz-name"),
                    type = $container.data("ratetype".toLowerCase()),//自定义汇率类型，暂时支持百分位/千分位
                    callback = $container.data("callback");
                callback = that.makeCallBack(callback);

                if(vname && !name){
                    $container.attr("data-name",vname);
                }
                //重置rateType值，如果输入了type，就以type为准
                rateType = type ? type :rateType;
                var symbol = rateType =='percent'?'%':(rateType =='thousands'?'‰':'');
                var defaultDecimal = rateType =='percent'?4:(rateType =='thousands'?3:'');;//设置默认小数，百分位为四位，千分位为三位
                var defaultMax = that.initMax(max,rateType,defaultDecimal,superMax);
                $container.removeAttr("name").data('rateType',rateType);
                //superMin最小值，优先级最高;
                min = superMin ? superMin:(min?min:0);
                if(decimal == undefined){
                    decimal = defaultDecimal
                }
                settings = {
                    elem:$container,
                    rateType:rateType,
                    // decimal:decimal?decimal:2,
                    decimal:decimal,
                    minus:minus,
                    max:defaultMax,
                    isSuperMin:superMin !== undefined?true:false,
                    min:min,
                    callback:callback,
                    percent:true,
                }
                //if(!$container.parent().hasClass("pl")){
                //    $container.wrap('<div class="pl rateWrap"></div>').after('<span name="percentsymbol" class="inputMark">'+symbol+'</span>')
                //}

                if(!$container.parent().hasClass("pl")){ //添加symbol
                    var width = 0,wrapWidth = 0,rateWidth=0;
                    width = $container.data('width');//获取传过来的宽度参数
                    if(!width){ //如果没有参数
                        if($('.layui-input:visible')[0]){
                            width = $('.layui-input:visible')[0].clientWidth; //保存输入框与layui-input长度一致
                        }
                        if(!width){//当表格是可编辑的汇率输入框为0时，需要重置它的宽度
                            width = 146;
                        }
                    }
                    width = parseInt(width);
                    wrapWidth = width + 'px';
                    rateWidth = width - 18;

                    $container[0].clientWidth = rateWidth;
                    // $container.css({
                    //     "width":rateWidth+"px"
                    // });
                    $container.css("cssText",'border:none !important;width:'+rateWidth+'px')
                    $container.wrap('<div class="pl rateWrap" style="width:'+wrapWidth+'"></div>').after('<span name="percentsymbol" class="inputMark">'+symbol+'</span>')
                }
                $container.addClass("init textRight input-inline");

                var v = $container.val();
                if(!reset && v !=''){ //存贮默认值
                    $container.data('defaultvalue',v);
                }

                var num = new FormatNumber();
                num.init(settings);

                // 强制：如果 visible 是空的，就让 hidden 也为空
                var $hidden = $('#' + (name || vname));
                $container.on('input blur', function() {
                    if ($(this).val().trim() === '') {
                        $hidden.val('');
                    }
                });
            }
        })
    }
    RateType.fn.initMax = function (max,rate,decimal,superMax) {
        var that = this;
        if (superMax !== undefined){
            max = superMax;
        }
        if(max){
            var decimal = rate =='percent'?4:(rate =='thousands'?3:'');;//设置默认小数，百分位为四位，千分位为三位
            var  r = rate =='percent'?100:(rate =='thousands'?1000:''); //换算比
            if(max > 1 && superMax === undefined){
                max = 1;
            }
            max = bt.money.mul(max,r,decimal);
        }else{
            max = rate =='percent'?100:(rate =='thousands'?1000:''); //换算比
        }
        return max;
    }

    RateType.fn.makeCallBack = function(callBack){
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

    RateType.fn.val = function(bizName){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var id = that.currentObject.data('id')||that.currentObject.data('name');
        if(bizName === undefined){
           // var rawVal = that.currentObject.val();
            var rawVal = $('#'+id).val()
            if (rawVal === null || rawVal === undefined || rawVal.trim() === '') {
                return null;
            }
            return rawVal;
        }else{
            var superMax= that.currentObject.data('supermax');
            var max = superMax ===undefined?1:superMax;
            var superMin = that.currentObject.data('supermin');
            var min = superMin === undefined?0:superMin;

            if(typeof bizName === 'string' && !bizName.trim()){
                bizName = '';
            }else  if(bizName > max){
                bizName = max;
            }else if(bizName < min){
                bizName = min;
            }

            that.currentObject.val(bizName).removeClass("init");
            $('#'+id).remove();
            that.render(that.currentObject,'reset')
        }

    };

    RateType.fn.attr= function(attr,value){
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
            //将真实的值赋给输入框,然后再初始化
            var currentValue = that.currentObject.siblings("input").val();

            that.currentObject.removeClass("init").val(currentValue);

            that.currentObject.siblings("input").remove();
            that.render(that.currentObject)
        }
    };
    //获取当前元素利率参数并转换为具体符号
    RateType.fn.getElementRate= function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var currentRate = that.currentObject.data("ratetype".toLowerCase());
        if(!currentRate){
            if(that.rate){
                currentRate = that.rate;
            }else{
                currentRate = that.syncGetSysRate();
            }
        }
        return that.toSymbol(currentRate);
    };

    //将利率转化为符号
    RateType.fn.toSymbol = function (rateType) {
        var that = this;
        if(!rateType){
            return
        }else{
            var symbol = rateType =='percent'?'%':(rateType =='thousands'?'‰':'');
            return symbol;
        }
    }

    //同步请求获取系统利率参数
    RateType.fn.syncGetSysRate= function(){
        var that = this;
        var sysRate = '';
        $.ajax({
            type:'GET',
            url:basePath + '/sys/config/type/sys/name/rate',
            async:false,
            dataType: 'json',
            success: function(data) {
                var rate = data.value; //0是percent 1是thousands
                if(rate == 1){
                    sysRate = "thousands";
                }else if(rate == 0){
                    sysRate = 'percent';
                }
            }
        });
        that.rate = sysRate;
        var obj = {
            rate:sysRate,
            time:Date.parse(new Date())
        }
        localStorage.setItem('rateType',JSON.stringify(obj));//将obj存储到rateType字段
        return sysRate;
    };
    /**
     利率格式化
     @param {Number|String} num 要格式化数字
     @param {String} type 格式化类型'percent','thousand'
     @param {Int} decimal 小数位数， 默认为2， 不足补0
     @param {String} separator 千位分隔符 默认为逗号
     */

    RateType.fn.format= function(num,type,decimal,separator,symbol1){
        var that = this;
        if(num == null || num ==""){
            return num;
        }
        num = parseFloat(num);
        type = type || that.getLocalStorageRate() || that.syncGetSysRate();
        var symbol;
        //转换成千分/百分 value 为值，rate转换比例
        if(type){
            if(type =='percent'){
                decimal = decimal|| 4 ;
                num =  bt.money.mul(num,100,decimal);
                symbol = '%';

            }else if(type =='thousands'){
                decimal = decimal || 3;
                num =  bt.money.mul(num,1000,decimal);
                symbol = '‰';
            }
        }
        var obj = {
            separator: separator === undefined ? ',':separator,
            decimal: decimal
        }

        num =  bt.money.format(num,obj) + (symbol1 === undefined?symbol:symbol1);

        return num
    };

    function throwError(msg) {
        throw new Error("[bt-biz-RateType:" + msg);
    };

    var rateType = new RateType();
    var obj = {
        init:function(ops){
            rateType.init(ops);
            return this;
        },
        render:function(){
            rateType.render();
        },
        get:function(bizName){
            var obj = $('input[data-biz-name="'+bizName+'"]');
            if(obj.length ===0){
                throw new Error("[bt-biz-RateType:元素容器找不到，具体设置格式请参考文档API");
            }
            rateType.currentObject = obj;
            return rateType;
        },
        getFun:function(){
            return rateType;
        },
        getSystemRate:function(){ //获取系统利率符号
            var sysRate = '';
            if(rateType.rate){
                sysRate =  rateType.rate;
            }else{
                sysRate = rateType.syncGetSysRate();
            }
            return rateType.toSymbol(sysRate)
        }
    }
    exports('rateType',obj);
})
