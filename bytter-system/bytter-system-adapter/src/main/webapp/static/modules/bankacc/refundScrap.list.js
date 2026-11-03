bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.moneyType.init();
    var dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    var selectType = bt.selectType.init();

    var accountTable = bt.accountTable.init();

    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    }

    bt.corp.init();
    var selectType;

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
            'url':basePath+"/bankacc/refundblending/datatable"
        },
        buttons: [
            {
                hide: $("#blendSign").val() == 2,
                text: bt.lang('common.button.manual.scrap'),
                className:'scrapButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ){
                        var data = dt.rows('.selected').data();
                        scrap(data);
                    }
                }
            },{
                hide: $("#blendSign").val() == 0,
                text: bt.lang('common.button.cancel.scrap'),
                className:'cancelScrapButton',
                action: function (e, dt, node, config) {
                    if(e.currentTarget.className.indexOf('disabled') === -1 ) {
                        var data = dt.rows('.selected').data();
                        cancelScrap(data);
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
            var blendSign = dict.get('blendSign').val();
            $('.aotuButton').removeClass('layui-disabled');
            $('.manualButton').removeClass('layui-disabled');
            $('.cancelButton').removeClass('layui-disabled');
            $('.scrapButton').removeClass('layui-disabled');//作废
            $('.cancelScrapButton').removeClass('layui-disabled');
            if(blendSign ==='0') {//未勾兑
                $('.cancelButton').addClass('layui-disabled');
            }
            if(blendSign ==='1') {//已勾兑
                $('.aotuButton').addClass('layui-disabled');
                $('.manualButton').addClass('layui-disabled');
            }
        },
    })
});
//作废
function scrap(data) {
    if(data.length == 0){
        parent.layer.alert(bt.lang('bankacc.message.NoPaymentblending.dtl'),{icon:2});
        return false;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    $.ajax({
        url: basePath + "/bankacc/refundblending?scrap",
        method: 'POST',
        data: {ids: ids},
        dataType: 'json',
        success: function (result) {
            if (result.success == true) {

                parent.layer.alert(bt.lang('bankacc.message.scrap.success'), {icon: result.success ? 1 : 2});
                bt.datatable.refreshTable("appTableDtl");
                // bt.navtab.refreshParent();
                var navParentId = parent.$(".layui-body .layui-tab-title > li.layui-this").attr('parentid');
                parent.$(".layui-body .layui-tab-title > li[lay-id='"+navParentId+"']").addClass('innerTab').removeClass('init');
            }
        }
    });
}
//取消作废
function cancelScrap(data) {
    if(data.length == 0){
        parent.layer.alert(bt.lang('bankacc.message.scrap.dtl'),{icon:2});
        return false;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    $.ajax({
        url: basePath + "/bankacc/refundblending?cancelScrap",
        method: 'POST',
        data: {ids: ids},
        dataType: 'json',
        success: function (result) {
            if (result.success == true) {
                parent.layer.alert(bt.lang('bankacc.message.cancel.scrap.success'), {icon: result.success ? 1 : 2});
                bt.datatable.refreshTable("appTableDtl");
                // bt.navtab.refreshParent();
                var navParentId = parent.$(".layui-body .layui-tab-title > li.layui-this").attr('parentid');
                parent.$(".layui-body .layui-tab-title > li[lay-id='"+navParentId+"']").addClass('innerTab').removeClass('init');
            }
        }
    });
}
