/**
 * 业务组件：票据池接口业务组件
 * zhenghj
 * 2020年1月3日
 */
layui.define(['jquery','form'],function(exports){
    var $  = layui.jquery,form = layui.form();
    var BillPoolif = function () {
        this.bizName = "poolif";
        this.currentObject = null;
    }

    BillPoolif.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */

    BillPoolif.prototype.init = function (option) {
        var _this = this;
        $('.bt-biz-billPoolif').each(function(){
            var that = $(this);
            _this.initSelect(that,option);
        });
    }
    BillPoolif.prototype.render = function (option){
        var _this = this;
        $('.bt-biz-billPoolif:not(.init)').each(function(){
            var that = $(this);
            _this.initSelect(that,option);
        });
    }

    function throwError(msg) {
        throw new Error("[bt-biz-billPoolif]:" + msg);
    }

    BillPoolif.prototype.initSelect = function(elem,option){
        var that = this;
        var name = elem.data("name");
        var className = "biz-billPoolif-select" + name;
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
                textField:"pifName",
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

    BillPoolif.prototype.setWindowValue = function(jThis) {
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

    BillPoolif.prototype.makeCallBack = function(callBack){
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

    BillPoolif.prototype.reset = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        that.currentObject.html("").removeClass("init");
        that.render()
    }

    BillPoolif.prototype.getData = function(elem,callback,option){
    	var data;
    	var params={};
		$.ajax({
	    	type: "GET",
	    	url: basePath + "/billpool/poolif/getAllPoolif",
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
    BillPoolif.prototype.attr = function(obj){
        this.currentObject.removeClass("init");
        this.render(obj);
    }
    BillPoolif.prototype.val = function(bizName){
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

    var billPoolif = new BillPoolif();
    var obj = {
        init: function(option){
            billPoolif.init(option);
            return this;
        },
        render:function(){
            billPoolif.render();
        },
        get:function(bizName){
            var currentObj = window.biz[billPoolif.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            billPoolif.currentObject = currentObj;
            return billPoolif;
        },
    };
    //输出test接口
    exports('billPoolif', obj);
});