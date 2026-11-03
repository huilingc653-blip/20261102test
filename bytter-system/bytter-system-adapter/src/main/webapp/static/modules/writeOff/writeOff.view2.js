var moneyType,moneyFun;
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {
     var $ = bt.jquery, form = bt.form();
     moneyType = bt.moneyType.init();
     moneyFun = moneyType.getFun();

//     var appTableDoc = bt.datatable.init({
//         elem: "#appTableDoc",
//         checkbox: false,
//         mulselect: false,
//         jumpPageSelect: false,//开启跨页选择
//         paging: false,//开启跨页选择
//         ignoreButtons: ['colvis', 'output', 'print', 'state'],
//         isInitData: true, //initdate
//         searchForm: {
//         },
//         order: [[1, 'desc']],
//         ajax: {
//             'url': basePath + "/accountReceivable/writeOff/docDatatable"
//         },
//         buttons: [
//         ],
//         columns: [
//             {data: "code", bSortable: true},
//             {data: "corpName", bSortable: false},
//             {data: "batchBusTypeName", bSortable: true},
//             {data: "sourceName", bSortable: true},
//             {data: "writeOffStatusName", bSortable: true}
//         ]
//     });

//     var appTableDtl = bt.datatable.init({
//         elem: "#appTableDtl",
//         checkbox: false,
//         mulselect: false,
//         jumpPageSelect: false,//开启跨页选择
//         paging: false,//开启跨页选择
//         isInitData: true, //initdate
//         searchForm: {
//         },
//         ignoreButtons: ['colvis', 'output', 'print', 'state'],
//         order: [[1, 'desc']],
//         ajax: {
//             'url': basePath + "/accountReceivable/writeOff/docDatatable"
//         },
//         buttons: [
//         ],
//         columns: [
//             {data: "code", bSortable: true},
//             {data: "corpName", bSortable: false},
//             {data: "batchBusTypeName", bSortable: true},
//             {data: "sourceName", bSortable: true},
//             {data: "writeOffStatusName", bSortable: true}
//         ]
//     });

     var appTableSure = bt.datatable.initMultiTables({
         elem: "#appTableSure",
         //checkbox: false,
         //mulselect: true,
         selectCss: true,
         //jumpPageSelect: true,//开启跨页选择
         isInitData: true,
         searchForm: {
            formElem: "#sureSearchForm"
         },
         order: [[1, 'desc']],
         ajax: {
             'url': basePath + "/accountReceivable/writeOff/sureDatatable"
         },
         buttons: [
            {
                 extend: "colvis",
                 text: bt.lang('common.button.theadHide'),
                 columns: ':not(:first):not(:last)'
             }
         ],
         columns: [
            {data: "docManageVo.code", bSortable: false},
             {
                  data: "docMoney", bSortable: true, className: 'textRight',
                  render: function (data, type, full, meta) {
                      return bt.money.format(full.docMoney);
                  }
              },
             {data: "docManageVo.externalName", bSortable: true},
             {data: "docManageVo.corpName", bSortable: false},
             {data: "docManageVo.batchBusTypeName", bSortable: false},
             {data: "docManageVo.sourceName", bSortable: false},
             {data: "docManageVo.writeOffStatusName", bSortable: false},
             {data: "afterDocWriteOffStatusName", bSortable: false},
             {data: "writeOffTypeName", bSortable: false},
             {data: "docManageVo.receiptDate", bSortable: false},
             {data: "docManageVo.endDate", bSortable: false},
             {data: "docManageVo.agingDate", bSortable: false},
             {
               data: "docManageVo.writeOffBal", bSortable: true, className: 'textRight',
               render: function (data, type, full, meta) {
                   return bt.money.format(full.docManageVo.writeOffBal);
               }
             },
             {
                data: "afterDocWriteOffBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.afterDocWriteOffBal);
                }
             }
         ],
         btClickRowCallback: function (data1) {

         },
         btDrawCallback: function (setting) { //回调方法
         },
         summary: {
             pageSum: false,
             allSum: true,
             selectSum: false,
             format: {
                 decimal: 2
             },
             sumList: [
                 {
                     money: true,
                     colNum: '1',
                     dataName: 'docMoney'
                 }/*,{
                     money: true,
                     colNum:'13',
                     dataName:'docManageVo.writeOffBal'
                 },
                 {
                     money: true,
                     colNum:'14',
                     dataName:'afterDocWriteOffBal'
                 }*/
             ]
         }
     });


        //获取流水表格数据
        var tbDtl;
        function getDtlDate() {
            if (null == tbDtl) {
//                tbDtl = bt.datatable.initMultiTables({
//                    elem: "#appTableDtl",
//                    isInitData: true,
//                    checkbox: false,
//                    mulselect: true,
//                    selectCss: true,
//                    jumpPageSelect: false,//开启跨页选择
//                    searchForm: {
//                        formElem: "#searchFormDtl",
//                        formFilter: "dtlSearch"
//                    },
//                    order: [[1, 'desc']],
//                    ajax: {
//                        'url': basePath + "/accountReceivable/writeOff/dtlDatatable"
//                    },
//                    buttons: [
//                        {
//                            extend: "colvis",
//                            text: bt.lang('common.button.theadHide'),
//                            columns: ':not(:first):not(:last)'
//                        }
//                    ],
//                    columns: [
//                        {data: "bankSerialNo", bSortable: true},
//                        {data: "transTime", bSortable: true},
//                        {data: "curName", bSortable: true},
//                        {
//                            data: "amt", bSortable: true, className: 'textRight',
//                            render: function (data, type, full, meta) {
//                                return bt.money.format(full.amt);
//                            }
//                        },
//                        {
//                            data: "writeOffBal", bSortable: true, className: 'textRight',
//                            render: function (data, type, full, meta) {
//                                return bt.money.format(full.writeOffBal);
//                            }
//                        },
//                        {data: "accName", bSortable: true},
//                        {data: "bankName", bSortable: true},
//                        {data: "bankAcc", bSortable: true},
//                        {data: "oppAccName", bSortable: true},
//                        {data: "oppAccNo", bSortable: true},
//                        {data: "returnTime", bSortable: true, format: "YYYY-MM-DD hh:mm:ss"},
//                        {data: "uses", bSortable: true},
//                        {data: "postscript", bSortable: true},
//                        {data: "writeOffStatusName", bSortable: true},
//                        {data: "writeOffTypeName", bSortable: true}
//                    ],
//                    btDrawCallback: function () {
//                    },
//                    btClickRowCallback: function (data1) {
//                    }
//                });
            } else {
                bt.datatable.refreshTable("appTableDtl");
            }
        }

        var tbBill;
        function getBillData() {
            if (null == tbBill) {
                tbBill = bt.datatable.initMultiTables({
                    elem: "#appTableBill",
                    checkbox: false,
                    mulselect: true,
                    selectCss: true,
                    isInitData: true,
                    jumpPageSelect: false,//开启跨页选择
                    searchForm: {
                        formElem: "#searchFormBill",
                        formFilter: "billSearch"
                    },
                    order: [[1, 'desc']],
                    ajax: {
                        'url': basePath + "/accountReceivable/writeOff/billDatatable"
                    },
                    buttons: [
                        {
                            extend: "colvis",
                            text: bt.lang('common.button.theadHide'),
                            columns: ':not(:first):not(:last)'
                        }
                    ],
                    columns: [
                        {data: "billTypeName", bSortable: true},
                        {data: "billFlagName", bSortable: true},
                        {data: "corpCodeAndName", bSortable: true},
                        {data: "blRmtrUnnIdNo", bSortable: true},
                        {data: "payer", bSortable: true},
                        {data: "acceptor", bSortable: true},
                        {data: "origNo", bSortable: true},
                        {data: "billMoney", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.billMoney);
                            }
                        },
                        {data: "realMoney", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.realMoney);
                            }
                        },
                        {data: "writeOffBal", bSortable: true,
                             render: function (data, type, full, meta) {
                                 return bt.money.format(full.writeOffBal);
                             }
                         },
                        {data: "startDate", bSortable: true},
                        {data: "endDate", bSortable: true},
                        {data: "receDate", bSortable: true},
                        {data: "briefIdName", bSortable: true},
                        {data: "enterStockName", bSortable: true},
                        {data: "newStmIssuofpprIndName", bSortable: true},
                        {data: "blgrdbagcrclPrmtIndName", bSortable: true},
                        {data: "sbbllRngStrtSn", bSortable: true},
                        {data: "sbbllRngEndSn", bSortable: true},
                        {data: "blStdAmt", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.blStdAmt);
                            }
                        },
                        {data: "transferWayName", bSortable: true,
                            render: function (data, type, full, meta) {
                                if (full.briefId == '8018' && full.transferWay == '0') {
                                    data = '票据池质押';
                                }
                                return data;
                            }
                        },
                        {data: "writeOffStatusName", bSortable: true},
                        {data: "writeOffTypeName", bSortable: true}
                    ],
                    btDrawCallback: function () {
                    },

                });
            } else {
                bt.datatable.refreshTable("appTableBill");
            }
        }

        var tbLc;
        function getLcData() {
            if (null == tbLc) {
                tbLc = bt.datatable.initMultiTables({
                    elem: "#appTableLc",
                    isInitData: true,
                    checkbox: false,
                    mulselect: true,
                    selectCss: true,
                    jumpPageSelect: false,//开启跨页选择
                    searchForm: {
                        formElem: "#searchFormLc",
                        formFilter: "lcSearch"
                    },
                    order: [[1, 'desc']],
                    ajax: {
                        'url': basePath + "/accountReceivable/writeOff/lcDatatable"
                    },
                    buttons: [
                        {
                            extend: "colvis",
                            text: bt.lang('common.button.theadHide'),
                            columns: ':not(:first):not(:last)'
                        }
                    ],
                    columns: [
                        {data: "arrivalCode", bSortable: true},
                        {data: "corpName", bSortable: true},
                        {data: "creditNo", bSortable: true},
                        {data: "lcInter", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.dict("letter_of_credit_inter", full.lcInter);
                            }
                        },
                        {data: "handDocBank", bSortable: true},
                        //{data: "beneficiary", bSortable: true},
                        {data: "corpName", bSortable: true, className: "center"},
                        {
                            data: "billMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.billMoney);
                            }
                        },
                        {
                            data: "payMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.payMoney);
                            }
                        },
                        {
                            data: "incomingMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.incomingMoney);
                            }
                        },
                        {
                            data: "writeOffBal", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.writeOffBal);
                            }
                        },
                        {
                            data: "mortgageMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.mortgageMoney);
                            }
                        },
                        {
                            data: "negotiationMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.negotiationMoney);
                            }
                        },
                        {data: "curName", bSortable: true},
                        {data: "paymentDate", bSortable: true, className: "center"},
                        {data: "corpName", bSortable: true},
                        {data: "writeOffStatusName", bSortable: true},
                        {data: "writeOffTypeName", bSortable: true}
                    ],
                    btDrawCallback: function () {
                    },

                });
            } else {
                bt.datatable.refreshTable("appTableLc");
            }
        }

        function initRelationTable(){
            var paymentMeansSel = $("#paymentMeans").val();
            if("1" == paymentMeansSel){
                getDtlDate()
            }else if("2" == paymentMeansSel){
                getBillData()
            }else if("3" == paymentMeansSel){
                getLcData()
            }
        }
        initRelationTable();


     form.on('submit(submit)', function (data) {
        if(!data.field.rate){
            data.field.rate = 1;
        }
        var v1 = Number($("#money2").val());
        var v2 = Number($("#money3").val());
        var curType1 = $('#curType1').val();
        var curType2 = $('#curType2').val();
        var operationType = $('#operationType').val();
        //idType 表示存放ID类型 如果doc 就是多个应收单 如果是dlt 就是多个流水
        var obj={"idType":"doc"};
        if(operationType==="OO"){
           obj[$('#executeMoney').data('id')]=bt.money.format($('#executeMoney').html().split(' ')[1]);

        }else if(operationType==="MO"||operationType==="OM"){
            var  executeClass=[];
            if($('.NS5-2')!=null&&$('.NS5-2').length>0){
                executeClass=$('.NS5-2');// 一个流水对多应收单
                for(var i=0;i < executeClass.length;i++){
                   obj[$(executeClass[i]).data('id')]=bt.money.format($(executeClass[i]).html());
                }
            }else {
                executeClass=$('.NS6-2');//一个应收单对多个流水
                obj={"idType":"dtl"};
                for(var i=0;i < executeClass.length;i++){
                   obj[$(executeClass[i]).data('id')]=bt.money.format($(executeClass[i]).html().split(' ')[1]);
                }
            }


        }
        data.field['executeMoneyMap'] = obj ;
        if(operationType != 'OO' && v2 > v1){
            if(curType1 == curType2){
                layer.alert("系统无法判断与哪一笔进行优先核销，请一对一勾选后手工核销！", {icon: 2, title:bt.lang('手工核销提示')});
            }else{
                layer.alert("系统无法判断与哪一笔进行优先核销或是否存在汇率尾差，请一对一勾选后手工核销！", {icon: 2, title:bt.lang('手工核销提示')});
            }
            return false;
        }
        bt.loading.show();
        var action = "/accountReceivable/writeOff/sureWriteOff";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            if(result.success){
                result.msg = bt.lang("submitted.successfully.pending.approval");
            }
            parent.layer.open({content:result.msg, yes: function(){
                parent.layer?parent.layer.closeAll():layer.closeAll();
                bt.navtab.refreshCurrentTable("appTableDoc");
            }
            });
        },'JSON');
        return false;
     });
});

function cancle(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
}

function switchCur(){
    var curTypeTag1 = $('#curTypeTag1').text();
    var curTypeTag2 = $('#curTypeTag2').text();
    $('#curTypeTag1').text(curTypeTag2);
    $('#curTypeTag2').text(curTypeTag1);

    var curType1 = $('#curType1').val();
    var curType2 = $('#curType2').val();
    $('#curType1').val(curType2);
    $('#curType2').val(curType1);

    var nr = moneyType.get('rate').val();
    var or1 = $('#rateTemp').val();
    var or2 = $('#rateTemp0').val();
    if(or2 != nr){
        nr = moneyFun.div(1,nr,4);
        $('#rateTemp0').val(nr);
        moneyType.get('rate').val(nr);
    }else if(or2 == nr){
        moneyType.get('rate').val(or1);
    }

    calculate();
}

function updateRate(){
    var rate0 = moneyType.get('rate').val();
    if(rate0 <= 0){
        moneyType.get('rate').val('');
        return false;
    }
    $('#rateTemp').val(rate0);
    calculate();

}

function calculate(){
    var rate0 = moneyType.get('rate').val();
    $("#ma").text($("#money0").val());
    $("#mb").text(moneyFun.format(moneyFun.mul($("#money0").val(), rate0)));

    var operationType = $('#operationType').val();
    if(operationType == 'OO'){
        var arr1 = $('#OOM1').html().split(' ');
        var OOM1 = moneyFun.format(arr1[1]);
        var arr2 = $("#OOM2").html().split(' ');
        var OOM2 = moneyFun.format(moneyFun.mul(arr2[1], rate0));
        OOM1 = OOM1.replace(/,/g,'');
        OOM2 = OOM2.replace(/,/g,'');
        $('#executeMoney').html(arr1[0] + ' ' +(Number(OOM1) > Number(OOM2)?OOM2:OOM1));
        $('#OOM3').html(arr1[0] + ' ' +(Number(OOM1) > Number(OOM2)?OOM2:OOM1));
        $('#NS1').html((Number(OOM1) > Number(OOM2)?"部分核销":"已核销"));
        $('#NS2').html((Number(OOM1) >= Number(OOM2)?"已核销":"部分核销"));
    }else if(operationType == 'MO'){
        var arr1 = $('#MO1').html().split(' ');
        var OOM1 = moneyFun.format(moneyFun.mul(arr1[1], rate0));
        OOM1 = OOM1.replace(/,/g,'');
        var err = $('.NS5-1');
        var errc = $('.NS5-1-1');
        var err2 = $('.NS5-2');
        var err3 = $('.NS5');
        var tempMoney = moneyFun.format(0);
        for(var i=0;i < err.length;i++){
            var tempArr = [$(errc[i]).html(), $(err[i]).html()];
            var m1 = moneyFun.format(tempArr[1]);
            var m3 = Number(m1.replace(/,/g,''));
            var m2 = Number(OOM1) > Number(m3)?m1:OOM1;
            tempMoney = moneyFun.format(moneyFun.add(m2, tempMoney));
            $(err2[i]).html(m2);
            $(err3[i]).html(Number(m1.replace(/,/g,'')) <= Number(m2.replace(/,/g,''))?"已核销":"部分核销");
        }
        OOM1 = OOM1.replace(/,/g,'');
        tempMoney = tempMoney.replace(/,/g,'');
        $("#sumWriteOffBal").html($(errc[0]).html() + ' ' +tempMoney);
        $('#executeMoney2').html($(errc[0]).html() + ' ' +(Number(OOM1) > Number(tempMoney)?tempMoney:OOM1));
        $('#NS3').html((Number(OOM1) > tempMoney?"部分核销":"已核销"));
        $("#money2").val(Number(OOM1));
        $("#money3").val(Number(tempMoney));
    }else if(operationType == 'OM'){
        var arr1 = $('#OOM1').html().split(' ');
        var OOM1 = moneyFun.format(arr1[1]);
        var err = $('.NS6-1');
        var err2 = $('.NS6-2');
        var err3 = $('.NS6');
        var tempMoney = moneyFun.format(0);
        var tempMoney2 = moneyFun.format(0);
        var tempMoney3 = moneyFun.format(0);
        OOM1 = OOM1.replace(/,/g,'');
        var tempMoney4 = Number(moneyFun.div(OOM1, err.length));
        for(var i=0;i < err.length;i++){
            var tempArr = $(err[i]).html().split(' ');
            var m1 = tempArr[1];
            tempMoney2 = moneyFun.format(moneyFun.add(m1, tempMoney2));
            m1 = moneyFun.format(moneyFun.mul(m1, rate0));
            tempMoney3 = moneyFun.format(moneyFun.add(m1, tempMoney3));
            var m3 = Number(m1.replace(/,/g,''));
            var m2 = Number(OOM1) >= m3?2:1;
            m1 = Number(OOM1) > Number(m3)?m1:OOM1;
            tempMoney = moneyFun.format(moneyFun.add(m1, tempMoney));
            $(err2[i]).html(arr1[0] + ' ' + m1);
            $(err3[i]).html(m2 == 2?"已核销":"部分核销");
        }
        tempMoney = tempMoney.replace(/,/g,'');
        $("#sumWriteOffBal2").html(arr1[0] + ' ' + tempMoney);
        $('#executeMoney').html(arr1[0] + ' ' +(Number(OOM1) > Number(tempMoney)?tempMoney:OOM1));
        $('#NS1').html((Number(OOM1) > tempMoney?"部分核销":"已核销"));

        $("#ma").text(moneyFun.format(tempMoney2));
        $("#mb").text(moneyFun.format(tempMoney3));
        $("#money2").val(Number(OOM1));
        $("#money3").val(Number(tempMoney));
    }
    var v1 = Number($("#money2").val());
    var v2 = Number($("#money3").val());

    var curType1 = $('#curType1').val();
    var curType2 = $('#curType2').val();
    if(operationType != 'OO' && v2 > v1){
        if(curType1 == curType2){
            layer.alert("系统无法判断与哪一笔进行优先核销，请一对一勾选后手工核销！", {icon: 2, title:bt.lang('手工核销提示')});
        }else{
            layer.alert("系统无法判断与哪一笔进行优先核销或是否存在汇率尾差，请一对一勾选后手工核销！", {icon: 2, title:bt.lang('手工核销提示')});
        }
    }
}

function toInfoPage(data){
    var arr = data.split('-');

    var rate0 = 1;
    var rateOpenStatus = $('#rateOpenStatus').val();
    if($('#rateOpenStatus').val()){
        rate0 = moneyType.get('rate').val();
    }

    var money0 = 0, money1 = 0;
    var operationType = $('#operationType').val();
    if(operationType == 'OO'){
        money0 = $('#executeMoney').html().split(' ')[1];
        money1 = $('#OOM3').html().split(' ')[1];
    }else if(operationType == 'MO'){
        money0 = $('#executeMoney2').html().split(' ')[1];
    }else if(operationType == 'OM'){
        money0 = $('#executeMoney').html().split(' ')[1];
    }
    money0 = money0.replace(/,/g,'');

     parent.layer.open({
         title: arr[2],
         type: 2,
         area: ['1400px', '800px'],
         maxmin: true,
         content: basePath + "/accountReceivable/writeOff/info?type="+arr[0]+'&businessId='+arr[1]+'&money0='+money0+'&rate='+rate0+'&rateOpenStatus='+rateOpenStatus,
         end:function(){
             //bt.datatable.refreshTable("appTableDoc");
         }
     });
}