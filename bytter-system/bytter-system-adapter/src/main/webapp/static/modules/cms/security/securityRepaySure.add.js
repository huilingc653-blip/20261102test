var moneyType,dateType,rateTypes,moneyFun,isdis;
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
     * 生成列表
     */
    bttable = bt.bttable.init({
        elem: "#appTable",
        mulselect:false,
        checkbox: false,
        'url': basePath + "/cms/security/repaySure/listRepaySure",
        urldata:{"securityInfoId":$("#infoId").val(),"securityIssueCode":$("#securityIssueCode").val(),
            "levelName":$("#levelName").val(),"productName":$("#productName").val(),
            "gedRepayDate":dateType.get("ged_repayDate").val(),"ledRepayDate":dateType.get("led_repayDate").val()},
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data:"securityIssueCode",className:"center",render:function(data, row){
                     isdis="disabled";
                     if(data.status==0||data.status==-1){
                         isdis="";
                     }
                    return data.securityIssueCode;
                }},
            {data:"levelName",className:"center",render:function(data, row){
                    return  data.levelName;
                }},
            {data:"productName",className:"center",render:function(data, row){
                    return data.productName;
                }},
            {data:"repayDate", bSortable: true,className:"center",render:function(data, row){
                var html='';
                html+='<input '+isdis+' data-biz-name = "repayDate_'+row+'" name="repayDate_'+row+'" id="repayDate_'+row+'" type="text" value="'+data.repayDate+'" class="layui-input bt-biz-dateType" ' +
                    'data-type="date" lay-verify="required"/>';
                    return html;
                }},
            {data:"planRepayDate",className:"center",render:function(data, row){
                    return bt.date.format(data.planRepayDate, "yyyy-MM-dd");
                }},
            {data:"repayMoney",bSortable: true,className:"textRight",render: function ( data, row ){
                var html='';
                html+='<input '+isdis+' lay-verify="required" divName="repayMoney" data-name="repayMoney_'+row+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-callback="calMoney" value="'+moneyFun.format(data.repayMoney)+'"  type="text" data-decimal="2" id="repayMoney_'+row+'" data-biz-name="repayMoney_'+row+'" data-min="0" />';
                    return html;
                }},
            {data:"rate",bSortable: true, className: "textRight",render:function(data, row){
                var html='';
                html+='<input '+isdis+' type="text"  data-name="rate_'+row+'" data-biz-name="rate_'+row+'" lay-verify="required" value="'+data.rate+'" class="layui-input bt-biz-rateType">';
                return html;
                }},
            {data:"repayInterest",className:"textRight",render: function ( data, row ){
                    var html='';
                    html+='<input '+isdis+' divName="repayInterest" lay-verify="required" data-name="repayInterest_'+row+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-callback="calMoney" value="'+moneyFun.format(data.repayInterest)+'"  type="text" data-decimal="2" id="repayInterest_'+row+'" data-biz-name="repayInterest_'+row+'" data-min="0" />';
                    return html;
                }},
            {data:"repayAmount",className:"textRight",render: function ( data,row ){
                var html='';
                html+='<span id="repayAmount_'+row+'">'+bt.money.format(data.repayAmount);+'</span>';
                    return html;
                }},
            {data:"remainMoney",className:"textRight",render: function ( data ){
                    return bt.money.format(data.remainMoney);
                }},
            {data:"isRepay",className:"center",render:function(data, row){
                    return bt.dict("nisBillhead.isPayed", data.isRepay);
                }},
            {data:"rmk",className:"center",render:function(data, row){
                var html='';
                html+='<input '+isdis+'  id="rmk_'+row+'" value="'+(data.rmk==null?"":data.rmk)+'" class="layui-input" type="text"/>';

                    return html;
                }},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data,row) {
                    return bt.dict("security.repay.sure.status", data.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, row) {
                    var html='';
                    if(data.isChose!='1'){
                        if(data.status==0||data.status==-1) {
                            html += '<a class="" href="#" sureId="' + data.id + '" thisrow="' + row + '" onclick="add(this)">' + bt.lang("加入") + '</a>';
                        }
                    }
                    return html;
                }}
        ],
        tableInitCallback:function(data){
            addFun();
        }
    });

    bttable1 = bt.bttable.init({
        elem: "#appTable1",
        mulselect:false,
        checkbox: false,
        'url': basePath + "/cms/security/repaySure/listRepaySureChose",
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

            {data:null,bSortable:false,className:'center',render: function (data, row) {
                    var html='';
                    html += '<a class="" href="#" sureId="'+data.id+'"  onclick="del(this)">' + bt.lang("删除") + '</a>';
                    return html;
                }}
        ]
    });


    form.on('submit(save)', function(data){

        bt.loading.show();
        var action = "/cms/security/repaySure/save";

        $.post(basePath + action,{infoId:$("#infoId").val()},function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                var webObj = bt.navtab.getParentDom();
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrent();
            }
        },'JSON');

        return false;
    });
});
function search() {
    bttable.reload({urldata:{"securityInfoId":$("#infoId").val(),"securityIssueCode":$("#securityIssueCode").val(),
            "levelName":$("#levelName").val(),"productName":$("#productName").val(),
            "gedRepayDate":dateType.get("ged_repayDate").val(),"ledRepayDate":dateType.get("led_repayDate").val()}});
    bttable1.reload({urldata:{"securityInfoId":$("#infoId").val(),"securityIssueCode":$("#securityIssueCode").val(),
            "levelName":$("#levelName").val(),"productName":$("#productName").val(),
            "gedRepayDate":dateType.get("ged_repayDate").val(),"ledRepayDate":dateType.get("led_repayDate").val()}});
}

function add(el){
    var id=$(el).attr("sureId");
    var row=Number($(el).attr("thisrow"));
    var repayDate=dateType.get("repayDate_"+row).val();
    var repayMoney=moneyType.get("repayMoney_"+row).val();
    var rate=rateTypes.get("rate_"+row).val();
    var repayInterest=moneyType.get("repayInterest_"+row).val();
    var rmk=$("#rmk_"+row).val();
    var action="/cms/security/repaySure/addPaySure";
    bt.jquery.ajax({
        type: "POST",
        url: basePath + action,
        data: JSON.stringify({"id":id,"repayDate":repayDate,"repayMoney":repayMoney,"rate":rate,"repayInterest":repayInterest,"rmk":rmk}),
        dataType:"json",
        async: false,
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        success: function(result){
            search()
        }
    });
}

function del(el){
    var id=$(el).attr("sureId");

    var action="/cms/security/repaySure/deletePaySure";
    bt.jquery.ajax({
        type: "POST",
        url: basePath + action,
        data: JSON.stringify({"id":id}),
        dataType:"json",
        async: false,
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        success: function(result){
            search()
        }
    });
}

function calMoney(){
    // 遍历本金
    $("input[divName^='repayMoney']").each(function(i){
        var row=i;
        var repayMoney=moneyType.get("repayMoney_"+row).val();
        var repayInterest=moneyType.get("repayInterest_"+row).val();
        var repayAmount=moneyFun.add(repayMoney,repayInterest);
        $("#repayAmount_"+row).html(bt.money.format(repayAmount));
    })

}

function addFun(){
    //重新渲染datatype和moneyType
    dateType.render();
    moneyType.render();
    rateTypes.render();//利率
}
