/**
 * 组件：单选框业务组件
 * 刘勇军
 * 2017年7月20日
 * 0.0.1 首次发布
 * 0.0.2 添加disabledvalue支持多个值
 * 0.0.3 在datatable中，使用此控件，需要重新计算列宽
 * 0.0.4 新增加data-value值，用于设置选中值
 * 0.0.5 新增加data-displayData/filter值，用于设置选中值
 * 0.0.6 新增加data-disabled值，用于设置全部禁用
 * 0.0.7 添加必填*标识方法
 */


bt.define(['jquery','form' ], function(exports) {
   var $ = bt.jquery , form = bt.form();

   var RadioType = function () {
        this.config ={
            elem:""
        };
        this.bizName = "radioType";
        this.currentObject = null;
    }

    RadioType.fn = RadioType.prototype;

    RadioType.fn.v = '0.0.6';

    /**
     * 方法入口，初始化
     */
    RadioType.fn.init = function(ops){
        var that = this;
        that.render(ops)
    };

    RadioType.fn.render = function(ops){
        var that = this;
        if(ops && typeof ops ==="object"){
            that.getData(ops);
        }else {
            var settings = {};
            var domObj = $('.bt-biz-RadioType:not(.init)');
            bt.each(domObj,function(){
                var $container = $(this);
                var name = $container.data('name'),
                    vname = $container.attr('name'),
                    defaultValue = $containerdata('defaultValue'.toLowerCase()),
                    url = $container.data('url'),//数据路径
                    type = $container.data('type'),
                    value = $container.data('value'),
                    valueField = $container.data('valueField'.toLowerCase()),
                    textField = $container.data('textField'.toLowerCase()),
                    disabledValue = $container.data('disabledValue'.toLowerCase()),
                    required = $container.data('required'),
                    bizName = $container.data("biz-name"),
                    filterData = elem.data('filterdata'.toLowerCase()),
                    displayData = elem.data('displayData'.toLowerCase()),
                    disabled = elem.data("disabled"); //所有值都禁用
                    callback = $container.data("callback");

                callback = that.makeCallBack(callback);
                //判定value值是否为空格
                if(value){
                    String.prototype.trim = function () {
                        return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
                    };
                    value.toString().trim();
                }
                settings = {
                    elem:$container,
                    name:name?name:vname,
                    defaultValue:value?value:defaultValue,
                    url:url,
                    type:type?type:"POST",
                    valueField:valueField,
                    textField:textField,
                    disabledValue:disabledValue,
                    disabled:disabled,
                    required:required,
                    callback:callback,
                    data:undefined,
                }
                that.getData(settings);

            })
        }
    };

    RadioType.fn.getData = function(settings){
        var that = this;
        if(settings.url !==undefined && settings.data ==undefined){
            $.ajax({
                type: settings.type,
                url: settings.url,
                dataType: 'json',
                success: function(result, status, xhr) {
                    settings.data = result;
                    var html = that.getHtml(settings);
                    settings.elem.html(html).addClass("init");
                    that.setRequireIcon(settings.elem,settings);//设置必填标识
                    form.render("radio");
                    that.callback(settings);
                    // form.renderIcon();
                    //在datatable中，使用此控件，需要重新计算列宽
                    if($.fn.dataTable){
                        $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
                    }

                },
            });
        }
        if(settings.data){
            var html = that.getHtml(settings);
            settings.elem.html(html).addClass("init");
            that.setRequireIcon(settings.elem,settings);//设置必填标识
            form.render("radio");
            that.callback(settings);
            //在datatable中，使用此控件，需要重新计算列宽
            if($.fn.dataTable){
                $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
            }
        }
    };

    RadioType.fn.getHtml = function(settings){
        var data = settings.data;
        if(data){
            var html = '', name = settings.name, valueField = settings.valueField, textField = settings.textField ,
                defaultValue = settings.defaultValue,required = settings.required ,disabledValue = settings.disabledValue,
                filterData = settings.filterData,displayData = settings.displayData,disabled = settings.disabled;

             //过滤需要显示的数据
            var displayArr =[];
            var newData = [];
            if(displayData != undefined){
                displayData = displayData.toString();
                if(displayData.indexOf(',') !=-1){
                    displayArr = displayData.split(',');
                }else{
                    displayArr.push(displayData)
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
            }else{
                newData = data;
            }

            //过滤不需要显示的数据
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

            if( disabledValue !== undefined){

                var disabledValue = disabledValue.toString();
                if(disabledValue.indexOf(",")!==-1){
                    disabledValue = disabledValue.split(",");
                }
            }
            //判断defaultvalue是否有空值
            if(defaultValue !== undefined){
                defaultValue = defaultValue.toString();
                if(defaultValue.length === 0){
                    defaultValue = undefined;
                }
            }
            var disabledItem = '';

            for(var i = 0;i < twoData.length;i++){
                var vObj = twoData[i];
                html += '<input type="radio" ';
                if(name){
                    html += 'name="'+name+'" '
                }
                if(valueField){
                    html += 'value="'+vObj[valueField]+'" '
                }

                if(textField){
                    html += 'title="'+vObj[textField]+'" '
                }


                if(defaultValue !== undefined ){
                    if(defaultValue !== '' &&  defaultValue == vObj[valueField]){
                        html += 'checked="" '
                    }
                }

                if(disabledValue !== undefined ){
                    //disabledValue为数组时时
                    if(typeof disabledValue ==="object"){
                        for(var j = 0 ; j < disabledValue.length ; j ++){
                            if(disabledValue[j] == vObj[valueField]){
                                disabledItem ='disabled=""'
                                break
                            }
                        }
                    }else{
                        if(disabledValue == vObj[valueField]){
                            disabledItem ='disabled=""'
                        }
                    }
                }
                if(disabled == true){ //全部禁用时起用
                    disabledItem ='disabled=""'
                }

                html += disabledItem +' ';

                if(required){
                    html += 'lay-verify="required" '
                }

                html += 'lay-filter ="'+name+'">';
                disabledItem = '' //重置为空
            }
        }
        return html
    };

    RadioType.fn.callback = function(settings){
        var that = this;
        form.on('radio('+settings.name+')',function(data){
            var callback = settings.callback;
            if(callback && typeof callback ==="function"){
                callback(data)
            }
        })
    };

    RadioType.fn.makeCallBack = function(callBack){
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

    //设置必填标识
    RadioType.prototype.setRequireIcon = function($elem,obj){
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

    RadioType.fn.val = function(bizName){
        var that = this;
        if(bizName === undefined){
            if(!that.currentObject){
                return "";
            }
            return that.currentObject.find('input:checked').val();
        }else{

            that.currentObject.find('input[value="'+bizName+'"]').prop('checked',true);
            form.render("radio")
        }

    };

    RadioType.fn.attr= function(attr,value){
        var that = this;
        if(!that.currentObject){
                return "";
        }

        if(attr && arguments.length === 1 ){
            return that.currentObject.attr(attr);
        }

        if(arguments.length === 2 ){
            if(attr.indexOf("data-") !== -1){
                var attrName = attr.replace("data-",'');
                that.currentObject.data(attrName,value).removeClass("init");
                that.render()
            }else{
                that.currentObject.attr(attr,value);
            }
        }
    };

    function throwError(msg) {
        throw new Error("[bt-biz-RadioType:" + msg);
    };

    var radioType = new RadioType();
    var obj = {
        init:function(ops){
            radioType.init(ops);
            return this
        },
        get:function(bizName){
            var obj = $('[data-biz-name="'+bizName+'"]');
            if(obj.length ===0){
                throw new Error("[bt-biz-radioType:元素容器找不到，具体设置格式请参考文档API");
            }
            radioType.currentObject = obj;
            return radioType;
        },
    }
   exports('radioType',obj)
})
