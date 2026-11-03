
bt.use(['form','jquery','element','navtab','tableGrid'], function(){
    var form = bt.form(),$ = bt.jquery,element = parent.bt.element();
    $getId = function(currnet){
        return currnet.parents('tr').data('id');
    }
       //增加子项
   var addFunc = function(){
     var id = $getId($(this));
        parent.layer.open({
            type: 2,
            area: ['850px', '400px'],
            maxmin: false,
            content: basePath + "/sys/role/view/?parentId=" + id,
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
        }
        });
     }
 var table = bt.tableGrid.init({
    elem: "#role-table",
    url: basePath + "/sys/role/tableGrid.json",
    colModel: [
        { name: "code", label: bt.lang('sys.role.code')},
        { name: "name", label: bt.lang('sys.role.roleName')},
        { name: "description", label: bt.lang('sys.role.description'),},

        { label: bt.lang('common.table.thead.operation'),sortable:false,width:200,formatter:function(cellvalue, options, rowObject){
            var html = '<a href="javascript:void(0)" data-row="' + rowObject.id + '" class="assign">'+bt.lang('accountGrant.assign')+'</a>';
            html += '<span class="text-explode">|</span>';
            html += '<a href="javascript:void(0)" data-row="' + rowObject.id + '" class="assign-user">'+bt.lang('accountGrant.assginUser')+'</a>';
            html += '<span class="text-explode">|</span>';
            html += '<a href="javascript:void(0)" data-row="' + rowObject.id + '" class="update">'+bt.lang('common.button.edit')+'</a>';
            if(rowObject.useable!==2){
                html += '<span class="text-explode">|</span>';
                html += '<a href="javascript:void(0)" data-row="' + rowObject.id + '" class="delete">'+bt.lang('common.button.delete')+'</a>';
            }
            return html;
        }}
    ],
    addFunc:addFunc,
});
//修改
  table.on('click', '.update', function (event) {
  var data = table.grid("getOriginalData",$(this).data("row"));
        parent.layer.open({
            type: 2,
            area: ['850px', '400px'],
            maxmin: false,
            content: basePath + "/sys/role/view/?id=" + data.id,
            end:function(flag){
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
});
//删除
 table.on('click', '.delete', function (event) {
  var data = table.grid("getOriginalData",$(this).data("row"));
        parent.layer.confirm(bt.lang('confirm.delete.this.record'), function () {
            $.get(basePath + "/sys/role/delete/" + data.id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            table.trigger("reloadGrid");
            },'JSON');
        });
});

//资源授权
 table.on('click', '.assign', function (event) {
  var data = table.grid("getOriginalData",$(this).data("row"));
        bt.navtab.addNavtab(bt.lang('accountGrant.assign')+' - '+ data.name,basePath+"/sys/role/assign?id=" + data.id);
});

//分配用户
 table.on('click', '.assign-user', function (event) {
  var data = table.grid("getOriginalData",$(this).data("row"));
       var name = $(this).parents('tr').find('td:eq(0)').text();
        parent.layer.open({
            title:bt.lang('accountGrant.assginUser'),
            type: 2,
            area: ['850px', '550px'],
            maxmin: false,
            content: basePath + "/sys/role/assignUser?id=" + data.id
        });
});

    //查询重置
    $("#queryReset").click(function(){
        $("#roleName").val("");
        $("#roleCode").val("");
    });
});
