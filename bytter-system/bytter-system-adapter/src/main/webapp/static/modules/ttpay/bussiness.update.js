var rowNum = 0;
var dicts;
var select;
bt.use(['form','layedit','select','btdate','dicts','dateType','selectType','corp','bttable'], function(){
    var $ = bt.jquery,form = bt.form();
    var moneynum = bt.moneynum;
    dicts=bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    select=bt.selectType.init();
    // var busTypeUrl =basePath + "/ttpay/shopType/getTypeName";
//生成列表
    bt.bttable.init({
        elem: "#relevanceTB",
        url: basePath + "/ttpay/bussiness/listTpBussinessTextVO?tpBusinessId=" + $("#id").val(),
        buttons: [
            {
                text: '<i class="layui-icon">&#xe654;</i>' + bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }
        ],
        columns:[
            {
                data:"payType",
                render:function(data, row){
                    rowNum = row;
                    if (isNull(data.payType)) {
                        return '<div class="bt-biz-dict" data-group="TTPAY_PAY_TYPE" data-required="true" data-defaultvalue="" data-type="select" id="payType" name="tpBussinessTextVOS[' + rowNum + '].payType" data-name="tpBussinessTextVOS[' + rowNum + '].payType" ></div>'
                        // return '<div class="bt-biz-selectType" data-name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo" data-defaultvalue="" data-textField="shopType" data-valueField="id"  data-biz-name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo" data-url="'+busTypeUrl+'" data-required="true"  data-search="true"  name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo"  ></div>'
                    }else{
                        return  '<div class="bt-biz-dict" data-group="TTPAY_PAY_TYPE" data-required="true" data-defaultvalue="' + data.payType + '" data-type="select" id="payType" name="tpBussinessTextVOS[' + rowNum + '].payType" data-name="tpBussinessTextVOS[' + rowNum + '].payType" ></div>'
                        // return '<div class="bt-biz-selectType" data-name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo" data-defaultvalue="' + data.shoptypeNo + '" data-textField="shopType" data-valueField="id"  data-biz-name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo" data-url="'+busTypeUrl+'" data-required="true"    data-search="true"  name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo"  ></div>'
                    }
                }
            },
            {
                data:"payeeMode",
                render:function(data, row){
                    rowNum = row;
                    if (isNull(data.payeeMode)) {
                        return '<div class="bt-biz-dict" data-group="ttpay.payeeMode" data-required="true" data-type="select"  data-name="tpBussinessTextVOS[' + rowNum + '].payeeMode" name="tpBussinessTextVOS[' + rowNum + '].payeeMode" data-biz-name="payeeMode_' + rowNum + '" ></div>'
                    }else{
                        return '<div class="bt-biz-dict" data-defaultvalue="' + data.payeeMode + '" data-group="ttpay.payeeMode" data-required="true" data-type="select"  data-name="tpBussinessTextVOS[' + rowNum + '].payeeMode" name="tpBussinessTextVOS[' + rowNum + '].payeeMode" data-biz-name="payeeMode_' + rowNum + '" ></div>'
                    }
                }
            },
            {
                data:"loginNo",
                render:function(data, row){
                    rowNum = row;
                    if (isNull(data.loginNo)) {
                        return '<input  name="tpBussinessTextVOS['+rowNum+'].loginNo" maxlength="50" id="loginNo" class="layui-input" type="text"/>'
                    }else{
                        return '<input  name="tpBussinessTextVOS['+rowNum+'].loginNo" maxlength="50" value="' + data.loginNo + '" id="loginNo" class="layui-input" type="text"/>'
                    }
                }
            },
            {
                data:"phone",
                render:function(data, row){
                    rowNum = row;
                    if (isNull(data.phone)) {
                        return '<input  name="tpBussinessTextVOS['+rowNum+'].phone" id="phone" maxlength="11" class="layui-input" type="text"/>'
                    }else{
                        return '<input  name="tpBussinessTextVOS['+rowNum+'].phone" value="' + data.phone + '" id="phone" maxlength="11" class="layui-input" type="text"/>'
                    }
                }
            },
            {
                data:"email",
                render:function(data, row){
                    rowNum = row;
                    if (isNull(data.email)) {
                        return '<input style="width: 100%"  name="tpBussinessTextVOS['+rowNum+'].email" maxlength="50" id="email" class="layui-input" type="text"/>'
                    }else{
                        return '<input style="width: 100%"  name="tpBussinessTextVOS['+rowNum+'].email" maxlength="50" value="' + data.email + '" id="email" class="layui-input" type="text"/>'
                    }
                }
            },
            {
                data:"rmk",
                render:function(data, row){
                    rowNum = row;
                    if (isNull(data.rmk)) {
                        return '<input  name="tpBussinessTextVOS['+rowNum+'].rmk" id="rmk" class="layui-input" type="text"/>'
                    }else{
                        return '<input  name="tpBussinessTextVOS['+rowNum+'].rmk" value="' + data.rmk + '" id="rmk" class="layui-input" type="text"/>'
                    }
                }
            },
            {
                data:null,
                render:function(data, row){
                    var reInfo='<button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
                    return reInfo;
                }
            }
        ],
        delRowCallback:function(data,$tr){
            $tr.remove();
        },
        tableInitCallback:function(){
            bt.dateType.init();
            addFun();
        }
    });
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var action = "/ttpay/bussiness/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent();
                }
            }
            bt.loading.hide();
        },'JSON');
        return false;
    });

});
function add(){
    var html='<tr>';
    // var busTypeUrl =basePath + "/ttpay/shopType/getTypeName";
    // html += '<td><div class="bt-biz-selectType" data-name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo" data-defaultvalue="1" data-textField="shopType" data-valueField="id" data-biz-name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo" data-url="'+busTypeUrl+'" data-required="true"  data-search="true"  name="tpBussinessTextVOS[' + rowNum + '].shoptypeNo"  ></div></td>';
    html += '<td><div class="bt-biz-dict" data-group="TTPAY_PAY_TYPE" data-required="true" data-defaultvalue="1" data-type="select" id="payType" name="tpBussinessTextVOS[' + rowNum + '].payType" data-name="tpBussinessTextVOS[' + rowNum + '].payType" ></div></td>';
    html += '<td><div class="bt-biz-dict" data-group="ttpay.payeeMode" data-required="true" data-type="select"  data-name="tpBussinessTextVOS[' + rowNum + '].payeeMode" name="tpBussinessTextVOS[' + rowNum + '].payeeMode" data-biz-name="payeeMode_' + rowNum + '" ></div></td>';
    html+='<td ><input  name="tpBussinessTextVOS['+rowNum+'].loginNo" id="loginNo" class="layui-input" type="text" maxlength="50"/></td>';
    html+='<td ><input  name="tpBussinessTextVOS['+rowNum+'].phone" id="phone" class="layui-input" type="text" maxlength="11"/></td>';
    html+='<td ><input style="width: 100%"  name="tpBussinessTextVOS['+rowNum+'].email" id="email" class="layui-input" type="text" maxlength="50"/></td>';
    html+='<td ><input  name="tpBussinessTextVOS['+rowNum+'].rmk" id="rmk" class="layui-input" type="text"/></td>';
    html+='<td style="width: 100%"><button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
    html+='</tr>';
    $("#relevanceTB tbody").append(html);
    addFun();
};
function addFun() {
    dicts.render();
    select.render();
    //+1为了以防id重复
    rowNum++;
}
/**
 * 判断是否null
 * @param data
 */
function isNull(data) {
    return (data == " " || data == "" || data == undefined || data == null) ? true : false;
}
/**
 * 检查不允许输入汉字及特殊字符
 * @param obj
 */
function checkNO(obj) {
    var regx =/[\u4e00-\u9fa5]/,//汉字
        regEn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im,//英文特殊字符
        regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;//中文特殊字符
    if (regx.test(obj.value)||regEn.test(obj.value)||regCn.test(obj.value)) {
        obj.value = "";
    }
}