/**
 * 业务组件：票据池业务组件
 * zhenghj
 * 2020年1月6日
 */
layui.define(['jquery','form'],function(exports){
    var $  = layui.jquery,form = layui.form();
    var BillPool = function () {
        this.bizName = "billPool";
        this.currentObject = null;
    }

    BillPool.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */

    BillPool.prototype.init = function (option) {
        var _this = this;
        $('.bt-biz-billPool').each(function(){
            var that = $(this);
            _this.initSelect(that,option);
        });
    }
    BillPool.prototype.render = function (option){
        var _this = this;
        $('.bt-biz-billPool:not(.init)').each(function(){
            var that = $(this);
            _this.initSelect(that,option);
        });
    }

    function throwError(msg) {
        throw new Error("[bt-biz-billPool]:" + msg);
    }

    BillPool.prototype.initSelect = function(elem,option){
        var that = this;
        var name = elem.data("name");
        var className = "biz-billPool-select" + name;
        elem.addClass(className);

        var defaultValue = elem.data('defaultValue'.toLowerCase());
        var required = !!elem.data('required');
        var disabled = elem.data('disabled');
        var disabledValue = elem.data('disabledValue'.toLowerCase());
        var callback = elem.data('callback');
        if(callback){
            callback = that.makeCallBack(callback);
        }
        var data = that.getData(elem,callback,option);
        bt.use(['select'], function() {
            bt.select.init({
                elem:elem,
                valueField: "id",
                textField:"bpName",
                defaultVal:defaultValue,
                name:name,
                data:data,
                required:required,
                change:callback,
                search:true,
                disabled:disabled,
                disabledValue:disabledValue,
            });
            $(elem).addClass('init');
        })
        that.setWindowValue(elem);
    }

    BillPool.prototype.setWindowValue = function(jThis) {
        var that = this;
        var bizName = jThis.data('biz-name'.toLowerCase());
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[that.bizName]) {
                window.biz[that.bizName] = [];
            }
            window.biz[that.bizName][bizName] = jThis;
        }
        jThis.addClass('init');
    }

    BillPool.prototype.makeCallBack = function(callBack){
    	if(callBack && typeof(callBack) != "function"){
    		//校验下拉树的回调方法是否存在
    		try {
    			callBack = eval(callBack);
    		} catch(e) {
    			alert(callBack + 'not exist！');
    		}
    	}
    	return callBack;
    }

    BillPool.prototype.reset = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        that.currentObject.html("").removeClass("init");
        that.render()
    }

    BillPool.prototype.getData = function(elem,callback,option){
    	var data;
    	var params={};
		$.ajax({
	    	type: "GET",
	    	url: basePath + "/billpool/pool/getAllPool",
	       	data: params,//将对象序列化成JSON字符串
	       	dataType:"json",
	       	async: false, 
	    	success: function(result){
	    		if(result){
	    			data=result;
	        	}
	    	},
	    	error: function(res){
	    	}
	    });
        return data;
    }
    BillPool.prototype.attr = function(obj){
        this.currentObject.removeClass("init");
        this.render(obj);
    }
    BillPool.prototype.val = function(bizName){
        var that = this;
        if(!bizName){
            if(!that.currentObject){
                return "";
            }
            var v = that.currentObject.find("option:selected").val();
            var value = v;
            return value;

        }else{
            if(that.currentObject.find("select").length !== 0){
                if(typeof bizName ==='string'){
                    that.currentObject.find("select").val(bizName);
                }
                if(typeof bizName ==='object'){
                    for(var key in bizName){
                        if(key =="disabled" && bizName[key]){
                            that.currentObject.find("select").attr("disabled",true)
                        }
                        if(key =="disabled" && !bizName[key]){
                            that.currentObject.find("select").removeAttr("disabled")
                        }
                        if(key =="disabledValue" && bizName[key]){
                            that.currentObject.find('select option[value="'+bizName[key]+'"]').attr("disabled",true)
                        }
                    }
                }
                form.render("select")
            }
            form.render("select")
        }

    }

    var billPool = new BillPool();
    var obj = {
        init: function(option){
            billPool.init(option);
            return this;
        },
        render:function(){
            billPool.render();
        },
        get:function(bizName){
            var currentObj = window.biz[billPool.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            billPool.currentObject = currentObj;
            return billPool;
        },
    };
    //输出test接口
    exports('billPool', obj);
});