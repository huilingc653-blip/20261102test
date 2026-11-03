bt.use(['jquery', 'form', 'element', 'treeGrid'], function () {
    $ = bt.jquery, element = parent.bt.element();

    var addFunc = function (rowData) {
        var id = (rowData && rowData.id) ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/module/view?parentId=" + id,
            end: function (flag) {
                if(flag){
                    treeGrid.trigger("reloadGrid");
                }
            }
        });
    }

    var editFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/module/view?id=" + id,
            end: function (flag) {
                if(flag){
                    treeGrid.trigger("reloadGrid");
                }
            }
        });
    }

    var delFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        parent.layer.confirm(bt.lang("sys.module.delete.tips.info"), function () {
            $.get(basePath + "/sys/module/delete/" + id, function (result) {
                treeGrid.trigger("reloadGrid");
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    var treeGrid = bt.treeGrid.init({
        elem: "#moduleTable",
        url: basePath + "/sys/module/tree.json",
        ExpandColumn: "name",
        colModel: [
            // 模块名称
            {name: "name", label: bt.lang('sys.module.name')},
            {name: "sort", label: bt.lang('common.message.sort'), width: 50, editable: true, align: "center"},
            // 操作
            {
                label: bt.lang('common.table.thead.operation'),
                width: 100,
                formatter: function (cellvalue, options, rowObject) {
                    var html = '';
                    // 增加子模块
                    html += '<a href="javascript:void(0)" row="' + rowObject.id + '" class="add-sub">' + bt.lang(
                        'sys.module.addSub') + '</a>';
                    if (rowObject.parentId) {
                        html += '<span class="text-explode"> | </span>';
                        // 分配菜单
                        html += '<a href="javascript:void(0)" row="' + rowObject.id + '" class="add-menus">' + bt.lang(
                            'sys.module.menu.allot') + '</a>';
                    }
                    return html;
                }
            },
        ],
        cellurl: basePath + '/sys/module/updateSort',
        navPosition: "top",
        addFunc: addFunc,
        editFunc: editFunc,
        delFunc: delFunc
    });


    //添加树表全部收起，全部展开
    treeGrid.grid("customButton", {
        title: bt.lang("sys.module.export.assigned.menus"),
        buttonicon: "fa-external-link",
        onClickButton: function () {
            //导出已分配的菜单
            var form = $("<form>");//定义一个form表单
            form.attr("style", "display:none");
            form.attr("target", "");
            form.attr("method", "post");
            form.attr("id", "exportFrom");
            form.attr("action", basePath + "/sys/menu/export");
            var input = $("<input>");
            input.attr("type", "hidden");
            // input.attr("name","menuIds");
            // input.attr("value",menuIds);
            $("body").append(form);//将表单放置在web中
            form.append(input);
            form.submit();//表单提交
            form.remove();
        }
    });

    treeGrid.on("click", ".add-menus", function () {
        var data = treeGrid.grid("getOriginalData", $(this).attr("row"));
        bt.navtab.addNavtab(bt.lang("sys.module.allot") + ' - ' + name, basePath + "/sys/module/assign?id=" + data.id);
    });
    //增加子项
    treeGrid.on("click", ".add-sub", function () {
        var data = treeGrid.grid("getOriginalData", $(this).attr("row"));
        addFunc(data);
    });
});
