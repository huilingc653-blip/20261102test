/**
 * 组件：货币类型业务组件
 * 刘勇军
 * 2017年6月27日
 * 0.0.1 实现千分位、有效数字、正数、负数；
 * 0.0.2 将capNum  money方法，全部合并在一起；
 * 0.0.3 添加linkstart参数，用于金额区间业务；
 * 0.0.4 添加reset方法；
 * 0.0.5 解决reset方法中，有多个隐藏input的问题；
 * 0.0.6 解决多次重置的、最大最小时时，大写不起作用，添加了linkElemDiv参数
 * 0.0.7 赋值时，触发回调
 * 0.0.8 赋值时，取消触发回调
 * 0.0.9 新增加reset方法，重置参数
 * 0.0.10 新增加attr方法，设置参数
 * 0.0.11 修改reset方法，重置参数
 * 0.0.12 修改当min 等于 -0时
 * 0.0.13 删除重置方法，统一有form来处理
 * 0.0.14 第一次初始化时，把默认值存贮起来
 * 0.0.15 val()传空字符患的问题
 * 0.0.16 赋值不能超过１００亿，超过了，直接提示并终止赋值

 */


layui.define(['jquery', 'formatnumber','money',"capNumber","layer"], function(exports) {
    var $ = layui.jquery;
    var btMoney = bt.money;

    var Money = function () {
        this.config ={
            elem:""
        };
        this.bizName = "moneyType";
        this.currentObject = null;
    }

    Money.fn = Money.prototype;

    Money.fn.v = '0.0.15';

    /**
     * 方法入口，初始化
     */
    Money.fn.init = function(options){
        var that = this;
        that.render()
    }

    Money.fn.render = function(reset){
        var that = this;
        var settings = {};
        // var domObj = ops || $('.bt-biz-moneyType:not(.init)');
        var domObj = $('.bt-biz-moneyType:not(.init)');
        bt.each(domObj,function(){
            var $container = $(this);
            var decimal = $container.data("decimal");
            if($container[0].tagName !=="INPUT"){
                var text = $container.text();
                var decimal = $container.data("decimal")
                var num = btMoney.format(text,{
                    decimal:decimal
                });
                $container.text(num);
            }else{
                var minus = $container.data('minus'),
                    max = $container.data('max'),
                    min = $container.data('min'),
                    name = $container.data('name'),
                    vname = $container.attr('name'),
                    percent = $container.data("type") =="percent",
                    bizName = $container.data("biz-name"),
                    separator = $container.data("separator"),
                    callback = $container.data("callback"),
                    toUp = $container.data("toUp".toLowerCase()),
                    moneyEndId = $container.data("moneyendid".toLowerCase()),
                    link = $container.data("link"),
                    linkstart = $container.data("linkstart".toLowerCase()),
                    callback = that.makeCallBack(callback),
                    convert  = $container.data("convert"),
                    budget  = $container.data("budget");

                if( min == 0){ //当最小值等于-0时，直接改成0
                    min = 0;
                }

                if(vname && !name){
                    $container.attr("data-name",vname);
                }
                if(toUp){
                    bt.capNumber.init({
                        elem:$container,
                        linkElemDiv:"#"+toUp,
                    })
                }
                if(convert == true){
                    decimal = bt.excRateConvert;
                }
                if(budget == true){
                    decimal = bt.excRateBudget;
                }

                $container.removeAttr("name");
                $container.css(
                    {
                        "text-align":"right",
                        "padding-right":"5px"
                    }
                );
                //页面已声明maxlength时保留声明值，否则默认17
                var maxlength = $container.attr('maxlength') || 17;
                settings = {
                    elem:$container,
                    // decimal:decimal?decimal:2,
                    decimal:decimal,
                    minus:minus,
                    max:max,
                    min:min,
                    moneyEndId:moneyEndId?moneyEndId:"",
                    link:link?link:"",
                    linkstart:linkstart,
                    separator:separator,
                    callback:callback,
                    percent:percent,
                    linkElemDiv:toUp?"#"+toUp:"",
                    maxlength:maxlength,
                }

                if(moneyEndId){
                    var startName = vname?vname:name;
                    $("#"+moneyEndId).data("link",moneyEndId).data('linkstart',startName);
                    if( $container.parents('td').find(".date-mid").length ){
                        $container.parents('td').addClass("input100");
                    }else{
                        if( $container.parents('td').find('#'+moneyEndId).length ){
                            $container.parents('td').addClass("input100");
                        }
                    }
                }
                var v = $container.val();
                if(!reset && v !=''){ //存贮默认值
                    $container.data('defaultvalue',v);
                }
                $container.attr('maxlength',maxlength);//输入的值限制长度，页面已声明maxlength时保留，默认17

                var num = new FormatNumber();
                num.init(settings);

                if(!window.biz){
                    window.biz = [];
                }

                if(!window.biz[that.bizName]){
                    window.biz[that.bizName] = [];
                }
                window.biz[that.bizName][$container.data('biz-name'.toLowerCase())] =  $container;
            }
            $container.addClass("init");

        });
        // //解决moneyType格式化之后，重置的未被格式化 liuyj 20170802
        // domObj.parents('form').find('button[type=reset]').off('mousedown').on('mousedown',function(){
        //     //重置form中所有的moneyType
        //     var moneyTypes =$(this).parents('form').find('.bt-biz-moneyType.init');
        //     bt.each(moneyTypes,function () {
        //         $(this).removeClass("init").siblings('input[type="hidden"]').remove();
        //     });
        //     setTimeout(function(){
        //         if(bt.moneyType){
        //             bt.moneyType.init();//moneyType重置
        //         }
        //     }, 120);
        // });
    }

    Money.fn.makeCallBack = function(callBack){
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

    Money.fn.val = function(bizName){
        var that = this;
        var currentObject = that.currentObject;
        var id = currentObject.data('id');
        if(id === undefined){
            id = currentObject.data('name');
        }

        if(!bizName && bizName === undefined){
            if(!currentObject){
                return "";
            }
            return $("#"+id).val();
        }else{
            if(typeof bizName ==='string' || typeof bizName ==='number'){
/*              控制为千亿则不会进如该方法，则注掉
                if ( ( bizName -0 ) > 999900000000 ) { //如果传入的值大100亿，直接提示并终止赋值
                    layer.alert(bt.lang("common.msg.moreThanNineBillion"), {icon: 3,closeBtn:0}, function (index) { //输入的金额不能超过100亿'
                        layer.close(index);
                    });
                    return
                }*/
                currentObject.val(bizName);
            }
            if(typeof bizName ==='object'){
                for(var key in bizName){
                    currentObject.data(key,bizName[key]);
                }
                currentObject.val($("#"+id).val())
            }
            currentObject.removeClass("init");
            $("#"+id).remove();
            that.render('set') //参数代表是重置参数标识，
        }
    };

    Money.fn.attr = function(attr,value){
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
                that.currentObject.data(attrName,value).removeClass("init");
                var id = that.currentObject.data("moneyendid");
                //关联组件
                if(id !== undefined){
                    that.currentObject.siblings('input[type="text"]').removeClass("init");
                }
                var v = that.currentObject.siblings('input[type="hidden"]').val();

                that.currentObject.siblings('input[type="text"]').val(v); //将未格式化的值

                that.currentObject.siblings('input[type="hidden"]').remove();

                that.render('set');
            }else{
                that.currentObject.attr(attr,value);
            }
        }
    };
    //删除属性方法
    Money.fn.removeAttr = function(attr){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        if(attr){
            that.currentObject.removeAttr(attr)
        }
    }

    Money.fn.reset = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        that.currentObject.removeClass("init");
        that.currentObject.siblings("input").remove();
        that.render('set')
    }

    function throwError(msg) {
        throw new Error("[bt-biz-money:" + msg);
    };

    var money = new Money();
    var obj = {
        init:function(ops){
            money.init(ops);
            return this
        },
        get:function(bizName){
            var currentObj = window.biz[money.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            money.currentObject = currentObj;
            return money;
        },
        render:function(){
            money.render()
        },
        getFun:function(){
            return btMoney
        },
    }
    exports('moneyType',obj)
})
