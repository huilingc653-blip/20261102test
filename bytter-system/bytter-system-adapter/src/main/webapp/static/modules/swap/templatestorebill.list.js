var form,configType,datatable;
bt.use(['form','element','datatable','selectType','corp','dicts','moneyType'], function(){
    form = bt.form(),$  = bt.jquery,element = bt.element();
    configType = $("input[name='dealType']").val();
    var status;
    var bizStatus = bt.dicts.init();
    bt.moneyType.init();
    bt.selectType.init();
    bt.corp.init();
    element.on('tab(docDemoTabBrief)', function(data){
        status = this.getAttribute('data-status');
        $("input[name='status']").val(status);
        addbnts(data.index);
        datatable.draw();
        if(data.index===0){
            $(".biz-dict-selecttemp_status").show();
            bizStatus.get('biz-temp_status').attr({
                'data-defaultvalue':""
            });
        }else{
            $(".biz-dict-selecttemp_status").hide();
        }
    });
    loaddatatable();
    function loaddatatable() {
        datatable = bt.datatable.init({
            elem: "#appTable",
            colReorder: true,
            checkbox: true,
            order: [[2, 'asc']],
            paging: true,
            fixedColumns: {
                leftColumns:2
            },
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/erp/swap/templatestore/bill/datatable"
            },
            btInitComplete: function (settings, data) {
                addbnts(0)
            },
            buttons: [
            ]
            , columns: [
                {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    if(!full.status|| full.status===-1){
                        return '<a class="" href="javascript:;" data-row="'+meta.row+'" onclick="enterings(this)">'+bt.lang("common.button.makeup")+'</a>';
                    }else{
                        return '<a class="" href="javascript:;" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.dtl.view")+'</a>';
                    }
                }},{
                    data: "status", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("templatestore.status", full.status);
                    }
                },
                {data: "code", bSortable: true},{data: "billType", bSortable: true,render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_TYPE",full.billType);
                }},{data: "billFlag", bSortable: true,render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_FLAG",full.billFlag);
                }},
                {data: "tradeType", bSortable: true,render: function (data, type, full, meta) {
                    return bt.dict("bill_operation_type",full.tradeType);
                }},
                {data: "corpName", bSortable: false},
                //{data: "payerAcc", bSortable: true},
                {data: "payer", bSortable: true},
                {data: "acceptor", bSortable: true},
                {data: "money", bSortable: true, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.money);
                    }
                },
                {data: "transTime", bSortable: true},
                {data: "maturityDate", bSortable: true},
                {data: "receiver", bSortable: true},
                {data: "opName", bSortable: false},
                {data: "templateName", bSortable: false},
                {data: "typeName", bSortable: false}
            ],
            summary: {
                pageSum: false,
                allSum: true,
                selectSum: true,
                sumTextCol:2,
                format: {
                    decimal: 2
                },
                sumList: [
                    {
                        colNum: '10',
                        dataName: 'money'
                    }
                ]
            }
        });
    }
    //批量补录
    function enteringdata(list) {
        var ids = [];
        for(var i=0;i<list.length;i++){
            var dId = list[i].id;
            if(dId){
                ids.push(dId);
            }
        }
        var ids = ids.join(",");
        if (checkItemStatus(ids)){
            parent.layer.alert(bt.lang("his.dtl.template.status.changed"), {icon: 2});
            return false;
        }
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang("button.batch.collection"),basePath + "/erp/swap/templatestore/enterings/"+configType+"?ids="+ids);
            return;
        }
        var perContent = layer.open({
            type: 2,
            title: bt.lang('button.batch.collection'),
            area: ['820px', '300px'],
            maxmin: true,
            content: basePath + "/erp/swap/templatestore/enterings/"+configType+"?ids="+ids,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
    function dealStatus(list,status){
        var ids = [];
        for(var i=0;i<list.length;i++){
            var dId = list[i].id;
            if(dId){
                ids.push(dId);
            }
        }
        var action = "/erp/swap/hisdtltemplate/status/"+status;
        $.post(basePath + action,{"ids":ids,"type":configType},function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                datatable.draw();
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
            }
        },'JSON');
        return false;
    }
    function addbnts(index){
        datatable.buttons( '.store_botton' ).remove();
        if(index===0){
            datatable.button().add(1, {
                text: bt.lang('button.batch.collection'),
                className: "store_botton",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        enteringdata(rowData);
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            });
            datatable.button().add(2, {
                text: bt.lang('common.button.submit'),
                className: "store_botton",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        for(var i=0;i<rowData.length;i++){
                        	if(!rowData[i].templateName){
                                layer.msg((i+1)+bt.lang("message.alert.no.operation"));
                                return;
                            }
                        }
                        dealStatus(rowData,1);
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            } );
            datatable.button().add(3, {
                text: bt.lang('common.button.complete'),
                className: "store_botton",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        dealStatus(rowData,10);
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            } );
        }
        if(index===1){
            datatable.button().add(1, {
                text: bt.lang('common.button.send.confirmed'),
                className: "store_botton",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        if(checkItemOnWaitComfire(rowData,2,1)){
                            parent.layer.alert(bt.lang("his.dtl.template.status.changed"), {icon: 2});
                            return false;
                        }
                        dealStatus(rowData,2);
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            } );
            datatable.button().add(2, {
                text: bt.lang('common.button.back'),
                className: "store_botton",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        if(checkItemOnWaitComfire(rowData,-1,1)){
                            parent.layer.alert(bt.lang("his.dtl.template.status.changed"), {icon: 2});
                            return false;
                        }
                        dealStatus(rowData,-1);
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            } );
        }
        if(index===2||index===3||index===4||index===5){
            datatable.button().add(1, {
                text: bt.lang('common.button.back'),
                className: "store_botton",
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0,len);
                    if(rowData && rowData.length>0){
                        dealStatus(rowData,-1);
                    }else{
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            } );
        }
    }
    resetCallback = function () {
        if (status) {
            $("input[name='status']").val(status);
        }else{
            $("input[name='status']").val(-100);
        }
    }
});
//查看明细
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var perContent = layer.open({
        type: 2,
        area: ['800px', '600px'],
        maxmin: true,
        content: basePath + "/erp/swap/hisdtltemplate/view/" + data.id+"?type="+configType
    });
    layer.full(perContent);
}
//补录
function enterings(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (checkItemStatus(data.id)){
        parent.layer.alert(bt.lang("his.dtl.template.status.changed"), {icon: 2});
        return false;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang("common.button.makeup"),basePath + "/erp/swap/templatestore/entering/" + data.id+"?type="+configType);
        return;
    }
    var perContent = layer.open({
        type: 2,
        area: ['800px', '600px'],
        maxmin: true,
        content: basePath + "/erp/swap/templatestore/entering/" + data.id+"?type="+configType,
        end:function(){
            datatable && datatable.draw();
        }
    });
    layer.full(perContent);
}
function changeStatus(data,text,selectedData,beforeValue) {
    if($.trim(data.value)==""){
        $("input[name='status']").val(-100);
    }else{
        $("input[name='status']").val(data.value);
    }
}
function checkItemStatus(ids,status) {
    var statusError = false;
    $.ajax({
        url:basePath+"/erp/swap/templatestore/checkStatus?ids="+ids+"&type=accebill",
        method: 'GET',
        async:false,
        success: function (result) {
            console.log(result);
            result = JSON.parse(result);
            if (!result.success) {
                statusError = true;
            } else {
                statusError = false;
            }
        }
    });
    return statusError;
}

/**
 * 待确认页面,校验记录是否能发送确认或者打回
 * @param list
 * @param status
 * @param index
 * @returns {boolean}
 */
function checkItemOnWaitComfire(list,status,index) {
    var ids = [];
    for(var i=0;i<list.length;i++){
        var dId = list[i].id;
        if(dId){
            ids.push(dId);
        }
    }
    var statusError = false;
    $.ajax({
        url:basePath+"/erp/swap/templatestore/checkComfire?ids="+ids+"&type=accebill",
        method: 'GET',
        async:false,
        success: function (result) {
            console.log(result);
            result = JSON.parse(result);
            if (!result.success) {
                statusError = true;
            } else {
                statusError = false;
            }
        }
    });
    return statusError;
}