var columns,sysCode,dataUrl,businessIds;
bt.use(['element', 'form','layedit','dateType','dicts','datatable','moneyType','corp','selectType','date'], function(){
    var $ = bt.jquery,form = bt.form();
    dataType = bt.dateType.init();
    dicts = bt.dicts.init();
    moneyFun = bt.moneyType.getFun();
    bt.corp.init();
    bt.selectType.init();
    sysCode=$("#sysCode").val();//系统代码
    setColumns(sysCode);//设置表格显示数据
    $("."+sysCode+"Div").show();//显示对应模块
    form.render();
    initTable("#"+sysCode+"Table",columns,dataUrl);//初始化表格
    businessIds=$("#businessIds").val();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

});

function setColumns(sysCode) {
    switch(sysCode) {
        case "rat"://银行授信
            columns = [
                {
                    data: null, bSortable: false, className: 'center',
                    render: function (data, type, full, meta) {
                        return '<a href="javascript:void(0);" class="businessInfo-select" data-row="' + meta.row + '">' + bt.lang("common.button.select") + '</a>';
                    }
                },
                {
                    data: "ratMode", className: 'center', bSortable: false,//授信模式
                    render: function (data, type, full, meta) {
                        return bt.dict("RatBkration.RatMode", full.ratMode);
                    }
                },
                {data: "ratCode", className: 'center', bSortable: false},//授信编号
                {data: "contractNo", className: 'center', bSortable: false},//授信合同号
                {data: "corpName", className: 'center', bSortable: false},//授信单位
                {data: "bankName", className: 'center', bSortable: false},//银行信息
                {data: "curName", className: 'center', bSortable: false},//币种
                {
                    data: "money", className: 'textRight', bSortable: false,//授信额度
                    render: function (data, type, full, meta) {
                        var money = moneyFun.format(full.money);
                        return money;
                    }
                },
                {
                    data: "beginDate", className: 'center', bSortable: false,//授信开始时间
                    render:function(data,type,full,meta){
                        return bt.date.format(full.beginDate,'yyyy-MM-dd');
                    }
                },
                {
                    data: "endDate", className: 'center', bSortable: false,//授信结束时间
                    render:function(data,type,full,meta){
                        return bt.date.format(full.endDate,'yyyy-MM-dd');
                    }
                }
            ];
            dataUrl="/cms/rat/ratbkration/ratBkData";
            break;
        case "accebill"://承兑汇票
            columns = [
                {
                    data: null, bSortable: false, className: 'center',
                    render: function (data, type, full, meta) {
                        return '<a href="javascript:void(0);" class="businessInfo-select" data-row="' + meta.row + '">' + bt.lang("common.button.select") + '</a>';
                    }
                },
                {data:"billFlagName",bSortable:true},
                {data:"billTypeName",bSortable:true},
                {data:"origNo",bSortable:true},
                {data:"payer",bSortable:true},
                {data:"payerBankName",bSortable:true},
                {data:"emitTypeName",bSortable:true},
                {data:"emitGuandisSignName",bSortable:true},
                {data:"startDate",bSortable:true},
                {data:"endDate",bSortable:true},
                {data:"payee",bSortable:true},
                {data:"billMoney",bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        if(data){
                            return bt.money.format(data);
                        }
                        return '';
                    }
                },
                {data:"bailMoney",bSortable:true,className:'textRight',
                    render: function ( data, type, full, meta ) {
                        if(data){
                            return bt.money.format(data);
                        }
                        return '';
                    }},
                {data:"payDateStr",bSortable:true},
                {data:"billAnomaliesStr",bSortable:true},
                {data:"dueRemindStr",bSortable:true,className:'textCenter'},
                {data:"briefIdName",bSortable:true}
            ];
            dataUrl="/bill/accebill/abvoucherinfoVice/guaranteeDatatable";
            break;
        case "loan"://内部贷款
            columns = [
                {
                    data: null, bSortable: false, className: 'center',
                    render: function (data, type, full, meta) {
                        return '<a href="javascript:void(0);" class="businessInfo-select" data-row="' + meta.row + '">' + bt.lang("common.button.select") + '</a>';
                    }
                },
                {data:"infoBillCode",className:"center",bSortable:true},
                {data:"netName",className:"center",bSortable:false},
                {data:"corpName",className:"center",bSortable:false},//申请单位
                {data:"contractCode",className:"center",bSortable:true},//贷款合同编号
                {data:"createDate",className:"center",bSortable:true},//录入日期
                {data:"sendBillCode",className:"center",bSortable:true},//贷款发放编号
                {data:"curName",className:"center",bSortable:false},//币别
                {data:"loanMoney",className:"textRight",bSortable:false,//贷款金额
                    render: function (data, type, full, meta) {
                        var money = moneyFun.format(full.loanMoney);
                        return money;
                    }
                },
                {data:"sendMoney",className:"textRight",bSortable:false,//发放金额
                    render: function (data, type, full, meta) {
                        var money = moneyFun.format(full.sendMoney);
                        return money;
                    }

                },
                {data:"loanDate",className:"center",bSortable:true,
                    render:function(data,type,full,meta){
                        return bt.date.format(full.loanDate,'yyyy-MM-dd');
                    }},//发放日期
                {data:"repayDate",className:"center",bSortable:true,
                    render:function(data,type,full,meta){
                        return bt.date.format(full.repayDate,'yyyy-MM-dd');
                }}//到期日期
            ];
            dataUrl="/cms/loan/cmsloaninfo/loanInfoList";
            break;
        case "provide"://银行借款
            columns = [
                {
                    data: null, bSortable: false, className: 'center',
                    render: function (data, type, full, meta) {
                        return '<a href="javascript:void(0);" class="businessInfo-select" data-row="' + meta.row + '">' + bt.lang("common.button.select") + '</a>';
                    }
                },
                {data:"bankName",bSortable:true,render: function ( data, type, full, meta ){
                        return full.bankName;
                    }},
                {data:"corpName",bSortable:true,render: function ( data, type, full, meta ){
                        return full.corpName;
                    }},
                {data:"infoBillCode",bSortable:true},
                {data:"sendBillCode"},
                {data:"contractCode"},
                {data:"createDate"},
                {data:"curName",bSortable:true,render: function ( data, type, full, meta ){
                        return full.curName;
                    }},
                {data:"plMoney",bSortable:true,className:"textRight",render: function ( data, type, full, meta ){
                        return bt.money.format(full.plMoney);
                    }},
                {data:"sendMoney", className:"textRight",render: function ( data, type, full, meta ){
                        return bt.money.format(full.sendMoney);
                    }},
                {data:"plDate"},
                {data:"plReDate"}
            ];
            dataUrl="/cms/provide/loaninfo/queryLoanInfoData";
            break;
        case "lcs"://信用证
            columns = [
                {
                    data: null, bSortable: false, className: 'center',
                    render: function (data, type, full, meta) {
                        return '<a href="javascript:void(0);" class="businessInfo-select" data-row="' + meta.row + '">' + bt.lang("common.button.select") + '</a>';
                    }
                },
                {
                    data: "issuingBank",
                    className: "center",
                    bSortable: false
                }, {
                    data: "reBank",
                    className: "center",
                    bSortable: false
                }, {
                    data: "lcInter",
                    className: "center",
                    bSortable: true,
                    render : function (data, type, full, meta) {
                        return bt.dict("letter_of_credit_inter",full.lcInter);
                    }
                }, {
                    data: "applicantName",
                    className: "center",
                    bSortable: true
                }, {
                    data: "beneficiary",
                    className: "center",
                    bSortable: false
                }, {
                    data: "creditNo",
                    className: "center",
                    bSortable: true
                }, {
                    data: "reCreditNo",
                    className: "center",
                    bSortable: false
                }, {
                    data: "contractNo",
                    className: "center",
                    bSortable: false
                }, {
                    data: "issuingDate",
                    className: "center",
                    bSortable: true
                }, {
                    data: "expiryDate",
                    className: "center",
                    bSortable: true
                }, {
                    data: "curName",
                    className: "center",
                    bSortable: false
                }, {
                    data: "creditMoney",
                    className: "textRight",
                    bSortable: true,
                    render : function (data, type, full, meta) {
                        return bt.money.format(full.creditMoney);
                    }
                }, {
                    data: "delayDates",
                    className: "center",
                    bSortable: false
                }
            ];
            dataUrl="/international/credit/createCredit/listLcNotGua";
            break;

    }


}


/**
 * 初始化表格操作
 * @param tableId 表格tableId
 * @param columns 表格数据列
 * @param dataUrl 表格数据访问地址
 */
function initTable(tableId,columns,dataUrl){
    var datatable = bt.datatable.init({
        elem: tableId,
        dom: 'Brtlip',
        colReorder: true,
        fixedColumns: {
            leftColumns:1,//代表冻结左边两列
        },
        ajax: {
            'url': basePath+dataUrl
        },
        searchForm:{
            formFilter: "search",
            formElem:"#"+sysCode+"Search"
        },
        // isInitData: false,//初始化不加载数据，点击查询后加载
        buttonsLayout:false,
        columns:columns
    });

    datatable.on('click', '.businessInfo-select', function () {//点击选择触发
        var data = datatable.row($(this).data('row')).data();
        parent.setBusinessInfo(data);
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
}


