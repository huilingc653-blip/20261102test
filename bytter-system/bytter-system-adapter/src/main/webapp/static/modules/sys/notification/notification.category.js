bt.use(['jquery', 'treeGrid', 'selectTree'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;

    var parentIdTree =  bt.selectTree.init({
        url:basePath + "/sys/notification/categoryTree",
        elem:".notificationCategory-parentId",
        name:'parentId',
        selected:$("#categoryParentId").val(),
        currentNode:'$!{category.id}'
    });

    var menuIdTree =  bt.selectTree.init({
        url:basePath + "/sys/menu/tree",
        elem:".menu-parentId",
        name:'menuId',
        selected:$("#categoryMenuId").val(),
        placeholder:bt.lang('sys.notification.category.menuBelong')
    });
   // parent.userIdLayerIndex = null;
    var addFunc = function(rowData) {
        var id = (rowData && rowData.id) ? rowData.id:'';
        parent.userIdLayerIndex = parent.layer.open({
            type: 2,
            area: ['850px', '650px'],
            maxmin: true,
            content: basePath + "/sys/notification/categoryView?parentId=" + id,
            end: function () {
                // 编辑/新增保存成功，将刷新数据
                if(window.top.editSuccessForCategoryViewFlag){
                    table.trigger("reloadGrid");
                }
                window.top.editSuccessForCategoryViewFlag = false
            }
        });
    }

    var editFunc = function(rowData){
        var id = rowData.id ? rowData.id:'';
        parent.userIdLayerIndex = parent.layer.open({
            type: 2,
            area: ['850px', '650px'],
            content: basePath + "/sys/notification/categoryView?id=" + rowData.id,
            end: function () {
                // 编辑/新增保存成功，将刷新数据
                if(window.top.editSuccessForCategoryViewFlag){
                    table.trigger("reloadGrid");
                }
                window.top.editSuccessForCategoryViewFlag = false
            }
        });
    }

    var delFunc = function(rowData){
        var ids = [];
        var id = rowData.id ? rowData.id:'';
        if(!id){
            return;
        }
        ids.push(id);
        // 删除后将不可恢复是否继续？
        layer.confirm(bt.lang('sys.notification.deleteShow'), function () {
            $.post(basePath + "/sys/notification/deleteCategory", {ids: ids}, function (result) {
                table.trigger("reloadGrid");
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    var table = bt.treeGrid.init({
        elem:"#notificationTable",
        url:basePath+"/sys/notification/category.json",
        ExpandColumn:"name",
        mtype: "post",
        disparityHeight:205,
        colModel:[
            // 分类名称
            { name: "name", label: bt.lang('sys.notification.categoryName')},
            // 常量值
            { name: "tag", label: bt.lang('sys.notification.constant') },
            // 所属菜单
            { name: "menuName", label: bt.lang('sys.notification.menuBelong')},
            // 资源值
            { name: "resourceContent", label: bt.lang('sys.notification.resourceValue')},
            // 列表跳转地址
            { name: "listUrl", label: bt.lang('sys.notification.listJump'),width:100},
            // 详情跳转地址
            { name: "detailUrl", label: bt.lang('sys.notification.detailJump'),width:100},
            // 排序
            { name: "sort", width:50, label: bt.lang('common.message.sort')},
            // 操作
            { label: bt.lang('common.table.thead.operation'),sortable:false,width:180,formatter:function(cellvalue, options, rowObject){
                //增加子模块
                var html = '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="add-sub">'+bt.lang("sys.module.addSub")+'</a>';
                return html;
            }}
        ],
        navPosition:"top",
        addFunc:addFunc,
        editFunc:editFunc,
        delFunc:delFunc
    });

    table.on("click",".add-sub",function(){
        var data = table.grid("getOriginalData",$(this).data("row"));
        addFunc(data);
    });


    $("#searchBtn").click(function () {
        var corpIds = corp.get("ins_corpId").val();
        var status = $('#status').val();
        $("#notificationTable").jqGrid('setGridParam', {
            postData: {
                'id': corpIds,
                'status': status
            }
        }).trigger("reloadGrid"); //重新载入
    });
});
