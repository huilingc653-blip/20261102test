bt.use(['form','dicts','select', 'datatable', 'selectType', 'moneyType','btImport'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1, 'asc']],
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/systemProvince/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('sys:systemProvince:add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: "启用",
                hide:bt.isnotResource('sys:systemProvince:update'),
                action: function (e, dt, node, config) {
                    qiyong(dt);
                }
            },{
                text: "禁用",
                hide:bt.isnotResource('sys:systemProvince:update'),
                action: function (e, dt, node, config) {
                    jinyong(dt);
                }
            },
            {
                text: bt.lang('common.button.imports'),
                hide:bt.isnotResource('sys:systemProvince:add'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    //layer.alert(bt.lang('common.message.indevelopment'));
                    importExcel();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "provinceCode", bSortable: true},
            {data: "provinceName", bSortable: true},
            {data: "statusName", bSortable: false},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if(bt.isResource('sys:systemProvince:list')){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    }
                    if(bt.isResource('sys:systemProvince:update')){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    }
                    if(bt.isResource('sys:systemProvince:delete')){
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

    if(!bt.isResource('sys:systemProvince:list')){
        $("#searchId").attr('hidden',true);
    }

    //导入初始化
    importExcel = function () {
        //证书签名
        // var signDataMsg = signData("开票登记导入", 9);
        // if (signDataMsg != "") {
        //     bt.loading.hide();
        //     layer.alert(bt.lang(signDataMsg), {icon: 2});
        //     return false;
        // }
        up = bt.btImport.init({
            code: "40000007",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            success: function (head, data, select) {
                $.ajax({
                    type: "POST",
                    // url: basePath + "/bill/accebill/abemitbill/saveImport",
                    url: bt.isResource('sys:systemProvince:add') ? (basePath + "/sys/systemProvince/saveImport") : "",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(select),
                    //重写success方法
                    success: function (result) {
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }
        });
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/sys/systemProvince/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/systemProvince/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function qiyong(dt) {
    var data = dt.rows('.selected').data();
    if(!data || data.length == 0){
        parent.layer.alert("请选择需要启用的数据", {icon: 2});
        return false;
    }
    var ids = "";
    for(var i = 0; i < data.length; i++){
        if("" == ids){
            ids = data[0].id;
        }else {
            ids = ids + "," + data[i].id;
        }
    }
    parent.layer.confirm("是否确认启用？", function () {
        $.post(basePath + "/sys/systemProvince/modify",{id:ids,status:'1'}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function jinyong(dt) {
    var data = dt.rows('.selected').data();
    if(!data || data.length == 0){
        parent.layer.alert("请选择需要禁用的数据", {icon: 2});
        return false;
    }
    var ids = "";
    for(var i = 0; i < data.length; i++){
        if("" == ids){
            ids = data[0].id;
        }else {
            ids = ids + "," + data[i].id;
        }
    }
    parent.layer.confirm("是否确认禁用？", function () {
        $.post(basePath + "/sys/systemProvince/modify",{id:ids,status:'0'}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function view(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/systemProvince/view/" + data.id
    });
}

function update(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/sys/systemProvince/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/systemProvince/update?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

function deleteColumn(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/sys/systemProvince/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
} 