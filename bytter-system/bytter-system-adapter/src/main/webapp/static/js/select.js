/**
 * 单选下拉组件
 * 本组件依赖layui的jquery,form
 * liuyj
 * 2017年4月14日
 * 0.0.1 单选
 * 0.0.2 必填
 * 0.0.3 禁用
 * 0.0.4 搜索
 * 0.0.5 事件监听
 * 0.0.6 添加dataAttr自定义属性
 * 0.0.7 添加filterData参数,用于过滤option数据
 * 0.0.8 容器中有data-defaultvalue时，支持设置默认值
 * 0.0.9 给input添加bt-input-inline样式，控制宽度
 * 0.0.10 解决动态加载select时，如果为必填项，添加class "required-mark"
 * 0.0.11 提共setValue()，设置参数
 * 0.0.12 ignore-required-icon class的不添加必填项
 * 0.0.13 添加disabledValue 参数
 * 0.0.14 在datatable中，使用此控件，需要重新计算列宽
 * 0.0.15 新增加width参数，用于设置下拉框宽度
 * 0.0.16 新增加displayData，用于设置显示数据
 * 0.0.17 新增加selectText，设置下拉框是否有‘请选择’内容
 * 0.0.18 网点新增加下拉框内容拼接code
 * 0.0.19 点击回调新增bv返回值，bv 点击之前的值
 * 0.0.20 国际化
 * 0.0.21 请选择项直接改成value=""，解决后台拿到空格的问题
 * 0.0.22 取消0.0.21
 * 0.0.23 _that.appendHtml方法传递一个参数
 * 0.0.24 新增more参数，判断是否要美化下拉框
 */


bt.define(['jquery','form'], function (exports) {
    "use strict";
    var $ = bt.jquery,
        form = bt.form();
    var Select = function(){
        /**
         *  默认配置
         */
        this.config = {
            elem:undefined,//容器
            url:undefined,//数据路径
            type:'GET',
            name:undefined,//
            valueField: "",//数据id
            textField:"",//数据名称
            disabled:false,//选择框是否可用;
            dataAttr:{},//其它数据参数
            //本地数据"
            data:undefined,
            required:false,//是否必选
            search:true,//不支持智能查询
            // displayData:"10,11,12",显示数据
            // /width:'',//设置宽度
            disabledValue:undefined,//禁用
            display:"inline",//行内块 block 行元素
            // defaultVal:'',
            label:undefined,//设置下拉框标题
            placeholder:{value:" ",text:bt.lang('common.placeholder.select')},//"请选择"
            selectText:true,//设置下拉框是否有‘请选择’内容

            // filterData:'10,11,12',过滤数据
            change:undefined,//点击选择时，回调
            initCallBack:function(elem){
                return elem
            },
            bizName:'',//标识corp组件
            more:false,//参数more true 代表有大量的下拉框，下拉框美化组件不渲染，改由调用者进行美化

        };
        this.filter ='';
        this.bizName = "Select";
        this.currentObject = null;
    }

    /**
     * 参数设置
     * @param {Object} options
     */
    Select.prototype.set = function(options){
        var that = this;
        $.extend(true, that.config, options);

        if(that.config.inputValue){
            that.config.search = true;
        }
        return that
    }

    /**
     * 获取html字符串
     * @param {Object} data
     */
    Select.prototype.getHtml = function(index) {
        var that = this,_config = that.config;
        var data = _config.data;
        var selectHtml ='';
        var dataAttr = _config.dataAttr || _config.dataattr;

        if(_config.name){
            selectHtml += '<select name="'+_config.name +'" ';
        }else{
            throwError('error: name参数未定义或设置出错，具体设置格式请参考文档API.')
        }

        if(_config.width){
            selectHtml += 'data-width="'+_config.width +'" ';
        }

        if(_config.required){
            selectHtml += 'lay-verify="required"  class ="layui-input"'+' '
        }

        if(_config.filterid !=''){
            selectHtml += 'lay-filter='+that.filter+index +' ';
        }

        if(_config.inputValue){
            selectHtml += 'data-inputValue="inputValue"'+' ';
        }

        if(_config.disabled){
            selectHtml += 'disabled' +' ';
        }
        //根据传过来的参数，设置自定义属性
        if(dataAttr && typeof dataAttr ==="object"){
            bt.each(dataAttr,function(key,v){
                selectHtml += 'data-'+key +'="'+v+'" ';
            })
        }

        if(!_config.search ){
            selectHtml += '>'
        }else{
            selectHtml += 'lay-search="" > '
        }
        if(data && _config.placeholder && _config.placeholder.text !="" && _config.selectText){
            selectHtml += '<option value=" ">'+_config.placeholder.text+'</option>'
        }
        if(_config.inputValue){
            selectHtml += '<option value="inputText" class="extra"></option>'
        }

        if(_config.bizName){
            that.bizName = _config.bizName;
        }
        //过滤需要显示的数据
        var displayData = _config.displayData;
        var displayArr =[];
        var newData = [];
        var valueField = _config.valueField;
        if(displayData != undefined){
            displayData = displayData.toString();
            if ("all" === displayData.toLowerCase()) {
                newData = data
            }else {
                if(displayData.indexOf(',') !=-1){
                    displayArr = _config.displayData.split(',');
                }else{
                    displayArr.push(displayData);
                }
                //过滤需要显示的数据
                bt.each(displayArr,function(i,v){
                    for(var j = 0; j < data.length ; j++){
                        var d = data[j];
                        if(v == d[valueField]){
                            newData.push(d);
                        }
                    }
                })
            }
        }else{
            newData = data;
        }

        //过滤不需要显示的数据
        var filterData = _config.filterData;
        var twoData =[];
        var filterArr =[];
        if(filterData != undefined){
            filterData = filterData.toString();
            if(filterData.indexOf(',') !==-1){
                filterArr = filterData.split(',');
            }else{
                filterArr.push(filterData)
            }
            //过滤掉不需要的数据
            bt.each(newData,function(i,v){
                var d = v[valueField];
                var flag = false;
                for(var j = 0; j < filterArr.length ; j++){
                    if(filterArr[j] == d){
                        flag = true;
                        break;
                    }
                }
                if(!flag ){
                    twoData.push(v);
                }
            })
        }else{
            twoData = newData;
        }
        var disabledValue = _config.disabledValue;
        if(disabledValue !== undefined){ //转为数组
            disabledValue = disabledValue.toString();
            disabledValue = disabledValue.split(',');
        }
        for(var i = 0;i <twoData.length;i++){
            var obj = twoData[i];
            selectHtml +="<option "+(_config.defaultVal==obj[_config.valueField]?'selected':'')+" value="+obj[_config.valueField] +' ';

            if(disabledValue){
                for(var j = 0;j < disabledValue.length;j++){
                    bt.each(obj,function(key,value){
                        if(_config.valueField !== value && key!=="disabled"){
                            selectHtml +='data-'+key +'= "'+value+'"  ';
                        }
                        if((key==="disabled" && value) || (_config.valueField == key && value == disabledValue[j])){
                            selectHtml += 'disabled  ';
                        }
                    })
                }
            }
            var text = '';
            if(_config.bizName){
                text = '('+ obj.code +')  ' + obj[_config.textField];
            }else{
                text = obj[_config.textField];
            }
            selectHtml +=">"+text+'</option>';
        }

        selectHtml +='</select>';
        return selectHtml;
    };

    function throwError(msg) {
        throw new Error(msg);
    };

    function randomNum(){
        var num = Math.random()*1000000;
        num = parseInt(num, 10);
        return num;
    }

    Select.prototype.setFilter = function(){
        var num = randomNum();
        this.filter = 'selectFilter' + num
    }

    Select.prototype.onChange = function(i,result){
        var that = this,filter = that.filter + i;
        form.on('select('+filter+')',function(data){
            var change = that.makeCallBack(that.config.change);
            var v = data.value;
            var bv = data.othis.parent().data("bv");//获取前一个选中的值;
            data.othis.parent().data("bv",v);//设置当前选中的值
            if( change !== undefined && typeof(change) === "function"){
                var text = $(data.elem).find("option:selected").text();
                var valueField = that.config.valueField;
                var selectedData = null;
                bt.each(result,function(i,item){
                    if(item[valueField] == data.value){
                        selectedData = item
                    }
                })
                change(data,text,selectedData,bv)
            }
        })
    }

    Select.prototype.makeCallBack = function(callBack){
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
    var selectDom;
    Select.prototype.init = function(){

        var _that = this;
        _that.setFilter();
        var _config = _that.config;

        if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
            throwError('select error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }

        var $container;

        if(typeof(_config.elem) === 'string') {
            $container = $('' + _config.elem + '');
        }
        _config.initCallBack = _that.makeCallBack(_config.initCallBack);

        if(typeof(_config.elem) === 'object') {
            $container = _config.elem;
        }
        if($container.length === 0) {
            throwError('select error:找不到elem参数配置的容器，请检查.');
        }
        //disabledValue转为数组
        _that.stringToArry();

        if(_config.defaultVal !== undefined){
            $container.data('bv',_config.defaultVal)
        }

        if(_config.data !== undefined && typeof(_config.data) === 'object'){
            _that.appendHtml( _config.data );
            // selectDom = $container.find("select");
            //在datatable中，使用此控件，需要重新计算列宽
            $container.addClass('init');//给容器添加init class标识
            if($.fn.dataTable){
                $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
            }
            var bizName = $container.data('biz-name'.toLowerCase());
            if (bizName) {
                if (!window.biz) {
                    window.biz = [];
                }
                if (!window.biz[_that.bizName]) {
                    window.biz[_that.bizName] = [];
                }
                var type = $container.data('type')
                window.biz[_that.bizName][bizName] = {type:type?type:'select',obj:$container,data:_config.data};
            }

        }else{
            if(_config.url == undefined){
                throwError('select error:'+_config.elem+'调用select时未传传递url参数，请检查.');
            }else{
                $.ajax({
                    type: _config.type,
                    url: _config.url,
                    dataType: 'json',
                    success: function(result, status, xhr) {
                        _config.data = result;
                        _that.appendHtml(result);
                        selectDom = $container.find("select");

                        _config.initCallBack(selectDom,result);
                        //在datatable中，使用此控件，需要重新计算列宽
                        if($.fn.dataTable){
                            $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
                        }
                        //corp控件存贮object 开始
                        var jThis = $container;
                        var bizName = jThis.data('biz-name'.toLowerCase());
                        if (bizName) {
                            if (!window.biz) {
                                window.biz = [];
                            }
                            if (!window.biz[_that.bizName]) {
                                window.biz[_that.bizName] = [];
                            }
                            var type = jThis.data('type')
                            window.biz[_that.bizName][bizName] = {type:type?type:'select',obj:jThis,data:result};
                        }
                        //结束
                    },
                    // error: function(xhr, status, error) {
                    //     throwError('selectselect error:' + error);
                    // },
                    complete: function(xhr, status) {
                        _config.elem = $container;
                    }
                });
            }
        }
    }

    Select.prototype.getElemDefaultValue = function($this){
        var that = this,
            v = $this.data("defaultvalue");
        if(v || v =="0"){
            that.config.defaultVal = v;
        }
    }
    //将字符串数字转为数组
    Select.prototype.stringToArry = function(){
        var that = this;
        var disabledValue = that.config.disabledValue;
        //disabledValue值转换为数组
        var disabledValueArr = [];
        if( typeof disabledValue =='string' && disabledValue.indexOf(',')){ //如果是多个值时
            disabledValueArr = disabledValue.split(',');
        }else{
            disabledValueArr.push(disabledValue);
        }
    }

    Select.prototype.appendHtml = function(result){
        var that = this,_config = that.config,$container = $(_config.elem);
        var length = $container.length;
        bt.each($container,function(index,item){
            var othis = $(this);
            // if(length ==1){
            if(_config.defaultVal ==undefined){
                that.getElemDefaultValue(othis);
            }
            // }
            // if()
            // that.getElemDefaultValue(othis);
            var html = that.getHtml(index);
            othis.html(html).addClass("layui-inline");
            if(_config.required){
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

            if(_config.label !==undefined && _config.label !== ''){
                if(!$container.siblings("label").length > 0){
                    $container.before('<label class="layui-form-label">'+_config.label+'</label>')
                }
            }
            that.onChange(index,result)
        })
        if(!_config.more){ //如果有大量下拉框时，不对下拉框进行美化，由调用者自己美化
            form.render('select')//自动化渲染
        }

        bt.each($container,function(){
            $(this).find("input").addClass("bt-input-inline");
        })
    }

    //设置值
    Select.prototype.setValue = function(opt){
        var that = this;
        if(typeof opt ==="string"){
            that.config.defaultVal = opt;
        }
        if(typeof opt ==="object"){
            that.set(opt)
        }
        that.init()
    }

    //通过bizName获取值/设置值的方法
    Select.prototype.val = function(value){
        var that = this,elem;
        var currentObject = that.currentObject;
        if(value ===undefined){
            if(!currentObject){
                return "";
            }
            elem = currentObject.obj;
            return elem.find("select option:selected").val()
        }else{
            elem = currentObject.obj;
            elem.find('select').val(value);
            form.render('select');
        }
    }

    //通过bizName设置属性方法
    Select.prototype.attr = function(obj){
        var that = this,elem;
        var currentObject = that.currentObject;
        elem = currentObject.obj;
        if(typeof obj ==="object"){
            for(var key in obj){
                if(key =='disabled'){
                    var v = obj[key];
                    if(v){
                        elem.find('select').attr('disabled',true);
                    }else{
                        elem.find('select').removeAttr('disabled');
                    }
                }
            }
        }
        form.render('select');
    }



    function throwError(msg) {
        throw new Error("[select:" + msg);
    };

    function creatSelect(){ // 创建一个实例
        return new Select();
    }
    var select; //声明全局select变量

    var obj = {
        init:function(options){
            select = new Select();
            select.set(options)
            select.init();
            return select;
        },
        render:function(ops){ //
            select = creatSelect();
            if(ops){
                select.set(ops)
                select.init();
            }
            return this;
        },
        get:function(bizName){ //通过bizName获取对应的select对象
            var currentObj = window.biz[select.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            select.currentObject = currentObj;
            return select;
        }
    }
    exports('select', obj);
})