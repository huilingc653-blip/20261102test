var selectType,dicts,dateType;
bt.use(['form','datatable','btdate','corp','moneyType','rateType','dateType','selectType','date','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dateType = bt.dateType.init();
    selectType=bt.selectType.init();
    dicts = bt.dicts.init();

    //校验本位币
    form.verify({
        isNatualCur: function(value, item){ //value：表单的值、item：表单的DOM对象
            var isToCurr = dicts.get("isConvertStandCur").val();//是否折合本位币的值
            if(!value && isToCurr == "1"){
                return bt.lang("table.report.isNatualCur");
            }
        }
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        fixedColumns: {
            leftColumns:1,//代表冻结左边两列
            // rightColumns:1//代表冻结右边一列
        },
        isInitData: false,//初始化不加载数据，点击查询后加载
        order: [2,'desc'],
        exportFile:{
            customPrint:{
                header:function(){
                    var printHtml = '<tr>\n' +
                        '            <th rowspan="2">'+bt.lang("table.report.corpId")+'</th>\n'+//单位
                        '            <th rowspan="2">'+bt.lang("cms.report.loanType")+'</th>\n' +//融资方式
                        '            <th colspan="5">'+bt.lang("cms.report.toDateBalance")+'</th>\n' +//截止本期金额
                        '            <th colspan="5">'+bt.lang("cms.report.toLastYearBalance")+'</th>\n' +//截止去年同期
                        '            <th colspan="2">'+bt.lang("cms.report.increaseRate")+'</th>\n' +//增长率
                        '        </tr>\n' +
                        '        <tr>\n' +
                        '            <th>'+bt.lang("cms.report.contractMoney")+'</th>\n' +//截止本期合同金额
                        '            <th>'+bt.lang("cms.report.sendMoney")+'</th>\n' +//截止本期发放金额
                        '            <th>'+bt.lang("cms.report.repayMoney")+'</th>\n' +//截止本期还款金额
                        '            <th>'+bt.lang("cms.report.loanRate")+'</th>\n' +//截止本期放款占比
                        '            <th>'+bt.lang("cms.report.repayRate")+'</th>\n' +//截止本期还款占比
                        '            <th>'+bt.lang("cms.report.contractMoney")+'</th>\n' +//截止去年同期合同金额
                        '            <th>'+bt.lang("cms.report.sendMoney")+'</th>\n' +//截止去年同期发放金额
                        '            <th>'+bt.lang("cms.report.repayMoney")+'</th>\n' +//截止去年同期还款金额
                        '            <th>'+bt.lang("cms.report.loanRate")+'</th>\n' +//截止去年同期还款占比
                        '            <th>'+bt.lang("cms.report.repayRate")+'</th>\n' +//截止去年同期还款占比
                        '            <th>'+bt.lang("cms.report.loanGrowthRate")+'</th>\n' +//放款增长率
                        '            <th>'+bt.lang("cms.report.repayGrowthRate")+'</th>\n' +//还款增长率
                        '        </tr>';
                    return printHtml;
                },
            },
        },
        ajax: {
            'url': basePath+"/cms/report/cmsloanstatements/datatable"
        },searchForm:{
            formFilter:"search",
            formElem:"#searchForm"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:"corpName",className:"center",bSortable:true},//单位
            {data:"loanSignName",className:"center",bSortable:false},//融资种类
            // {data:"curName",className:"center",bSortable:false},//币别
            {data:"curContractMoney",className:"textRight",bSortable:false,//本期合同金额
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.curContractMoney);
                }
            },
            {data:"curSendMoney",className:"textRight",bSortable:false,//本期发放金额
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.curSendMoney);
                }
            },
            {data:"curRepayMoney",className:"textRight",bSortable:false,//本期还款金额
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.curRepayMoney);
                }
            },
            {data:"curLoanRate",bSortable:true,className:'center',//本期放款占比:如果合同金额为0，则占比显示--
                render: function ( data, type, full, meta ) {
                    if(full.curContractMoney==moneyFun.format(0)){
                        return "--"
                    }else{
                        return bt.rateType.getFun().format(full.curLoanRate,'percent',2);
                    }
                }
            },
            {data:"curRepayRate",bSortable:true,className:'center',//本期还款占比:如果已放款金额为0，则占比显示--
                render: function ( data, type, full, meta ) {
                    if(full.curSendMoney==moneyFun.format(0)){
                        return "--"
                    }else{
                        return bt.rateType.getFun().format(full.curRepayRate,'percent',2);
                    }
                }
            },
            {data:"lastContractMoney",className:"textRight",bSortable:false,//去年同期合同金额
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.lastContractMoney);
                }

            },
            {data:"lastSendMoney",className:"textRight",bSortable:false,//去年同期发放金额
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.lastSendMoney);
                }

            },
            {data:"lastRepayMoney",className:"textRight",bSortable:false,//去年同期还款金额
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.lastRepayMoney);
                }

            },
            {data:"lastLoanRate",bSortable:true,className:'center',//去年同期放款占比：如果合同金额为0，则占比显示--
                render: function ( data, type, full, meta ) {
                    if(full.lastContractMoney==moneyFun.format(0)){
                        return "--"
                    }else{
                        return bt.rateType.getFun().format(full.lastLoanRate,'percent',2);
                    }
                }
            },
            {data:"lastRepayRate",bSortable:true,className:'center',//去年同期还款占比：如果已放款金额为0，则占比显示--
                render: function ( data, type, full, meta ) {
                    if(full.lastSendMoney==moneyFun.format(0)){
                        return "--"
                    }else{
                        return bt.rateType.getFun().format(full.lastRepayRate,'percent',2);
                    }
                }
            },
            {data:"loanGrowthRate",bSortable:true,className:'center',//放款增长率:如果去年同期金额-放款金额为0，则显示--
                render: function ( data, type, full, meta ) {
                    if(full.lastSendMoney==moneyFun.format(0)){
                        return "--"
                    }else{
                        return bt.rateType.getFun().format(full.loanGrowthRate,'percent',2);
                    }
                }
            },
            {data:"lastRepayRate",bSortable:true,className:'center',//还款增长率:如果去年同期金额-还款金额为0，则显示--
                render: function ( data, type, full, meta ) {
                    if(full.lastRepayMoney==moneyFun.format(0)){
                        return "--"
                    }else{
                        return bt.rateType.getFun().format(full.repayGrowthRate,'percent',2);
                    }
                }
            }
        ],
        // rowsGroup:[2,3,4,7,8,9],
        summary:{
            pageSum:true,
            format:{
                decimal: 2
            },
            sumList:[
                {
                    colNum:'2',
                    dataName:'curContractMoney'
                },
                {
                    colNum:'3',
                    dataName:'curSendMoney'
                },
                {
                    colNum:'4',
                    dataName:'curRepayMoney'
                },
                {
                    colNum:'7',
                    dataName:'lastContractMoney'
                },
                {
                    colNum:'8',
                    dataName:'lastSendMoney'
                },
                {
                    colNum:'9',
                    dataName:'lastRepayMoney'
                }

            ]
        }
    });
    changeCurSelect();
});

//控制币别是否可选
function changeCurSelect() {
    var isConvertStandCur=dicts.get("isConvertStandCur").val();
    var curId=selectType.get("curId").val();
    $("#curCode").val(curId);
    if(isConvertStandCur=="1"){//折合本位币时，把币别设置成空，不可选
        selectType.get("convertCurId").attr({"data-disabled":false,"data-required":true});
    }else{//不折合本位币,设置币别可选
        selectType.get("convertCurId").attr({"data-value": "","data-disabled":true,"data-required":false});
    }
    form.renderIcon();
    checkConvertCurCallBack();
}



//币别改变事件
function changeCur() {
    var curId=selectType.get("curId").val();
    checkConvertCurCallBack();
    // $("#curCode").val(curId);
}


function checkConvertCurCallBack() {
    var origCur = selectType.get("curId").val();
    var convertCurId = selectType.get("convertCurId").val();
    var date = dateType.get("endDate").val();
    if (isNotEmpty(origCur) && isNotEmpty(convertCurId) && isNotEmpty(date)) {
        $.ajax({
            url: basePath + "/cms/report/cmsloanstatements/checkConverCurId",
            type:"post",
            data:{
                "origCurId": origCur,
                "convertCurId": convertCurId,
                "date": date + "-01"
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("convertCurId").val("");
                }
            }
        });
    }
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}