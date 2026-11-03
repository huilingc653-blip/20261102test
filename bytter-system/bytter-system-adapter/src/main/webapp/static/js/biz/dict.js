/**
 * 业务组件：字典
 * 周亮
 * 2017年6月5日
 * 0.0.2新增必填参数
 * 0.0.3 下拉框新增加disabled参数
 * 0.0.4 下拉框新增加disabledValue参数
 * 0.0.5 下拉框bizname对象参数
 * 0.0.6 新增加data-value属性，用于设置选中项
 * 0.0.7 新增加data-width属性，用于设置下拉框宽度
 * 0.0.8 新增加data-display属性，用于设置下拉框宽度
 * 0.0.9 对radio val()方法进行了优化
 * 0.0.10 新增加attr()重置控件参数/ 取消setWindowValue()，通过直接获取
 * 0.0.11 修复attr()数据问题
 * 0.0.12 复选框添加了默认选中参数
 * 0.0.13 radio新增加disabled参数，用于全部禁用
 * 0.0.14 新增加多选下拉框
 * 0.0.15 下拉框新增加selectText参数，用于设置下拉框是否有请选择
 * 0.0.16 checkbox 新增加 displayData/ filterData  参数，设置过滤参数,多个值用逗号隔开; 新增加 value 参数，用于设置 复选框选中值
 * 0.0.17 下拉框新增more参数，判断是否要美化下拉框，针对大量下拉框要美化，目前为了提升性能
 * 0.0.18 新增setRequired方法，用于设置字典的必填
 * 0.0.19 复选框新增checkAll参数
 */
layui.define(['jquery','form'],function(exports){
    var $  = layui.jquery,form = layui.form();
    var Dict = function () {
        this.bizName = "dicts";
        this.currentObject = null;
    }

    Dict.prototype.v = '0.0.13';

    /**
     * 方法入口，初始化
     */

    Dict.prototype.init = function () {
        var that = this;
        that.render();
    }
    Dict.prototype.render = function (){
        var _this = this;

        if(!bt.dictContext){
            throwError("未引入dict.js");
        }

        $('.bt-biz-dict:not(.init)').each(function(index,item){
            var that = $(this);
            //表单类型
            var type = that.data('type');
            var dictGroup = that.data("group");
            if(!type){
                throwError("尚未配置data-type属性！")
            }

            if(!dictGroup){
                throwError("尚未配置data-group属性！")
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
                case 'mulselect':
                    _this.initMulselect(that);
                    break;
            }

            that.removeAttr('data-group');
            // that.removeAttr('data-name');
            // that.removeAttr('data-type');//调用attr方法时，可能页面还没有加载完成控件数据
            that.removeAttr('data-required');
        });
    }

    function throwError(msg) {
        throw new Error("[bt-biz-dict]:" + msg);
    }
    Dict.prototype.initCheckBox = function(elem){
        var that = this;
        var data = [];
        var name = elem.data("name");
        var bizName = elem.data('biz-name');

        if(bizName === undefined || !that[bizName]){ //无对应的数据时，直接用缓存里面的数据
            var dictGroup = elem.data("group");
            var dicts = bt.dictContext[dictGroup];
            if(!dicts){
                throwError("字典组:"+dictGroup+"不存在")
            }
            for(var value in dicts){
                var key = dicts[value];
                data.push({
                    value:value,
                    text:key.name,
                    index:key.index
                });
            }
            data.sort(function(a,b){
                return a.index - b.index;
            });
            var className = "biz-dict-select" + name;
            elem.addClass(className);
            if(bizName !== undefined){
                that[bizName] = data;
            }
        }else{
            data = that[bizName];
        }
        var defaultValue = elem.data('defaultValue'.toLowerCase());// 默认值
        var required = isRequired(elem.data('required'));

        var value = elem.data('value');// 设置值
        var displayData = elem.data("displayData".toLowerCase()); // 复选框显示的值
        var filterData = elem.data("filterData".toLowerCase());//复选框需要过滤的值
        var checkall = elem.data("checkall".toLowerCase());//复选框需要过滤的值
        if(checkall === undefined){
            checkall = true;
        }
        var callback = elem.data('callback');
        if(callback){
            callback = that.makeCallBack(callback);
        }

        bt.use(['checkboxs'], function() {
            bt.checkboxs.init({
                    elem:elem,
                    name: name,
                    value:value,
                    change:callback,
                    valueField: "value",
                    textField:"text",
                    checkAll:!!checkall,
                    displayData:displayData,  //需要展示的数据 多个数据时，用逗号隔开
                    filterData:filterData,   // 需要过滤掉的数据 多个数据时，用逗号隔开
                    data:data,
                    required:required,
                    checkedValue:defaultValue
                }
            );
        });
        elem.addClass('init');
    }

    Dict.prototype.initSelect = function(elem){
        var that = this;
        var data = [];
        var name = elem.data("name");
        var bizName = elem.data('biz-name');
        if( bizName === undefined || !that[bizName]){//无对应的数据时，直接用缓存里面的数据
            var dictGroup = elem.data("group");
            var dicts = bt.dictContext[dictGroup];
            if(!dicts){
                throwError("字典组:"+dictGroup+"不存在")
            }

            for(var key in dicts){
                var value = dicts[key];
                data.push({
                    value:key,
                    text:value.name,
                    index:value.index
                });
            }
            data.sort(function(a,b){
                return a.index - b.index;
            });
            var className = "biz-dict-select" + name;
            elem.addClass(className);
            if(bizName !== undefined){
                that[bizName] = data;
            }
        }else{
            data = that[bizName];
        }
        var defaultValue = elem.data('defaultValue'.toLowerCase());
        var value = elem.data("value");
        var required = isRequired(elem.data('required'));
        var disabled = elem.data('disabled');
        var width = elem.data('width');
        var filterData = elem.data('filterdata'.toLowerCase());
        var displayData = elem.data('displayData'.toLowerCase());
        var disabledValue = elem.data('disabledValue'.toLowerCase());
        var selectText = elem.data('selecttext'.toLowerCase());//设置下拉框是否有“请选择”内容
        var callback = elem.data('callback');
        var more = elem.data('more'); //参数more true 代表有大量的下拉框，下拉框美化组件不渲染，改由调用者进行美化
        if(callback){
            callback = that.makeCallBack(callback);
        }
        if(value){
            $.trim(value.toString())
        }
        var initValue;
        if(value !== undefined && value !==''){
            initValue = value;
        }else{
            initValue = defaultValue;
        }
        var options={
            elem:elem,
            valueField: "value",
            textField:"text",
            defaultVal:initValue,
            name:name,
            data:data,
            more:more ==true?true:false,//参数more true 代表有大量的下拉框，下拉框美化组件不渲染，改由调用者进行美化
            required:required,
            change:callback,
            search:true,
            disabled:disabled,
            width:width,
            filterData:filterData,
            displayData:displayData,
            disabledValue:disabledValue,
            selectText:selectText
        };
        var placeholder = elem.data("placeholder");
        if(placeholder != undefined){
            if(placeholder.indexOf('=')!=-1){
                var placeholders=placeholder.split('=');
                options.placeholder={value:placeholders[0],text:placeholders[1]};
            }else{
                options.placeholder=null;
            }
        }
        bt.use(['select'], function() {
            bt.select.init(options);
        })
        elem.addClass('init');
        // that.setWindowValue(elem);
    }
    //多选
    Dict.prototype.initMulselect = function(elem) {
        var that = this;
        var data = [];
        var name = elem.data("name");
        var bizName = elem.data('biz-name');
        if( bizName === undefined || !that[bizName]){//无对应的数据时，直接用缓存里面的数据
            var dictGroup = elem.data("group");
            var dicts = bt.dictContext[dictGroup];

            if(!dicts){
                throwError("字典组:"+dictGroup+"不存在")
            }

            for(var key in dicts){
                var value = dicts[key];
                data.push({
                    value:key,
                    text:value.name,
                    index:value.index
                });
            }
            data.sort(function(a,b){
                return a.index - b.index;
            });
            var className = "biz-dict-mulselect" + name;
            elem.addClass(className);
            if(bizName !== undefined){
                that[bizName] = data;
            }
        }else{
            data = that[bizName];
        }
        var defaultValue = elem.data('defaultValue'.toLowerCase());
        var value = elem.data("value");
        var required = isRequired(elem.data('required'));
        var disabled = elem.data('disabled');
        var filterData = elem.data('filterdata'.toLowerCase());
        var displayData = elem.data('displayData'.toLowerCase());
        var itemDisabled = elem.data("disabledvalue".toLowerCase())
        var callback = elem.data('callback');
        var beforeInit = elem.data("beforeInit".toLowerCase());
        if(callback){
            callback = that.makeCallBack(callback);
        }

        if(beforeInit){
            beforeInit = that.makeCallBack(beforeInit);
        }

        if(value){
            $.trim(value.toString())
        }

        var options={
            elem:elem,
            valueField: "value",
            textField:"text",
            defaultvalue:value?value:defaultValue,
            name:name,
            data:data,
            required:required,
            callback:callback,
            disabled:disabled,
            itemDisabled:itemDisabled,
            filterData:filterData,
            displayData:displayData,
            beforeInit:beforeInit
        };

        var placeholder = elem.data("placeholder");
        if(placeholder != undefined){
            if(placeholder.indexOf('=')!=-1){
                var placeholders=placeholder.split('=');
                options.placeholder={value:placeholders[0],text:placeholders[1]};
            }else{
                options.placeholder=null;
            }
        }else{
            options.placeholder=bt.lang('common.placeholder.select');
        }
        bt.use(['mulselect'], function() {
            bt.mulselect.init(options);
        })
        elem.addClass('init layui-inline');

    }

    Dict.prototype.initRadio = function(elem){
        var that = this;
        var data = [];
        var name = elem.data("name");
        var bizName = elem.data('biz-name');
        if( bizName === undefined || !that[bizName]){//无对应的数据时，直接用缓存里面的数据
            var dictGroup = elem.data("group");
            var dicts = bt.dictContext[dictGroup];
            if(!dicts){
                throwError("字典组:"+dictGroup+"不存在")
            }

            for(var value in dicts){
                var key = dicts[value];
                data.push({
                    value:value,
                    text:key.name,
                    index:key.index
                });
            }
            data.sort(function(a,b){
                return a.index - b.index;
            });
            var className = "biz-dict-radio" + name;
            elem.addClass(className);
            var beforeinit = elem.data('beforeinit');
            if(beforeinit){
                beforeinit = that.makeCallBack(beforeinit);
                data = beforeinit(data)
            }
            if(bizName !== undefined){
                that[bizName] = data;
            }
        }else{
            data = that[bizName];
        }
        var defaultValue = elem.data('defaultValue'.toLowerCase());
        var value = elem.data("value");
        var disabledValue = elem.data('disabledValue'.toLowerCase());
        var required = isRequired(elem.data('required'));
        var filterData = elem.data('filterdata'.toLowerCase());
        var displayData = elem.data('displayData'.toLowerCase());
        var radionDisabled = elem.data("disabled"); //所有值都禁用
        var callback = elem.data('callback');
        if(callback){
            callback = that.makeCallBack(callback);
        }
        if(value){
            value.toString().trim();
        }
        bt.use(['radioType'], function() {
            bt.radioType.init({
                elem:elem,
                valueField: "value",
                textField:"text",
                defaultValue: value !== ''&& value !== undefined?value:defaultValue,
                disabledValue:disabledValue,
                name:name,
                data:data,
                required:required,
                filterData:filterData,
                displayData:displayData,
                disabled:radionDisabled,//全部禁用
                callback:callback
            });
        })
        elem.addClass('init');
        // that.setWindowValue(elem);
    }


    Dict.prototype.setWindowValue = function(jThis) {
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

    Dict.prototype.makeCallBack = function(callBack){
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

    Dict.prototype.val = function(bizName){
        var that = this;
        var currentObject = that.currentObject;
        if(bizName ===undefined){
            if(!currentObject){
                return "";
            }
            var value;
            if(currentObject.find("input:radio").length !== 0){

                value = currentObject.find("input:radio:checked").val();

            }else if(currentObject.find("select:not([multiple='multiple'])").length !== 0){

                value = currentObject.find("option:selected").val();

            }else if (currentObject.find("select[multiple='multiple']").length !== 0){
                var selectedOptions = currentObject.find("option:selected");
                value = [];
                for(var i= 0 ; i < selectedOptions.length;i++){
                    var v = $(selectedOptions[i]).val();
                    if(v !=''){
                        value.push(v)
                    }
                }
            }else if ( currentObject.find("input:checkbox").length !== 0 ){
                value = [];
                var checkboxs = currentObject.find("input:checkbox:checked");
                for(var i = 0; i < checkboxs.length;i++){
                    var v = $(checkboxs[i]).val();
                    if(v != '' && v !== 'all' ){
                        value.push(v);
                    }
                }
            }
            return value;

        }else{
            if(currentObject.find("select:not([multiple='multiple'])").length !== 0){
                if(typeof bizName ==='object'){
                    for(var key in bizName){
                        if(key =="disabled" && bizName[key]){
                            currentObject.find("select").attr("disabled",true)
                        }
                        if(key =="disabled" && !bizName[key]){
                            currentObject.find("select").removeAttr("disabled")
                        }
                        if(key =="disabledValue" && bizName[key]){
                            currentObject.find('select option[value="'+bizName[key]+'"]').attr("disabled",true)
                        }
                    }
                }else{
                    currentObject.find("select").val(bizName);
                }
            }else if(currentObject.find("input:radio").length !== 0){
                currentObject.find('input[value='+bizName+']')[0].checked = true;
                form.render("radio");
            }else if (currentObject.find("select[multiple='multiple']").length !== 0){
                // currentObject.data('defaultvalue',bizName);
                currentObject.data('value',bizName);//重置时，应该是对value值进行更改
                currentObject.html('')
                that.initMulselect(currentObject)
            }else if(currentObject.find("input:checkbox").length !== 0){
                var value = bizName.split(",");
                var checkboxs = currentObject.find("input:checkbox:checked");
                for(var i = 0; i < checkboxs.length;i++){
                    var v = $(checkboxs[i]).val();
                    currentObject.find('input[value='+v+']')[0].checked = false;
                }
                for(var j = 0; j < value.length;j++){
                    var v = value[j];
                    currentObject.find('input[value='+v+']')[0].checked = true;
                }
                form.render("checkbox");
            }

            form.render("select");
        }
    };

    Dict.prototype.attr = function(param){
        var that = this;
        if(!that.currentObject){
            return "";
        }

        if(param !== undefined && typeof param ==='string'){
            return that.currentObject.attr(param);
        }

        if(param !== undefined && typeof param ==='object'){
            var currentObject = that.currentObject;
            for(var key in param){
                var attrName;
                if(key.indexOf("data-") !== -1){
                    attrName= key.replace("data-",'');

                }else{
                    attrName = key;
                }
                currentObject.data(attrName.toLowerCase(),param[key])
            }
            var type = currentObject.data('type');

            switch (type){
                case 'checkbox':
                    currentObject.html('')
                    that.initCheckBox(currentObject)
                    break;
                case 'select':
                    currentObject.html('')
                    that.initSelect(currentObject)
                    break;
                case 'radio':
                    currentObject.html('')
                    that.initRadio(currentObject)
                    break;
                case 'mulselect':
                    currentObject.html('')
                    that.initMulselect(currentObject)
                    break;
            }
        }
    };


    /**
     * 设置必填方法
     * isRequired　true为必填　false 设消必填
     *
     * */
    Dict.prototype.setRequired = function (isRequired) {
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var currentObject = that.currentObject;
        currentObject.data('required',isRequired);
        var type = currentObject.data('type');

        function updateRequired(elem) {
            isRequired?elem.attr('lay-verify','required'):elem.removeAttr('lay-verify');
        }
        switch (type){
            case 'checkbox':
                updateRequired(currentObject.find('input[type="checkbox"]'))
                break;
            case 'select':
                updateRequired(currentObject.find('select'))
                break;
            case 'radio':
                updateRequired(currentObject.find('input[type="radio"]'))
                break;
            case 'mulselect':
                updateRequired(currentObject.find('select'));
                break;
        }
        form.renderIcon();
    }

    function isRequired(required){
        if(!required){
            return false;
        }
        if(required == 'true'){
            return true
        }
        if(required == 'false'){
            return false
        }
        return  required
    }

    var dict = new Dict();
    var obj = {
        init: function(option){
            dict.init(option);
            return this;
        },
        render:function(){
            dict.render();
        },
        get:function(bizName){
            var currentObj = $('[data-biz-name="'+bizName+'"]');
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            dict.currentObject = currentObj;
            return dict;
        },
    };
    //输出test接口
    exports('dicts', obj);
});
