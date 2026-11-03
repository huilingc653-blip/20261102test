/**
 * 树表功能
 * 本组件依赖layui的from，以及jquery
 * 周亮
 * 2017年5月25日
 * 0.0.1 多选select
 */
layui.define(['jquery'],function(exports){
    var $  = layui.jquery;
    var PickList = function () {
        this.config ={
            elem:undefined,
            width:200,
            height:undefined,
            add: '<i class="fa fa-chevron-right" aria-hidden="true"></i>',
            addAll: '<i class="fa fa-arrow-right" aria-hidden="true"></i>',
            remove: '<i class="fa fa-chevron-left" aria-hidden="true"></i>',
            removeAll: '<i class="fa fa-arrow-left" aria-hidden="true"></i>',
            showTitle:true,
            leftTitle:undefined,
            rightTitle:undefined,
            data:undefined,
            resultData:undefined,
            valueField: undefined,//数据id
            textField:undefined,//数据名称
        }
    }

    PickList.prototype.v = '0.0.1';

    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    PickList.prototype.init = function (options){
        if (options.elem === undefined) {
            throwError('PickList Error:请配置参数elem,此参数为jQuery选择器!');
        }

        var that = this;
        $.extend(true, that.config, options);

        that.config.elem = $(that.config.elem);
        that.initContainer();
        return that;
    }

    PickList.prototype.fill = function (selected,data) {
        var that = this;
        var _config = that.config;
        var option = '';
        _config.elem.find(selected).find('option').detach();
        if(!data){
            return;
        }
        $.each(data, function (key, val) {
            option += '<option data-id=' + (_config.valueField?val[_config.valueField]:val.id) + '>' + (_config.textField?val[_config.textField]:val.text) + '</option>';
        });
        _config.elem.find(selected).append(option);
    };


    PickList.prototype.fillData = function () {
        var that = this;
        var _config = that.config;

        that.fill('.pickData',_config.data)
    }

    PickList.prototype.fillResultData = function () {
        var that = this;
        var _config = that.config;
        that.fill('.pickListResult',_config.resultData)
    }

    PickList.prototype.controll = function () {
        var pickThis = this.config.elem;

        pickThis.find(".pAdd").on('click', function () {
            var p = pickThis.find(".pickData option:selected");
            p.clone().appendTo(pickThis.find(".pickListResult"));
            p.remove();
        });

        pickThis.find(".pAddAll").on('click', function () {
            var p = pickThis.find(".pickData option");
            p.clone().appendTo(pickThis.find(".pickListResult"));
            p.remove();
        });

        pickThis.find(".pRemove").on('click', function () {
            var p = pickThis.find(".pickListResult option:selected");
            p.clone().appendTo(pickThis.find(".pickData"));
            p.remove();
        });

        pickThis.find(".pRemoveAll").on('click', function () {
            var p = pickThis.find(".pickListResult option");
            p.clone().appendTo(pickThis.find(".pickData"));
            p.remove();
        });
    };


    PickList.prototype.setData = function (data) {
        var that = this;
        that.config.data = data;
    }

    PickList.prototype.setResultData = function (data) {
        var that = this;
        that.config.resultData = data;
    }

    PickList.prototype.render = function () {
        var that = this;
        var pickThis = this.config.elem;
        that.fillData();
        that.fillResultData();
    }

    PickList.prototype.getSelectedValue = function () {
        var that = this;
        var _config = that.config;
        var objResult = [];
        _config.elem.find(".pickListResult option").each(function () {
            var id = $(this).data('id');
            if(id){
                objResult.push({
                    id: id,
                    text: this.text
                });
            }

        });
        return objResult;
    };

    PickList.prototype.initContainer = function () {
        var that = this;
        var _config = that.config;
        var pickListHtml =
            '<table class="pickList-container" >';
            if(_config.showTitle){
                pickListHtml+=
                '<thead>' +
                '<tr>' +
                '<th class="picktitle">'+_config.leftTitle+'</th><th></th><th class="picktitle" >'+_config.rightTitle+'</th>' +
                '</tr>' +
                '</thead>';
            }
            pickListHtml+=
            '   <tbody><tr>'+
            '   <td style="width: '+_config.width+'px">' +
            '<select class="pickListSelect pickData" lay-ignore multiple '+ (_config.height?'style="height:'+_config.height+'px!important"':'') +'></select>' +
            '</td>'+
            '   <td class="pickListButtons">' +
            '	<button type="button" class="pAdd btn layui-btn layui-btn-primary layui-btn-small">' + _config.add + '</button>' +
            '	<button type="button" class="pRemove btn layui-btn layui-btn-primary layui-btn-small">' + _config.remove + '</button>' +
            '   <button type="button" class="pAddAll btn layui-btn layui-btn-primary layui-btn-small">' + _config.addAll + '</button>' +
            '	<button type="button" class="pRemoveAll btn layui-btn layui-btn-primary layui-btn-small">' + _config.removeAll + '</button>' +
            '</td>'+
            '   <td style="width: '+_config.width+'px">' +
            '<select class="pickListSelect pickListResult" lay-ignore multiple '+ (_config.height?'style="height:'+_config.height+'px!important"':'') +'></select>' +
            '</td>'+
            '   </tr></tbody>'+
            '</table>';
        _config.elem.append(pickListHtml);
        that.render();
        that.controll();
    };

    function throwError(msg) {
        throw new Error(msg);
    };

    var pickList = new PickList();
    var obj = {
        init: function(option){
            pickList.init(option);
            return this;
        },
        setData:function(data){
            pickList.setData(data);
        },
        setSelectedData:function(data){
            pickList.setResultData(data);
        },
        getSelectedValue:function(){
            return pickList.getSelectedValue()
        },
        render:function () {
            pickList.render();
        }
    };
    //输出test接口
    exports('pickList', obj);
}).addcss('../../pickList/css/pickList.css', 'pickListcss');