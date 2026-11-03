/**
 * 网点、单位联动组件
 */
layui.define(['form','jquery','selectTree'],function(exports){
    var form = layui.form(),$  = layui.jquery;
    var NetCorp = function () {
        this.config ={
            elem:undefined,
            mapper:{
                id:'id',
                name:'name'
            },
            items:[]
        }
    }
    NetCorp.prototype.v = '0.0.1';


    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    NetCorp.prototype.init = function (options){
        var that = this;
        $.extend(true, that.config, options);
        var _config = that.config;

        // if (_config.elem === undefined) {
        //     throwError('NetCorp Error:请配置参数elem,此参数为jQuery选择器!');
        // }
        if(_config.items.length == 0){
            throwError('NetCorp Error:未配置下拉选择器对象!');
        }

        that.render();

    };

    NetCorp.prototype.render = function(){
        var that = this;
        var _config = that.config;
        var html = '';
        html+='<div class="layui-inline">';
        html+='<input type="text" name="'+_config.netName+'" class="layui-input">';
        html+='</div>';
        html+='<div class="layui-inline">';
        html+='<input type="text" name="'+_config.corpName+'" class="layui-input">';
        html+='</div>';
        $(_config.elem).append(html);
        console.log();
    };
    
    NetCorp.prototype.set = function(data){

    };

    function throwError(msg) {
        throw new Error(msg);
    }

    var linkage;
    var obj = {
        currentObj : null,
        init: function(option){
            linkage = new NetCorp();
            linkage.init(option);
            linkage.currentObj = linkage;
            obj.currentObj = linkage;
            return this;
        },
        initLinkAge: function(option){
            linkage = new NetCorp();
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
    exports('netCorp', obj);
});