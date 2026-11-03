var dateTypes, moneyTypes, selectTypes, dicts,corp;
var rownum = 0,mulselects;
var guaCode;//担保方式对应code
var selectPre;//前一次选择的担保方式数组
var pDids="";//抵押物id
var form;
var rowIndex = 0;
bt.use([ 'form', 'jquery', 'dateType','moneyType', 'selectType', 'dicts','btUpload', 'selectTable', 'checkboxs', 'corp',
    'bankInfo', 'mulselect' ],function() {
    $ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();// 时间
    moneyTypes = bt.moneyType.init();// 金额
    selectTypes = bt.selectType.init();// 下拉框
    dicts = bt.dicts.init();// 字典
    bt.bankInfo.init();//联行号
    corp = bt.corp.init();
    moneyFun = bt.moneyType.getFun();
    mulselects = bt.mulselect.init();
    selectPre = new Array();
    var count = $("#WayCount").val();
    for (var k = 0; k < count; k++) {
        selectPre.push($("#index" + (k + 1)).val())
    }
    pDids=$("#pDids").val();
    // 附件处理
    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem : "#uploader",
        isExpand : true,
        auto : false,
        gid : gid
    });
    // 监听提交
    form.on('submit', function(data) {
        bt.loading.show();
        var action = "/cms/guarantee/guaExtContractChange/save";
        data.field.status = $(this).attr("value");
        data.field.attachmentId = up.getGroupId();
        $.post(basePath + action, data.field, function(result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon : result.success ? 1 : 2}, function() {
                parent.layer.closeAll();
                if (result.success) {
                    if (!bt.navtab) {
                        location.reload();
                        bt.navtab.refreshParentTable();
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        if (data.field.id == "") {
                            bt.navtab.refreshCurrent();
                        } else {
                            bt.navtab.closeCurrent();
                        }
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});

function createHtml(guaWay) {
    var html = '<div id="div' + guaWay.id + '">';
    html += '<label>' + guaWay.name + '</label>';
    html += '<div class="layui-inline">';
    if (guaWay.isPledge == '1') {//担保方式的是否抵质押字段为是
        html += '<input type="text" id="' + guaWay.code + 'money" data-name="guaDtls[' + rowIndex + '].money"  value="'+$("#money"+guaWay.code).val()+'" readOnly="true" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + guaWay.code + 'money"  lay-verify="required">';
        html += '<input type="hidden" id="' + guaWay.code + 'wayCodes" name="guaDtls[' + rowIndex + '].wayCodes" value="' + guaWay.id + '" >';
        html += '<input type="hidden" id="' + guaWay.code + 'pledgeIds" name="guaDtls[' + rowIndex + '].pledgeIds"  value="'+$("#pledgeIds"+guaWay.code).val()+'">';
        html += '<input type="hidden" id="' + guaWay.id + 'pDYIds"  value="'+$("#pDids"+guaWay.code).val()+'">';
        html += '</div>';
        html += '<div class="layui-inline">';
        html += '<button class="layui-btn" type="button"  data-code="' + guaWay.code + '"   data-pledgeProperty="' + guaWay.pledgeProperty + '" ';
        html += 'onclick="selectGuaPledge(this)">'
            + bt.lang("common.button.select") + '</button>';
        html += '</div>';
    } else {
        html += '<input type="text" id="' + guaWay.code + 'money" data-name="guaDtls[' + rowIndex + '].money"   value="'+$("#money"+guaWay.code).val()+'" data-decimal="2" class="layui-input bt-biz-moneyType" data-biz-name="' + guaWay.code + 'money" lay-verify="required">';
        html += '<input type="hidden" id="' + guaWay.code + 'wayCodes" name="guaDtls[' + rowIndex + '].wayCodes" value="' + guaWay.id + '">';
        html += '</div>';
    }
    html += '</div>';
    $("#guaMoney").append(html);
    rowIndex++;
    bt.moneyType.init();//金额
}

/**判断字符串是否为空*/
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "|| ms =="undefined"){
        return true;
    }
    return false;
}
//选择抵质押物清单
function selectGuaPledge(elem){
    var pledgeProperty=$(elem).data('pledgeproperty');
    guaCode=$(elem).data('code');
    var params = beforeSelect(pledgeProperty);
    if(bt.empty(params)){
        return;
    }
    openSelectPage(params);
}
/**
 * 选择前判断必须的参数是否为空
 * @param params
 */
function beforeSelect(pledgeAttr){
    var corpId = $("#corpId").val()==undefined?bt.corp.init().get("corpId").val():$("#corpId").val();
    if(bt.empty(corpId) || corpId == 0){
        //选择单位
        parent.layer.msg(bt.lang("gua.contract.checkCorpInfo"), {icon: 2});
        return "";
    }
    var curId = $("#curId").val();//币别
    /*if(bt.empty(curId)){
        //选择币别
        parent.layer.msg(bt.lang("gua.contract.checkCurIdInfo"), {icon: 2});
        return "";
    }*/
    var startDate = dateTypes.get("startDate").val();//担保开始日期
    var endDate = dateTypes.get('endDate').val();//担保结束日期
    if(bt.empty(startDate)||bt.empty(endDate)){
        parent.layer.msg(bt.lang("gua.contract.checkDateInfo"), {icon: 2});
        return "";
    }
    var guaId = $("#guaId").val();
    var pledgeMsg=$("#"+guaCode+"pledgeIds").val();
    if(bt.empty(pledgeMsg)){
        pledgeMsg="";
    }
    var params = {
        corpId : corpId,
        curId : curId,
        startDate : startDate.replace(" ", ""),
        endDate : endDate.replace(" ", ""),
        pledgeAttr : pledgeAttr,
        pDids:pDids,
        // pZids:$("#pZids").val(),
        pledgeMsg:pledgeMsg,
        guaId:guaId
    }
    return params;
}
/**
 * 打开相应的页面
 * @param params
 */
function openSelectPage(params) {
    var param = parseParam(params);
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/cms/guarantee/guaContractInfo/toPage?" +param);
        return;
    }
}
//json转url参数
var parseParam = function(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function(i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};
/**
 * 页面回调函数
 */
var updateInfo = function(data,type,totalMoney,pIds){
    if(moneyFun.toNumber(totalMoney)>0){
        moneyTypes.get(guaCode+"money").val(totalMoney);
        $("#"+guaCode+"pledgeIds").val(data);
        var guaId=$("#"+guaCode+"wayCodes").val();
        if(type=="0"){
            pDids+=pIds;
            $("#"+guaId+"pDYIds").val(pIds);
        }

    }
    if(moneyFun.toNumber(totalMoney)<=0){
        moneyTypes.get(guaCode+"money").val("");
        $("#"+guaCode+"pledgeIds").val("");
    }
}
/**
 * 拼接担保方式的id,用于担保关联担保方式表
 */
var guaWayIds = function(){
    var guaId = "";
    $('#guaTable #guaMoney div').each(function(i){
        guaId+= $(this).attr('id').val();
    });
}

/**
 *重新选择担保时间后执行的回调函数
 */
bt.dateChange = function(){
    bt.reSelect();
}
/**
 * 用户重新选择单位,币别,担保时间后
 * 将选择的担保方式和内容清空
 */
bt.reSelect = function(){
    $("#guaMoney").empty();
    $("#pDids").val("");
    $("#pZids").val("");
    $("#guaId").val("");
    clearData();
}

//选择担保方式回调事件
bt.changeGuaWay = function(data) {
    var selectNew=new Array();//选中的最新数据
    var selectChange =new Array();//改变前后变化的数据
    $.each(data,function(i){
        selectNew[i]=data[i].id;
    });

    selectChange=getDif(selectNew,selectPre);
    function getDif(a,b){
        var array1=a.length>=b.length?a:b;
        var array2=a.length>=b.length?b:a;
        return array1.concat(array2).filter(function(v, i, arr) {
            return arr.indexOf(v) === arr.lastIndexOf(v);
        });
    }

    if(selectPre.length>data.length){//选择减少
        $.each(selectChange,function(i){
            var guaDiv=$("#guaMoney").find('#div' + selectChange[i]);
            if(guaDiv.length>0){
                var pd=$("#"+selectChange[i]+"pDYIds").val();
                if(!bt.empty(pd)){
                    pDids=pDids.replace(pd,"");
                }
                guaDiv.remove();
            }
        });
    }else if(selectPre.length<data.length){//选择增多
        $.each(data,function(i){
            var guaDiv=$("#guaMoney").find('#div' + data[i].id);
            if(guaDiv.length>0){
                return true;
            }
            createHtml(data[i])
        });
    }
    selectPre.splice(0,selectPre.length);
    selectPre=selectNew;
}

//初始化担保方式事件
bt.initGuaWay = function(data) {
    var guaWaysValue = $("#guaWays").val();
    if(guaWaysValue.length){
        var arr=new Array();
        arr = guaWaysValue.split(",");
        $.each(data,function(i){
            for(var j=0;j<arr.length;j++)
            {
                var str1 =data[i].id;
                var str2 = arr[j];
                if(str1==str2){
                    createHtml(data[i]);
                }
            }
        });
    }
}
function clearData(){
    selectPre=new Array();
}

/**加载还款计划*/
function repayLan(){
    var id = $("#id").val();
    var params = "id=" +id;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/guarantee/guaExtContractInfo/toRepayPlan?" + params);
    }
}

/***
 * 是否集团内担保单位回调函数
 * @param data
 */
bt.changeType = function (data) {
    var html = '';
    if (data.value == '0'){
        html = '<input class="layui-input" id="guaPerson" name="guaPerson" type="text" lay-verify="required">';
        $("#guaPersonInfo").empty();
        $("#guaPersonInfo").html(html);
    }
    if (data.value == '1'){
        html += '<div class="bt-biz-corp"';
        html += 'data-biz-type="corp"';
        html += 'data-type="selectTree"';
        html += 'data-required="true"';
        html += 'data-tree-type="check"';
        html += 'data-name="guaPerson"';
        html += 'data-scope="all"';
        html += 'data-resource="gua:contract:info:apply">';
        html += '</div>';
        $("#guaPersonInfo").empty();
        $("#guaPersonInfo").html(html);
    }
    corp.render();
    form.render();
}