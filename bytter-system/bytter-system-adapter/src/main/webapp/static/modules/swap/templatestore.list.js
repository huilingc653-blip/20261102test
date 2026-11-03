var form,configType,erpDatatable,corp,bizStatus,dateType,selectType,newSet,status;
bt.use(['form','element','datatable','selectType','dateType','mulselect','bank','corp','moneyType','dicts'], function(){
    form = bt.form(),$  = bt.jquery,element = bt.element();
    configType = $("input[name='dealType']").val();
    newSet = $("#newSet").val();
    bt.moneyType.init();
    bt.mulselect.init();
    corp=bt.corp.init();
    bizStatus=bt.dicts.init();
    dateType=bt.dateType.init();
    selectType = bt.selectType.init();

    bt.bank.init({elem: '.bank'});
    element.on('tab(docDemoTabBrief)', function(data){
        status = this.getAttribute('data-status');
        $("input[name='status']").val(status);
        addbnts(data.index);
        erpDatatable.draw();
        if(data.index===0){
            $(".biz-dict-selecttemp_status").show();
            bizStatus.get('biz-temp_status').attr({
                'data-defaultvalue':""
            });
        }else{
            $(".biz-dict-selecttemp_status").hide();
        }
    });
    initDataTable();

    resetCallback = function () {
        if (status) {
            $("input[name='status']").val(status);
        }else{
            $("input[name='status']").val(-100);
        }
    }
});

//批量补录
var enteringdata = function (list) {
    var ids = [];
    for(var i=0;i<list.length;i++){
        var dId = list[i].dtlId;
        if(dId){
            ids.push(dId);
        }
    }
    var ids = ids.join(",");
    if (checkItemStatus(ids)){
        parent.layer.alert(bt.lang("his.dtl.template.status.changed"), {icon: 2});
        return false;
    }

    var newSet = $("#newSet").val();
    var url=basePath + "/erp/swap/templatestore/enterings/"+configType+"?ids="+ids;
    if(newSet == 1){
        url= basePath + "/erp/swap/hisDtlComField/netSettings/" + configType+"?ids="+ids;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang("button.batch.collection"),url);
        return;
    }
    var perContent = layer.open({
        type: 2,
        title: bt.lang('button.batch.collection'),
        area: ['820px', '300px'],
        maxmin: true,
        content: url,
        end:function(){
            erpDatatable && erpDatatable.draw();
        }
    });
};
var dealStatus = function (list,status){
    var ids = [];
    for(var i=0;i<list.length;i++){
        var dId = list[i].dtlId;
        if(dId){
            ids.push(dId);
        }
    }
    var action = "/erp/swap/hisdtltemplate/status/"+status;
    $.post(basePath + action,{"ids":ids,"type":configType},function(result){
        parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        if(result.success){
            erpDatatable.draw();
            if(!bt.navtab){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
        }
    },'JSON');
    return false;
};

var addbnts = function(index){
    erpDatatable.buttons( '.store_botton' ).remove();
    if(index===0){
        erpDatatable.button().add(1, {
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
        erpDatatable.button().add(2, {
            text: bt.lang('common.button.submit'),
            className: "store_botton",
            action: function (e, dt, node, config) {
                var data = dt.rows('.selected').data();
                var len = data.length;
                var rowData = data.splice(0,len);
                if(rowData && rowData.length>0){
                    for(var i=0;i<rowData.length;i++){
                        if(rowData[i].status != 0 && rowData[i].status != -1){
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
        erpDatatable.button().add(3, {
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
        erpDatatable.button().add(1, {
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
        erpDatatable.button().add(2, {
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
        erpDatatable.button().add(1, {
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
};

var getDataField=function(){
    var action = "/erp/swap/templatestore/dynaDataHeader";
    var dataField={};
    bt.jquery.ajax({
        type: "GET",
        url: basePath + action,
        data: {"type":configType},
        dataType:"json",
        async: false,
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        success: function(result){
            dataField = result;
        },
        error: function(res){
        }
    });
    return dataField;
};
var getDataColumns=function(dataField){
    var dataColumn=new Array();


    if(dataField&&dataField.length>0){
        for(i=0;i<dataField.length;i++){
            var field={};
            if("status" == dataField[i].field){
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        return bt.dict("templatestore.status", full.status);
                    }
                }
            }else if("num"==dataField[i].type){
                field={data:dataField[i].field,title:bt.lang(dataField[i].text).text,bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        return bt.money.format(data,{separator: "",decimal: 0});
                    }
                }
            }else if("money"==dataField[i].type){
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        return bt.money.format(data);
                    }
                }
            }else if("dynaSource"==dataField[i].type){
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        var colIndex = meta.col;
                        try{
                            var name =  meta.settings.aoColumns[colIndex].name;
                            var gCode = bt.dynaDataSource(configType,name);
                            return bt.dynaSource(gCode, data);
                        }catch (e) {
                            return ''
                        }
                    }
                };
            }else if("action"==dataField[i].type){
                field={data:null,bSortable:false,title:bt.lang(dataField[i].text),className:'center',render: function (data, type, full, meta) {
                    if(!full.status|| full.status===-1){
                        return '<a class="" href="javascript:;" data-row="'+meta.row+'" onclick="enterings(this)">'+bt.lang("common.button.makeup")+'</a>';
                    }else{
                        return '<a class="" href="javascript:;" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.dtl.view")+'</a>';
                    }
                }}
            }else{
                field={data:dataField[i].field,title:bt.lang(dataField[i].text),bSortable:true};
            }
            dataColumn[i]=field;
        }
    }
    return dataColumn;
};

var initDataTable=function(){
    var dataField=getDataField();
    var dataColumns=getDataColumns(dataField);
    //生成列表
    if(erpDatatable){
        $('#appTable').DataTable().destroy();
        $('#appTable').html('');
    }
    erpDatatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[2, 'desc']],
        paging: true,
        fixedColumns: {
            leftColumns:2
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/erp/swap/templatestore/datatable"
        },
        btInitComplete: function (settings, data) {
            addbnts(0)
        },
        buttons: [],
        columns:dataColumns,
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
                    colNum: '9',
                    dataName: 'amt'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/erp/swap/templatestore/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });
};

//查看明细
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();


    var newSet = $("#newSet").val();
    var url=basePath + "/erp/swap/hisdtltemplate/view/" + data.dtlId+"?type="+configType;
    if(newSet == 1){
        url= basePath + "/erp/swap/hisDtlComField/view/" + data.dtlId+"?type="+configType;
    }

    var perContent = layer.open({
        type: 2,
        area: ['800px', '600px'],
        maxmin: true,
        content: url
    });
    layer.full(perContent);
}
//补录
function enterings(el){

    var newSet = $("#newSet").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (checkItemStatus(data.dtlId)){
        parent.layer.alert(bt.lang("his.dtl.template.status.changed"), {icon: 2});
        return false;
    }
    var url=basePath + "/erp/swap/templatestore/entering/" + data.dtlId + "?type=" + configType;
    if(newSet == 1){
        url= basePath + "/erp/swap/hisDtlComField/netSetting/" + data.dtlId + "?type=" + configType;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang("common.button.makeup"), url);
        return;
    }
    var perContent = layer.open({
        type: 2,
        area: ['800px', '600px'],
        maxmin: true,
        content: url,
        end:function(){
            erpDatatable && erpDatatable.draw();
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
function checkItemStatus(ids) {
    var statusError = false;
    $.ajax({
        url:basePath+"/erp/swap/templatestore/checkStatus?ids="+ids,
        method: 'GET',
        async:false,
        success: function (result) {
            result = JSON.parse(result);
            if (!result.success) {
                statusError = true;
            }else{
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
        var dId = list[i].dtlId;
        if(dId){
            ids.push(dId);
        }
    }
    var statusError = false;
    $.ajax({
        url:basePath+"/erp/swap/templatestore/checkComfire?ids="+ids,
        method: 'GET',
        async:false,
        success: function (result) {
            // console.log(result);
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