var datatable,moneyFun;
bt.use(['form','datatable','date','money'], function(){
    $ = bt.jquery,form = bt.form();
    moneyFun = bt.moneyType.getFun();

    var batchPrint0 = {
        text: bt.lang('receipt.button.batchPrint0'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //综合打印
            batchPrint(e, dt, node, config, '0');
        }
    }
    var batchPrint1 = {
        text: bt.lang('receipt.button.batchPrint1'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //A4一页一份（横向）
            batchPrint(e, dt, node, config, '1');
        }
    }
    var batchPrint2 = {
        text: bt.lang('receipt.button.batchPrint2'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //A4一页两份（纵向）
            batchPrint(e, dt, node, config, '2');
        }
    }
    var batchPrint3 = {
        text: bt.lang('receipt.button.batchPrint3'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //A5一页一份（横向）
            batchPrint(e, dt, node, config, '3');
        }
    }
    var batchPrintAll = {
        extend: 'collection',
        text: bt.lang('common.button.batchPrint'),//按钮名称,
        buttons: [
            batchPrint0,
            batchPrint1,
            batchPrint2,
            batchPrint3
        ],
        fade: true
    };
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [1,'desc'],
        searchForm: {
            formElem: "#commonForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/receipt/etransbill/datatable"
        },
        buttons: [
            batchPrintAll,
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"billNo",bSortable:true},
            {
                data: "voucherNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.voucherNo != "" && full.voucherNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewVoucher(this)">' + full.voucherNo + '</a>';
                    }
                    return html;
                }
            },
            {
                data: "origVoucherNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {//origVoucherNo
                    var html = "";
                    if (full.origVoucherNo != "" && full.origVoucherNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewOldVoucher(this)">' + full.origVoucherNo + '</a>';
                    }
                    return html;
                }
            },
            {data:"billType",bSortable:true,className: 'center',
                render:function (data,type,full,meta) {
                    return bt.dict('eVisaType.billType',full.billType);
                }
            },
            {data:"corpName",bSortable:true,className: 'center'},
            {data:"depositRateName",bSortable:true,className: 'center'},//存款类型
            {data:"accNo",bSortable:true},//存款账号
            {data:"accName",bSortable:true},
            {data:"currentAcc",bSortable:true,className: 'center'},//活期账号
            {data:"currentAccName",bSortable:true,className: 'center'},
            {data:"rate",bSortable:true,
                render:function (data, type, full, meta ) {
                    return moneyFun.format(full.rate * 100, {decimal:4});
                }
              },
            {data:"curName",bSortable:true,className: 'center'},
            {data:"money",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.money);
                }
            },
            {data:"summaryContent",bSortable:true,className: 'center'},//摘要
            {data:"tallyDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.date.format(full.tallyDate, "yyyy-mm-dd")
                }
            },
            {data:"prints",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.prints == 1){
                        return bt.lang('common.sc_yn.no');
                    }else{
                        return bt.lang('common.sc_yn.yes');
                    }
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this,0)">'+bt.lang("common.button.preview")+'</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this,1)">'+bt.lang("common.button.print")+'</a>';
                    return html;
                }
            }
        ]
    });
});
function view(el,opType){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var zIndex = 100;
    if(opType == "1"){
        zIndex = -100;
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     zIndex: opType == "1"?-100 :100,
    //     maxmin: true,
    //     shade:opType == "1"?0:0.3,
    //     content: basePath + "/receipt/etransbill/view/" + data.id+"/"+opType
    // });
    var content = basePath + "/receipt/etransbill/view/" + data.id+"/"+opType;
    var shade = opType == "1"?0:0.3;
    openLayer && openLayer(content,zIndex,shade)
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/receipt/etransbill/update?id=" + data.id);
            return;
        }
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/receipt/etransbill/update?id=" + data.id,
        //     end:function(){
        //         datatable && datatable.draw();
        //     }
        // });

        var content = basePath + "/receipt/etransbill/update?id=" + data.id;
        openLayer && openLayer(content,undefined,undefined,function () {
            datatable && datatable.draw();
        })
    }
}
function deleteColumn(el){
    parent.layer.closeAll();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/receipt/etransbill/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function viewVoucher(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo
    // });
    var content = basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo
    openLayer && openLayer(content)
}
function viewOldVoucher(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var content = basePath + "/chowmatistic/choinnerfinance/viewByBillNo/" + data.origVoucherNo;
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: path
    // });
    openLayer && openLayer(content)

}


/**
 * 批量打印
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchPrint(e, dt, node, config, printType) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var billTypes = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            if(rowData[i].billType == '17'){
                ids += rowData[i].billNo;
            }else {
                ids += rowData[i].id;
            }
            billTypes += rowData[i].billType;
        } else {
            if(rowData[i].billType == '17'){
                ids += "," + rowData[i].billNo;
            }else {
                ids += "," + rowData[i].id;
            }
            billTypes += "," + rowData[i].billType;
        }
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/receipt/eleBillQuery/batchPrint?ids=" + ids+"&billTypes="+billTypes+"&printType="+printType
    // });
    var content = basePath + "/receipt/eleBillQuery/batchPrint?ids=" + ids+"&billTypes="+billTypes+"&printType="+printType
    openLayer && openLayer(content)
}
