/**
 * 时间组件
 * 本组件依赖layui的laydate，以及jquery
 * liuyj
 * 2017年4月24日
 * 0.0.1 年月日时分秒
 * 0.0.2 年月日
 * 0.0.3 时间范围
 * 0.0.4 解决input样式及中间符号
 * 0.0.5 新增设置值的方法setValue(),及添加设置默认值的参数
 * 0.0.6 解决必填及验证问题
 * 0.0.7 将时间格式化整合在一起
 * 0.0.8 代码优化
 * 0.0.9 启动最新的laydate5.0组件

 */
layui.define(['dateType','jquery','form'], function(exports){
    "use strict";
    var laydate = layui.dateType,$ = layui.jquery,form = layui.form();
    var Btdate =function(){
        this.config={
            elem:undefined,//容器
            placeholder:" ",//单独输入框中默认值
            name:undefined,//独立输入框架属性名称
            istoday:false,//是否显示今天
            issure:true,//是否显示确认
            isclear:true,//是否显示清空
            defaultVal:"",//设置默认值
            // min:"1900-01-01 00:00:00",//最小日期
            // max:"2099-12-31 23:59:59",//最大日期
            start:"",//最大日期
            startname:undefined,//开始输入框属性名称
            startplaceholder:" ",//开始输入框默认
            startDefaultVal:"",//设置默认值
            endname:undefined,//结束输入框属性名称
            endplaceholder:" ",//开始输入框默认
            endDefaultVal:"",//设置默认值
            labelname:undefined,//标签
            format:"YYYY-MM-DD",
            istime: false,//是否显示时分秒设置
            // event:'click',
            required:false
        }

        this.randomNumber = 0;
    };
    /**
     * 参数设置
     * @param {Object} options
     */
    Btdate.prototype.set = function(options){
        var that = this;
        $.extend(true, that.config, options);
        that.randomNum();
        return that;
    };

    //初始化
    Btdate.prototype.init = function(){
        var that = this;
        var _config = that.config;
        if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
            throwError('btdate error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        if(_config.defaultVal.length > 19){ //解决后台时间长度超过19的问题
            _config.defaultVal = _config.defaultVal.substr(0,19);
        }
        //保持格式一致性
        _config.placeholder =_config.placeholder.substr(0,_config.format.length);
        //单一时间
        if(_config.name !== undefined && _config.startname ===undefined &&_config.endname ===undefined){
            var html =getSingleHtml(_config);
            $(_config.elem).html(html);
            laydate.init(null,_config.choose);//将回调方法传给dateType组件
        }
        //时间范围
        if(_config.name == undefined && _config.startname !==undefined &&_config.endname !==undefined){
            var html = gethtml(_config,that.randomNumber);
            $(_config.elem).html(html);
            laydate.init();
            // setDoubleDate(_config);
        }
        if(_config.name == undefined && ( _config.startname === undefined || _config.endname ===undefined)){
            throwError('btdate error:需要配置name参数(时间范围需要配置startname/endname)，请检查.');
        }

        
    };

    //生成单个时间输入HTML
    function getSingleHtml(ops){
        var temp ='';
        var html = setDateRequired(ops.required,ops.format);
        if(ops.labelname !==undefined){
            temp +='<label class="layui-form-label">'+ops.labelname+'</label>';
        }
        var maxMinHtml = '';

        if(ops.min){
            maxMinHtml += ' data-min ="'+ ops.min +'"';
        }

        if(ops.max){
            maxMinHtml += ' data-max ="'+ ops.max +'"';
        }

        temp += '<input class="layui-input bt-input-inline bt-biz-dateType"  placeholder="'+ops.placeholder+'" ';
        temp += 'value="'+(ops.defaultVal?ops.defaultVal:"")+'" id="'+ops.name+'" name="'+ops.name +'"';
        temp += html;
        temp += maxMinHtml;
        temp += '/>';
        return temp;
    };

    //生成时间范围HTML
    function gethtml(ops,num){
        var temp ='';
        var html = setDateRequired(ops.required,ops.format);

        var startMinHtml = '',endMaxHtml = '';
        if(ops.min){
            startMinHtml += ' data-min ="'+ ops.min +'"';
        }

        startMinHtml += ' data-endid ="endid'+num + '"';

        if(ops.max){
            endMaxHtml += ' data-max ="'+ ops.max +'"';
        }
        endMaxHtml += ' id ="endid'+ num +'"';

        if(ops.labelname !==undefined){
            temp +='<label class="layui-form-label">'+ops.labelname+'</label>'
        }
        temp += '<input class="layui-input bt-input-inline bt-biz-dateType" placeholder="'+ops.startplaceholder+'" ';
        temp += 'value ="'+(ops.startDefaultVal?ops.startDefaultVal:ops.startplaceholder)+'" id="'+ops.startname+'" name="'+ops.startname+'"';
        temp += html;
        temp += startMinHtml;
        temp += '>';
        temp += ' <span class="date-mid">~</span> ';
        temp += '<input class="layui-input bt-input-inline bt-biz-dateType"  placeholder="'+ops.endplaceholder+'" ';
        temp += 'value ="'+(ops.endDefaultVal?ops.endDefaultVal:ops.startplaceholder)+'"  name="'+ops.endname +'"';
        temp += html;
        temp += endMaxHtml;
        temp += '>';
        return temp;
    };

    //设置时间输入的必值属性
    function setDateRequired(required,format){
        var html ='';
        if(required & format =="YYYY-MM-DD"){
            html += ' lay-verify="date"'
        }
        if(required & format =="YYYY-MM-DD hh:mm:ss"){
            html += ' lay-verify="datetime"  data-type="datetime"'
        }
        return html;
    }

    //时间范围逻辑
    // function setDoubleDate(ops){
    //     var start = {
    //         max: ops.max,
    //         istoday: ops.istoday,
    //         format:ops.format,
    //         istime: ops.istime,
    //         event:ops.event,
    //         choose: function(datas){
    //             end.min = datas; //开始日选好后，重置结束日的最小日期
    //             end.start = datas //将结束日的初始值设定为开始日
    //         }
    //     };

    //     var end = {
    //         // min: laydate.now(),
    //         format:ops.format,
    //         max: ops.max,
    //         istoday: ops.istoday,
    //         istime: ops.istime,
    //         event:ops.event,
    //         choose: function(datas){
    //             start.max = datas; //结束日选好后，重置开始日的最大日期
    //         }
    //     };

    //     $('#'+ops.startname).click(function(){
    //         start.elem = this;
    //         laydate(start);
    //     });

    //     $('#'+ops.endname).click(function(){
    //         end.elem = this;
    //         laydate(end);
    //     });
    // };


    //一个时间
    // function setSingleDate(ops,elem){
    //     var setting={
    //         elem:elem,
    //         istoday:ops.istoday,
    //         issure:ops.issure,
    //         isclear:ops.isclear,
    //         min:ops.min,
    //         max:ops.max,
    //         format:ops.format,
    //         istime: ops.istime,
    //         event:ops.event,
    //         choose:function(datas){
    //             if(typeof ops.choose ==='function'){
    //                 ops.choose(datas)
    //             }
    //         }
    //     };
    //     laydate(setting);
    // };

    Btdate.prototype.setValue = function(ops){
        var that = this;
        bt.each(ops,function(key,v){
            if(key =="value"){
                that.config.defaultVal = v
            }
            if(key =="startValue"){
                that.config.startDefaultVal = v;
            }
            if(key =="endValue"){
                that.config.endDefaultVal = v
            }
        });
        that.init();
    }

    //时间格式化
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    Btdate.prototype.randomNum = function () {
        var num = Math.random() * 1000000;
        num = parseInt(num, 10);
        this.randomNumber = num;
        return num;
    }

    function throwError(msg) {
        throw new Error(msg);
    };
    var obj = {
        init:function(ops){
            var btdate = new Btdate();
            btdate.set(ops);
            btdate.init();
            return btdate;
        }
    };
    //输入口
    exports('btdate', obj);
});