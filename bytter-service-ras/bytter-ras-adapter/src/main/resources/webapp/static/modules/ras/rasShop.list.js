var datatable;
bt.use(['form','datatable','corp','btImport','dicts'], function(){
    $ = bt.jquery;
    bt.form();
    bt.corp.init();
    bt.dicts.init();

    //生成门店列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/shop/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: bt.lang('ras.shopButton.start'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        startButton(rowData);//批量更新状态
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            },{
                text: bt.lang('ras.shopButton.close'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        closeButton(rowData);//批量更新状态
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }
        ]
        ,columns: [
            {data:"code",className:'center',bSortable:true},
            {data:"name",className:'center',bSortable:true},
            {data:"corpName",className:'center',bSortable:true},
            {data:"shopTypeName",className:'center',bSortable:true},
            {data:"asDate",className:'center',bSortable:true},
            {data:"ecMoney",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.ecMoney);
                }
            },
            {data:"shopNo",className:'center',bSortable:true},
            {data:"shopName",className:'center',bSortable:true},
            {data:"isCorpName",className:'center',bSortable:true},
            {data:"rmk",className:'center',bSortable:true},
            {data:"vSignName",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }}
        ]
    });
    //导入门店数据
    importExcel = function () {
        up = bt.btImport.init({
            code: "80000001",
            titleRows: 0,
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/ras/shop/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }
});
//跳转新增页面
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/ras/shop/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/shop/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
//启动按钮
function startButton(list){
    var ids = [];//id+状态集
    for(var i=0;i<list.length;i++){
        var dId = list[i].id+"|"+list[i].vSign;//拼接状态
        if(dId){
            ids.push(dId);
        }
    }
    parent.layer.confirm(bt.lang('ras.message.sureStart'),function(){
        var action = "/ras/shop/updateStatus/" + ids + "/04";
        $.post(basePath + action, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                datatable.draw();
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
    });
}

//禁用按钮
function closeButton(list){
    var ids = [];//id+状态集
    var ids2 = [];//id集
    for(var i=0;i<list.length;i++){
        var dId = list[i].id+"|"+list[i].vSign;//拼接状态
        if(dId){
            ids.push(dId);
            ids2.push(list[i].id);
        }
    }
    //校验该门店是否存在待稽核的数据
    var msg = bt.lang('ras.message.sureClose');
    $.ajax({
        type: "POST",
        url: basePath + "/ras/shop/checkShopNotAudit/"+ids2,
        dataType: "json",
        async: false,
        success: function (result) {
            if (result.obj) {
                msg = bt.lang('ras.message.shopHaveNotAudit');
            }
        }
    });
    parent.layer.confirm(msg,function(){
        var action = "/ras/shop/updateStatus/" + ids + "/05";
        $.post(basePath + action, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                datatable.draw();
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
    });
}
//跳转视图页面
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,basePath + "/ras/shop/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/shop/view/" + data.id
    });
}
//跳转编辑页面
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/ras/shop/update/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/ras/shop/update/" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}