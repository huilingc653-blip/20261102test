var datatable;
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();

    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
    }
    var accountTable = bt.accountTable.init();

    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    }

    bt.corp.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns:{
            rightColumns:1
        },
        checkbox: true,
        order: [[2, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/bisaccdtlcheck/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:bankAcc:bisAccDtlCheck:apply'),
                text: bt.lang('netbank.button.setCheckBankAcc'),
                action: function ( e, dt, node, config ) {
                    setCheckBankAcc();
                }
            },{
                extend: "colvis",
                hide:bt.isnotResource('netbank:bankAcc:bisAccDtlCheck:apply'),
                text: bt.lang('common.button.batchHandled'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchHandled(data);
                }
            },{
                extend: "colvis",
                hide:bt.isnotResource('netbank:bankAcc:bisAccDtlCheck:apply'),
                text: bt.lang('common.button.batchUnHandled'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchUnHandled(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "statusName", bSortable: true},
            {data: "checkDate", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "bankName", bSortable: true},
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {
                data: "checkBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.checkBal);
                }
            },
            {
                data: "diffBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.diffBal);
                }
            },
            {data: "createDate", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang('common.button.viewFlow') + '</a>';
                    return html;
                }
            }
        ]
    });
});

function setCheckBankAcc(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('netbank.button.setCheckBankAcc'),basePath + "/bankacc/bisaccdtlcheck/setCheckBankAcc");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccdtlcheck/setCheckBankAcc",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function view(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.viewFlow'),basePath + "/bankacc/bisaccdtlcheck/queryDtl/" + data.bankAcc + "/" +data.curId + "/" +data.checkDate);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccdtlcheck/queryDtl/" + data.bankAcc + "/" +data.curId + "/" +data.checkDate
    })
}

function batchHandled(data){
    var len = data.length;
    if (len < 1) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchHandled'),function(){
        $.post(basePath + "/bankacc/bisaccdtlcheck/batchHandled", {ids: ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function batchUnHandled(data){
    var len = data.length;
    if (len < 1) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchUnHandled'),function(){
        $.post(basePath + "/bankacc/bisaccdtlcheck/batchUnHandled", {ids: ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
