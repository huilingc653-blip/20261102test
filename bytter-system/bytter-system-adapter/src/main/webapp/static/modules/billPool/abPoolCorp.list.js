var form,$,selectTree,corp,billPoolCorp;
bt.use(['form','selectTree','treeTable','corp','billPoolCorp'], function () {
    $ = bt.jquery, form = bt.form();

    corp = bt.corp.init();
    billPoolCorp = bt.billPoolCorp.init();

    bt.treeTable.init({
        elem: '#appTable',
        column: '1'
    });

    //全选
    form.on('checkbox(checkAll)', function (data) {
        var child = $('input[name="checkDetailId"]');
        child.each(function (index, item) {
            if(!item.disabled){
                item.checked = data.elem.checked;
                // 对选中的数据添加背景颜色
                var trObj = $('tr[data-tt-id="' + item.value + '"]');
                if(data.elem.checked){
                    trObj.attr("style", "background-color:#d2efd3");
                }else {
                    trObj.removeAttr("style");
                }
            }
        });
        form.render('checkbox');
    });

    $(document).keyup(function(event){
        if(event.keyCode == 13){
            searchForm();
        }
    });

});

// 重置
function resetClick(){
    $('#corpCode').val("");
    $('#corpName').val("");
}

// 查询票据池单位
function searchForm(){
    var action =basePath + "/billPool/corp/listPoolCorp";
    $("#searchForm").attr("action",action).attr("method", "post");
    $("#searchForm").submit();
}


// 新增
function addBillPoolCorp(){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billPool/corp/add",
        end:function(){
            searchForm();
        }
    });
}

// 增加下级单位
function addLowerCorp(id, bustypeLevel){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billPool/corp/addLower?parentId="+id+'&bustypeLevel='+bustypeLevel,
        end:function(){
            searchForm();
        }
    });
}

// 修改
function updateCorp(billPoolId){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billPool/corp/update?id=" + billPoolId,
        end:function(){
            searchForm();
        }
    });
}

//选中行tr时回调
function checkTr(id){

    // 选中添加样式
    var checkObj = $('input[value="'+id+'"]')[0];
    var trObj = $('tr[data-tt-id="' + id + '"]');
    if(!checkObj.disabled) {
        if("rgb(210, 239, 211)" !== trObj.css("background-color")){
            checkObj.checked = true;
            trObj.attr("style", "background-color:#d2efd3");
        }else {
            checkObj.checked = false;
            trObj.removeAttr("style");
        }
        form.render();
    }

    // 判断全选按钮是否选中
    var checkboxs = $('input[name="checkDetailId"]:not(:disabled)');
    var child = $('input[name="checkDetailId"]:checked');
    if(checkboxs.size() === child.size()){
        $('input[name="checkAll"]')[0].checked=true;
    }else {
        $('input[name="checkAll"]')[0].checked=false;
    }
    form.render();
}


//批量删除
function deleteBillPoolCorp(){
    var param;
    var child = $('input[name="checkDetailId"]:checked');
    if(child.length === 0){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var msg='billPool.corp.deleteCorpWarn2';
    child.each(function (index, item) {
        if(index < 1){
            param = item.value;
        }else {
            param += ','+item.value;
        }
        if($("tr[data-tt-parent-id='"+item.value+"']").length > 0){
           msg='billPool.corp.deleteCorpWarn';
        }
    });

    layer.confirm(bt.lang(msg),function(){
        bt.loading.show();
        $.post(basePath + "/billPool/corp/delete",{"ids":param}, function(result){
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

//启用禁用
function changeBillPoolCorp(id,operate){
    var msgTips;
    if(operate === "disable"){
        msgTips='common.message.confirm.taboo';
    }else{
        msgTips='common.message.confirm.batchEnable';
    }
    parent.layer.confirm(bt.lang(msgTips),function(){
        $.post(basePath + "/billPool/corp/disableOrEnable/"+operate,{"id":id}, function(result){
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                searchForm();
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
