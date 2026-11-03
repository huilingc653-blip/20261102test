bt.use(['form', 'datatable','dicts','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    var dict = bt.dicts.init();
    bt.mulselect.init();
    var appTableDtl = bt.datatable.initMultiTables({
        elem:"#appTableDtl",
        checkbox:true,
        mulselect:true,
        selectCss:true,
        jumpPageSelect:false,//开启跨页选择
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        order: [[1, 'desc']],
        ajax:{
            'url':basePath+"/bankacc/paymentblending/paymentDtlDataTable"
        },
        buttons: [
            {
                text: bt.lang('common.button.cancel.paymentblending'),
                className:'scrapButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ) {
                        var data = dt.rows('.selected').data();
                        cancel(data);
                    }
                }
            }
        ],
        columns:[
            {data: "blendSignName", bSortable: true},
            {data: "transTime", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "amt", bSortable: true,className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "remark", bSortable: true},
            {data: "uses", bSortable: true}
        ],
        btClickRowCallback:function(data){

        },
        btDrawCallback:function(setting){ //回调方法

        }
    })
});
//取消勾兑
function cancel(data) {
    bt.loading.show();
    if(data.length == 0){
        bt.loading.hide();
        parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'),{icon:2});
        return false;
    }
    var dtlId;
    for(var i = 0;i<data.length;i++){
        if(data[i].blendSign == '0'){
            bt.loading.hide();
            parent.layer.alert(bt.lang('bankacc.message.paymentblending.dtl'), {icon: 2});
            return false;
        }
        dtlId += ","+data[i].id;
    }
    var billId = $("#billId").val();
    var isFrom = $("#from").val();
    bt.loading.hide();
    parent.layer.confirm(bt.lang('common.message.confirm.cancelmanual'), function () {
        bt.loading.show();
        $.ajax({
            url: basePath + "/bankacc/paymentblending?cancel",
            method: 'POST',
            data: {dtlId: dtlId, billId: billId, isFrom: isFrom},
            async:true,
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success) {
                    parent.layer.alert(bt.lang('bankacc.message.cancel.paymentblending.success'), {icon: result.success ? 1 : 2});
                    if (!bt.navtab) {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        // bt.datatable.refreshTable("appTableDtl");
                        // $("#eqs_bankSerialNo").val("#");
                        bt.navtab.getCurrentDom().$("#eqs_bankSerialNo").val("#");
                        // bt.datatable.refreshTable("appTableBill");
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        bt.navtab.refreshCurrentTable();
                    }
                }
            }
        });
    });
}
