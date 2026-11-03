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
            'url': basePath+"/receipt/eleBillQuery/datatable"
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
                data: "oldVoucher", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if(full.generateMode == 'S' || full.generateMode == 'D' || full.generateMode == 'G'
                        || full.generateMode == 'C' || full.generateMode == 'B' || full.generateMode == 'Z'
                        || full.generateMode == 'W') {
                        if (full.oldVoucher != "" && full.oldVoucher != null) {
                            if (full.billType == "07" || full.billType == "08" || full.billType == "09" || full.billType == "10" || full.billType == "16" || full.billType == "21" ||
                                full.billType == "22" || full.billType == 12 || full.billType == 13 || full.billType == 14 || full.billType == 15) {
                                html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewOldVoucher(this)">' + full.oldVoucher + '</a>';
                            } else {
                                html += full.oldVoucher;
                            }
                        }
                    }else{
                        if (full.oldVoucher != "" && full.oldVoucher != null) {
                            html += full.oldVoucher;
                        }
                    }
                    return html;
                }
            },
            {data:"billType",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.billTypeName;
                }
            },
            {data:"tallyDate",bSortable:true},
            {data:"prints",bSortable:true, className: 'center',
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

function view(el,opType){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var content = ""
    if(data.billType == "01" || data.billType == "02" || data.billType == "03"
    ||data.billType == "19" || data.billType == "20"){
        content = basePath + "/receipt/eaccrualbill/view/" + data.id+"/"+opType
    }
    if(data.billType =="04" || data.billType == "05"){
        content = basePath + "/receipt/eaccrualbill/viewDepositIst/" + data.id+"/"+opType
    }
    if(data.billType == "07"){
        content =  basePath + "/receipt/eloanbill/view/" + data.id+"/"+opType
    }
    if(data.billType == "08" || data.billType == "09" || data.billType == "10"){
        content = basePath + "/receipt/enoticebill/view/" + data.id+"/"+opType
    }
    if(data.billType == "11" ){
        content = basePath + "/receipt/epoundagebill/view/" + data.id+"/"+opType
    }
    if(data.billType == "12" || data.billType == "13" || data.billType == "14" || data.billType == "15"){
        content = basePath + "/receipt/etransbill/view/" + data.id+"/"+opType
    }
    if(data.billType == "16"){
        content = basePath + "/receipt/eacceptbill/view/" + data.id+"/"+opType
    }
    if(data.billType == "17"){
        content = basePath + "/receipt/ecomparebill/view/" + data.billNo+"/"+opType
    }
    if(data.billType == "21" || data.billType == "22"){
        content = basePath + "/receipt/eprecativeloanbill/view/" + data.id+"/"+opType
    }

    if(content){
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     zIndex: opType == "1"?-100 :10000, //设置电子回单打印层级
        //     maxmin: true,
        //     shade:opType == "1"?0:0.3,
        //     content: content
        // });
        var zIndex = opType == "1"?-100 :10000;
        var shade = opType == "1"?0:0.3;
        openLayer && openLayer(content,zIndex,shade)
    }
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
    var content = "";
    if(data.billType == "07"){
        content = basePath + "/cms/loan/cmsloaninfo/viewByBillCode/" + data.oldVoucher
    }
    if(data.billType == "08" || data.billType == "09" || data.billType == "10"){
        content = basePath + "/netbank/nisbillhead/viewByBillNo/" + data.oldVoucher;
        var prefix = (data.oldVoucher).substring(0,1);
        if( prefix == "A"){
            content = basePath + "/allocation/nisallocationreq/viewByBillNo/" + data.oldVoucher;
        }
    }
    if(data.billType == "16"){
        content = basePath + "/bill/accebill/abreceivebill/view?accId=" + data.oldVoucherId
    }
    if(data.billType==21) {
        //委托贷款
        content = basePath + "/cms/precative/loansend/viewbybillcode?billCode=" + data.oldVoucher
    }else if(data.billType==22){
        //委托贷款还款
        content = basePath + "/cms/precative/loanrepay/view?sendCode=''&type=view"+"&billCode=" + data.oldVoucher

    }else if(data.billType == 12 || data.billType == 13 || data.billType == 14 || data.billType == 15){
        content = basePath + "/chowmatistic/choinnerfinance/viewByBillNo/" + data.oldVoucher;
    }
    if(content){
        openLayer && openLayer(content)
    }
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

