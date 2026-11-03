var datatable;
bt.use(['form', 'datatable', 'corp', 'dicts', 'bank', 'select', 'money'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();

    var indexColumns = [
        {data: "corpName", bSortable: false},
        {data: "account", bSortable: false},
        {data: "accountName", bSortable: false,className: "textCenter"},
        {data: "currencyName", bSortable: false,className: "textCenter"},
        {data: "accountType", bSortable: false,className: "textCenter",render:function(data, type, full, meta){
                return bt.dict("InnerAccount.AccType", full.accountType);
        }},
        {data: "balance", bSortable: false,className: "textRight",render:function(data, type, full, meta){
                return bt.money.format(data);
        }},
        {data: "erpBalance", bSortable: false,className: "textRight",render:function(data, type, full, meta){
                return bt.money.format(data);
        }},
        {data: "status", bSortable: false,className: "textCenter",render:function(data, type, full, meta){
                return bt.dict("fc.corpCompareBill", full.status);
        }},
        {
            data: null, bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
                var html = '';
                if(full.status==="-2" || full.status==="-1" || full.status==="0"){
                    html += '<a href="#" data-row="' + meta.row + '" onclick="compare(false,this)">'+bt.lang("fc.corpCompareBill.compare")+'</a>';
                }
                if(full.status==="95"){
                    html += '<a href="#" data-row="' + meta.row + '" onclick="print(this)">'+bt.lang("common.button.print")+'</a>';
                }
                return html;
            }
        }
    ];
    var queryColumns = [
        {data: "corpName", bSortable: false},
        {data: "account", bSortable: false},
        {data: "accountName", bSortable: false,className: "textCenter"},
        {data: "currencyName", bSortable: false,className: "textCenter"},
        {data: "accountType", bSortable: false,className: "textCenter",render:function(data, type, full, meta){
                return bt.dict("InnerAccount.AccType", full.accountType);
            }},
        {data: "balance", bSortable: false,className: "textRight",render:function(data, type, full, meta){
                return bt.money.format(data);
            }},
        {data: "erpBalance", bSortable: false,className: "textRight",render:function(data, type, full, meta){
                return bt.money.format(data);
            }},
        {data: "status", bSortable: false,className: "textCenter",render:function(data, type, full, meta){
                return bt.dict("fc.corpCompareBill", full.status);
            }},
        {data: "billMonth", bSortable: false,className: "textCenter"},
        {data: "same", bSortable: false,render:function(data, type, full, meta){
                return bt.dict("fc.corpCompareBillSame", full.same);
            }},
        {data: "opUserName", bSortable: false},
        {data: "opDate", bSortable: false},
        {data: null, bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
                var html = '';
                if($('input[name="type"]').val()==='approve'){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.workFlow.trackFlow(\''+full.processInstanceId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                }
                if(full.status==="95"){
                    html += '<a href="#" data-row="' + meta.row + '" onclick="print(this)">'+bt.lang("fc.corpCompareBill.print")+'</a>';
                }
                return html;
            }
        }
    ];
    var buttons = $("#type").val()==="index"?[
        {
            extend: "colvis",
            text: bt.lang('fc.corpCompareBill.batchCompare'),
            columns: ':not(:first):not(:last)',
            action: function ( e, dt, node, config ) {
                compare(true,null);
            }
        }]:[];

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons:buttons,
        ajax: {'url': basePath + "/check/corpCompareBill/datatable"},
        columns: ($("#type").val()==="query"||$("#type").val()==="approve")?queryColumns:indexColumns,
    });
    // datatable.on('xhr.dt', function (e, settings, json, xhr) {
    //     if(json.notExistBalance){
    //         parent.layer.alert(bt.lang("fc.corpCompareBill.thisMonthCanBeCheck"), {icon: 2});
    //     }
    // });
});
function compare(batch,el) {
    var date = $('select[name="year"]').val()+"-"+$('select[name="month"]').val()
    if(batch){
        var datas = layui.jquery("#appTable").DataTable().rows('.selected').data();
        if (datas.length < 1) {
            parent.layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 3});
            return;
        }
        var temp = false;
        var rowData = datas.splice(0, datas.length);
        var items=[];
        for (var i = 0; i < rowData.length; i++) {
            if(rowData[i].same==="0"){
                temp = true;
                break;
            }
            var item ={
                id:rowData[i].billId,
                account:rowData[i].account,
                isCheck:rowData[i].same,
                status:11,
                billMonth:date,
                corpId:rowData[i].corpId,
                accountBalance:rowData[i].balance
            }
            items.push(item);
        }
        if(temp){
            parent.layer.alert(bt.lang('fc.corpCompareBill.temp.alert'), {icon: 3});
            return;
        }
        parent.layer.confirm(bt.lang("fc.corpCompareBill.submitResult"),{icon: 3}, function () {
            $.ajax({
                url: basePath + "/check/corpCompareBill/compareBatch",
                method: 'POST',
                contentType: "application/json",
                data:JSON.stringify(items),
                dataType: 'json',
                success: function (result) {
                    if(result.success){
                        parent.layer.closeAll();
                    }
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
                    datatable && datatable.draw();
                }
            });
        });
    }else{
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        var billId = data.billId==null?"":data.billId;
        parent.layer.open({
            type: 2,
            area: ['1050px', '560px'],
            maxmin: true,
            content: basePath + "/check/corpCompareBill/compare?billId=" + billId+"&account="+data.account+"&date="+date,
            end:function () {
                datatable && datatable.draw();
            }
        });
    }

}

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var date = $('select[name="year"]').val()+"-"+$('select[name="month"]').val()
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/check/corpCompareBill/approve?billId=" + data.billId+"&account="+data.account+"&date="+date+"&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/corpCompareBil/approve?billId=" + data.billId+"&account="+data.account+"&date="+date+"&taskId=" + data.taskId,
        end:function () {
            datatable && datatable.draw();
        }
    });
}


function print(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('fc.corpCompareBill.print'), basePath + "/check/corpCompareBill/print?billId=" + data.billId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/check/corpCompareBill/print?billId=" + data.billId,
            end: function () {
                location.reload();
            }
        });
    }
}