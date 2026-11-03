/**
 * 银行帐号格式化组件
 * 本组件依赖jquery
 * liuyj
 * 2017年4月25日
 * 0.0.1 弹出框式格式化
 * 0.0.2 丢去焦点时，隐藏
 * 0.0.3 解决多个bank的问题
 * 0.0.4 解决input过宽的问题
 * 0.0.5 解决input输入字母的问题，注册事件采用委托方式;
 * 0.0.6 更新为offset定位;
 * 0.0.7 银行帐号内容不进行校验;
 * 0.0.8 代码优化;
 * 0.0.9 maxLength由21改成25
 * 0.0.10 maxLength 更改成小写
 * 0.0.11 maxlength由25改成30
 * 0.0.12 不允许输入中文
 */
layui.define(['jquery','form'], function(exports){
    var $ = layui.jquery,form = layui.form();
    var Bank =function(){
        this.config={
            elem:".bt-biz-bank",//容器
            // maxLength:25,
            maxLength:30 // 20200224接黄茜要求，将默认长度由25改成30
        };
    }
    /**
     * 参数设置
     * @param {Object} options
     */
    Bank.prototype.set = function(options){
        var that = this;
        $.extend(true, that.config, options);
        return that
    }

    //初始化
    Bank.prototype.init = function(){
        var that = this;
        var _config = that.config;
        if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
            throwError('btdate error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        var maxLength = $(_config.elem).data("maxLength".toLocaleLowerCase());
        if(maxLength){
            _config.maxLength = maxLength;
        }
        var html = getHtml(_config);

        if($(".inputCard-wrap").length ===0){
            $("body").append(html)
        }
        setBankNum(_config);
    }

    //生成HTML
    var getHtml = function(ops){
        var temp ='';
        temp +='<div class="inputCard-wrap  layui-layer-tips" style="background:#0a95e4;color:#fff;font-weight:400;line-height:16px;border-radius:3px">';
        temp += '<i class="layui-layer-TipsG layui-layer-TipsT" style="border-right-color: rgb(53, 149, 204);"></i><div style="max-width:450px">';
        // 下面是t2优化时的代码
        //temp +='<div class="inputCard-wrap  layui-layer-tips" style="background:#17a0d9;color:#fff;font-weight:400;line-height:16px;border-radius:3px">';
        //temp += '<i class="layui-layer-TipsG layui-layer-TipsT" style="border-right-color:#17a0d9;"></i><div style="max-width:450px">';
        temp += '<div class="panelCard layui-layer-TipsG"></div></div></div>';
        return temp;
    }

    function setBankNum(ops){
        var panel = $(".inputCard-wrap");
        var $input = $(ops.elem);
        /* 银行卡号实时验证放大显示 */
        $("body").on("input propertychange paste",ops.elem,function(e){
            // $(e.target).val($(e.target).val().replace(/[^0-9]/g,''));//只能输入数字
            $(e.target).val($(e.target).val().replace(/[\u4e00-\u9fa5]/g,''));//不允许输入中文
            var self = $.trim(e.target.value),
                val = self;
            if(self.length > ops.maxLength){
                this.value = self.substr(0, ops.maxLength);
                return this.value;
            }
            if(val == self && val !==''){
                setoffset($(this),panel)
                panel.show();
                val = self.replace(/(....)(?=.)/g, '$1 ');
                panel.find('div').html(val);
            }else{
                panel.hide();
                return self;
            }
        });
        $input.unbind('focusin');
        $input.bind('focusin',function(e){
            var self = $.trim(e.target.value),
                val = self.replace(/(....)(?=.)/g, '$1 ');
            $(".inputCard-wrap")
            if(val != '') {
                setoffset($(this),panel)
                panel.show();
                panel.find('div').html(val);
            }
        });
        $input.unbind('focusout');
        $input.bind('focusout',function(e){
            panel.hide();
        });
    }
    //设置放大数字框定位.
    function setoffset(inputElem,divElem){
        var offset = inputElem.offset();
        var lt = offset.left;
        var tp = offset.top ;
        var h = inputElem.height()+2;
        if(tp < 70){
            tp = tp + h +10;
            divElem.find('i').removeClass('layui-layer-TipsT').addClass('layui-layer-TipsB');
        }else{
            tp = tp - h -20;
            divElem.find('i').removeClass('layui-layer-TipsB').addClass('layui-layer-TipsT');
        }
        divElem.css({
            left:lt + "px",
            top:tp +"px"
        })
    }

    function throwError(msg) {
        throw new Error(msg);
    };
    var obj = {
        init:function(ops){
            var bank = new Bank();
            bank.set(ops);
            bank.init();
        }
    }
    //输入口
    exports('bank', obj);
}).addcss('../../../css/bank.css','bankcss')
