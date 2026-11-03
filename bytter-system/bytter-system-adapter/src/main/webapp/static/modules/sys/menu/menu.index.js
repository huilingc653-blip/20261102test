
bt.use(['jquery','form','element','treeGrid'], function(){
    $ = bt.jquery,element = parent.bt.element();

    var addFunc = function(rowData) {
        var id = (rowData && rowData.id) ? rowData.id:'';
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/menu/view?parentId=" + id,
            end: function () {
                if(window.top.refreshParentFlag){
                    treeGrid.trigger("reloadGrid");
                }
                window.top.refreshParentFlag = false
            }
        });
    }

    var editFunc = function(rowData){
        var id = rowData.id ? rowData.id:'';
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/menu/view?id=" + id,
            end:function(){
                if(window.top.refreshParentFlag){
                    treeGrid.trigger("reloadGrid");
                }
                window.top.refreshParentFlag = false
            }
        });
    }

    var delFunc = function(rowData){
        var id = rowData.id ? rowData.id:'';
        //"删除该菜单将会一同删除对应的资源、角色关联信息，是否继续？"
        parent.layer.confirm(bt.lang("sys.menu.deleteMenu"),function(){
            $.get(basePath + "/sys/menu/delete/" + id, function(result){
                treeGrid.trigger("reloadGrid");
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        })
        // },{icon:2});
    }

    var treeGrid = bt.treeGrid.init({
        elem:"#menuTable",
        url:basePath+"/sys/menu/tree.json",
        ExpandColumn:"name",
        colModel:[
            // 菜单名称
            { name: "name",label:bt.lang('sys.menu.menuName')},
            { name: "url",label:"URL" },
            { name: "sort",label:bt.lang('common.message.sort'),width:50 ,editable:true,align:"center"},
            { name: "status",label:bt.lang('common.table.status'),width:50 ,align:"center",formatter:function(cellvalue, options, rowObject){
                return rowObject.status == 1 ?bt.lang('common.button.enable'):bt.lang('common.button.taboo');
            }},
            { label:bt.lang('common.table.thead.operation'),width:100,formatter:function(cellvalue, options, rowObject){
                var html = '';
                    html+='<a href="javascript:void(0)" row="'+rowObject.id+'" class="add-sub">'+bt.lang("sys.menu.addSubMenu")+'</a>';
                    html+='<span class="text-explode"> | </span>';
                    html+='<a href="javascript:void(0)" row="'+rowObject.id+'" class="add-resources">'+bt.lang("sys.resource.add")+'</a>';
                    return html;
            } },
        ],
        cellurl : basePath + '/sys/menu/updateSort',
        navPosition:"top",
        addFunc:addFunc,
        editFunc:editFunc,
        delFunc:delFunc
    });

    //增加子项
    treeGrid.on("click",".add-sub",function(){
        var data = treeGrid.grid("getOriginalData",$(this).attr("row"));
        addFunc(data);
    });


    treeGrid.on("click",".add-resources",function(){
        var data = treeGrid.grid("getOriginalData",$(this).attr("row"));
        bt.navtab.addNavtab(bt.lang("sys.resource.add") +' - '+ data.name,basePath+"/sys/resources/index?id=" + data.id);
    });

});
