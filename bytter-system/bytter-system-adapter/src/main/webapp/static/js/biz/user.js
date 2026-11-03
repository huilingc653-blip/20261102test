/**
 * 业务组件：用户选择
 * 周亮
 * 2017年7月29日
 * 0.0.1创建用户选择功能
 */
layui.define(['jquery','form'],function(exports){
    var $  = layui.jquery,form = layui.form();
    var User = function () {
        this.bizName = "users";
        this.currentObject = null;
    }

    User.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */

    User.prototype.init = function () {
        var that = this;
        that.render();
    }
    User.prototype.render = function (){
        var _this = this;
        $('.bt-biz-user:not(.init)').each(function(){
            var that = $(this);
            //表单类型
            var type = that.data('type');
            if(!type){
                throwError("尚未配置data-type属性！")
            }
            var flag = true;
            switch (type){
                case 'checkbox':
                    _this.initCheckBox(that);
                    break;
                case 'select':
                    _this.initSelect(that);
                    break;
                case 'radio':
                    _this.initRadio(that);
                    break;
            }
        });
    }

    function throwError(msg) {
        throw new Error("[bt-biz-user]:" + msg);
    }

    User.prototype.initCheckBox = function(elem){
        var that = this;
        var resource = elem.data("resource");
        var name = elem.data("name");
        var className = "biz-user-checkboxs" + name;
        elem.addClass(className);
        var defaultValue = elem.data('defaultValue'.toLowerCase());
        var required = !!elem.data('required');
        var data = that.getData(elem);
        bt.use(['checkboxs'], function() {
            bt.checkboxs.init({
                    elem:elem,
                    name: name,
                    valueField:"value",
                    textField:"text",
                    checkAll:true,
                    data:data,
                    required:required
                }
            );
        });
    }

    User.prototype.initSelect = function(elem){
        var that = this;
        var name = elem.data("name");
        var className = "biz-user-select" + name;
        elem.addClass(className);

        var defaultValue = elem.data('defaultValue'.toLowerCase());
        var required = !!elem.data('required');
        var disabled = elem.data('disabled');
        var disabledValue = elem.data('disabledValue'.toLowerCase());
        var callback = elem.data('callback');
        if(callback){
            callback = that.makeCallBack(callback);
        }
        var data = that.getData(elem,callback);
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
        })
        that.setWindowValue(elem);
    }

    User.prototype.initRadio = function(elem){
        var that = this;
        var name = elem.data("name");
        var className = "biz-user-radio" + name;
        elem.addClass(className);
        var defaultValue = elem.data('defaultValue'.toLowerCase());
        var disabledValue = elem.data('disabledValue'.toLowerCase());
        var required = !!elem.data('required');
        var callback = elem.data('callback');
        if(callback){
            callback = that.makeCallBack(callback);
        }
        var data = that.getData(elem,callback);
        bt.use(['radioType'], function() {
            bt.radioType.init({
                elem:elem,
                valueField: "value",
                textField:"text",
                defaultValue:defaultValue,
                disabledValue:disabledValue,
                name:name,
                data:data,
                required:required,
                callback:callback,
            });
        })
        that.setWindowValue(elem);
    }


    User.prototype.setWindowValue = function(jThis) {
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

    User.prototype.makeCallBack = function(callBack){
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

    User.prototype.reset = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        that.currentObject.html("").removeClass("init");
        that.render()
    }
    
    User.prototype.getData = function(elem,callback){
        var corpVal = elem.data('corp-value');
        if(!corpVal){
        	var corpId=elem.data('corp-id');
        	if(corpId){
        		corpVal = $("#"+corpId).val();
        	}
        }
        if(!corpVal){
        	var corpName=elem.data('corp-name');
        	if(corpName){
            	corpVal = $("input[name='"+corpName+"']").val();
        	}
        }
        var resource = elem.data('resource');
    	var data;
    	var params={"corpId":(corpVal||''),"resourceCode":(resource||'')};
		$.ajax({
	    	type: "POST",
	    	url: basePath + "/sys/user/loadUserByCorpIdOrResource",
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

    User.prototype.val = function(bizName){
        var that = this;
        if(!bizName){
            if(!that.currentObject){
                return "";
            }
            var v = that.currentObject.find("option:selected").val();
            var r = that.currentObject.find("input:radio:checked").val();
            var value = v ? v : r;
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
            if(that.currentObject.find("input:radio").length !== 0){
                that.currentObject.find('input[value='+bizName+']').attr("checked",true); 
                form.render("radio")
            }
            form.render("select")
        }

    }

    var user = new User();
    var obj = {
        init: function(option){
        	user.init(option);
            return this;
        },
        render:function(){
        	user.render();
        },
        get:function(bizName){
            var currentObj = window.biz[user.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            user.currentObject = currentObj;
            return user;
        },
    };
    //输出test接口
    exports('users', obj);
});