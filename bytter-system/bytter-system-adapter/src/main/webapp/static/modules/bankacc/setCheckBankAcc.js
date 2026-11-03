var datatable;
bt.use(['form', 'datatable', 'corp', 'bank', 'selectType','dicts', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.mulselect.init();
    bt.selectType.init();
    bt.dicts.init();//此初始化测试没有构建到
    bt.bank.init({
        elem: '.bank',
    });
    bt.mulselect.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [1, 'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/bisaccdtlcheck/queryBankAccDatatable"
        },
        buttons: [
            {
                extend: "colvis",
                hide:bt.isnotResource('netbank:bankAcc:bisAccDtlCheck:apply'),
                text: bt.lang('bisAccDtlCheck.button.batchCheck'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    setCheckBankAccSave(data, "1");
                }
            }, {
                extend: "colvis",
                hide: bt.isnotResource('netbank:bankAcc:bisAccDtlCheck:apply'),
                text: bt.lang('bisAccDtlCheck.button.batchUnCheck'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    setCheckBankAccSave(data, "0");
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "bankAcc", bSortable: true},
            {data: "isCheckStr", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: false},
            {data: "bankName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {
                data: "status", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                   var html="";
                   if(full.status == -2){
                       html = bt.lang('table.bisAccDtlCheck.statusDel');
                   }else{
                       html = bt.lang('table.bisAccDtlCheck.statusNormal');
                   }
                    return html;
                }
            },
            {data: "validSignStr", bSortable: true},
            {data: "isOnlineStr", bSortable: true}
        ]
    });
});

function setCheckBankAccSave(data, checkSign){
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
    var msg = "netbank.message.confirm.batchCheck";
    if (checkSign == "0") {
        msg = "netbank.message.confirm.batchUnCheck";
    }
    parent.layer.confirm(bt.lang(msg),function(){
        $.post(basePath + "/bankacc/bisaccdtlcheck/setCheckBankAccSave", {ids: ids, checkSign:checkSign}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}