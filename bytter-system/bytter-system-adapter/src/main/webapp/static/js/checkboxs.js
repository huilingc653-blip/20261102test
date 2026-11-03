/**
 * 时间组件
 * 本组件依赖layui框架
 * liuyj
 * 2017年4月18日
 * 0.0.1 单选
 * 0.0.2 多选
 * 0.0.3 全选
 * 0.0.4 修复页面上多个checkboxs的问题
 * 0.0.5 新增传参选中功能
 * 0.0.6 在datatable中，使用此控件，需要重新计算列宽
 * 0.0.7 corp组件调用时，新增加bizName参数
 * 0.0.8 新增加initGetCurrent()方法，用于重置复选框;
 * 0.0.9 新增加initCompleteCallback方法，用于初始化完成的回调,新增disabledValue参数，显示非禁用数据;
 * 0.1.0 新增加设置attr 非禁用只有一个值时，直接让他选中
 * 0.1.1 新增加 fixedCheck 参数，设置复选框必选值,多个值用逗号隔开
 * 0.1.2 新增加 displayData/ filterData  参数，设置过滤参数,多个值用逗号隔开; 新增加 value 参数，用于设置 复选框选中值
 * 0.1.3 添加必填*标识方法
 * 0.1.4 必填*标识方法统一在form中处理
 */
bt.define(['jquery', 'form','element'], function(exports) {
    var $ = bt.jquery,
    form = bt.form(),
    element = bt.element();
    // var layer = parent.layer === undefined ? layui.layer : parent.layer;
    var Checkboxs = function(){
      /**
     *  默认配置
     */
      this.config = {
        elem:undefined,//空器
        url:undefined,
        name:"",
        valueField:"",
        textField:"",
        data:undefined,
        checkedValue:undefined,//设置默认选中
        value:'',// 设置值
        checkAll:true,
        change:undefined,
        fixedCheck:undefined,//设置复选框的必选项
        // initCompleteCallback:function () { //初始化完成的回调
        //
        // } ,
        bizName:'',//接收corp 传递的bizName
        displayData:'',  // 需要展示的数据 多个数据时，用逗号隔开
        filterData:'',   // 需要过滤掉的数据 多个数据时，用逗号隔开
        // disabled:'' 全部禁用
        //  disabledValue:'22,33'//禁用某个内容，多个参数用","隔开;
        // disabledValue:'22,33'// 非禁用的复选框，多个参数时用","隔开;
      }
      this.param = randomNum();
      this.bizName = "checkbox";
      this.currentObject = null;
    };
     /**
   * 参数设置
   * @param {Object} options
   */
    Checkboxs.prototype.set = function(options){
        var that = this;
        $.extend(true, that.config, options);
        return that
    };

    Checkboxs.prototype.render = function(){
        var _that = this;
        var _config = _that.config;
        if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
            throwError('checkboxs error: elem参数未定义或设置出错，具体设置格式请参考文档API.');
        }
        var $container;
        if(typeof(_config.elem) === 'string') {
            $container = $('' + _config.elem + '');
        }
        if(typeof(_config.elem) === 'object') {
            $container = _config.elem;
        }

        if(_config.bizName){ //corp组件传过来的bizName,checkbox本身是从页面上去获取
            _that.bizName = _config.bizName;
        }
        var bizName = $container.data('biz-name'.toLowerCase());//checkbox本身的
        if(bizName){
            _config.bizName = bizName;
        }

        //添加一个id属性
        var id = "checkbox" + _that.param;
        var newContainer = '<div class="layui-form" data-id ="'+id+'"></div>';
        if(_config.bizName){ //corp组件要保留原div
            $container.html(newContainer);
        }else{
            $container.after(newContainer).detach();
        }
        //获取必选参数
        var fixedCheck = _config.fixedCheck;

        if( fixedCheck!== undefined ){
            if( fixedCheck.indexOf(',') !== -1 ){
                _config.fixedCheck = fixedCheck.split(',');
            }else{
                _config.fixedCheck = [fixedCheck];
            }
        }

        if(_config.data !== undefined && typeof(_config.data) === 'object'){
            var html =_that.getHtml();
            $('[data-id='+id+']').html(html);
            _that.singleCheck($('[data-id='+id+']'));
            //在datatable中，使用此控件，需要重新计算列宽
            if($.fn.dataTable){
                $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
            }
            form.render('checkbox');//自动化渲染
            var initCallback = _config.initCompleteCallback;
            if(initCallback && typeof initCallback ==='function'){
                initCallback(_config.data);
            }
        }else{
            $.ajax({
            type: 'get',
            url: _config.url,
            dataType: 'json',
            success: function(result, status, xhr) {
                _config.data = result;
                var html =_that.getHtml();
                $('[data-id='+id+']').html(html);
                _that.singleCheck($container);
                //在datatable中，使用此控件，需要重新计算列宽
                if($.fn.dataTable){
                    $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
                }

                //corp控件存贮object 开始
                if (_config.bizName) { //corp组件执行
                    var jThis = $container;
                    var bizName = jThis.data('biz-name'.toLowerCase());
                    if(bizName){
                        if (!window.biz) {
                            window.biz = [];
                        }
                        if (!window.biz[_that.bizName]) {
                            window.biz[_that.bizName] = [];
                        }
                        var type = jThis.data('type')?jThis.data('type'):'checkbox';
                        window.biz[_that.bizName][bizName] = {type:type,obj:jThis,data:result};
                    }
                }
                //自动化渲染
                form.render('checkbox');
                var initCallback = _config.initCompleteCallback;
                if(initCallback && typeof initCallback ==='function'){
                    initCallback(result);
                }
                //结束
            },
          });
        }
   };

    //监听全选事件
    Checkboxs.prototype.toggleAll = function(id){
        var that = this;
        form.on('checkbox('+id+')', function(data){
            $(data.elem).siblings('input:not([disabled])').prop('checked',data.elem.checked);
            var fixedCheck = that.config.fixedCheck;//获取必选项参数
            if( fixedCheck ){//设置必选项选中
                for( var i = 0;i < fixedCheck.length;i++ ){
                    $(data.elem).siblings('input[value= '+ fixedCheck[i] +']:not([disabled])').prop('checked',true);
                }
            }
            var change = that.config.change;
            if( change !== undefined && typeof(change) === "function"){
                change(data,that.config.data);
            }
            form.render('checkbox');
        });
    };

    //监听每一个选择
    Checkboxs.prototype.singleCheck = function($container){
        var _that = this;
         var checkAllfilter = $container.find('input[data-all]').data('all');//全选按钮
         if(checkAllfilter){
             _that.toggleAll(checkAllfilter)
         }
         var childfilter = $container.find('input[data-child]').data('child');//非全选按钮
         if(childfilter){
             form.on('checkbox('+childfilter+')', function(data){
                 var that = this;
                 var fixedCheck = _that.config.fixedCheck;//获取必选项参数
                 if( fixedCheck && !data.elem.checked ){
                    for( var i = 0;i < fixedCheck.length;i++ ){
                        if( data.value  == fixedCheck[i] ){
                            data.elem.checked = true;
                            $(this).prop('checked',true);
                            layer.msg(bt.lang('common.msg.requiredSelectMsg'));//此项为必选，不可以修改
                            break;
                        }
                    }
                 }
                 var parent = $(data.elem).parent();
                 var length = parent.find('input[data-child]').length;
                 var checkedLen = parent.find('input[data-child]:checked').length;
                 if(length == checkedLen){
                     $container.find('input[data-all]').prop("checked",true);
                 }else{
                     $container.find('input[data-all]').prop("checked",false);
                 }
                 var change = _that.config.change;
                 if( change !== undefined && typeof(change) === "function"){
                     change(data,_that.config.data);
                 }
                 form.render('checkbox');
             })
         }
    };
    //生成HTML代码
    Checkboxs.prototype.getHtml = function(){
        var that = this;
        var _config = that.config;
        // var data = _config.data;
        var temp ='';
        var all = false;
        var checkedAry;
        var required = _config.required;
        var checkedVal = _config.checkedValue !=undefined ? _config.checkedValue:_config.checkedvalue;

        if ( _config.value !== undefined && _config.value !== '' ){
            checkedVal = _config.value;
        }
        var checkedArr=[];

        var data = filterData( _config.data, _config );

        //设置默认值
        if(checkedVal !== undefined){
            if(checkedVal =='all'){
                all = true;
            }else{
                checkedArr = stringToArray(checkedVal);
                var count = 0;
                bt.each(data,function(index,item){
                    if(checkedArr.indexOf(item.value) > -1){
                        count++
                    }
                });
                if(count && count == data.length){
                    all = true;
                }
                // if(data.length ===checkedArr.length){
                // if(data.sort().toString() == checkedArr.sort().toString()){ //判断两组数组是否相等
                //     all = true;
                // }
            }
        }
        var disabledItem ='';
        if(_config.disabled == true){ //如果等于true将全部禁用
            disabledItem = 'disabled=""';
        }

        if(_config.checkAll){
            var dataItem;
            if(typeof (_config.checkAllData)==="object" ){
                dataItem = _config.checkAllData;
            }else if(_config.checkAllData === undefined){
                dataItem =  {
                    name:"all",
                    value:"all",
                    title:bt.lang('common.check.all'),
                    disabled:"false",
                    filter:"checkAll"
                };
            }
            if(dataItem){
                if( _config.name ){
                    temp += '<input type="checkbox" data-extra="all" lay-skin="primary" name="'+ _config.name+'"' +' '
                }
                if(dataItem.title ){
                    temp += 'title="'+dataItem.title+'"'+' '
                }
                if(dataItem.value ){
                    temp += 'value="'+dataItem.value+'"'+' '
                }
                if(dataItem.disabled ==false ||  _config.disabled == true){
                    temp += 'disabled=""'+' '
                }
                if(required){
                    temp += 'lay-verify="required"'+' '
                }
                if(dataItem.filter !==undefined){
                    temp += 'data-all="'+dataItem.filter+that.param+'"'+' ' +'lay-filter="'+dataItem.filter+that.param+'"'
                }else{
                    temp += 'data-all="checkAll'+that.param+'"'+' ' +'lay-filter="checkAll'+that.param+'" '
                }
                temp += all?"checked":'';
                temp += '>'
            }
        }
        var disabledValue = _config.disabledValue;//禁用的数组value，设置复选框中子项的禁用
        var disabledValueArr = stringToArray(disabledValue);

        var abledValue = _config.abledValue;//非禁用的数组value，设置复选框中子项的非禁用
        var abledValueArr = stringToArray(abledValue);

        for(var i =0;i <data.length;i++ ){
            var item = data[i];
            if(item[_config.valueField]!==null){
                temp += '<input type="checkbox" lay-skin="primary" value="'+item[_config.valueField]+'"' +' '
            }

            if(item[_config.textField]!==null){
                temp += 'title="'+item[_config.textField]+'"'+' '
            }
            if(_config.name!==null){
                temp += 'name="'+ _config.name+'"'+' ';
            }else{
                throwError('checkboxs error: elem参数name 未定义或设置出错，具体设置格式请参考文档API.');
            }
            if(disabledValueArr.length){
                for(var j = 0 ;j < disabledValueArr.length;j++){
                    if(disabledValueArr[j] == item[_config.valueField]){
                        disabledItem = 'disabled=""';
                        break;
                    }
                }
            }
            //不在非禁用菜单中的，就要被禁用
            if(abledValueArr.length){
                var abledFlag = true;
                for(var k = 0 ;k < abledValueArr.length;k++){
                    if(abledValueArr[k] == item[_config.valueField]){
                        abledFlag = false;
                        break;
                    }

                }
                if(abledFlag){
                    disabledItem = 'disabled=""';
                }
            }

            if(item.disabled == false){
                disabledItem = 'disabled=""';
            }
            temp += disabledItem +' ';
            if(_config.disabled != true){ //重置禁用标识
                disabledItem ='';
            }

            if(item.filter !==undefined){
                temp += 'data-child="'+item.filter+that.param+'"'+' ' +'lay-filter="'+item.filter+that.param+'"'
            }else{
                temp += 'data-child="child'+that.param+'"'+' ' +'lay-filter="child'+that.param+'"'
            }
           //选中设置
            if(all){//如果全选
                temp += "checked ";
            }else{
                if(checkedArr.length){
                    bt.each(checkedArr,function(i,v){
                        if(item[_config.valueField] == v ){
                            temp += "checked "
                        }
                    })
                }
            }
            if(required){
                temp += 'lay-verify="required"'+' '
            }
            temp += '>'
        }
        return temp;
    };

    function stringToArray(str) {
        if(str === undefined){
            return [];
        }
        var arr = [];
        var str = str.toString();
        if( typeof str =='string' && str.indexOf(',') !== -1){ //如果是多个值时 arr = str.split(',');
            arr = str.split(',');
        }else{
            arr.push(str);
        }
        return arr;
    }

    // 如果参数中有displayData/filterData ，将对数据重新过滤
    function filterData(data,config){
        var dataArr = [];//
        if ( data.length ) {
            var valueName = config.valueField;
            if ( config.displayData ) {
                var displayDataArr = stringToArray( config.displayData );

                for ( var i = 0 ,length = displayDataArr.length ; i < length ; i++ ){
                    for ( var j = 0 ,len = data.length ; j < len ; j++ ){
                        if ( data[j][valueName] == displayDataArr[i] ) { //如果对应的valueName 相符合，就把数据添加到数组中
                              dataArr.push( data[j]);
                              break
                        }
                    }
                }
            }
            if ( config.filterData ) {
                var filterDataArr = stringToArray( config.filterData );

                for ( var i = 0 ,length = data.length ; i < length ; i++ ){
                    var isNotFilter = true;
                    for ( var j = 0 ,len = filterDataArr.length ; j < len ; j++ ){
                        if ( data[i][valueName] == filterDataArr[j] ) { //如果对应的valueName 不相符合，就把数据添加到数组中
                            isNotFilter = false
                            break
                        }
                    }

                    if ( isNotFilter ){
                        dataArr.push( data[i]);
                        isNotFilter = true;
                    }
                }
            }
        }

        if ( dataArr.length ){
            return dataArr;
        } else {
            return data;
        }
    }

    //重置复选框
    Checkboxs.prototype.attr = function (name,value) {
        if( name == undefined || value == undefined){
            return;
        }
        var that = this;
        if(that.currentObject){ //暂时只支持data-disabledvalue(禁用);data-abledvalue(非禁用）
            var elem = that.currentObject.obj;
            var attrName =  name.replace('data-','');
            elem.data(attrName,value);
            elem.find('input:checked').prop('checked',false);//设置时,清除所有选中的
            var valueArr = stringToArray(value);
            var length = valueArr.length;
            if( name == 'data-disabledvalue'){ //设置禁用
                elem.find('input[disabled]').removeAttr('disabled');
                for(var i = 0; i < length;i++){
                    elem.find('input[value="'+valueArr[i] +'"]').attr('disabled','');
                }
            }else if( name == 'data-abledvalue'){ //设置非禁用
                if(value == 'all'){
                    elem.find('input[disabled]').removeAttr('disabled');
                }else{
                    elem.find('input[data-child]').attr('disabled','');
                    for(var i = 0;i< length;i++){
                        var inputElem = elem.find('input[value="'+valueArr[i] +'"]');
                        inputElem.removeAttr('disabled');
                        if(length == 1){ //设置值只有一个时，直接让他选中
                            inputElem.prop('checked',true);
                        }
                    }
                }
            }
            form.render('checkbox');
        }

    }
    //设置与获取值的方法
    Checkboxs.prototype.val = function (value) {
        var that = this;
        if( that.currentObject ){
            var elem = that.currentObject.obj;
            if( value !== undefined ){
                elem.find('input:checked').prop('checked',false);//设置时,清除所有选中的
                var valueArr = stringToArray(value);
                var length = valueArr.length;
                for(var i = 0; i < length;i++){
                    elem.find('input[value="'+valueArr[i] +'"]').prop('checked',true);
                }
                form.render('checkbox');
            }else{
                return elem.find('input:checked').val();
            }
        }

    }

    //设置与获取值的方法
    Checkboxs.prototype.getValue = function () {
        var that = this;
        if( that.currentObject ){
            var elem = that.currentObject.obj;
            var checkeds = elem.find('input:checked');
            var value = '';
            if(checkeds.length){
                for(var i = 0; i<checkeds.length; i++){
                    if(!i){
                        value = $(checkeds[i]).val();
                    }else {
                        value = value + ',' + $(checkeds[i]).val();
                    }
                }
            }
            return value;
        }

    }

    function throwError(msg) {
        throw new Error(msg);
    };

    function randomNum(){
        var num = Math.random()*100;
        num = parseInt(num, 10);
        return num;
    }
    var t = new Checkboxs();
    var obj = {
        init:function(options){
            var t = new Checkboxs();
            t.set(options)
            t.render();
            return t;
        },
        initGetCurrent:function (options) {
            var t = new Checkboxs();
            t.set(options)
            t.render();
            return this;
        },
        get:function (bizName) {
            var currentObj = window.biz[t.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            t.currentObject = currentObj;
            return t;
        }
    };
    exports('checkboxs', obj);
});
