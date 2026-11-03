var dict, selectType, accountTable,moneyType,moneyFun,dateType,dateFun,corp,bankType;
//定义结算方式对应表格类型名称
var paymentMeans2Table = new Map();
paymentMeans2Table.set("1","Dtl");
paymentMeans2Table.set("2","Bill");
paymentMeans2Table.set("3","Lc");

var paymentMeansSel = $("#paymentMeansDefault").val();
function paymentMeansCallBack(tabPaymentMeansSel){
    //根据选择的结算方式，控制表格隐藏或者显示
    paymentMeansSel = tabPaymentMeansSel;
    showOrHideByTableType();
    let dataSel = bt.datatable.getSelectData("#appTableDoc")[0];
//    autoButtonDisabled(dataSel);
//    tableInitByTableType(dataSel);
    refreshTableByTableType();
}

function setIdByTableType(idVal){
    paymentMeans2Table.forEach(function(value,key){
        if(key == paymentMeansSel){
            $("#eqs_"+value.toLowerCase()+"_id").val(idVal);
        }
    });
}

function refreshTableByTableType(){
    if("1" == paymentMeansSel){
        bt.datatable.refreshTable("appTableDtl");
    }else if("2" == paymentMeansSel){
        bt.datatable.refreshTable("appTableBill");
    }else if("3" == paymentMeansSel){
        bt.datatable.refreshTable("appTableLc");
    }
}

//控制自动核销按钮是否禁用
//function autoButtonDisabled(checkRuleFlag){
//    //结算方式为银行流水且已匹配规则时高亮
//    var bankAccTypeStr = '';
//    $('input[name="eqs_matchingRule"]:checked').each(function () {
//        bankAccTypeStr += $(this).val() + ",";
//    });
//    if(checkRuleFlag && "1" == paymentMeansSel && bankAccTypeStr
//        && bankAccTypeStr.indexOf("1")!=-1 && bankAccTypeStr.indexOf("5")!=-1){
//        $(".aotuButton").removeClass('disabled');
//    }else{
//        $(".aotuButton").addClass('disabled');
//        //勾流水，强制核销置灰
//        if($('#appTableDtl').DataTable().rows('.selected').count() > 0){
//            $(".forceButton").addClass('disabled');
//        }else{
//            if($('#appTableDoc').DataTable().rows('.selected').count() > 0){
//                $(".forceButton").removeClass('disabled');
//            }
//        }
//    }
//}

//控制强制核销按钮是否禁用
function forceButtonDisabled(){
    if($('#appTableDtl').DataTable().rows('.selected').count() > 0 || $('#appTableBill').DataTable().rows('.selected').count() > 0 || $('#appTableLc').DataTable().rows('.selected').count() > 0){
        $(".forceButton").addClass('disabled');
    }else{
        if($('#appTableDoc').DataTable().rows('.selected').count() == 1){
            $(".forceButton").removeClass('disabled');
        }
    }
}

function showOrHideByTableType(){
    paymentMeans2Table.forEach(function(value,key){
        if(key == paymentMeansSel){
            $("#div_"+value).show();
        }else {
            $("#div_"+value).hide();
        }
    });
}

function getDataByTableType(){
    if("1" == paymentMeansSel){
        getDtlDate()
    }else if("2" == paymentMeansSel){
        //getBillData()
    }else if("3" == paymentMeansSel){
        //getLcData()
    }
}

function getManualDataByTableType(){
    if("1" == paymentMeansSel){
        return getManualDtlData()
    }else if("2" == paymentMeansSel){
        return getManualBillData()
    }else if("3" == paymentMeansSel){
        return getManualLcData()
    }
}

//清除查询列表
function clearForm(){
    if("1" == paymentMeansSel){
        clearDtlForm()
    }else if("2" == paymentMeansSel){
        //clearBillForm()
    }else if("3" == paymentMeansSel){
        //clearLcForm()
    }
}

//根据所选应收单获取匹配的银行流水信息
function tableInitByTableType(data){
    if("1" == paymentMeansSel){
        tableInitDtl(data)
    }else if("2" == paymentMeansSel){
        tableInitBill(data)
    }else if("3" == paymentMeansSel){
        tableInitLc(data)
    }
}




function clearDocFrom(){
    $("#doc_Id").val("");

    $("#doc_likes_code").val("");
    selectType.get("doc_eqs_batchBusType").val("");
    dateType.get("doc_ged_receiptDate").val("");
    dateType.get("doc_led_receiptDate").val("");
    corp.get("doc_eqs_corpId").val("");
    dict.get('doc_eqs_source').val("");
    dateType.get("doc_ged_endDate").val("");
    dateType.get("doc_led_endDate").val("");
    $("#doc_likes_contractCode").val("");
    dict.get('doc_eqs_voucherType').val("");
    dateType.get("doc_ged_agingDate").val("");
    dateType.get("doc_led_agingDate").val("");
    //客商名称、单位
    dict.get('doc_eqs_externalType').val("");
    dict.get('doc_ins_writeOffStatus').attr({'value':'0,1'})
    dict.get('doc_eqi_status').val("");

    // $("#doc_eqs_curId").val($("#curIdRMB").val());
//    selectType.get("doc_eqs_curId").val("");
    dict.get('doc_eqs_writeOffType').val("");

    selectType.get('doc_eqs_curIdShow').val(" ");
    $("#doc_eqs_curId").val("");
    $("#doc_likes_externalName").val("");
}

//业务种类初始化前回调
batchBusTypeBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}

// 选择单位回调
docCorpCallback = function (id) {
    // 重新根据单位ID加载账户表格
    //accountTable.get("receipt_payAccNo").setCorp(id);
    //accountTable.get("receipt_payeeAccNo").setCorp(id);
    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + id;
    bt.selectType.get("doc_eqs_batchBusType").attr("data-url", busTypeUrl);
}

accountCallBack3 = function (data) {
    $("#dtl_likes_bankAcc").val(data.bankAcc);
}

accountCallBack4 = function (data) {
    $("#dtl_likes_oppAccNo").val(data.bankAcc);
}
//供应商选择
bt.externalCorpCallback = function (data, dom) {
    $("#doc_likes_externalName").val(data.name);
}
bt.externalCorpBankAccParams = function (data, dom) {
    externalCorpBankAcc.set({
        "corpId": corp.get("doc_eqs_corpId").val() ? corp.get("doc_eqs_corpId").val() : "",
        type: "2"
    });
    return true;
}



bt.use(['form','element', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'bankType', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {


    var laydate = layui.laydate;
    var element = bt.element();
    $ = bt.jquery, form = bt.form();
    bankType = bt.bankType.init();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    moneyType = bt.moneyType.init();
    moneyFun = moneyType.getFun();
    transTimeRuleCallback = function(){
        getDtl();
    }
    dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    externalCorpBankAcc = bt.externalCorpBankAcc.init();
    selectType = bt.selectType.init();
    accountTable = bt.accountTable.init();

    element.on('collapse(collapse)',function(data){
        if(data.show){
            data.title.addClass('open');
            data.title.find('>span').show()
        }else {
             data.title.removeClass('open');
             data.title.find('>span').hide()
        }
    })

    //初始时禁用“取消核销”
    $('.cancelButton').addClass('disabled');

    // 选择单位回调
    dtlCorpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("dtl_likes_bankAcc").setCorp(id);
        accountTable.get("dtl_likes_oppAccNo").setCorp(id);
    }

    corp=bt.corp.init();

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

    element.on('tab(paymentMeansTab)', function(data){
        paymentMeansCallBack($(this).attr("data-paymentMeans"));
    });

    var statusValue = '';
    /*var doc_Id = $("#doc_Id").val();
    var initdate = false;
    if(doc_Id != ""){
        initdate = true;
    }*/
    tableInitDtl();
    getBillData();
    getLcData();
    var appTableDoc = bt.datatable.initMultiTables({
        elem: "#appTableDoc",
        menuName: '核销处理',
        checkbox: true,
        mulselect: true,
        selectCss: true,
        jumpPageSelect: false,//开启跨页选择
        isInitData: true, //initdate
        searchForm: {
            formElem: "#docSearchForm",
            formFilter: "docSearch",
            callback:function(data){ //点击查询按钮的回调方法
                clearForm();
                setIdByTableType("#");
                getDataByTableType();
            }
        },
//        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/accountReceivable/writeOff/docDatatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('accountReceivable:writeOff:op'),
                text: bt.lang('自动核销'),
                className: 'aotuButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        var data = $('#appTableDoc').DataTable().rows('.selected').data();
                        auto(data);
                    }
                }
            },
//            {
//                hide: bt.isnotResource('accountReceivable:writeOff:op'),
//                text: bt.lang('手工核销'),
//                className: 'manualButton disabled',
//                action: function (e, dt, node, config) {
//                    if (e.currentTarget.className.indexOf('disabled') === -1) {
//                        var data = $('#appTableDoc').DataTable().rows('.selected').data();
//                        manual(data);
//                    }
//                }
//            }, {
//                hide: bt.isnotResource('accountReceivable:writeOff:op'),
//                text: bt.lang('取消核销'),
//                className: 'cancelButton disabled',
//                action: function (e, dt, node, config) {
//                    if (e.currentTarget.className.indexOf('disabled') === -1) {
//                        var data = $('#appTableDoc').DataTable().rows('.selected').data();
//                        cancel(data);
//                    }
//                }
//            }, {
//                 hide: bt.isnotResource('accountReceivable:writeOff:op'),
//                 text: bt.lang('强制核销'),
//                 className: 'forceButton disabled',
//                 action: function (e, dt, node, config) {
//                     if (e.currentTarget.className.indexOf('disabled') === -1) {
//                         var data = $('#appTableDoc').DataTable().rows('.selected').data();
//                         force(data);
//                     }
//                 }
//            },
            {
                //将“手工核销，取消核销，强制核销”合在一个下拉
                text: function(){
                    // 这里可以返回动态文本
                    return currentWriteOffType + '<span class="layui-edge"></span>';
                },
                className: 'write-off-button', //添加一个类名用于样式控制
                action: function (e, dt, node, config) {
                    var menu = $('#writeOffMenu');
                    //设置菜单的位置，使其显示在按钮下方
                    var offset = $(e.currentTarget).offset();
                    menu.css({
                        top: offset.top + $(e.currentTarget).outerHeight(),
                        left: offset.left
                    }).show();

                    // 阻止事件冒泡，防止菜单立即隐藏
                    e.stopPropagation();
                }
            }
//           {
//                extend: "colvis",
//                text: bt.lang('common.button.theadHide'),
//                columns: ':not(:first):not(:last)'
//            }
        ],
        columns: [
            {data: "code", bSortable: true},
            {data: "corpName", bSortable: false},
            {data: "batchBusTypeName", bSortable: true},
            {data: "sourceName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.docSource", full.source);
                }
            },
            {data: "writeOffStatusName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.status", full.writeOffStatus);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.status == 11) {
                        var row = appTableDoc.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    if(full.status == -3){
                        return "审批通过";
                    }
                    return bt.dict("docManage.status", full.status);
                }
            },
            {data: "writeOffTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.type", full.writeOffType);
                }
            },
            {data: "voucherTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("account.receivable.doc.voucherType", full.voucherType);
                }
            },
            {data: "receiptDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "agingDate", bSortable: true},
            {
                data: "receivableMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.receivableMoney);
                }
            },
            {
                data: "writeOffBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.writeOffBal);
                }
            },
            {data: "curName", bSortable: true},
            {data: "externalName", bSortable: true},
            {data: "externalTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("external.type", full.externalType);
                }
            },
            {data: "contractCode", bSortable: true}
        ],
        btClickRowCallback: function (data1) {
            var data = bt.datatable.getSelectData("#appTableDoc");
            var bankAccTypeStr = '';
            var writeOffStatus = dict.get('doc_ins_writeOffStatus').val();
            $('input[name="eqs_matchingRule"]:checked').each(function () {
                bankAccTypeStr += $(this).val() + ",";
            });
            /*if(data.length >1 && bankAccTypeStr.indexOf("1") != -1 && writeOffStatus === '0'){
                parent.layer.alert(bt.lang('bankacc.message.receipt.rules.money'), {icon: 2});
                clearForm();
                setIdByTableType("#");
                getDataByTableType();
                return false;
            }*/
//            if(data.length >1 && writeOffStatus != '2'){
//                var curName = data[0].curName;
//                for(var i =1;i<data.length;i++){
//                    if(curName!=data[i].curName){
//                        parent.layer.alert(bt.lang('多条数据必须为同一币种！'), {icon: 2});
//                        clearForm();
//                        setIdByTableType("#");
//                        getDataByTableType();
//                        return false;
//                    }
//                }
//            }

//            $('.manualButton').removeClass('disabled');
//            if(data.length >=1){
//                for(var i =0;i<data.length;i++){
//                    if(data[i].writeOffStatus == '2'){
//                        $('.manualButton').addClass('disabled');
//                    }
//                }
//            }else {
//                $('.manualButton').addClass('disabled');
//            }

            //需求冲突带确认 && data[0].writeOffStatus!="2"
            if(data.length==1 && data[0].writeOffStatus!="0"){
                $('.cancelButton').removeClass('disabled');
            }else {
                $('.cancelButton').addClass('disabled');
            }
            if(data.length==1 && data[0].writeOffStatus!="2"){
                $('.forceButton').removeClass('disabled');
            }else {
                $('.forceButton').addClass('disabled');
            }
            if (data1[0].isSelect == true) {
//                tableInitByTableType(data1[0]);
            }else if(data.length == 0){
                clearForm();
                setIdByTableType("#");
                getDataByTableType();
            }
            forceButtonDisabled();
        },
        btDrawCallback: function (setting) { //回调方法
            setIdByTableType("#");
            refreshTableByTableType();
        }
    });

    $(".class-paymentMeans-td-1").width($("#div_Dtl").find('td:first-child').width());
    $('#hide-search').hide()
    $('#hide-search-dtl').hide()
    $('#hide-search-bill').hide()
    $('#hide-search-lc').hide()
});


function showMoreBtn(){
   if($('#hide-search')[0].style.display!='none'){
       $('.ui-arrow-down').attr('src',basePath+'/static/images/table/icon_arrow_down.svg')
       $('.ui-more-btn').removeClass('ui-more-btn-active')
       $('.ui-more-btn').text('更多筛选')
       $('#hide-search').hide()
   }else{
       $('.ui-arrow-down').attr('src',basePath+'/static/images/table/icon_arrow_up.png')
       $('.ui-more-btn').addClass('ui-more-btn-active')
       $('.ui-more-btn').html('收起筛选')
       $('#hide-search').show()
   }
}

function showMoreBtnDtl(){
   if($('#hide-search-dtl')[0].style.display!='none'){
       $('.ui-arrow-down-dtl').attr('src',basePath+'/static/images/table/icon_arrow_down.svg')
       $('.ui-more-btn-dtl').removeClass('umb-active')
       $('.ui-more-btn-dtl').html('更多筛选')
       $('#hide-search-dtl').hide()
   }else{
       $('.ui-arrow-down-dtl').attr('src',basePath+'/static/images/table/icon_arrow_up.png')
       $('.ui-more-btn-dtl').addClass('umb-active')
       $('.ui-more-btn-dtl').html('收起筛选')
       $('#hide-search-dtl').show()
   }
}

function showMoreBtnBill(){
   if($('#hide-search-bill')[0].style.display!='none'){
       $('.ui-arrow-down-bill').attr('src',basePath+'/static/images/table/icon_arrow_down.svg')
       $('.ui-more-btn-bill').removeClass('umb-active')
       $('.ui-more-btn-bill').html('更多筛选')
       $('#hide-search-bill').hide()
   }else{
       $('.ui-arrow-down-bill').attr('src',basePath+'/static/images/table/icon_arrow_up.png')
       $('.ui-more-btn-bill').addClass('umb-active')
       $('.ui-more-btn-bill').html('收起筛选')
       $('#hide-search-bill').show()
   }
}

function showMoreBtnLc(){
   if($('#hide-search-lc')[0].style.display!='none'){
       $('.ui-arrow-down-lc').attr('src',basePath+'/static/images/table/icon_arrow_down.svg')
       $('.ui-more-btn-lc').removeClass('umb-active')
       $('.ui-more-btn-lc').html('更多筛选')
       $('#hide-search-lc').hide()
   }else{
       $('.ui-arrow-down-lc').attr('src',basePath+'/static/images/table/icon_arrow_up.png')
       $('.ui-more-btn-lc').addClass('umb-active')
       $('.ui-more-btn-lc').html('收起筛选')
       $('#hide-search-lc').show()
   }
}

var tbDtl = null;

//跟踪当前选中的核销类型
var currentWriteOffType = '手工核销'; //默认值

//绑定下拉菜单选项的点击事件
$(document).on('click','.write-off-option',function(){
    var type = $(this).data('type');
    var text = $(this).text();
    var data = $('#appTableDoc').DataTable().rows('.selected').data();

    //更新当前选中的核销类型
    currentWriteOffType = text;

    //直接找到按钮元素更新
    $('.write-off-button').html(text + '<span class="layui-edge"></span>');

    //更新active类
    $('.write-off-option').removeClass('active');
    $(this).addClass('active');

    switch(type){
        case 'manual':
            manual(data);
            break;
        case 'cancel':
            cancel(data);
            break;
        case 'force':
            force(data);
            break;
    }
    $('#writeOffMenu').hide(); //隐藏菜单
});

//点击页面其他位置时隐藏菜单
$(document).click(function(e){
    //确保点击的不是按钮或菜单本身
//    if(!$(e.target).closest('.write-off-button, #writeOffMenu').length) {
        $('#writeOffMenu').hide();
//    }
});

//自动勾兑
function auto(data) {
    var dtlTableSel = $('#appTableDtl').DataTable().rows('.selected').data();
    var billTableSel = $('#appTableBill').DataTable().rows('.selected').data();
    var lcTableSel = $('#appTableLc').DataTable().rows('.selected').data();

//    var bankAccTypeStr = '';
//    $('input[name="eqs_matchingRule"]:checked').each(function () {
//        bankAccTypeStr += $(this).val() + ",";
//    })
//    if (bankAccTypeStr.trim() == "") {
//        //请先选择匹配规则
//        parent.layer.alert(bt.lang('bankacc.message.paymentblending.matchingrules'), {icon: 2});
//        return false;
//    }

    var docId = '';
    if(data.length==0 || (dtlTableSel.length==0 && billTableSel.length==0 && lcTableSel.length==0)){
        parent.layer.alert(bt.lang('请勾选应收单和银行流水范围！'), {icon: 2, title:bt.lang('自动核销提示')});
        return false;
    }
    if(dtlTableSel.length==0 && (billTableSel.length>0 || lcTableSel.length>0) ){
        parent.layer.alert(bt.lang('收入为票据或信用证，请使用手工核销！'), {icon: 2, title:bt.lang('自动核销提示')});
        return false;
    }
//    if(data.length!=1 || data[0].writeOffStatus != '0'){
//        parent.layer.alert(bt.lang('请选择一条未核销的应收单据'), {icon: 2, title:bt.lang('自动核销提示')});
//        return false;
//    }else {
//        docId = data[0].id;
//    }

    for(var i=0;i<data.length;i++){
//        if(data[i].writeOffStatus != '0'){
//            parent.layer.alert(bt.lang('请选择未核销的应收单据'), {icon: 2, title:bt.lang('自动核销提示')});
//            return false;
//        }else {
            docId+=data[i].id+',';
//        }
    }


    var dtlId = "";

    for(var i=0;i<dtlTableSel.length;i++){
//        if(dtlTableSel[i].writeOffStatus != '0'){
//            parent.layer.alert(bt.lang('请选择未核销的流水'), {icon: 2, title:bt.lang('自动核销提示')});
//            return false;
//        }else {
            dtlId+=dtlTableSel[i].id+',';
//        }
    }


    let transTimeRule = dict.get("transTimeRule").val();
//    if(checkDtlRule(false)){
        autoRulesSetting(docId, dtlId, 1, paymentMeansSel, "", data, dtlTableSel);
//    }else {
//        parent.layer.alert(bt.lang('不满足自动匹配条件！'), {icon: 2});
//    }
}

//手工勾兑
function manual(data) {
    var dtlTableSel = $('#appTableDtl').DataTable().rows('.selected').data();
    var billTableSel = $('#appTableBill').DataTable().rows('.selected').data();
    var lcTableSel = $('#appTableLc').DataTable().rows('.selected').data();

    bt.loading.show();
    if (data.length == 0 || (data.length > 0 && (dtlTableSel.length == 0 && billTableSel.length == 0 && lcTableSel.length == 0))) {
        bt.loading.hide();
//        parent.layer.alert(bt.lang('请选择未核销或部分核销的应收单！'), {icon: 2});
        parent.layer.alert(bt.lang('请勾选应收单和收入范围！'), {icon: 2, title:bt.lang('手工核销提示')});
        return false;
    }
    //应收单据信息
    var docId="";
    var docMoney=0;
    //关联数据金额
    var relationMoney=0;
    var docCurName=data[0].curName;
    var docWriteOffStatus="";
    for (var i = 0; i < data.length; i++) {
        if (data[i].writeOffStatus == '2') {
            bt.loading.hide();
            parent.layer.alert(bt.lang('请选择未核销或部分核销的应收单！'), {icon: 2, title:bt.lang('手工核销提示')});
            return false;
        }
        if(data.length>1 && data[i].curName!=docCurName){
            bt.loading.hide();
//            parent.layer.alert(bt.lang('多条应收单必须同一币种！'), {icon: 2, title:bt.lang('手工核销提示')});
            parent.layer.alert(bt.lang('请一对一勾选后手工核销！'), {icon: 2, title:bt.lang('手工核销提示')});
            return false;
        }
//        if(data.length>1 && data[i].writeOffStatus=='1'){
//            bt.loading.hide();
//            parent.layer.alert(bt.lang('请一对一勾选后手工核销！'), {icon: 2, title:bt.lang('手工核销提示')});
//            return false;
//        }
        docMoney = bt.money.add(docMoney, data[i].writeOffBal);
        docWriteOffStatus=data[i].writeOffStatus;
        if(i === 0){
            docId += data[i].id;
        }else {
            docId += "," + data[i].id;
        }
    }

    var manualData = getManualDataByTableType();
    if (manualData.flag) {
         //带确认逻辑 || (manualData.relationWriteOffStatus=='1'&&docWriteOffStatus=='1')
        if((manualData.length>1&&data.length>1) ){
            bt.loading.hide();
            parent.layer.alert(bt.lang('请一对一勾选后手工核销！'), {icon: 2, title:bt.lang('手工核销提示')});
            return false;
        }
        if(docCurName!=manualData.relationCurName){
            //弹出汇率填写页面
//            editWriteOffExchangeRate(docId, manualData.relationId, 2, paymentMeansSel, "",
//                                     data.length,docCurName,docMoney, manualData.length,manualData.relationCurName,manualData.relationMoney);
            sureWriteOff(docId, manualData.relationId, 2, paymentMeansSel, "", "");
        }else {
            if(data.length>1 && moneyFun.sub(docMoney,manualData.relationMoney)>0){
                bt.loading.hide();
                layer.alert("系统无法判断与哪一笔进行优先核销，请一对一勾选后手工核销！", {icon: 2, title:bt.lang('手工核销提示')});
                return false;
            }
            if(manualData.length>1 && moneyFun.sub(docMoney,manualData.relationMoney)<0){
                bt.loading.hide();
                layer.alert("系统无法判断与哪一笔进行优先核销，请一对一勾选后手工核销！", {icon: 2, title:bt.lang('手工核销提示')});
                return false;
            }
            if(moneyFun.sub(docMoney,manualData.relationMoney) > 0){
                bt.loading.hide();
                layer.confirm("金额不一致，是否部分核销？", {icon: 2, title:bt.lang('手工核销提示')}, function(){
                    bt.loading.show();
                    sureWriteOff(docId, manualData.relationId, 2, paymentMeansSel, "", "", "2");
                });
            }else if(moneyFun.sub(docMoney,manualData.relationMoney) < 0){
                bt.loading.hide();
                layer.confirm("金额不一致，是否继续？", {icon: 2, title:bt.lang('手工核销提示')}, function(){
                    bt.loading.show();
                    sureWriteOff(docId, manualData.relationId, 2, paymentMeansSel, "", "");
                });
            }else {
                sureWriteOff(docId, manualData.relationId, 2, paymentMeansSel, "", "");
            }
        }
    } else {
        bt.loading.hide();
        parent.layer.alert(bt.lang(manualData.msg), {icon: 2, title:bt.lang('手工核销提示')});
        return false;
    }
}


//跳转强制核销页面
function force(data) {
    if (data.length == 0) {
        parent.layer.alert(bt.lang('请选择未核销或部分核销的应收单！'), {icon: 2, title:bt.lang('强制核销提示')});
        return false;
    }else if(data.length > 1){
        parent.layer.alert(bt.lang('一次只能对一张应收单进行强制核销！'), {icon: 2, title:bt.lang('强制核销提示')});
        return false;
    }/*else if("1" == paymentMeansSel && $('#appTableDtl').DataTable().rows('.selected').count() > 0){
         parent.layer.alert(bt.lang('强制核销时，不允许选择银行流水！'), {icon: 2});
         return false;
    }*/else{
     var docId=data[0].id;

     parent.layer.open({
         type: 2,
         title:'强制核销应收单',
         area: ['1100px', '600px'],
         maxmin: true,
         content: basePath + "/accountReceivable/writeOff/viewForce?docId="+docId,
         end:function(){
             //bt.datatable.refreshTable("appTableDoc");
         }
     });
    }
}

//取消核销
function cancel(data) {
    if (data.length == 0) {
        parent.layer.alert(bt.lang('请选择已核销的应收单！'), {icon: 2, title:bt.lang('取消核销提示')});
        return false;
    }else if(data.length > 1){
        parent.layer.alert(bt.lang('一次只能对一张应收单进行取消核销！'), {icon: 2, title:bt.lang('取消核销提示')});
        return false;
    }
    var docId=data[0].id;
    var width = "540px";
    var height = '250px';
    $.ajax({
            url: basePath + "/accountReceivable/writeOff/queryWriteOffHisSize?docId="+docId,
            type:"GET",
            dataType:"json",
            async: false,
            success: function (result) {
                if(result&&result>1){
                    height = '400px'
                    width = "900px"
                }

            }
        });
    parent.layer.open({
        title: '取消核销确认页',
        type: 2,
        area: [width,height],
        maxmin: true,
        content: basePath + "/accountReceivable/writeOff/viewHis?docId="+docId,
        end:function(){
            bt.datatable.refreshTable("appTableDoc");
        }
    });
    /*parent.layer.confirm(bt.lang('确认取消核销？'), function () {
        bt.loading.show();
        $.ajax({
            url: basePath + "/accountReceivable/writeOff/cancelWriteOff",
            method: 'POST',
            data: {docId: docId},
            dataType: 'json',
            success: function (result) {
                bt.loading.hide();
                if (result.success == true) {
                    parent.layer.alert(bt.lang('取消核销成功！'), {icon: result.success ? 1 : 2});
                    bt.datatable.refreshTable("appTableDoc");
                    setIdByTableType("#");
                    refreshTableByTableType();
                }
            }
        });
    });*/
}

function cancelV1(data) {
    return data;
}

//跳转核销汇率填写页面
function editWriteOffExchangeRate(docId, relationId, writeOffType, paymentMeans, bankAccTypeStr,
                                  docNum,docCurName,docMoney, relationNum,relationCurName,relationOriginMoney){
    bt.loading.hide();
    parent.layer.open({
        type: 2,
        area: ['900px', '600px'],
        maxmin: true,
        content: basePath + "/accountReceivable/writeOff/editWriteOffExchangeRate?docId="+docId+"&relationId="+relationId+
                            "&writeOffType="+writeOffType+"&paymentMeans="+paymentMeans+"&bankAccTypeStr="+bankAccTypeStr+
                            "&docNum="+docNum+"&docCurName="+docCurName+"&docMoney="+docMoney+
                            "&relationNum="+relationNum+"&relationCurName="+relationCurName+"&relationOriginMoney="+relationOriginMoney,

        end:function(){
            //bt.datatable.refreshTable("appTableDoc");
        }
    });
}

//跳转自动核销匹配规则设置
function autoRulesSetting(docId, relationId, writeOffType, paymentMeans, exchangeRate, docManageApproveVoList, bisAccDtlVOList){

    var tempArr1 = [];
    for(var inx=0;inx < docManageApproveVoList.length;inx++){
        tempArr1.push(docManageApproveVoList[inx]);
    }
    var temp1 = JSON.stringify(tempArr1);

    var tempArr2 = [];
    for(var inx=0;inx < bisAccDtlVOList.length;inx++){
        tempArr2.push(bisAccDtlVOList[inx]);
    }
    var temp2 = JSON.stringify(tempArr2);

    var data = {};
    data.docId = docId;
    data.relationId = relationId;
    data.writeOffType = writeOffType;
    data.paymentMeans = paymentMeans;
    data.exchangeRate = exchangeRate;
    data.docManageApproveVoList = temp1;
    data.bisAccDtlVOList = temp2;

    //缓存数据
    localStorage.setItem(docId, JSON.stringify(data));
    bt.loading.hide();
    parent.layer.open({
        type: 2,
        area: ['850px', '300px'],
        maxmin: true,
        title: '自动核销匹配规则设置',
        content: basePath + "/accountReceivable/writeOff/autoRulesSetting?docId="+docId,
        end:function(){
            //bt.datatable.refreshTable("appTableDoc");
            /*var data = bt.datatable.getSelectData("#appTableDoc");
            if (data.length>0) {
                tableInitByTableType(data[0]);
            }else if(data.length == 0){
                clearForm();
                setIdByTableType("#");
                getDataByTableType();
            }*/
        }
    });
}

//跳转核销确认页面
function sureWriteOff(docId, relationId, writeOffType, paymentMeans, bankAccTypeStr, exchangeRate, businessType){
    if(!businessType){
        businessType = "1";
    }
    bt.loading.hide();
    parent.layer.open({
        title: '手工核销确认页',
        type: 2,
        area: ['1000px', '600px'],
        maxmin: true,
        content: basePath + "/accountReceivable/writeOff/viewSure?docId="+docId+"&relationId="+relationId+
                            "&writeOffType="+writeOffType+"&paymentMeans="+paymentMeans+"&bankAccTypeStr="+bankAccTypeStr+
                            "&exchangeRate="+exchangeRate+"&businessType="+businessType,
        end:function(){
            //bt.datatable.refreshTable("appTableDoc");
            /*var data = bt.datatable.getSelectData("#appTableDoc");
            if (data.length>0) {
                tableInitByTableType(data[0]);
            }else if(data.length == 0){
                clearForm();
                setIdByTableType("#");
                getDataByTableType();
            }*/
        }
    });
}


function changeDocCurShowCallback(data){
    $("#doc_eqs_curId").val(data.id);
}

function checkRow(rowIndex, checked, elem) {
    var tables = elem.parents('.DTFC_ScrollWrapper').find('table');
    //获取页面贮存的实例
    var currentTableList = elem.data('table');

    for (var i = 0; i < tables.length; i++) {
        var clickTr = $(tables[i]).find("tbody tr:eq(" + rowIndex + ") ");
        if (!clickTr.length) {
            continue;
        }
        checked ? clickTr.addClass(currentTableList.selectedStyle) : clickTr.removeClass(currentTableList.selectedStyle);
        if (clickTr.find("td:first-child input").length != 0) {
            clickTr.find('input[type="checkbox"]').prop("checked", checked);
        }
    }
    form.render('checkbox');
}


function checkboxHideShowCallback(){

}

//function dtlGegAmtChange(){
//    var dtlGegAmt = moneyType.get("dtl_geg_amt").val();
//    if(dtlGegAmt<=0){
//        moneyType.get("dtl_geg_amt").val('');
//    }
//}
//
//function dtlLegAmtChange(){
//    var dtlLegAmt = moneyType.get("dtl_leg_amt").val();
//    if(dtlLegAmt<=0){
//        moneyType.get("dtl_leg_amt").val('');
//    }
//}
//
//function billGegBillMoneyChange(){
//    var billGegBillMoney = moneyType.get("bill_geg_billMoney").val();
//    if(billGegBillMoney<=0){
//        moneyType.get("bill_geg_billMoney").val('');
//    }
//}
//
//function billLegBillMoneyChange(){
//    var billLegBillMoney = moneyType.get("bill_leg_billMoney").val();
//    if(billLegBillMoney<=0){
//        moneyType.get("bill_leg_billMoney").val('');
//    }
//}
