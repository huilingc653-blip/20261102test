
bt.use(['jquery', 'dicts','tableGrid'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var $ = bt.jquery;
    bt.dicts.init();
    var addFunc = function(){
        layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/homeWidget/view",
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }

    var editFunc = function(rowData){
        var id = rowData.id ? rowData.id:'';
        layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/homeWidget/view?id=" + rowData.id,
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }

    var delFunc = function(){
        var rowData = table.getSelectRowsData();
        var ids = [];
        var names = "";
        for (var i = 0; i < rowData.length; i++) {
            ids.push(rowData[i].id);
            names += rowData[i].username;
            if (i != rowData.length - 1) {
                names += ",";
            }
        }
        layer.confirm(bt.lang("sys.notification.deleteShow"), function () {
            $.post(basePath + "/sys/homeWidget/delete/", {ids: ids}, function (result) {
                table.trigger("reloadGrid");
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    var table = bt.tableGrid.init({
        elem: "#userTable",
        url: basePath + "/sys/homeWidget/tableGrid.json",
        colModel: [
            //所属分类
            { name: "categoryId", label: bt.lang('system.homeWidget.category'), formatter:function(cellvalue, options, rowObject){
                    return rowObject.categoryLang;
            }},
            //组件类型
            { name: "type", label:bt.lang('system.homeWidget.pluginType'), formatter:function(cellvalue, options, rowObject){
                    return rowObject.typeLang;
            }},
            //名称
            { name: "name", label:bt.lang('system.homeWidget.name')},
            //转向内容
            { name: "content", label: bt.lang('system.homeWidget.turnContent') },
            //最小宽度
            { name: "minWidth", label: bt.lang('system.homeWidget.minWidth') },
            //最大宽度
            { name: "maxWidth", label: bt.lang('system.homeWidget.maxWidth') },
            //高度
            { name: "height", label: bt.lang('system.homeWidget.high') },
            //排序
            { name: "sort", label: bt.lang('common.message.sort') },
            //创建时间
            { name: "createTime", label: bt.lang('system.homeWidget.createTime') }
        ],
        addFunc:addFunc,
        editFunc:editFunc,
        delFunc:delFunc,
    });


});
