
bt.use(['jquery', 'tableGrid'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var $ = bt.jquery;

    var addFunc = function(){
        parent.layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/serviceProduct/view/",
            end: function () {
                table.trigger("reloadGrid");
            }
        });
    }
    var editFunc = function(rowData){
        var id = rowData.id ? rowData.id:'';
        parent.layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/serviceProduct/view/?id=" + rowData.id,
            end: function () {
                table.trigger("reloadGrid");
            }
        });
    }

    var delFunc = function(){
        var rowData = table.getSelectRowsData();
        var ids = [];
        for (var i = 0; i < rowData.length; i++) {
            ids.push(rowData[i].id);
        }
        parent.layer.confirm(bt.lang("sys.notification.deleteShow"),function(){
            $.post(basePath + "/sys/serviceProduct/delete/", {serviceProductIds:ids}, function(result){
                table.trigger("reloadGrid");
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
    }

    var table = bt.tableGrid.init({
        elem: "#serviceProductTable",
        url: basePath + "/sys/serviceProduct/tableGrid.json",
        colModel: [
            // 服务名称
            { name: "name", label: "服务名称"},
            // 服务描述
            { name: "description", label: "服务描述"},
            // 服务WSDL URL
            { name: "url", label: "服务WSDL", formatter:function(cellvalue, options, rowObject) {
                // var html = '<a href="javascript:void(0);">'+rowObject.url+'</a>';
                var html = '<a target="_blank" href="'+rowObject.url+'">'+rowObject.url+'</a>';
                return html;
            }},
            // 服务状态
            { name: "status", label: "服务状态"},
        ],
        addFunc:addFunc,
        editFunc:editFunc,
        delFunc:delFunc
    });


});
