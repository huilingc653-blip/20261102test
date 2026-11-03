var datatable;
bt.use(['form','datatable','money'], function(){
    $ = bt.jquery,form = bt.form();

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
            'url': basePath+"/receipt/epoundagebill/datatable"
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
            {data:"payerName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"payerBank",bSortable:true},
            {data:"payeeName",bSortable:true},
            {data:"payeeAcc",bSortable:true},
            {data:"payeeBank",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"curId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.curName;
                }
            },
            {data:"purpose",bSortable:true},
            {data:"accrualDate",bSortable:true},
            {data:"payType",bSortable:true},
            {data:"tallyDate",bSortable:true},
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
            }}
        ]
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/receipt/epoundagebill/add");
        return;
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/receipt/epoundagebill/add",
    //     end:function(){
    //         datatable && datatable.draw();
    //     }
    // });

    openLayer && openLayer( basePath + "/receipt/epoundagebill/add",undefined,undefined,function () {
        datatable && datatable.draw();
    })
}
function view(el,opType){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var zIndex = 100;
    if(opType == "1"){
        zIndex = -100;
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     zIndex: zIndex,
    //     maxmin: true,
    //     content: basePath + "/receipt/epoundagebill/view/" + data.id+"/"+opType
    // });

    var shade = opType == "1"?0:0.3;
    openLayer && openLayer(basePath + "/receipt/epoundagebill/view/" + data.id+"/"+opType,zIndex,shade)
}
function viewVoucher(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo
    // });

    openLayer && openLayer(basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo)
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/receipt/epoundagebill/update?id=" + data.id);
            return;
        }
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/receipt/epoundagebill/update?id=" + data.id,
        //     end:function(){
        //         datatable && datatable.draw();
        //     }
        // });

        openLayer && openLayer( basePath + "/receipt/epoundagebill/update?id=" + data.id,undefined,undefined,function () {
            datatable && datatable.draw();
        })
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/receipt/epoundagebill/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
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
