var form,$,selectTree,corp,billPoolCorp;
bt.use(['form','selectTree','treeTable','dicts','element',], function () {
    $ = bt.jquery, form = bt.form();

    dicts=bt.dicts.init();
    bt.treeTable.init({
        elem: '#appTable',
        column: '0'
    });


    $(document).keyup(function(event){
        if(event.keyCode == 13){
            searchForm();
        }
    });

});

// 重置
function resetClick(){
    $('#cellCodeAndName').val("");
    dicts.get("validSign").val('1');
}

// 查询管理单元
function searchForm(){
    var action =basePath + "/sys/cell/listCell";
    $("#searchForm").attr("action",action).attr("method", "post");
    $("#searchForm").submit();
}


// 新增
function addCell(){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/cell/add",
        end:function(){
            searchForm();
        }
    });
}

// 修改
function updateCell(id){
    if(!id){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }

    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/cell/update?id=" + id,
        end:function(){
            searchForm();
        }
    });
}

//删除
function deleteCell(id){
    if(!id){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }

    layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        bt.loading.show();
        $.post(basePath + "/sys/cell/delete",{"id":id}, function(result){
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                searchForm();
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
            }
            bt.loading.hide();
        },'JSON');
    });
}

//查看
function viewCell(id){

    if(!id){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }

    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/cell/view?id="+id,
        end:function(){
            searchForm();
        }
    });
}

//导出
function excelCell(){

    // 判断表格是否有数据
    if($('#appTable tbody tr').length < 1){
        layer.alert(bt.lang('common.msg.temporarily_no_data'), {icon: 7});
        return false;
    }

    var columns=new Array();

    for(var i =0;i<$('th').length-1;i++){
        columns[i]=$('th')[i].innerHTML;
    }

    var exportUrl =  basePath + "/sys/cell/excelCell";
    var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
    try {
        form.append($("<input></input>").attr("type", "hidden").attr("name", "columns").attr("value", columns));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "cellCodeAndName").attr("value", $('#cellCodeAndName').val()));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "validSign").attr("value", dicts.get("validSign").val()));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", bt.navtab.getCurrentDom().name));
        form.appendTo('body');
        form.submit();
    } catch (e) {
    } finally {
        form.remove();
    }
}
