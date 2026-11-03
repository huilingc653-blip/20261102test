var billTable = {};
var form,$,billPool,billPoolCorp,moneyType;
bt.use(['form','billPool', 'datatable', 'btdate', 'select', 'dateType','mulselect', 'dicts','billPoolCorp','moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.billPoolCorp.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/billpool/limitAllotCheck/datatable"
        },
        buttons: [
            //新增、导出
            {
                text: bt.lang('common.button.audit'),
                hide: bt.isnotResource('abpool:limitallotcheck:resource:op'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    audit(data);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "corpName", bSortable: true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data: "createTime", bSortable: true},
            {data: "limtTypeName", bSortable: true},
            {data: "poolMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.poolMoney);
                }},
            {data: "lockMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.lockMoney);
                }},
            {data: "validMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.validMoney);
                }},
            {data: "applyMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.applyMoney);
                }},
            {data: "rmk", bSortable: true},
            {data: "parentName", bSortable: true},
            {data: "bustypeLevel", bSortable: true},
            {data: "bpName", bSortable: true},
            {data: "checkTypeName", bSortable: true},
            {data: "checkMsg", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "optStatusName", bSortable: true},
            {data: "returnMsg", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html ='';
                    //交易失败的可重新发送或中止
                    if(data.optStatus==-1){
                        html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,16)">'+bt.lang("common.button.resend")+'</a>';
                        html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,-2)">'+bt.lang("common.button.tovoid")+'</a>  | ';
                    }
                    html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="detail(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
            }
        ],
        fixedColumns: {
            rightColumns: 1
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'4',
                    dataName:'poolMoney'
                },{
                    colNum:'5',
                    dataName:'lockMoney'
                },{
                    colNum:'6',
                    dataName:'validMoney'
                },{
                    colNum:'7',
                    dataName:'applyMoney'
                }
            ]
        }
    });
});

//审核
function audit(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids;
    var applyMoney = 0;
    for (i = 0; i < len; i++) {
        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
        applyMoney = applyMoney+rowData[i].applyMoney;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '430px'],
        maxmin: true,
        content: basePath + "/billpool/limitAllotCheck/batchAudit?ids=" + ids+"&applyMoney="+applyMoney+"&num="+len,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//修改
function update(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/pool/edit/" + id,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//明细查看
function detail(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billpool/limitAllotCheck/viewLimitAllotCheck/"+data.id,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

//修改状态
function updateStatus(el, status) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    var msg = bt.lang('common.message.confirm.enable');
    if (status == 0) {
        msg = bt.lang('common.message.confirm.taboo');
    }
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/billpool/pool/updateStatus", {"id": id, 'status': status}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//删除数据
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids;
    for (i = 0; i < len; i++) {
        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/billpool/pool/batchDelete", {"ids": ids}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//操作   重发、中止
function operation(el,status){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var msg;
    if(status==16){
        msg = "common.message.confirm.bisquerycmd.resend";
    }else if(status==-2){
        msg = "common.message.confirm.tovoid";
    }
    parent.layer.confirm(bt.lang(msg),function(){
        bt.loading.show();
        $.post(basePath + "/bill/pool/abbillInstructionManage/operation",{"id":data.id,"type":"allotCheck","status":status}, function(result){
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
