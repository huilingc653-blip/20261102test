var moneyType,dateType,rateTypes,moneyFun;
bt.use(['form','element','dicts','rateType','moneyType','date','dateType','btUpload','bttable',], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    moneyFun=bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率
    rateFun = bt.rateType.getFun();
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法


    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    /**
     * 列表
     */
    bttable = bt.bttable.init({
        elem: "#appTable",
        mulselect:false,
        checkbox: false,
        'url': basePath + "/cms/security/repaySure/listRepaySureView",
        urldata:{"securityInfoId":$("#infoId").val(),"securityIssueCode":$("#securityIssueCode").val(),
            "levelName":$("#levelName").val(),"productName":$("#productName").val(),
            "gedRepayDate":dateType.get("ged_repayDate").val(),"ledRepayDate":dateType.get("led_repayDate").val()},
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data:"securityIssueCode",className:"center",render:function(data, row){
                    return data.securityIssueCode;
                }},
            {data:"levelName",className:"center",render:function(data, row){
                    return  data.levelName;
                }},
            {data:"productName",className:"center",render:function(data, row){
                    return data.productName;
                }},
            {data:"repayDate",className:"center",render:function(data, row){
                    return bt.date.format(data.repayDate, "yyyy-MM-dd");
                }},
            {data:"planRepayDate",className:"center",render:function(data, row){
                    return bt.date.format(data.planRepayDate, "yyyy-MM-dd");
                }},
            {data:"repayMoney",className:"textRight",render: function ( data ){
                    return bt.money.format(data.repayMoney);
                }},
            {data:"rate",className:"center",render:function(data, row){
                    return rateFun.format(data.rate);
                }},
            {data:"repayInterest",className:"textRight",render: function ( data ){
                    return bt.money.format(data.repayInterest);
                }},
            {data:"repayAmount",className:"textRight",render: function ( data ){
                    return bt.money.format(data.repayAmount);
                }},
            {data:"remainMoney",className:"textRight",render: function ( data ){
                    return bt.money.format(data.remainMoney);
                }},
            {data:"isRepay",className:"center",render:function(data, row){
                    return bt.dict("nisBillhead.isPayed", data.isRepay);
                }},
            {data:"rmk",className:"center",render:function(data, row){
                    return data.rmk;
                }},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data,row) {
                    return bt.dict("security.repay.sure.status", data.status);
                }
            }
        ]
    });



});
function search() {
    bttable.reload({urldata:{"securityInfoId":$("#infoId").val(),"securityIssueCode":$("#securityIssueCode").val(),
            "levelName":$("#levelName").val(),"productName":$("#productName").val(),
            "gedRepayDate":dateType.get("ged_repayDate").val(),"ledRepayDate":dateType.get("led_repayDate").val()}});
}

