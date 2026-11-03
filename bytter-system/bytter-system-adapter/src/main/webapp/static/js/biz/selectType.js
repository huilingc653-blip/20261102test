/**
 * 单选下拉组件
 * 本组件依赖layui的jquery,form
 * liuyj
 * 2017年7月10日
 * 0.0.1 实现下拉框组件
 * 0.0.2 更改无name时，也能生成select
 * 0.0.3 新增加disabled值
 * 0.0.4 新增加联动参数bv,itemDisabled值,回调新增加参数 select对象
 * 0.0.5 在datatable中，使用此控件，需要重新计算列宽
 * 0.0.6 新增加data-value值，用于设置参数值
 * 0.0.7 新增加data-displayData值，用于设置展示的数据
 * 0.0.8 新增加getSelectedData()，支持ie8
 * 0.0.9 新增加beforeinit回调方法，用于重置显示的text
 * 0.0.10 新增加bizName属性的，数据存存window
 * 0.0.11 新增加defaultvalue 值不在数据对象中时，直接把它添加了下拉框中，value格式为：'extra' + 时间戳
 * 0.0.12 新增加getData()，可以获取下拉框的数据对象，包括 value / text / elem
 * 0.0.13 更改为点击才数据加载
 * 0.0.14 新增加data-extraData 参数，设置false时，代表默认值在数据中找不到时，不会把此值设置到下拉框中
 * 0.0.15 attr()方法新增加可以传对象
 * 0.0.15 解决父级或者本身div没有定位时，下拉icon位置不对的问题
 * 0.0.16 getData方法，新增加获取选中的原始数据data.obj
 * 0.0.17 请选择项直接改成value=""，解决后台拿到空格的问题
 * 0.0.18 还原0.0.17
 * 0.0.19 新增beforeInitResiveData　回调方法，初始化之前可以更改数据展示
 * 0.0.20 新增选中数据特殊字符转义
 * 0.0.21 新增校验规则参数verify
 */


bt.define(['jquery','form','select','tools'], function (exports) {
    "use strict";
    var $ = bt.jquery,
        form = bt.form();
    var SelectType = function(){
        /**
         *  默认配置
         */
        this.config = {
        };
        this.bizName = "selectType";
        this.selectObjArr =[];
        this.currentObject = null;
        this.currentElementObj = null;//当前元素对象
    }

    SelectType.prototype.init = function(){
        this.render()
    }

    SelectType.prototype.render = function(){
        var that = this;
        var settings = {};
        var domObj = $('.bt-biz-selectType:not(.init)');
        bt.each(domObj,function(i,v){
            var $container = $(this);
            $container.data('index',new Date().getTime());//给容器设置索引
            var settings = getSelectParm($container)
            if(settings.link && settings.defaultValue){
                $container.data("bv",defaultValue)
            }
            that.initSelect($container,settings);
            $container.addClass("init");

            //新增加下拉框容器父级元素没有className layui-inline / layui-input-inline 解决下拉框icon位置的问题
            if($container[0].tagName == 'DIV'){
                if( !$container.hasClass( 'layui-inline') || !$container.hasClass( 'layui-input-inline' ) ){
                    $container.addClass('layui-inline')
                }
            }else{
                $container.wrap('<div class="layui-inline"></div>')
            }
        })
    }

    SelectType.prototype.initSelect = function($elem,settings){
        var that = this, _config = settings;
        var initSelectHtml = that.getSelectContainer(_config); //生成下拉框初始容器
        $elem.html(initSelectHtml);
        that.setRequireIcon($elem,_config);//设置必填标识
        // form.render("select");
        var defaultvalue = _config.defaultValue;//默认值
        if( defaultvalue !== undefined || _config.bizName){//如果默认值有
            that.clickSelectCallback($elem,_config);//获取下拉框数据，生成含有数据的下框
            $elem.data('initdata','true');//添加下拉框获取了数据的标识

        }else{//无默认值时，通过点击回调获取，执行代码写在form中
            form.render("select");
            $elem.data('get',that.clickSelectCallback);
        }

    }

    /**
     * 生成获取数据之前的下拉框容器
     * @param {Object} data
     */
    SelectType.prototype.getSelectContainer = function(o) {
        var that = this,_config = o;
        var selectHtml ='';
        selectHtml += '<select ';

        if(_config.name){
            selectHtml += 'name="'+_config.name +'"  ';
        }else{
            throwError('error: name参数未定义或设置出错，具体设置格式请参考文档API.')
        }

        if(_config.required && !_config.verify){
            selectHtml += 'lay-verify="required"  class ="layui-input"'+' '
        }
        else if(_config.verify){ // 添加一个自定义校验规则
            selectHtml += 'lay-verify="'+_config.verify+'"  class ="layui-input"'+' '
            _config.required = _config.required?_config.required:true
        }
        if(_config.disabled && _config.disabled !=false ){
            selectHtml += 'disabled'+' '
        }

        if(_config.search){
            selectHtml += 'lay-search'+' '
        }

        if(_config.inputValue){
            selectHtml += 'data-inputValue="inputValue"'+' ';
        }

        if(_config.width){
            selectHtml += 'data-width="'+_config.width +'"  ';
        }
        selectHtml += 'lay-filter="'+_config.filter+'"  ';
        selectHtml +='>'

        if(_config.selectText == undefined){
            selectHtml += '<option value=" ">'+_config.placeholder+'</option>'
        }

        if(_config.inputValue){
            selectHtml += '<option value="inputText" class="extra"></option>'
        }
        selectHtml +='</select>';
        return selectHtml;
    };

    /**
     * 生成取html字符串
     * @param {Object} data
     */
    var getHtml = function(data,o,elem) {
        var _config = o;
        var selectHtml ='';
        var ddHtml='';
        var itemDisabled = _config.itemDisabled;
        var disabledArr;
        if(itemDisabled ){
            disabledArr = toArray(itemDisabled)
        }
        var defaultvalue = _config.defaultValue,isFirst =false;

        if(defaultvalue !== undefined && defaultvalue != null){
            defaultvalue = $.trim(defaultvalue.toString())
            if(defaultvalue.length === 0){
                defaultvalue = undefined;
            }
        }

        if(defaultvalue && typeof defaultvalue !=="number" && defaultvalue.indexOf ("first") !==-1){
            isFirst = true;
        }
        //对显示数据或不显示数据进行过滤
        var twoData = filterData(data,_config);

        var beforeInitResiveData = _config.beforeInitResiveData;
        if(beforeInitResiveData && typeof beforeInitResiveData ==="function"){
            twoData = beforeInitResiveData(data);
        }
        var valueField = _config.valueField;
        var beforeinit = _config.beforeInit;

        var isExtra = true; //是否是data 以外的额外value，如果是，需要在下拉框中添加此选择项

        for(var i = 0;i <twoData.length;i++){
            var obj = twoData[i];

            selectHtml +='<option value="'+obj[valueField] +'" ';
            ddHtml += '<dd lay-value="'+ obj[valueField] +'"';
            if( isFirst && i ===0 ){
                selectHtml +='selected ';
            }else{
                if(defaultvalue !== undefined && defaultvalue == obj[valueField]){
                    isExtra = false;
                    selectHtml +='selected ';
                    ddHtml += ' class= "layui-this"';
                }
            }
            bt.each(obj,function(key,value){
                if(typeof value == 'object'){
                    selectHtml +='data-'+key +"= '"+JSON.stringify(value)+"'  ";
                }
                else if(_config.valueField !== value && key!=="disabled"){
                    selectHtml +='data-'+key +'= "'+value+'"  ';
                }
                if(key==="disabled" && value ){
                    selectHtml += 'disabled  ';
                    ddHtml += ' class="layui-disabled"';
                }
                bt.each(disabledArr,function(i,v){
                    if(valueField == key && v == value ){
                        selectHtml += 'disabled  ';
                        ddHtml += ' class="layui-disabled"';
                    }
                })
            })
            //显示内容回调，用于重置显示内容
            var selectText ="";
            if(beforeinit && typeof beforeinit ==="function"){
                selectText = beforeinit(obj[_config.textField],obj)
            }else{
                selectText = obj[_config.textField];
            }
            selectHtml +=">"+selectText+'</option>';
            ddHtml +=">"+selectText+'</dd>';
        }
        //当默认defaultvalue 在data中找不到时，需要在下拉框中添加一个选择项，并让它选中
        // extraData 等于false时，不需要添加额外参数
        if( !isFirst && isExtra && defaultvalue && _config.extraData){
            // var id = '';
            // id += 'extra';
            // id += new Date().getTime();
            selectHtml += "<option value='"+ defaultvalue +"' selected id='selectTypeDefaultValue1'>"+ defaultvalue +"</option>";
            ddHtml += '<dd lay-value="'+ defaultvalue +'" class= "layui-this" id="selectTypeDefaultValue2">'+ defaultvalue +'</dd>';
        }

        elem.find('select').append(selectHtml);
        // 解决默认值中有双引号或都单号的问题
        if($('#selectTypeDefaultValue1')){
            $('#selectTypeDefaultValue1').attr('value',defaultvalue).removeAttr('id')
        }
        if(_config.defaultValue != undefined || _config.bizName){ //如果有设置默认值，就直接渲染下拉框
            form.render('select');
        }else{ //如果没有默认值，直接在下拉框中的dl中添加数据
            //
            elem.find('dl').append(ddHtml);
            // 解决默认值中有双引号或都单号的问题
            if($('#selectTypeDefaultValue2')){
                $('#selectTypeDefaultValue2').attr('lay-value',defaultvalue).removeAttr('id')
            }
        }

    };

    //字符串转为数组
    var toArray = function(text){
        var that = this;
        var str = text.toString();
        if(str.indexOf(',') !==-1){
            return str.split(',');
        }else{
            var arr = [];
            arr.push(str);
            return arr;
        }
    }
    //过滤数据
    var filterData = function(data,config){
        var that = this;
        var _config = config;
        //过滤需要显示的数据
        var displayData = _config.displayData;
        var displayArr;
        var newData = [],valueField = _config.valueField;
        if(displayData != undefined && displayData !='all' ){
            displayArr = toArray(displayData)
            //过滤需要显示的数据
            bt.each(displayArr,function(i,v){
                for(var j = 0; j < data.length ; j++){
                    var d = data[j];
                    if(v == d[valueField]){
                        newData.push(d);
                    }
                }
            })
        }else{
            newData = data;
        }
        //过滤不需要显示的数据
        var filterData = _config.filterData;
        var twoData;
        var filterArr =[];
        if(filterData != undefined){
            twoData = toArray(filterData);
            //过滤掉不需要的数据
            bt.each(newData,function(i,v){
                var d = v[valueField];
                var flag = false;
                for(var j = 0; j < twoData.length ; j++){
                    if(twoData[j] == d){
                        flag = true;
                        break;
                    }
                }
                if(!flag ){
                    filterArr.push(v);
                }
            })
        }else{
            filterArr = newData;
        }
        return filterArr;
    }

    //设置必填标识
    SelectType.prototype.setRequireIcon = function($elem,obj){
        var othis = $elem;
        if(obj.required){
            if(othis.parents(".ignore-required-icon").length ===0){
                var td = othis.parents("td").prev("td");
                var text = $(td).text();
                if(text.indexOf("*") ==-1){
                    var td = othis.parents("td").prev("td");
                    var text = $(td).text();
                    if(text.indexOf("*") ==-1){
                        $(td).html('<span class="pl"><span class="required-icon">*</span></span>'+text)
                    }
                }
            }
        }
    }
    //在window中保存设置了bizname属性的数据
    var saveBizNameData = function($elem,data){
        var that = this;
        var typeName = "selectType";
        var bizName = $elem.data('biz-name'.toLowerCase());
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[typeName]) {
                window.biz[typeName] = [];
            }
            window.biz[typeName][bizName] = {type:'selectType',elem:$elem,obj:data};
        }
        //在datatable中，使用此控件，需要重新计算列宽
        if($.fn.dataTable){
            $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
        }
    }

    //
    SelectType.prototype.clickSelectCallback = function($elem,obj){
        var that = this;
        var _config = obj ? obj:getSelectParm($elem);
        var i = $elem.data('index');
        if(_config.url !='' && _config.url != undefined && _config.method =="post"){
            $.post(_config.url,_config.urldata,function(data){
                if(data){
                    getHtml(data,_config,$elem);
                    saveBizNameData($elem,data);
                    clickCallback(_config.callback,i,data);
                    initCallback(_config.initCallback,data,$elem);
                }
            },"json")
        }
        if(_config.url !='' && _config.url != undefined && _config.method =="get"){
            $.get(_config.url,function(data){
                if(data){
                    getHtml(data,_config,$elem);
                    saveBizNameData($elem,data);
                    clickCallback(_config.callback,i,data);
                    initCallback(_config.initCallback,data,$elem);
                }
            },"json")
        }

        if( _config.url === 'undefined' || !_config.url ){
            if(_config.getData && typeof _config.getData ==='function'){
                var data = _config.getData();
                getHtml(data,_config,$elem);
                saveBizNameData($elem,data);
                clickCallback(_config.callback,i,data);
                initCallback(_config.initCallback,data);
            }else{
                saveBizNameData($elem,[]);
            }
        }
    }

    var getSelectParm = function($elem){
        // var that = this;
        var $container = $elem;
        var settings = {};
        var url = $container.data("url");
        var valueField = $container.data('valueField'.toLowerCase()),
            name = $container.data('name'),
            textField = $container.data('textField'.toLowerCase()),
            disabled = $container.data('disabled'),
            defaultValue = $container.data("defaultvalue".toLowerCase()),
            required = $container.data("required"),
            verify = $container.data("verify"), // 校验方法
            search = $container.data("search"),
            inputValue = $container.data("inputValue".toLowerCase()),
            bizName = $container.data("biz-name"),
            placeholder = $container.data("placeholder"),
            filterData = $container.data("filterdata".toLowerCase()),//需要过滤掉的值
            method = $container.data("method"),
            itemDisabled = $container.data("itemDisabled".toLowerCase()),//禁用的值
            value = $container.data("value"),
            selectText = $container.data('selecttext'.toLowerCase()),//设置下拉框是否有“请选择”内容
            link = $container.data("link"),
            width = $container.data("width"),
            displayData = $container.data('displayData'.toLowerCase()),//需要显示的值
            initCallback = $container.data('initcallback'.toLowerCase()),//初始完成的回调
            beforeInit = $container.data('beforeinit'.toLowerCase()),//初始之前的回调
            beforeInitResiveData = $container.data('beforeInitResiveData'.toLowerCase()),//初始之前的要改数据的回调
            callback = $container.data("callback"),//选择时的回调
            i = $container.data('index'),//获取下拉框的索引号
            extraData = $container.data('extraData'.toLowerCase()),//当defaultValue参数在数据中找不到时，设置是否要追加到下拉框中
            getData = $container.data('getInitData'.toLowerCase());//不使用data-url异步，直接传入数据

        callback = makeCallBack(callback);
        beforeInit = makeCallBack(beforeInit);
        beforeInitResiveData = makeCallBack(beforeInitResiveData);
        initCallback = makeCallBack(initCallback);

        getData = makeCallBack(getData);
        //判定
        if(value){
            $.trim(value.toString())
        }

        settings = {
            elem:$container,
            url:(basePath?'':basePath)+url,
            name:name,
            valueField:valueField,
            textField: textField,
            disabled:disabled,
            itemDisabled:itemDisabled,
            defaultValue:value?value:defaultValue,
            required:required,
            verify:verify,
            search:search === undefined ? true:search,
            inputValue:inputValue,
            placeholder:placeholder?placeholder:bt.lang('common.placeholder.select'),//"请选择",
            selectText:selectText,
            callback:callback,
            initCallback:initCallback,
            beforeInit:beforeInit,
            beforeInitResiveData:beforeInitResiveData,
            filterData:filterData,
            displayData:displayData,
            filter:"select"+i,
            width:width,
            method:method?method:"get",
            bizName:bizName,
            // extraData:extraData !== false ? true:false,//设置了false时，当defaultValue参数在数据中找不到时，不会在下拉表格中添加此参数;
            extraData:!!extraData? true:false,//设置了false时，当defaultValue参数在数据中找不到时，不会在下拉表格中添加此参数;
        };

        if(getData){
            settings.getData = getData;
        }
        return settings;
    }

    var makeCallBack = function(callBack){
        if(callBack && typeof(callBack) != "function"){
            try {
                callBack = eval(callBack);
            } catch(e) {
                alert(callBack + 'not exist！');
            }

        }
        return callBack;
    }

    var clickCallback = function(callback,i,allData){
        var that = this;
        var callback = callback;
        form.on('select(select'+i+')',function(data){
            var v = data.value;
            var bv = data.othis.parent().data("bv");
            var callback = data.othis.parent().data("callback");
            callback = makeCallBack(callback);
            data.othis.parent().data("bv",v);
            if( callback && typeof callback ==="function"){
                var bizName = data.othis.parent().data('bizName');
                if(bizName){
                    var currentObj = window.biz['selectType'][bizName];
                    allData = currentObj['obj'];
                }
                var wrapObj = data.othis.parent();
                var valueField = wrapObj.data('valueField'.toLowerCase())
                var selectedData = toLowerCaseObj(v,allData,valueField);
                // 如果匹配不上内容时，则为输入的值
                if(!Object.keys(selectedData).length){
                    var textField = wrapObj.data('textField'.toLowerCase())
                    selectedData[textField] = v
                    selectedData[valueField] = v
                }
                callback(selectedData,data,bv,wrapObj)
            }
        })
    };

    // //获取选中的数据对象
    // var getSelectedData = function(v,data,valueField){
    //     var that = this;
    //     v = $.trim(v);
    //     if(v ==''){
    //         return ;
    //     }
    //     var valueName = valueField;
    //     var selectedData = null;
    //     for(var i = 0 ; i < data.length;i++){
    //         var item = data[i];
    //         for(var key in item){
    //             if(item[key] == v ){
    //                 selectedData = item;
    //                 return selectedData;
    //             }
    //         }
    //     }
    //     return selectedData;
    // }

    //获取选中的数据对象
    var getSelectedDataByValueField= function(v,data,valueField){
        var that = this;
        v = $.trim(v);
        if(v ==''){
            return ;
        }
        for(var i = 0 ; i < data.length;i++){
           if(data[i][valueField] === v){
               return data[i];
           }
        }
        return;
    }

    //将选中的数据对象进行属性小写化
    var toLowerCaseObj = function(v,data,valueField){
        var that = this;
        var obj = getSelectedDataByValueField(v,data,valueField);
        var lowerCaseObj = {};
        for(var key in obj){
            var name = key.toLowerCase();
            //将特殊符号转义
            if (obj[key] && typeof obj[key] ==='string' && obj[key].indexOf("&") !== -1){
                lowerCaseObj[name] = bt.tools.escapeHtml(obj[key]);
            }else {
                lowerCaseObj[name] = obj[key];
            }
        }
        return lowerCaseObj
    }

    var initCallback = function(initcallback,data,$elem){
        var initcallback = initcallback;
        if( initcallback && typeof initcallback ==="function"){
            initcallback(data,$elem)
        }
    };

    SelectType.prototype.val = function(bizName){
        var that = this;
        if(!that.currentObject){
            if(!that.currentElementObj.length){ //如果当前元素也不存在就直接返回
                return "";
            }
            if(!bizName){ //bizname没有代表获取值
                that.getInitValue();
            }
            return "";
        }
        var elem = that.currentObject.elem;
        if(bizName === undefined){
            return $.trim(elem.find("select option:selected").val())
        }else{
            if(typeof bizName =="object"){
                bt.each(bizName,function(key,value){
                    elem.data(key,value);
                });
                elem.html("").removeClass("init");
                that.render();
            }else{
                elem.find('select').val(bizName);
                form.render('select')
            }
        }
    };

    /**
     *  获取初始值方法
     *  defaultValue /value  当有value有值时，默认值为value，如果没有，看defaultValue
     * */
    SelectType.prototype.getInitValue = function(){
        var that = this;
        var value = that.currentElementObj.data('value');
        if(value !== undefined){
            return value
        }
        var defaultValue = that.currentElementObj.data("defaultvalue".toLowerCase());
        if(defaultValue !== undefined ){
            return defaultValue
        }

        return "";
    }

    SelectType.prototype.getSelectedData = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var allData = that.currentObject['obj'];
        var $currentElem = $(that.currentObject.elem)
        var valuefield = $currentElem.data('valuefield');
        var selectedId = $currentElem.find('option:selected').val();

        selectedId = $.trim(selectedId);
        var selectedData = {}
        if(selectedId){
            bt.each(allData,function (i,item) {
                if(item[valuefield] === selectedId){
                    selectedData = item;
                    return
                }
            })
        }
        return toLowerCaseObj(selectedId,allData,valuefield);
    }


    // 获取下拉框数据
    SelectType.prototype.setAttr = function(attrObj){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var elem = that.currentObject.elem;
        //更改参数
        for( var key in attrObj ){
            // data-required
            if(key.indexOf('required') != -1){
                if(attrObj[key]){
                    elem.find('select').attr('lay-verify','required')
                }else{
                    elem.find('select').removeAttr('lay-verify')
                }
            }else if(key.indexOf('disabled') != -1){
                if(attrObj[key] || "true" == attrObj[key]){
                    elem.find('select').prop('disabled',true);
                    elem.find('select').siblings(',layui-form-select').addClass('layui-select-disabled')
                }else{
                    elem.find('select').prop('disabled',false);
                    elem.find('select').siblings(',layui-form-select').removeClass('layui-select-disabled')

                }
            }
            elem.attr(key,attrObj[key]);
        }
    };


    /**
     * 1 重置参数及获取参数的方法
     * 2 参数可以是：object,也可以传入属性和值两个参数
     * */
    SelectType.prototype.attr = function(attr,value){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var obj = null;
        var elem = that.currentObject.elem;
        if(attr && arguments.length === 1 ){
            if(typeof attr === 'object' ){//参数为对象时
                obj = attr;

            }else{
                return elem.attr(attr);
            }
        }

        if(arguments.length === 2 ){//参数为两个字符串时
            obj = {};
            obj[attr] = value;
        }
        //更改参数
        for( var key in obj ){
            if( key.indexOf("data-") !== -1){
                var attrName = key.replace("data-",'').toLowerCase()
                elem.data(attrName,obj[key]);
            }else{
                elem.attr(key,obj[key]);
            }
        }
        elem.html("").removeClass("init");
        that.render();
    };

    // 获取下拉框数据
    SelectType.prototype.getData = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        var elem = that.currentObject.elem;
        var obj = that.currentObject.obj;
        var data = {};
        var valueField = elem.data('valueField'.toLowerCase());
        data.value = elem.find("select option:selected").val();
        if( valueField !== undefined ){
            for( var i = 0;i < obj.length;i++ ){
                if( obj[i] && obj[i][valueField] == data.value ){
                    data.obj = obj[i];
                }
            }
        }
        data.elem = elem;
        data.text = elem.find("select option:selected").text();
        return data;
    };

    SelectType.prototype.reset = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        that.currentObject.elem.html("").removeClass("init");
        that.render()
    }

    function throwError(msg) {
        throw new Error("[bt-biz-selectType:" + msg);
    };

    var selectType = new SelectType();
    var obj = {
        init:function(){
            selectType.init();
            return this;
        },
        get:function(bizName){
            selectType.currentObject = window.biz[selectType.bizName] && window.biz[selectType.bizName][bizName];
            if( !selectType.currentObject ){
                selectType.currentElementObj = $('[data-biz-name="'+bizName+'"]');
                if(selectType.currentElementObj.length){
                    selectType.currentObject = {
                        elem:selectType.currentElementObj,
                        obj:[],
                        type:'selectType'
                    }
                }else{
                    throwError(bizName+"尚未配置data-biz-name属性！")
                }
            }
            return selectType
        },
        render:function(){
            selectType.render()
        },
    }
    exports('selectType', obj);
})
