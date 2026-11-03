var datatable;
var dateType;
bt.use(['form','datatable','dateType','selectType','dicts','corp','page','moneyType','mulselect','money'],function(){
    $=bt.jquery,form=bt.form();
    bt.corp.init();
    bt.selectType.init();
    dicts=bt.dicts.init();
    bt.mulselect.init();
    dateType= bt.dateType.init();
    moneyType=bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: false,
        query: "search",
        checkbox:false,
        order: false,
        exportFile:{
            customPrint:{
                header:function(){
                    var printHtml = '<tr>\n' +
                        '        <th rowspan="2" >'+ bt.lang("bill.accebill.abBillDiscountApply.discountNet") +'</th>\n' +
                        '        <th rowspan="2" >'+ bt.lang("table.fcloanrepay.corpName") +'</th>\n' +
                        '        <th rowspan="2" >'+ bt.lang("table.fbsBudgetProcess.curCode") +'</th>\n' +
                        '        <th colspan="2" >'+ bt.lang('table.fcacc.accType01')+'</th>\n' +
                        '        <th colspan="2" >'+ bt.lang('table.fcacc.accType05')+'</th>\n' +
                        '        <th colspan="2" >'+ bt.lang('table.fcacc.accType08')+'</th>\n' +
                        '        <th colspan="2" >'+ bt.lang('table.fcacc.accType03')+'</th>\n' +
                        '        <th colspan="2" >'+ bt.lang('table.fcacc.accType02')+'</th>\n' +
                        '        <th colspan="2" >'+ bt.lang('table.fcacc.accType04')+'</th>\n'+
                        '</tr>\n' +
                        '<tr>\n' +
                        '        <th>'+bt.lang('table.cms.loan.interest.payable')+'</th>\n' +
                        '        <th>'+bt.lang('table.cmsProvideLoanRepay.onlineInterest')+'</th>\n' +

                        '        <th>'+bt.lang('table.cms.loan.interest.payable')+'</th>\n' +
                        '        <th>'+bt.lang('table.cmsProvideLoanRepay.onlineInterest')+'</th>\n' +

                        '        <th>'+bt.lang('table.cms.loan.interest.payable')+'</th>\n' +
                        '        <th>'+bt.lang('table.cmsProvideLoanRepay.onlineInterest')+'</th>\n' +

                        '        <th>'+bt.lang('table.choProductQuery.receivableMoney')+'</th>\n' +
                        '        <th>'+bt.lang('table.choBankFinance.blInterest')+'</th>\n' +

                        '        <th>'+bt.lang('table.choProductQuery.receivableMoney')+'</th>\n' +
                        '        <th>'+bt.lang('table.choBankFinance.blInterest')+'</th>\n' +

                        '        <th>'+bt.lang('table.choProductQuery.receivableMoney')+'</th>\n' +
                        '        <th>'+bt.lang('table.choBankFinance.blInterest')+'</th>\n' +
                        '    </tr>';
                    return printHtml;
                },
            },
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bi/fcAccInterest/getFcAccInterest"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"netName",bSortable:true,className:'center'},
            {data:"corpName",bSortable:true,className:'center'},
            {data:"curName",bSortable:true,className:'center'},
            {
                data: "tInterest_1", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.tInterest_1 == 0 || full.tInterest_1 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="viewReceivable(this,\'01\')">'+bt.money.format(full.tInterest_1)+'</a>';
                        return html;
                    }
                }
            },
            {
                data: "dInterest_1", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.dInterest_1 == 0 || full.dInterest_1 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewPaid(this,\'01\')">' + bt.money.format(full.dInterest_1) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "tInterest_5", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.tInterest_5 == 0 || full.tInterest_5 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewReceivable(this,\'05\')">' + bt.money.format(full.tInterest_5) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "dInterest_5", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.dInterest_5 == 0 || full.dInterest_5 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewPaid(this,\'05\')">' + bt.money.format(full.dInterest_5) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "tInterest_8", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.tInterest_8 == 0 || full.tInterest_8 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewReceivable(this,\'08\')">' + bt.money.format(full.tInterest_8) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "dInterest_8", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.dInterest_8 == 0 || full.dInterest_8 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewPaid(this,\'08\')">' + bt.money.format(full.dInterest_8) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "tInterest_3", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.tInterest_3 == 0 || full.tInterest_3 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewReceivable(this,\'03\')">' + bt.money.format(full.tInterest_3) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "dInterest_3", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.dInterest_3 == 0 || full.dInterest_3 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewPaid(this,\'03\')">' + bt.money.format(full.dInterest_3) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "tInterest_2", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.tInterest_2 == 0 || full.tInterest_2 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewReceivable(this,\'02\')">' + bt.money.format(full.tInterest_2) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "dInterest_2", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.dInterest_2 == 0 || full.dInterest_2 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewPaid(this,\'02\')">' + bt.money.format(full.dInterest_2) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "tInterest_4", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.tInterest_4 == 0 || full.tInterest_4 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewReceivable(this,\'04\')">' + bt.money.format(full.tInterest_4) + '</a>';
                        return html;
                    }
                }
            },
            {
                data: "dInterest_4", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    if(full.dInterest_4 == 0 || full.dInterest_4 == undefined){
                        return "-"
                    }else {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewPaid(this,\'04\')">' + bt.money.format(full.dInterest_4) + '</a>';
                        return html;
                    }
                }
            }
        ],
        summary :{
            pageSum: false,//小计功能关闭
            allSum: true,//汇总功能开启
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '3', //汇总列序号
                    dataName: 'dInterest_1' //汇总列发放金额
                },
                {
                    colNum: '4', //汇总列序号
                    dataName: 'tInterest_1' //汇总列贷款余额
                },
                {
                    colNum: '5', //汇总列序号
                    dataName: 'dInterest_5' //汇总列发放金额
                },
                {
                    colNum: '6', //汇总列序号
                    dataName: 'tInterest_5' //汇总列贷款余额
                },
                {
                    colNum: '7', //汇总列序号
                    dataName: 'dInterest_8' //汇总列发放金额
                },
                {
                    colNum: '8', //汇总列序号
                    dataName: 'tInterest_8' //汇总列贷款余额
                },
                {
                    colNum: '9', //汇总列序号
                    dataName: 'dInterest_3' //汇总列发放金额
                },
                {
                    colNum: '10', //汇总列序号
                    dataName: 'tInterest_3' //汇总列贷款余额
                },
                {
                    colNum: '11', //汇总列序号
                    dataName: 'dInterest_2' //汇总列贷款余额
                },
                {
                    colNum: '12', //汇总列序号
                    dataName: 'tInterest_2' //汇总列贷款余额
                },
                {
                    colNum: '13', //汇总列序号
                    dataName: 'dInterest_4' //汇总列贷款余额
                },
                {
                    colNum: '14', //汇总列序号
                    dataName: 'tInterest_4' //汇总列贷款余额
                }

            ]
        }


    });
});

var getParam=function(){
    var param="";
    param +="beginDate="+dateType.get('beginDate').val()+"&endDate="+dateType.get('endDate').val();
    return param;

}

function viewReceivable(el,accType){
    //应付
    var data = datatable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/bi/fcAccInterest/view?"+getParam()+"&corpId="+data.corpId+"&curId="+data.curId+"&accType="+accType+"&status=receivable"
    });

}

function viewPaid(el,accType){
    //实付
    var data = datatable.row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/bi/fcAccInterest/view?"+getParam()+"&corpId="+data.corpId+"&curId="+data.curId+"&accType="+accType+"&status=paid"
    });

}
	
	

	
