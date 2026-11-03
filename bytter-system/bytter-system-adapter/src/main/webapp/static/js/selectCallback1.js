/**
 * 扩展组件：通用选择回调
 * 周亮
 * 2017年6月30日
 * 0.0.1 组件初始化
 * 0.0.2 setInputSelectCallbackCallBack 方法中的top更改为window.top,为了解决多次选中时，偶尔出现top值为null的问题
 * 0.0.3 top改成了window，为了解决多次选择时，偶尔出现top 值为null的问题
 * 0.0.4 新增multiLayer参数，支持弹框中再弹框回调
 * 0.0.5 新增sendParamsToChildFrame参数,用于解决post传参中文乱码的问题，通过此参数，可以直接调用子iframe方法
 * 0.0.6 新增isSpliceParamsOnUrl参数,判断是否需要在url上拼接参数
 */
layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;
    var SelectCallback = function () {
        this.config = {
            prefix :'bt-biz-',
            name: 'bankAcc',
            width:850,
            // width:1028, //t2优化参数
            height:620,//600;
            //height:700,//600;//t2优化参数
            url:'',
            multiLayer:false, //弹框中再弹框回调
            multiIframes:false, // 三级iframe弹框回调
            isSpliceParamsOnUrl:true,//是否需要URL拼接参数
            // sendParamsToChildFrame:'', //向子iframe传递参数的方法名，此方法是子iframe的全局方法,解决post参数有中文乱码的问题
        }
    }

    SelectCallback.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */
    SelectCallback.prototype.init = function (options) {
        var that = this;
        $.extend(true, that.config, options);
        that.setInputSelectCallbackCallBack();
        var bankAcc = {};
        bankAcc.param = {};
        bankAcc.config = options;
        bankAcc.config.width = that.config.width;
        bankAcc.config.height = that.config.height;
        bankAcc.config.MultiLayer = that.config.multiLayer;
        bankAcc.config.multiIframes = that.config.multiIframes;
        bankAcc.config.isSpliceParamsOnUrl = that.config.isSpliceParamsOnUrl;
        if (options.sendParamsToChildFrame){
            bankAcc.config.sendParamsToChildFrame = options.sendParamsToChildFrame;
        }
        window.btSelectCallBack[that.config.prefix + that.config.name]['obj'] = bankAcc;
        $(document).on('click','.'+ that.config.prefix + that.config.name, function () {
            var _that = $(this);
            var beforeClick = _that.data("beforeClick".toLowerCase());
            if(beforeClick){
                beforeClick = that.makeCallBack(beforeClick);
                if(!beforeClick(_that)){
                    return;
                }
            }

            var className = _that.attr('class');
            var classArr = className.split(" ");
            var currentBizName;
            for(var i=0;i<classArr.length;i++){
                var n = classArr[i];
                if(n.indexOf(that.config.prefix)!=-1){
                    currentBizName = n;
                    break;
                }
            }
            if(!currentBizName){
                return;
            }
            window.btSelectCallBack[currentBizName]['obj'].param.currentDom = _that;
            var callBack = _that.data('callback');

            if (!callBack) {
                that.throwError("尚未配置data-callback属性！")
            }

            window.btSelectCallBack[currentBizName]['obj'].param.currentCallBack = that.makeCallBack(callBack);
            var sendParams = window.btSelectCallBack[currentBizName].sendParams;
            var maxminVal= true;
            if(window.btSelectCallBack[currentBizName]['obj'].config.maxmin!=undefined
                &&!window.btSelectCallBack[currentBizName]['obj'].config.maxmin ){
                maxminVal= false;
            }
            var url = window.btSelectCallBack[currentBizName]['obj'].config.url +'?_btv='+new Date().getTime();
            if (window.btSelectCallBack[currentBizName]['obj'].config.isSpliceParamsOnUrl){
                url += (sendParams?"&"+$.param(sendParams):'')
            }
            // console.log("123456")
            top.layer.open({
                type: 2,
                // id:'mytest',
                //area: [that.config.width + 'px', that.config.height + 'px'],
                // area: [window.btSelectCallBack[currentBizName]['obj'].config.width + 'px', window.btSelectCallBack[currentBizName]['obj'].config.height + 'px'],
                area: ['75%','75%'],
                maxmin: maxminVal,
                content:url,
                // content: window.btSelectCallBack[currentBizName]['obj'].config.url +'?_btv='+new Date().getTime() + (sendParams?"&"+$.param(sendParams):''),
                success: function (layero, index) {
                    window.btSelectCallBack[currentBizName]['obj'].param.currentIndex = index;
                    if (window.btSelectCallBack[currentBizName]['obj'].config.multiLayer){ //如果存在弹框中再弹框，把父级弹框的
                        var parentIndex = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        var body = top.layer.getChildFrame('body', index);
                        body.attr('layerindex',parentIndex);
                    }
                    if (window.btSelectCallBack[currentBizName]['obj'].config.multiIframes){ //存在三级iframe弹框回调
                        var parentIndex = top.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        var body = top.layer.getChildFrame('body', index);
                        body.data('multiiframes',true);
                    }
                    var sendParamCallback = window.btSelectCallBack[currentBizName]['obj'].config.sendParamsToChildFrame; //向子iframe传参回调方法
                    if (sendParamCallback){
                        var iframeWin = top.window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：
                        if (typeof(iframeWin[sendParamCallback]) != "function"){
                            try {
                                sendParamCallback = iframeWin.eval(sendParamCallback);
                                sendParamCallback(sendParams)
                            } catch (e) {
                                alert(sendParamCallback + 'not exist！');
                            }
                        }else {
                            iframeWin[sendParamCallback](sendParams)
                        }
                    }
                }
            });
        });

    }

    SelectCallback.prototype.throwError = function(msg) {
        var that = this;
        throw new Error("["+that.config.prefix + that.config.name+"]:" + msg);
    };
    /*
    *  top改成了window，为了解决多次选择时，偶尔出现top 值为null的问题
    */
    SelectCallback.prototype.setInputSelectCallbackCallBack = function () {
        var that = this;
        //if(!window.btSelectCallBack){
        //    window.btSelectCallBack = [];
        //}
        var currentBizName = that.config.prefix + that.config.name;
        window.btSelectCallBack[currentBizName] = {};
        window.btSelectCallBack[currentBizName]['callback'] = function (data) {
            var bizObj = window.btSelectCallBack[currentBizName]['obj'];
            bizObj.param.currentCallBack(data,bizObj.param.currentDom);
            top.layer.close(bizObj.param.currentIndex);
        }
    }

    SelectCallback.prototype.makeCallBack = function (callBack) {
        //校验回调方法是否存在
        if (callBack && typeof(callBack) != "function") {
            try {
                callBack = eval(callBack);
            } catch (e) {
                alert(callBack + 'not exist！');
            }

        }
        return callBack;
    }

    SelectCallback.prototype.set = function (bizName,obj) {
        if(typeof(obj) !== 'object'){
            return;
        }
        var that = this;
        var currentBizName = that.config.prefix + bizName;
        window.btSelectCallBack[currentBizName].sendParams = obj;

    };
    // SelectCallback.prototype.set = function (bizName,obj) {
    //
    // }

    var selectCallback = new SelectCallback();
    /**
     * 解决偶尔发生找不到回调的问题
     * */
    if(!window.btSelectCallBack){
        window.btSelectCallBack = [];
    }
    var obj = {
        init: function (option) {
            selectCallback.init(option);
            return this;
        },
        set:function(bizName,obj){
            selectCallback.set(bizName,obj);
        },
        // setParams:function(){
        //
        // }
    };
    //输出test接口
    exports('selectCallback1', obj);
});
