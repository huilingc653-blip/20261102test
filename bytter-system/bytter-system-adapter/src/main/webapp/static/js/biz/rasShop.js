/**
 * 业务组件：营收稽核门店信息业务组件
 * zhengg
 * 2019年5月5日
 * 0.0.1创建营收稽核门店下来选择
 */
layui.define(['jquery','form'],function(exports){
    var $  = layui.jquery,form = layui.form();
    var RasShop = function () {
        this.bizName = "rasShop";
        this.currentObject = null;
    }

    RasShop.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */

    RasShop.prototype.init = function (option) {
        var _this = this;
        $('.bt-biz-rasShop').each(function(){
            var that = $(this);
            _this.initSelect(that,option);
        });
    }
    RasShop.prototype.render = function (option){
        var _this = this;
        $('.bt-biz-rasShop:not(.init)').each(function(){
            var that = $(this);
            _this.initSelect(that,option);
        });
    }

    function throwError(msg) {
        throw new Error("[bt-biz-rasShop]:" + msg);
    }

    RasShop.prototype.initSelect = function(elem,option){
        var that = this;
        var name = elem.data("name");
        var className = "biz-rasShop-select" + name;
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
                valueField: "value",
                textField:"text",
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

    RasShop.prototype.setWindowValue = function(jThis) {
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

    RasShop.prototype.makeCallBack = function(callBack){
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

    RasShop.prototype.reset = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        that.currentObject.html("").removeClass("init");
        that.render()
    }
    
    RasShop.prototype.getData = function(elem,callback,option){
    	var corpId = "";
    	if(option&&option.corpId){
    		corpId=option.corpId;
    	}
        var grantType = elem.data('grantType');
//        if(!corpVal){
//        	var corpId=elem.data('corp-id');
//        	if(corpId){
//        		corpVal = $("#"+corpId).val();
//        	}
//        }
//        if(!corpVal){
//        	var corpName=elem.data('corp-name');
//        	if(corpName){
//            	corpVal = $("input[name='"+corpName+"']").val();
//        	}
//        }
    	var data;
    	var params={"corpId":corpId,"grantType":grantType};
		$.ajax({
	    	type: "POST",
	    	url: basePath + "/ras/shopGrant/loadShopByCorpIdOrGrantType",
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
    RasShop.prototype.attr = function(obj){
        this.currentObject.removeClass("init");
        this.render(obj);
    }
    RasShop.prototype.val = function(bizName){
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

    var rasShop = new RasShop();
    var obj = {
        init: function(option){
        	rasShop.init(option);
            return this;
        },
        render:function(){
        	rasShop.render();
        },
        get:function(bizName){
            var currentObj = window.biz[rasShop.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            rasShop.currentObject = currentObj;
            return rasShop;
        },
    };
    //输出test接口
    exports('rasShop', obj);
});