var dicts,rowMR=0,curRowIndex,sysCode,RMBName,RMBId;
bt.use(['form','datatable','select','corp','money','dicts','dateType','moneyType','selectType','bttable','money','date'],function () {
    $ = bt.jquery,form = bt.form();
    dicts=bt.dicts.init();
    RMBName=$("#RMBName").val();
    RMBId=$("#RMBId").val();

    var contractId=$("#contractId").val();
    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    bt.use(['form', 'datatable', 'corp', 'moneyType', 'btUpload', 'dicts', 'jquery', 'checkboxs'], function () {
        $ = bt.jquery, form = bt.form();
        bt.moneyType.init();
        var gInfoId = $("#annexNameInfo").val();
        var gSendId = $("#annexNameSend").val();
        bt.btUpload.init({
            elem: "#uploaderInfo",
            hasUpload: false,
            hasDelete: false,
            isExpand: true,
            gid: gInfoId
        });

        bt.btUpload.init({
            elem: "#uploaderSend",
            hasUpload: false,
            hasDelete: false,
            isExpand: true,
            gid: gSendId
        });

        // 担保方式
        initGuaWayDiv();
    });

    function closecurrent() {
        parent.layer ? parent.layer.closeAll() : layer.closeAll();
        return false;
    }

    function initGuaWayDiv() {
        var guaWaysValue = $("#guaWays").val();
        if (guaWaysValue.length) {
            var arr = new Array();
            arr = guaWaysValue.split(",");
            // 外币
            $.ajax({
                url: basePath + "/cms/guarantee/guaContractInfo/json",
                type: "get",
                dataType: "json",
                success: function (data) {
                    $.each(data, function (i) {
                        for (var j = 0; j < arr.length; j++) {
                            var str1 = data[i].id;
                            var str2 = arr[j];
                            if (str1 == str2) {
                                createHtml(data[i]);
                            }
                        }
                    });
                }
            });
        }
    }

    function createHtml(data) {
        var html = '<div id="div' + data.id + '">';
        html += '<label>' + data.name + '</label>';
        html += '<div class="layui-inline">';
        html += bt.money.format($("#money" + data.code).val());
        html += '</div>';
        $("#guaMoney").append(html);
        bt.moneyType.init();//金额
    }

    //新增行事件
    $("#add").click(function(){
        rowMR++;
        var html='<tr><input type="hidden" id="rowSign'+rowMR+'" value="'+rowMR+'">';
        html+='<input type="hidden" name="useDtls['+rowMR+'].contractId" value="'+contractId+'">';//担保合同id
        html+='<input type="hidden" name="useDtls['+rowMR+'].businessId" id="'+rowMR+'businessId" >';//业务id
        html+='<input type="hidden" id="'+rowMR+'BizId">';
        // html+='<div  id="'+rowMR+'businessIdInput"></div>';
        html+='<td align="center" >'+rowMR+'</td>';//序号
        html +='<td  align="center" style="width: 10%"><div class="bt-biz-dict" data-type="select" data-group="gua.busType" data-name="useDtls['+rowMR+'].sysCode" data-biz-name="'+rowMR+'sysCode" data-row="'+rowMR+'" data-callback="createBizInput"></div></td>';//业务类型
        html += '<td align="center" style="width: 10%">' +
            '<div class="layui-inline">'+
            '<input type="text" name="useDtls['+rowMR+'].itemNo" id="'+rowMR+'itemNo" readonly="readonly" class="layui-input" lay-verify="required">'+
            '</div>'+
            '<span>&nbsp</span>'+
            '<div class="layui-inline">'+
            '<button class="layui-btn" type="button" rowindex="'+rowMR+'" onclick="chooseBusinessByType('+rowMR+')">'+bt.lang("common.button.select")+'</button>'+
            '</div>'+
            '</td>';//业务编号
        html += '<td align="center"><span id="'+rowMR+'curName"></span><input type="hidden" name="useDtls['+rowMR+'].itemCurId" id="'+rowMR+'itemCurId" readonly="readonly" class="layui-input"></td>';//业务币种
        html+='<td align="center"><span id="'+rowMR+'busMoney"></span><input type="hidden" name="useDtls['+rowMR+'].itemMoney" id="'+rowMR+'itemMoney"  class="layui-input" readonly="readonly"></td>';//业务金额
        html += '<td align="center"><span id="'+rowMR+'busStartDate"></span><input type="hidden" name="useDtls['+rowMR+'].startDate"   id="'+rowMR+'startDate" class="layui-input"   rowindex="'+rowMR+'" readonly="readonly"></td>';//开始时间
        html += '<td align="center"><span id="'+rowMR+'busEndDate"></span><input type="hidden" name="useDtls['+rowMR+'].endDate"  id="'+rowMR+'endDate" class="layui-input"  rowindex="'+rowMR+'" readonly="readonly"></td>';//结束时间
        html+='<td align="center"><span id="'+rowMR+'busConCode"></span><input type="hidden" name="useDtls['+rowMR+'].itemConCode" id="'+rowMR+'itemConCode" ></td>';//业务合同编号
        html+='<td align="center">'
            + '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
            + '</td>';
        html+='</tr>';
        $("#appTable tbody:first").append(html);
        bt.dicts.init();
        bt.moneyType.init();
        bt.dateType.init();
        bt.selectType.init();
        form.render();
        return false;
    });

    //暂存或提交数据
    form.on("submit",function (data) {
        if(!checkData()){
            return false;
        }
        data.field.status=$(this).attr("value");
        bt.loading.show();//显示加载中
        var action = "/cms/guarantee/guaContractUse/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide(); //关闭加载中
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshParentTable();
                }
            }
        },'JSON');
        return false;
    });

    //初始化删除按钮
    var initRationTable=function(){
        //生成列表
        bt.bttable.init({
            scrollX:false,
            scrollY:false,
            clickTrSelected : false,
            elem: "#appTable",
            buttons: [
            ],
            columns:[],
            delRowCallback:function(data,$tr){
                $tr.remove();
                rowMR--;
            },
            tableInitCallback:function(){}
        });
    }
    initRationTable();

    bt.close = function(){
        if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
            bt.navtab.closeCurrent();
        }
    }

});

//提交前检查数据
function checkData() {
    var success = true;
    if(rowMR==0){
        success=false;
        parent.layer.alert(bt.lang('gua.message.noData'));
   }
    return success;
}

//选择业务单据后的回调函数
function setBusinessInfo(data) {
    $("#"+curRowIndex+"businessId").val(data.id);
    $("#"+curRowIndex+"BizId").val(data.id);
    switch(sysCode) {
        case "rat"://银行授信
            $("#"+curRowIndex+"itemNo").val(data.ratCode);
            $("#"+curRowIndex+"itemCurId").val(data.curCode);
            $("#"+curRowIndex+"curName").html(data.curName);
            $("#"+curRowIndex+"busMoney").html(bt.money.format(data.money));
            $("#"+curRowIndex+"itemMoney").val(data.money);
            $("#"+curRowIndex+"busStartDate").html(bt.date.format(data.beginDate));
            $("#"+curRowIndex+"startDate").val(data.beginDate);
            $("#"+curRowIndex+"busEndDate").html(bt.date.format(data.endDate));
            $("#"+curRowIndex+"endDate").val(data.endDate);
            $("#"+curRowIndex+"busConCode").html(data.contractNo);
            $("#"+curRowIndex+"itemConCode").val(data.contractNo);
            break;
        case "provide"://银行借款
            $("#"+curRowIndex+"itemNo").val(data.infoBillCode);
            $("#"+curRowIndex+"itemCurId").val(data.curId);
            $("#"+curRowIndex+"curName").html(data.curName);
            $("#"+curRowIndex+"busMoney").html(bt.money.format(data.plMoney));
            $("#"+curRowIndex+"itemMoney").val(data.plMoney);
            $("#"+curRowIndex+"busStartDate").html(bt.date.format(data.plDate));
            $("#"+curRowIndex+"startDate").val(data.plDate);
            $("#"+curRowIndex+"busEndDate").html(bt.date.format(data.plReDate));
            $("#"+curRowIndex+"endDate").val(data.plReDate);
            $("#"+curRowIndex+"busConCode").html(data.contractCode);
            $("#"+curRowIndex+"itemConCode").val(data.contractCode);
            break;
        case "loan"://内部贷款
            $("#"+curRowIndex+"itemNo").val(data.infoBillCode);
            $("#"+curRowIndex+"itemCurId").val(data.curCode);
            $("#"+curRowIndex+"curName").html(data.curName);
            $("#"+curRowIndex+"busMoney").html(bt.money.format(data.loanMoney));
            $("#"+curRowIndex+"itemMoney").val(data.loanMoney);
            $("#"+curRowIndex+"busStartDate").html(bt.date.format(data.loanDate));
            $("#"+curRowIndex+"startDate").val(data.loanDate);
            $("#"+curRowIndex+"busEndDate").html(bt.date.format(data.repayDate));
            $("#"+curRowIndex+"endDate").val(data.repayDate);
            $("#"+curRowIndex+"busConCode").html(data.contractCode);
            $("#"+curRowIndex+"itemConCode").val(data.contractCode);
            break;
        case "accebill"://承兑汇票
            $("#"+curRowIndex+"businessId").val(data.viceId);
            $("#"+curRowIndex+"BizId").val(data.viceId);
            $("#"+curRowIndex+"itemNo").val(data.origNo);
            $("#"+curRowIndex+"itemCurId").val(RMBId);//没有币别
            $("#"+curRowIndex+"curName").html(RMBName);
            $("#"+curRowIndex+"busMoney").html(bt.money.format(data.billMoney));
            $("#"+curRowIndex+"itemMoney").val(data.billMoney);
            $("#"+curRowIndex+"busStartDate").html(bt.date.format(data.startDate));
            $("#"+curRowIndex+"startDate").val(data.startDate);
            $("#"+curRowIndex+"busEndDate").html(bt.date.format(data.endDate));
            $("#"+curRowIndex+"endDate").val(data.endDate);
            $("#"+curRowIndex+"busConCode").html(data.contractNo);
            $("#"+curRowIndex+"itemConCode").val(data.contractNo);
            break;
        case "lcs"://国际结算
            $("#"+curRowIndex+"itemNo").val(data.creditNo);
            $("#"+curRowIndex+"itemCurId").val(data.issuingCurId);
            $("#"+curRowIndex+"curName").html(data.curName);
            $("#"+curRowIndex+"busMoney").html(bt.money.format(data.creditMoney));
            $("#"+curRowIndex+"itemMoney").val(data.creditMoney);
            $("#"+curRowIndex+"busStartDate").html(bt.date.format(data.issuingDate));
            $("#"+curRowIndex+"startDate").val(data.issuingDate);
            $("#"+curRowIndex+"busEndDate").html(bt.date.format(data.expiryDate));
            $("#"+curRowIndex+"endDate").val(data.expiryDate);
            $("#"+curRowIndex+"busConCode").html(data.contractNo);
            $("#"+curRowIndex+"itemConCode").val(data.contractNo);
            break;
    }

}

//打开选择业务单据页面
function chooseBusinessByType(rowIndex) {
    curRowIndex=rowIndex;//当前操作的行
    sysCode=dicts.get(rowIndex+"sysCode").val();
    var startDate=$("#startDate").val();
    var endDate=$("#endDate").val();
    var corpId=$("#conCorpId").val();
    var curId=$("#conCurId").val();
    var contractId=$("#contractId").val();
    var businessIds="";
    $.each($("input[name$='"+sysCode+"BusinessId']"), function (i, item) {
        if (!bt.empty($(item).val())) {
            businessIds += $(item).val()+",";
        }
    });
    if(bt.empty(sysCode)){//选择单据前校验
        layer.alert(bt.lang('gua.message.noBizType'));
        return false;
    }
    var param="?sysCode=" +sysCode+"&businessIds="+businessIds+"&corpId="+corpId+
        "&curId="+curId+"&startDate="+startDate+"&endDate="+endDate+"&contractIds="+contractId;
    layer.open({
        type: 2,
        area: ['900px','600px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaContractUse/selectBusiness"+param,
        end : function(){
        }
    });
}


/**
 * 关闭当前标签页
 */
closeTab = function(){
    bt.close();
}

/**
 * 选择业务品种后回调
 * @param data
 */
function createBizInput(data) {
    var rowIndex=data.othis.offsetParent().attr("data-row");
    var thisSysCode=data.value;//当前选中的业务系统代码
    $("#"+rowIndex+"BizId").attr("name",thisSysCode+"BusinessId");
}




