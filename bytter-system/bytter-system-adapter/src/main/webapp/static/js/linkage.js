/**
 * 联动组件
 * 本组件依赖layui的from，以及jquery
 * 周亮
 * 2017年4月18日
 * 0.0.1 基础联动
 * 0.0.2 加入传入默认值递归获取数据并选中
 * 0.0.3 加入数据字段映射功能
 * 0.0.4 修复同一个页面出现多个级联，产生冲突问题（2017年5月4日 19:39:46）
 * 0.0.5 添加必填required 参数 刘勇军
 * 0.0.6 去除会提交默认值选项，修复required参数无效问题
 * 0.0.7 在datatable中，使用此控件，需要重新计算列宽
 * 0.0.8 在set方法中，添加了设置必填标识的方法
 * 0.0.9 在reset方法中，点击 form重置按钮时，重置组件
 * 0.0.10 新增加点击回调方法 参数value 代表选中的值；select 代表下拉框对象
 */
layui.define(['form','jquery'],function(exports){
    var form = layui.form(),$  = layui.jquery;
    var Linkage = function () {
        this.config ={
            url:undefined,
            elem:undefined,
            parameterFormat:"",
            mapper:{
                id:'id',
                name:'name'
            },
            items:[],
            elemArr:[],
            reset:true
        }
        this.currentObject = null;
        this.currentRenderNumber = 0;
    }
    Linkage.prototype.v = '0.0.4';

    /**
     * 创建opetion标签，标识选中状态
     * @param current
     * @param next
     * @param parentId
     */
    Linkage.prototype.loadSelect = function(groupName, current, next, parentId,initCallback){
        var that = this;
        var _config = that.config;
        parentId = parentId?_config.parameterFormat+parentId:parentId;
        $.get(_config.url + parentId,function(result){
            var list = result;
            var _item = that.getItemByName(current,next);
            var html = '<option value="">'+that.getLabel(_item)+'</option>';
            var currentId;
            for (var i = 0; i < list.length; i++) {
                var flag = _item && typeof(_item.selected) != 'undefined' && _item.selected && _item.selected==list[i][_config.mapper.id];
                if(flag){
                    currentId = list[i][_config.mapper.id];
                }
                html += '<option value="'+list[i][_config.mapper.id] + '" '+ (flag?'selected':'')  + '>' + list[i][_config.mapper.name] + '</option>';
            }
            if(current){
                $('select[data-group="'+groupName+'"]').each(function(){
                    var that = $(this);
                    if(that.attr('name')==current){
                        that.html(html);
                    }
                });
            }else{
                $('select[data-group="'+groupName+'"]').each(function(){
                    var that = $(this);
                    if(that.attr('name')==next){
                        that.html(html);
                    }
                });
            }
            //若根据名字找到item，具有默认选中的值，有需要改变的子item
            if(_item && (typeof(_item.selected) != 'undefined') && (typeof(_item.change) != 'undefined') && currentId){
                that.loadSelect(groupName, '', _item.change, currentId);
            }
            form.render('select');
            if(initCallback){
                initCallback(list)
            }
            //在datatable中，使用此控件，需要重新计算列宽
            if($.fn.dataTable){
                $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
            }
        },'JSON')
    }

    Linkage.prototype.getItemByName = function(name,next){
        var that = this;
        var _config = that.config;
        for(var i=0;i<_config.items.length;i++){
            var item = _config.items[i];
            if(item.name === name||item.name===next){
                return item;
            }
        }
    }

    /**
     * 获取当前select的默认展示数据
     * @param item
     * @returns {string}
     */
    Linkage.prototype.getLabel = function(item){
        return (!item || (typeof(item.label) == 'undefined'))?bt.lang('common.placeholder.select'):item.label;
    }

    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    Linkage.prototype.init = function (options){
        var that = this;
        $.extend(true, that.config, options);
        var _config = that.config;

        if (_config.url === undefined) {
            throwError('Linkage Error:请配置远程数据URL!');
        }
        // if (_config.elem === undefined) {
        //     throwError('Linkage Error:请配置参数elem,此参数为jQuery选择器!');
        // }
        if(_config.items.length == 0){
            throwError('Linkage Error:未配置下拉选择器对象!');
        }

        that.render();

    }

    Linkage.prototype.render = function(){
        var that = this;
        var _config = that.config;
        if(_config.elem){
            $(_config.elem).empty();
        }
        var num = Math.random()*100;
        num = parseInt(num, 10);
        var groupName = 'group' + num;
        for(var i=0;i<_config.items.length;i++){
            var item = _config.items[i];
            if(item.group){
                groupName = item.group;
            }
            var callbackFlag = false;
            if(item.callback){
                callbackFlag = true;
            }
            var html = '<div class="layui-input-inline">'
            html +='<select name="'+item.name+'" lay-filter="'+item.name+'Filter" '+((typeof(item.change) == 'undefined')?'':'data-change="'+item.change)+'" '+(!item.required?'':'lay-verify="required"')+' data-group="'+ groupName +'"  '+(callbackFlag?'data-callback="'+item.callback+'"':"")+' '
                +(item.disabled?'disabled':"")+'>';
            html +='     <option value="">'+that.getLabel(item)+'</option>';
            html +='    </select>'
            html +='</div>';

            if(item.appendTo){
                if(typeof(item.appendTo) === 'string') {
                    $(item.appendTo).empty();
                    $(item.appendTo).append(html);
                    $(item.appendTo).addClass('init');
                    if(_config.reset){
                        _config.elemArr.push($(item.appendTo));//存贮dom
                    }
                }else if(typeof(item.appendTo) === 'object'){
                    item.appendTo.empty();
                    item.appendTo.append(html);
                    item.appendTo.addClass('init');
                    if(_config.reset){
                        _config.elemArr.push(item.appendTo);//存贮dom
                    }
                }
            }else if(_config.elem){
                if(typeof(_config.elem) === 'string') {
                    $(_config.elem).append(html);
                    $(_config.elem).addClass('init');
                    if(_config.reset){
                        _config.elemArr.push($(_config.elem));//存贮dom
                    }
                }else if(typeof(_config.elem) === 'object') {
                    _config.elem.append(html);
                    _config.elem.addClass('init');
                    if(_config.reset){
                        _config.elemArr.push(_config.elem);//存贮dom
                    }
                }
            }
            //当改变下拉框触发
            form.on('select('+item.name+'Filter)', function(data) {
                var value = data.value;
                var next = $(data.elem).data('change');
                var groupName = $(data.elem).data('group');
                var callback  = $(data.elem).data('callback');
                callback = that.makeCallBack(callback);
                that.loadSelect(groupName,'',next,value);
                if(callback){
                    callback(value,data.othis,data.elem)
                }
            });
        }
        that.loadSelect(groupName, _config.items[0].name,'','',_config.items[0].initCallback);
        $(_config.elem).addClass('init');
        // $(_config.elem).detach();
        //添加form重置方法
        if(_config.reset){
            that.reset();
        }
    }

    Linkage.prototype.set = function(data){
        var that = this;
        if(!data){
            return;
        }
        var flag = false;
        for(var i=0;i< that.currentObj.config.items.length;i++) {
            var item = that.currentObj.config.items[i];
            var appendToElem = item.appendTo;
            for(var j = 0;j<data.length;j++){
                var d = data[i];
                if(d.name && (d.selected || d.defaultValue !== undefined) && d.name==item.name){ // d.defaultValue 添加了判定是否是undefined 用于form 重置
                    flag = true;
                    if(d.defaultValue){
                        that.currentObj.config.items[i].selected = d.defaultValue;
                    }else{
                        that.currentObj.config.items[i].selected = d.selected;
                    }
                }
                if(d.required !== undefined && !flag ){
                    if(d.required === false){
                        appendToElem.find('select').removeAttr('lay-verify');
                        appendToElem.removeAttr('data-required');
                    }
                    if(d.required === true){
                        appendToElem.find('select').attr('lay-verify','required');
                        appendToElem.data('required','true')
                    }
                    item.required = d.required;
                }
            }
        }
        if(flag){
            that.currentObj.render();
        }
    }
    //点击 form重置按钮时，重置组件 liuyj
    Linkage.prototype.reset = function(){
        var that = this;
        that.config.reset = false;
        that.config.elemArr[0].parents('form').find('button[type=reset]').on('mouseup',function(){
            var resetData = [];
            bt.each(that.config.elemArr,function (i,ele) {
                var item = {};
                item.name = this.data('name');
                item.defaultValue = this.data('defaultvalue'.toLocaleLowerCase());
                resetData.push(item)
            });
            setTimeout(function(){
                that.set(resetData)
            }, 50);
        })
    }

    Linkage.prototype.makeCallBack = function (callBack) {
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

    function throwError(msg) {
        throw new Error(msg);
    };

    var linkage;
    var obj = {
        currentObj : null,
        init: function(option){
            linkage = new Linkage();
            linkage.init(option);
            linkage.currentObj = linkage;
            obj.currentObj = linkage;
            return this;
        },
        initLinkAge: function(option){
            linkage = new Linkage();
            linkage.init(option);
            linkage.currentObj = linkage
            return linkage;
        },
        get:function(itemName){
            linkage.currentObject = $('select[name="'+itemName+'"]');
            return linkage;
        },
        set:function(data){
            obj.currentObj.set(data);
        }
    };
    //输出test接口
    exports('linkage', obj);
});
