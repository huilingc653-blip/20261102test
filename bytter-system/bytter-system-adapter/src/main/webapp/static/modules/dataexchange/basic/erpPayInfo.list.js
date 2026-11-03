bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'moneyType','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.moneyType.init();
    var dict = bt.dicts.init();
    bt.mulselect.init();
    var selectType = bt.selectType.init();
    bt.corp.init();
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true,
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/dataexchange2/payable/transfer/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('erp:payinfo:apply'),
                text: bt.lang('common.button.transfer'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    transfer(data,node);
                }
            }
        ]
        ,columns: [
            {data:"voucherNoErp",bSortable:true},
            {data:"wishPayday",bSortable:true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data:"corpName",bSortable:true},
            {data:"payerAccNo",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"payerBankName",bSortable:true},
            {data:"voucherName",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data:"oppBankAcc",bSortable:true},
            {data:"oppAccName",bSortable:true},
            {data:"oppBankName",bSortable:true},
            {data:"dataSource",bSortable:true},
            {
                data: "urgencyFlag", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("sf_yn", full.urgencyFlag);
                }
            },
            {data:"abs",bSortable:true},
            {data:"createDate",bSortable:true},
        ]
    });
});
function transfer(data,button) {
    bt.loading.hide();

    if( button.hasClass('isSubmiting')){
        return
    }
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = '';
    for(var i = 0;i< data.length;i++){
        if(data[i].status != '0'){
            parent.layer.alert(bt.lang('erp.message.notransfer'), {icon: 2});
            return false;
        }
        ids += ""+data[i].id+",";
    }
    bt.loading.show();
    //增加已提交的标志
    button.addClass('isSubmiting');
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.ajax({
            url: basePath + "/dataexchange2/payable/transfer?transfer",
            method: 'POST',
            data: {ids: ids},
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                //解除已提交的标志
                button.removeClass('isSubmiting');
                if (result.success == true) {
                    parent.layer.alert(bt.lang('common.message.success'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTable");
                } else {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            }
        });
    },function () {
        bt.loading.hide();
        button.removeClass('isSubmiting');
    });
}